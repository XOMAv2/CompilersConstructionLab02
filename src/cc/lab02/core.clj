(ns cc.lab02.core
  (:require [cc.lab02.helpers :refer [json->grammar grammar->json]]
            [cc.lab02.morph :as morph]
            [clojure.string :as s])
  (:gen-class))

(defn -main
  [g-rec-path g-eps-path g-fact-path]
  (let [g-rec (json->grammar g-rec-path)
        g-rec (morph/remove-left-recursion g-rec)
        _ (grammar->json (s/replace g-rec-path #".json$" "-result.json") g-rec)

        g-eps (json->grammar g-eps-path)
        g-eps (morph/remove-epsilon-rules g-eps)
        _ (grammar->json (s/replace g-eps-path #".json$" "-result.json") g-eps)
        
        g-fact (json->grammar g-fact-path)
        g-fact (morph/left-factorization g-fact)
        _ (grammar->json (s/replace g-fact-path #".json$" "-result.json") g-fact)]))

#_(-main "./resources/grammar.json"
         "./resources/grammar-c.json"
         "./resources/grammar-simple.json")
