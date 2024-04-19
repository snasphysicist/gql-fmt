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

