(ns cc.lab02.helpers
  (:require [clojure.data.json :as json]
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
