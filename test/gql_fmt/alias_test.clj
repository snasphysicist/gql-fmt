(ns gql-fmt.alias-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest alias-field-test
  (let [q "query {first:person(id:1){name} second:person(id:2){name}}"
        expect (str
                "query {" "\n"
                "  first: person (id:1) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "  second: person (id:2) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
