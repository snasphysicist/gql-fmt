(ns gql-fmt.many-variables-test
  (:require
   [clojure.test :as test]
   [gql-fmt.format :as format]))

(test/deftest query-with-one-unnested-field-name-one-parameter-test
  (let [q (str
           "query($a:String!,$b:Int,$c:Boolean!,$d:Float)"
           "{user(a:$a,b:$b,c:$c,d:$d){name}}")
        expect (str
                "query ($a:String!," "\n"
                "       $b:Int," "\n"
                "       $c:Boolean!," "\n"
                "       $d:Float) {" "\n"
                "  user (a:$a," "\n"
                "        b:$b," "\n"
                "        c:$c," "\n"
                "        d:$d) {" "\n"
                "    name" "\n"
                "  }" "\n"
                "}")
        reformatted (format/reformat q)]
    (test/is (= expect reformatted))))
