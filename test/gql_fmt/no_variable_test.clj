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

(test/deftest query-with-one-nested-field-name-no-parameters-test
  (let [q "query{user{name}}"
        expect (str
                "query {" "\n"
                "  user {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat
                     q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-multiple-nested-field-names-no-parameters-test
  (let [q "query{user{name job}account{email username}}"
        expect (str
                "query {" "\n"
                "  user {" "\n"
                "    name" "\n"
                "    job" "\n"
                "  }" "\n"
                "  account {" "\n"
                "    email" "\n"
                "    username" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat
                     q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-one-deeply-nested-field-name-test
  (let [q "query{user{name{first{initial}}}}"
        expect (str
                "query {" "\n"
                "  user {" "\n"
                "    name {" "\n"
                "      first {" "\n"
                "        initial" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat
                     q)]
    (test/is (= expect reformatted))))

(test/deftest query-with-multiple-deeply-nested-field-names-no-parameters-test
  (let [q (str
           "query { "
           "user { "
           "name { "
           "first { initial full } "
           "last { initial full } "
           "} "
           "job { "
           "department { name location } "
           "responsibilities { technical managerial } "
           "} "
           "} "
           "account { "
           "email { "
           "history { current previous } "
           "verification { verified datetime } "
           "} "
           "payment { "
           "balance { value creditable } "
           "card { expiry issuer } "
           "} "
           "} "
           "} ")
        expect (str
                "query {" "\n"
                "  user {" "\n"
                "    name {" "\n"
                "      first {" "\n"
                "        initial" "\n"
                "        full" "\n"
                "      }" "\n"
                "      last {" "\n"
                "        initial" "\n"
                "        full" "\n"
                "      }" "\n"
                "    }" "\n"
                "    job {" "\n"
                "      department {" "\n"
                "        name" "\n"
                "        location" "\n"
                "      }" "\n"
                "      responsibilities {" "\n"
                "        technical" "\n"
                "        managerial" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "  account {" "\n"
                "    email {" "\n"
                "      history {" "\n"
                "        current" "\n"
                "        previous" "\n"
                "      }" "\n"
                "      verification {" "\n"
                "        verified" "\n"
                "        datetime" "\n"
                "      }" "\n"
                "    }" "\n"
                "    payment {" "\n"
                "      balance {" "\n"
                "        value" "\n"
                "        creditable" "\n"
                "      }" "\n"
                "      card {" "\n"
                "        expiry" "\n"
                "        issuer" "\n"
                "      }" "\n"
                "    }" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
