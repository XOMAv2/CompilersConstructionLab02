(ns cc.lab02.xml
  (:require [clojure.xml :as xml]
            [clojure.data.zip.xml :refer [xml-> xml1->]]
            [clojure.zip :as zip]))

(defn xml->grammar [path]
  (let [grammar (zip/xml-zip (xml/parse path))
        terms (->> (xml-> grammar :grammar :terminalsymbols :term)
                   (map zip/node)
                   (map :attrs)
                   (set))
        nonterms (->> (xml-> grammar :grammar :nonterminalsymbols :nonterm)
                      (map zip/node)
                      (map :attrs)
                      (set))
        prods (-> grammar
                  (xml-> :grammar :productions :production)
                  (->> (map zip/node)))
        lhs (->> prods
                 (map zip/xml-zip)
                 (map #(xml1-> % :production :lhs))
                 (map zip/node)
                 (map :attrs))
        rhs (->> prods
                 (map zip/xml-zip)
                 (map #(xml-> % :production :rhs :symbol))
                 (map #(map zip/node %))
                 (map #(map :attrs %))
                 (map vec))
        prods (map #(hash-map :lhs % :rhs %2) lhs rhs)
        start-symbol (->> (xml1-> grammar :grammar :startsymbol)
                          (zip/node)
                          (:attrs))]
    {:terms terms
     :nonterms nonterms
     :prods prods
     :start-symbol start-symbol}))
