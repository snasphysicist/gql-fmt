(ns gql-fmt.list-variable-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest query-with-list-variable-test
  (let [q "query($a:[Int],$b:[Int!],$c:[Int]!,$d:[Int!]!){name}"
        expect (str
                "query ($a:[Int]," "\n"
                "       $b:[Int!]," "\n"
                "       $c:[Int]!," "\n"
                "       $d:[Int!]!) {" "\n"
                "  name" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
