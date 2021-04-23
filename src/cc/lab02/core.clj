(ns cc.lab02.core
  (:require [cc.lab02.helpers :refer [json->grammar grammar->json]]
            [cc.lab02.morph :as morph]
            [clojure.string :as s])
  (:gen-class))

(defn -main
  [g-left-path g-eps-path]
  (let [g-left (json->grammar g-left-path)
        g-left (morph/remove-left-recursion g-left)
        _ (grammar->json (s/replace g-left-path #".json$" "-result.json") g-left)

        g-eps (json->grammar g-eps-path)
        g-eps (morph/remove-epsilon-rules g-eps)
        _ (grammar->json (s/replace g-eps-path #".json$" "-result.json") g-eps)]))

#_(-main "./resources/grammar.json"
         "./resources/grammar-c.json")
