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

(test/deftest query-with-multiple-unnested-field-names-one-parameter-test
  (let [q (str "query($id:String!)"
               "{"
                "user(id:$id){name(id:$id) job(id:$id)}"
                "account(id:$id){email(id:$id) username(id:$id)}"
                "}")
        expect (str
                "query ($id:String!) {" "\n"
                "  user (id:$id) {" "\n"
                "    name (id:$id)" "\n"
                "    job (id:$id)" "\n"
                "  }" "\n"
                "  account (id:$id) {" "\n"
                "    email (id:$id)" "\n"
                "    username (id:$id)" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-multiple-unnested-field-names-three-parameters-test
  (let [q (str "query($id:ID!,$email:String!,$age:Int!)"
               "{"
               "user(id:$id,email:$email,age:$age)"
               "{name(id:$id,email:$email)"
               "job(email:$email,age:$age)}"
               "account(age:$age,id:$id)"
               "{email(id:$id) 
                username(email:$email,id:$id,age:$age)}"
               "}")
        expect (str
                "query ($id:ID!, $email:String!, $age:Int!) {" "\n"
                "  user (id:$id, email:$email, age:$age) {" "\n"
                "    name (id:$id, email:$email)" "\n"
                "    job (email:$email, age:$age)" "\n"
                "  }" "\n"
                "  account (age:$age, id:$id) {" "\n"
                "    email (id:$id)" "\n"
                "    username (email:$email, id:$id, age:$age)" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
