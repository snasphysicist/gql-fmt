(ns gql-fmt.no-variable-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest query-with-one-unnested-field-name-no-parameters-test
  (let [q "query{user}"
        expect (str
                "query {" "\n"
                "  user" "\n"
                "}")
        reformatted (format/reformat
                     q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-two-unnested-field-names-no-parameters-test
  (let [q "query{user account}"
        expect (str
                "query {" "\n"
                "  user" "\n"
                "  account" "\n"
                "}")
        reformatted (format/reformat
                     q)]
    (test/is (= expect reformatted))))
