(ns arty-rec.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [arty-rec.core :as arty-rec])
  (:import clojure.lang.ExceptionInfo))

(deftest flatten-test
  (testing "Really simple grammars work"
    (let [grammar (arty-rec/create {"origin" "hello", "second" "world"})]
      (is (= "hello" (arty-rec/flatten grammar)))
      (is (= "world" (arty-rec/flatten grammar "second")))
      (is (thrown? ExceptionInfo (arty-rec/flatten grammar "non-existing")))))
  (testing "Symbols are expanded"
    (let [grammar (arty-rec/create {"step1" "<s1>#step2#</s1>"
                                    "step2" "<s2>#step3#</s2>"
                                    "step3" "<s3/>"})]
      (is (= "<s1><s2><s3/></s2></s1>" (arty-rec/flatten grammar "step1")))))
  (testing "Choice works"
    (let [grammar (arty-rec/create {"single" ["hello"]
                                    "multi"  ["hello" "world"]})]
      (is (= "hello" (arty-rec/flatten grammar "single")))
      (is (#{"hello" "world"} (arty-rec/flatten grammar "multi"))))))
