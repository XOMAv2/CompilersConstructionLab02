(defproject cc.lab02 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.zip "1.0.0"]
                 [camel-snake-kebab "0.4.2"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [org.clojure/data.json "2.0.2"]]
  :main ^:skip-aot cc.lab02.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
