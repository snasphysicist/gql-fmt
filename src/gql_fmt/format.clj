(ns gql-fmt.format
  (:require
   [alumbra.parser :as parser]
   [gql-fmt.configuration :as configuration]
   [gql-fmt.intermediate.operation :as intermediate.operation]
   [gql-fmt.intermediate.print :as print]
   [taoensso.timbre :as logging]))

(set! *warn-on-reflection* true)

(defn reformat
  "Reformat given string query `q`
   into configured standard format"
  [q]
  (let [parsed (parser/parse-document q)
        operations (:alumbra/operations parsed)
        _ (assert
           (= 1 (count operations))
           (str
            "One top level operation is assumed, found"
            (count operations)))
        context {:indent-level 0}
        context (configuration/with-configuration
                  context)
        intermediate (intermediate.operation/from
                      context
                      (first operations))
        intermediate (flatten intermediate)
        intermediate (remove nil? intermediate)]
    (logging/debug
     "Intermediate form"
     intermediate)
    (print/->string context intermediate)))
