(ns gql-fmt.variable-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest query-with-one-unnested-field-name-one-parameter-test
  (let [q "query($id:String!){user(id:$id){name}}"
        expect (str
                "query ($id:String!) {" "\n"
                "  user (id:$id) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-one-unnested-field-name-two-parameters-test
  (let [q (str "query($id:ID!,$email:String!)"
               "{user(id:$id,email:$email){name}}")
        expect (str
                "query ($id:ID!, $email:String!) {" "\n"
                "  user (id:$id, email:$email) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-one-unnested-field-name-three-parameters-test
  (let [q (str "query($id:ID!,$email:String!,$age:Int!)"
               "{user(id:$id,email:$email,age:$age){name}}")
        expect (str
                "query ($id:ID!, $email:String!, $age:Int!) {" "\n"
                "  user (id:$id, email:$email, age:$age) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
