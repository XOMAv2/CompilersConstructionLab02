(ns cc.lab02.morph
  (:require [cc.lab02.helpers :refer [json->grammar index-dissoc find-occurrence-indexes]]
            [clojure.math.combinatorics :as combo]
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

(defn find-generating-nonterms
  "Функция для поиска нетерминалов, пораждающих терминал/нетерминал sym."
  ([grammar sym]
   (let [eps-nts (->> (:prods grammar)
                      (filter #(get (second %) [sym]))
                      (map first)
                      (set))]
     (find-generating-nonterms grammar sym eps-nts)))
  ([grammar sym eps-nts]
   (let [new-nts (->> (:prods grammar)
                      (map (fn [[k v]]
                             (when (reduce #(or % (every? eps-nts %2)) false v)
                               k)))
                      (filter some?)
                      (set)
                      (clojure.set/union eps-nts))]
     (if (= new-nts eps-nts)
       new-nts
       (find-generating-nonterms grammar sym new-nts)))))

#_(find-generating-nonterms (json->grammar "resources/grammar.json") "F")

#_(combo/selections [0 1] 3)
#_(combo/subsets [0 1 2])

(defn remove-epsilon-rules [grammar]
  (let [eps-nts (find-generating-nonterms grammar (:epsilon grammar))

        find-replacement-for-chain
        (fn [chain]
          (->> (find-occurrence-indexes chain eps-nts)
               (combo/subsets)
               (map (comp reverse sort))
               (reduce (fn [acc to-dissoc]
                         (->> (reduce #(index-dissoc % %2) chain to-dissoc)
                              (vec)
                              (conj acc)))
                       [])))

        eliminate-eps-chains
        (fn [chains]
          (->> chains
               (mapcat find-replacement-for-chain)
               (filter #(and (not= % [(:epsilon grammar)])
                             (seq %)))
               (set)))

        prods (reduce (fn [prods [nt chains]]
                        (assoc prods nt (eliminate-eps-chains chains)))
                      {}
                      (:prods grammar))
        
        grammar (assoc grammar :prods prods)
        grammar (if (get eps-nts (:start-symbol grammar))
                  (let [new-start (str (:start-symbol grammar) "'")]
                    (-> grammar
                        (assoc :start-symbol new-start)
                        (update :nonterms conj new-start)
                        (assoc-in [:prods new-start] #{[(:start-symbol grammar)]
                                                       [(:epsilon grammar)]})))
                  grammar)]
    grammar))

#_(remove-epsilon-rules (json->grammar "resources/grammar-eps.json"))
#_(remove-epsilon-rules {:start-symbol "S"
                         :name "G_eps"
                         :nonterms #{"S" "C" "B" "A"}
                         :prods
                         {"S" #{["A" "B" "C"]}
                          "C" #{["c"] ["eps"]}
                          "B" #{["A" "C"]}
                          "A" #{["a"] ["eps"]}}
                         :terms #{"a" "c"}
                         :epsilon "eps"})

(defn- longest-common-prefix
  "Поиск наибольшего общего префикса для произвольного числа векторов vectors."
  [& vectors]
  (->> (or vectors '([]))
       (apply map =)
       (take-while true?)
       (count)
       (#(take % (first vectors)))))

(defn- select-rules-for-factorization
  "Поиск правых частей правил с наибольшим общим префиксом.
   Результатом является вектор, первый элемент которого - префикс, второй - подмножество правых
   частей. Если общий префикс отсутствует, то результатом функции будет nil."
  [nt-rhs]
  (reduce (fn [_ subset]
            (when-let [prefix (seq (apply longest-common-prefix subset))]
              (reduced [prefix subset])))
          nil
          (->> (vec nt-rhs)
               (combo/subsets)
               (filter #(> (count %) 1))
               (sort-by count >))))

#_(select-rules-for-factorization #{["1" "2" "3"] ["1" "2"] ["4"]})

(defn left-factorization-1
  "Поиск самой длинной общей цепочки для каждого из уже имеющихся нетерминалов грамматики и
   выделение следующих за цепочкой альтернатив в новый нетерминал."
  [grammar]
  (reduce (fn [grammar nt]
            (let [nt-rhs (get (:prods grammar) nt)]
              (if-let [[prefix rules] (select-rules-for-factorization nt-rhs)]
                (let [nt-rhs (apply disj nt-rhs rules)
                      new-nt (str (gensym (str nt "_")))
                      grammar (update grammar :nonterms conj new-nt)
                      nt-rhs (conj nt-rhs (conj (vec prefix) new-nt))
                      grammar (assoc-in grammar [:prods nt] nt-rhs)
                      prefix-count (count prefix)
                      new-nt-rhs (->> rules
                                      (map #(drop prefix-count %))
                                      (map #(if (empty? %) [(:epsilon grammar)] (vec %)))
                                      (set))
                      grammar (assoc-in grammar [:prods new-nt] new-nt-rhs)]
                  grammar)
                grammar)))
          grammar
          (:nonterms grammar)))

#_(left-factorization-1 {:nonterms #{"A" "B"}
                         :prods {"A" #{["a" "c" "d1"]
                                       ["a" "c" "d2"]
                                       ["a" "c"]
                                       ["z"]}
                                 "B" #{["p" "d"]}}
                         :epsilon "eps"})

(defn left-factorization [grammar]
  (loop [grammar grammar]
    (let [new-grammar (left-factorization-1 grammar)]
      (if (= new-grammar grammar)
        new-grammar
        (recur new-grammar)))))

#_(left-factorization {:nonterms #{"A" "B"}
                       :prods {"A" #{["a" "c" "d" "g" "k1"]
                                     ["a" "c" "d" "g" "k2"]
                                     ["a" "c"]
                                     ["z"]}
                               "B" #{["p" "d"]}}
                       :epsilon "eps"})
