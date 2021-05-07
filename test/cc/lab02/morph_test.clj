(ns cc.lab02.morph-test
  (:require [clojure.test :refer [deftest is]]
            [cc.lab02.morph]))

(deftest longest-common-prefix-test
  (is (= '() (@#'cc.lab02.morph/longest-common-prefix)))
  (is (= '() (@#'cc.lab02.morph/longest-common-prefix [])))
  (is (= '(1 2) (@#'cc.lab02.morph/longest-common-prefix [1 2])))
  (is (= '(1 2 3) (@#'cc.lab02.morph/longest-common-prefix [1 2 3] [1 2 3])))
  (is (= '(1 2 3) (@#'cc.lab02.morph/longest-common-prefix [1 2 3] [1 2 3] [1 2 3 4])))
  (is (= '() (@#'cc.lab02.morph/longest-common-prefix [1 2 3] [3 4 5]))))
