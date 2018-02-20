(ns arty-rec.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [arty-rec.core :as arty-rec])
  (:import clojure.lang.ExceptionInfo))

(deftest generate-test
  (testing "Really simple grammars work"
    (let [grammar (arty-rec/create {"origin" "hello", "second" "world"})]
      (is (= "hello" (arty-rec/generate grammar)))
      (is (= "world" (arty-rec/generate grammar "second")))
      (is (thrown? ExceptionInfo (arty-rec/generate grammar "non-existing")))))
  (testing "Symbols are expanded"
    (let [grammar (arty-rec/create {"step1" "<s1>#step2#</s1>"
                                    "step2" "<s2>#step3#</s2>"
                                    "step3" "<s3/>"})]
      (is (= "<s1><s2><s3/></s2></s1>" (arty-rec/generate grammar "step1")))))
  (testing "Choice works"
    (let [grammar (arty-rec/create {"single" ["hello"]
                                    "multi"  ["hello" "world"]})]
      (is (= "hello" (arty-rec/generate grammar "single")))
      (is (#{"hello" "world"} (arty-rec/generate grammar "multi")))))
  (testing "Modifiers work"
    (let [grammar (arty-rec/create {"base" "hello"
                                    "capitalize" "#base.capitalize#"})]
      (is (= "Hello" (arty-rec/generate grammar "capitalize"))))))
