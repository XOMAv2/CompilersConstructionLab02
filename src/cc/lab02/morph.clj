(ns cc.lab02.morph
  (:require [cc.lab02.helpers :refer [json->grammar]]
            [clojure.set]))

(defn remove-direct-left-recursion
  [nt grammar & {:keys [merge-nonterms? reset-new-nonterms?]}]
  (let [grammar (if reset-new-nonterms? (dissoc grammar :new-nonterms) grammar)
        old-prods (-> grammar :prods (get nt))
        betas (filter #(and (seq %)
                            (-> % (first) (not= nt))) old-prods)
        betas (map vec betas)
        alphas (filter #(and (> (count %) 1)
                             (-> % (first) (= nt))) old-prods)
        alphas (map (comp vec rest) alphas)]
    (cond
      (empty? alphas) grammar
      (empty? betas) (throw (Exception. "Отсутствует правило для выхода из рекурсии."))
      :else (let [new-nt (str nt "'")
                  nt-prods (concat betas (map #(conj % new-nt) betas))
                  nt-prods (set nt-prods)
                  new-nt-prods (concat alphas (map #(conj % new-nt) alphas))
                  new-nt-prods (set new-nt-prods)]
              (-> grammar
                  (assoc-in [:prods nt] nt-prods)
                  (assoc-in [:prods new-nt] new-nt-prods)
                  (update (if merge-nonterms? :nonterms :new-nonterms)
                          clojure.set/union #{new-nt}))))))

#_(remove-direct-left-recursion "E" (json->grammar "resources/grammar.json"))

(defn insert-rules
  "Для всех правил вида `target -> [source gamma]` грамматики grammar осуществялется замена
   нетерминала source правыми частями правил `source -> [sigma_i]`.
   gamma и sigma i-ые - цепочки терминалов и нетерминалов.
   Итог - правила вида `target -> [simga_i gamma]`."
  [target source grammar]
  (let [source-chains (-> grammar :prods (get source))
        target-chains (-> grammar :prods (get target))
        dc-pairs (for [t-chain target-chains
                       :when (= (first t-chain) source)]
                   [t-chain (map #(vec (concat % (rest t-chain))) source-chains)])
        target-chains (reduce (fn [chains [d c]]
                                (-> chains
                                    (disj d)
                                    (concat c)
                                    (set)))
                              target-chains
                              dc-pairs)]
    (assoc-in grammar [:prods target] target-chains)))

(defn remove-left-recursion [grammar]
  (loop [curr-nt (-> grammar :nonterms first)
         nts-visited []
         nts-to-visit (-> grammar :nonterms rest)
         grammar grammar]
    (if (nil? curr-nt)
      (-> grammar
          (update :nonterms clojure.set/union (:new-nonterms grammar))
          (dissoc :new-nonterms))      
      (let [grammar (reduce #(insert-rules curr-nt %2 %) grammar nts-visited)
            grammar (remove-direct-left-recursion curr-nt grammar)]
        (recur (first nts-to-visit)
               (conj nts-visited curr-nt)
               (rest nts-to-visit)
               grammar)))))

#_(remove-left-recursion (json->grammar "resources/grammar.json"))
