(ns cc.lab02.morph
  (:require [cc.lab02.helpers :refer [json->grammar]]
            [clojure.set]))

(defn remove-direct-left-recursion
  [nt grammar & {:keys [merge-nonterms?]}]
  (let [old-prods (-> grammar :prods (get nt))
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
