(ns gql-fmt.inline-value-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest query-with-inline-integer-value-test
  (let [q "query{name(id:6735)}"
        expect (str
                "query {" "\n"
                "  name (id:6735)" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-inline-string-value-test
  (let [q "query{name(id:\"person849304\")}"
        expect (str
                "query {" "\n"
                "  name (id:\"person849304\")" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-inline-boolean-value-test
  (let [q "query{name(full:true)}"
        expect (str
                "query {" "\n"
                "  name (full:true)" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-inline-list-string-value-test
  (let [q "query{name(ids:[\"person2398\",8347,true])}"
        expect (str
                "query {" "\n"
                "  name (ids:[\"person2398\", 8347, true])" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
