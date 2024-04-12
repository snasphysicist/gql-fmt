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

(test/deftest query-with-one-nested-field-name-multiple-parameters-test
  (let [q (str "query($id:ID!,$email:String!,$age:Int)"
               "{user(age:$age,email:$email,id:$id)"
               "{name(email:$email,id:$id)"
               "{first(age:$age)"
               "{initial(id:$id,email:$email,age:$age)}}}}")
        expect (str
                "query ($id:ID!, $email:String!, $age:Int) {" "\n"
                "  user (age:$age, email:$email, id:$id) {" "\n"
                "    name (email:$email, id:$id) {" "\n"
                "      first (age:$age) {" "\n"
                "        initial (id:$id, email:$email, age:$age)" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-multiple-deeply-nested-field-names-one-parameter-test
  (let [q (str
           "query($id:String!){"
           "user(id:$id){"
           "name(id:$id){"
           "first(id:$id){initial(id:$id) full(id:$id)}"
           "last(id:$id){initial(id:$id) full(id:$id)}}"
           "job(id:$id){"
           "department(id:$id){name(id:$id) location(id:$id)}"
           "responsibilities(id:$id){technical(id:$id) managerial(id:$id)}}"
           "}"
           "account(id:$id){"
           "email(id:$id){"
           "history(id:$id){current(id:$id) previous(id:$id)}"
           "verification(id:$id){verified(id:$id) datetime(id:$id)}}"
           "payment(id:$id){"
           "balance(id:$id){value(id:$id) creditable(id:$id)}"
           "card(id:$id){expiry(id:$id) issuer(id:$id)}}"
           "}}")
        expect (str
                "query ($id:String!) {" "\n"
                "  user (id:$id) {" "\n"
                "    name (id:$id) {" "\n"
                "      first (id:$id) {" "\n"
                "        initial (id:$id)" "\n"
                "        full (id:$id)" "\n"
                "      }" "\n"
                "      last (id:$id) {" "\n"
                "        initial (id:$id)" "\n"
                "        full (id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "    job (id:$id) {" "\n"
                "      department (id:$id) {" "\n"
                "        name (id:$id)" "\n"
                "        location (id:$id)" "\n"
                "      }" "\n"
                "      responsibilities (id:$id) {" "\n"
                "        technical (id:$id)" "\n"
                "        managerial (id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "  account (id:$id) {" "\n"
                "    email (id:$id) {" "\n"
                "      history (id:$id) {" "\n"
                "        current (id:$id)" "\n"
                "        previous (id:$id)" "\n"
                "      }" "\n"
                "      verification (id:$id) {" "\n"
                "        verified (id:$id)" "\n"
                "        datetime (id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "    payment (id:$id) {" "\n"
                "      balance (id:$id) {" "\n"
                "        value (id:$id)" "\n"
                "        creditable (id:$id)" "\n"
                "      }" "\n"
                "      card (id:$id) {" "\n"
                "        expiry (id:$id)" "\n"
                "        issuer (id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-multiple-deeply-nested-field-names-multiple-parameters-test
  (let [q (str
           "query($id:ID,$email:String!,$age:Int!){"
           "user(id:$id,email:$email){"
           "name(email:$email,age:$age){"
           "first(age:$age,id:$id,email:$email){"
           "initial(id:$id) full(email:$email,age:$age)}"
           "last(id:$id,age:$age){"
           "initial(email:$email,id:$id) full(id:$id,email:$email)}}"
           "job(age:$age){"
           "department(age:$age,email:$email){"
           "name(id:$id,age:$age) location(email:$email,id:$id,age:$age)}"
           "responsibilities(email:$email){"
           "technical(age:$age,email:$email) managerial(age:$age,id:$id)}}}"
           "account{"
           "email(id:$id,email:$email,age:$age){"
           "history{"
           "current(email:$email) previous(email:$email,age:$age,id:$id)}"
           "verification(email:$email,age:$age){"
           "verified(age:$id,id:$email) datetime}}"
           "payment(age:$email,id:$age,email:$id){"
           "balance(id:$id,age:$age){"
           "value(id:$id,email:$email) creditable(age:$age,email:$email)}"
           "card(age:$age,id:$id){"
           "expiry(age:$age,email:$email) issuer(email:$email,id:$id)}}}}")
        expect (str
                "query ($id:ID, $email:String!, $age:Int!) {" "\n"
                "  user (id:$id, email:$email) {" "\n"
                "    name (email:$email, age:$age) {" "\n"
                "      first (age:$age, id:$id, email:$email) {" "\n"
                "        initial (id:$id)" "\n"
                "        full (email:$email, age:$age)" "\n"
                "      }" "\n"
                "      last (id:$id, age:$age) {" "\n"
                "        initial (email:$email, id:$id)" "\n"
                "        full (id:$id, email:$email)" "\n"
                "      }" "\n"
                "    }" "\n"
                "    job (age:$age) {" "\n"
                "      department (age:$age, email:$email) {" "\n"
                "        name (id:$id, age:$age)" "\n"
                "        location (email:$email, id:$id, age:$age)" "\n"
                "      }" "\n"
                "      responsibilities (email:$email) {" "\n"
                "        technical (age:$age, email:$email)" "\n"
                "        managerial (age:$age, id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "  account {" "\n"
                "    email (id:$id, email:$email, age:$age) {" "\n"
                "      history {" "\n"
                "        current (email:$email)" "\n"
                "        previous (email:$email, age:$age, id:$id)" "\n"
                "      }" "\n"
                "      verification (email:$email, age:$age) {" "\n"
                "        verified (age:$id, id:$email)" "\n"
                "        datetime" "\n"
                "      }" "\n"
                "    }" "\n"
                "    payment (age:$email, id:$age, email:$id) {" "\n"
                "      balance (id:$id, age:$age) {" "\n"
                "        value (id:$id, email:$email)" "\n"
                "        creditable (age:$age, email:$email)" "\n"
                "      }" "\n"
                "      card (age:$age, id:$id) {" "\n"
                "        expiry (age:$age, email:$email)" "\n"
                "        issuer (email:$email, id:$id)" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
