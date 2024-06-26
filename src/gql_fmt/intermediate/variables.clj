(ns gql-fmt.intermediate.variables
  (:require [gql-fmt.intermediate.token :as token]
            [gql-fmt.intermediate.type :as type]
            [gql-fmt.transform :as transform]
            [taoensso.timbre :as logging]))

(set! *warn-on-reflection* true)

(defn ^:private variable
  "Produces the intermediate form corresponding
   to a single variable from an Alumbra operation"
  [context variable]
  (logging/debug
   "Variable -> intermediate form"
   variable)
  (let [name (:alumbra/variable-name variable)]
    (remove
     nil?
     (concat
      [(token/syntax-element
        context
        :prefix
        :before-variables)
       (token/string-literal
        name)
       (token/syntax-element
        context
        :delimiter
        :between-variable-and-argument)]
      (type/from context (:alumbra/type variable))))))

(defn from
  "Produces the intermediate form corresponding
   to the provided variables from an Alumbra operation"
  [context variables]
  (logging/debug
   "Variables -> intermediate form"
   variables)
  (let [column (-> variables first :alumbra/metadata :column)
        intermediate (map
                      #(variable context %)
                      variables)
        delimited (transform/inner-interleaved
                   intermediate
                   [(token/syntax-element
                     context
                     :delimiter
                     :between-variables)
                    (token/whitespace
                     context
                     :between-variables
                     (count variables)
                     column)])
        with-brackets [(token/syntax-element
                        context
                        :bracket
                        :opening-variables)
                       delimited
                       (token/syntax-element
                        context
                        :bracket
                        :closing-variables)
                       (token/whitespace
                        context
                        :after-variables)]]
    with-brackets))

