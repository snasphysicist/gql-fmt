(ns gql-fmt.fragment-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest fragment-definition-test
  (let [q "fragment names on Person { first last }"
        expect (str
                "fragment names on Person {" "\n"
                "  first" "\n"
                "  last" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest fragment-usage-test
  (let [q "query{user{...names}}"
        expect (str
                "query {" "\n"
                "  user {" "\n"
                "    ...names" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
