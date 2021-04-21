(ns cc.lab02.core
  (:require [cc.lab02.helpers :refer [json->grammar grammar->json]]
            [cc.lab02.morph :as morph])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [g-0 (json->grammar "./resources/grammar.json")
        g-0 (morph/remove-left-recursion g-0)
        _ (grammar->json "./resources/grammar-result.json" g-0)

        g-eps (json->grammar "./resources/grammar-eps.json")
        g-eps (morph/remove-epsilon-rules g-eps)
        _ (grammar->json "./resources/grammar-eps-result.json" g-eps)]))

#_(-main)