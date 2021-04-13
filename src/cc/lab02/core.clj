(ns cc.lab02.core
  (:require [cc.lab02.helpers :refer [xml->map]])
  (:gen-class))

(xml->map "./resources/grammar.xml")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
