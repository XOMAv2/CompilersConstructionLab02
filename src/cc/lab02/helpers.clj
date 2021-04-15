(ns cc.lab02.helpers
  (:require #_[clojure.xml :as xml]
            #_[clojure.data.zip.xml :refer [xml-> xml1->]]
            #_[clojure.zip :as zip]
            [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]))

(defn json->grammar [path]
  (let [json-string (slurp path)
        grammar (json/read-str json-string
                               :key-fn csk/->kebab-case-keyword)
        grammar (-> grammar
                    (update :terms set)
                    (update :nonterms set)
                    (update :prods set))]
    grammar))

(defn grammar->json [path grammar]
  (let [json-string (json/write-str grammar
                                    :key-fn csk/->camelCaseString)]
    (spit path json-string)))

#_(defn xml->grammar [path]
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
