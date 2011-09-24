(ns testspace
  (:use clojworks.xml clojure.test))

(deftest test-single-line
  (is (= "<foo />\n" (el-to-string 0 "foo")))
  (is (= "    <foo />\n" (el-to-string 4 "foo")))
  )

(deftest test-attributes
  (is (= "<foo />\n" (el-to-string 0 "foo" nil )))
  (is (= "<foo />\n" (el-to-string 0 "foo" { } )))
  (is (= "<foo a=\"1\" />\n" (el-to-string 0 "foo" { "a" "1" })))
  (is (= "<foo a=\"1\" b=\"2\" />\n" (el-to-string 0 "foo" { "a" "1" "b" "2" })))
  )

(deftest test-text
  (is (= "<bar />\n" (el-to-string 0 "bar" nil nil)))
  (is (= "<bar></bar>\n" (el-to-string 0 "bar" nil "")))
  (is (= "<bar>one two three</bar>\n" (el-to-string 0 "bar" nil "one two three")))
  )

(deftest test-with-children
  (is (= "<foo>\n</foo>\n" (el-to-string 0 "foo" nil nil nil )))
  (is (= "<foo>\n</foo>\n" (el-to-string 0 "foo" nil nil [ ] )))
  (is (= "<foo>\n    <bar />\n</foo>\n" (el-to-string  0 "foo" nil nil [ "bar" ]  )))
  (is (= "<foo>\n    <bar />\n</foo>\n" (el-to-string  0 "foo" nil nil [ "bar" ]  )))
  )
(run-tests)
