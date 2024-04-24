(ns gql-fmt.intermediate.argument
  (:require [gql-fmt.intermediate.token :as token]
            [gql-fmt.intermediate.value :as value]
            [gql-fmt.transform :as transform]
            [taoensso.timbre :as logging]))

(set! *warn-on-reflection* true)

(defn ^:private from
  "Converts a single argument to the intermediate form"
  [context argument]
  (let [argument-name (:alumbra/argument-name argument)
        type (-> argument
                 :alumbra/argument-value
                 :alumbra/value-type)
        variable-name (-> argument
                          :alumbra/argument-value
                          :alumbra/variable-name)
        intermediate [(token/string-literal argument-name)
                      (token/syntax-element
                       context
                       :delimiter
                       :between-argument-and-type)
                      (value/from
                       context
                       (:alumbra/argument-value
                        argument))]]
    (logging/debug
     "Formatting argument with name"
     argument-name
     "with type"
     type
     "with variable name"
     variable-name
     "as"
     intermediate
     "full"
     argument)
    intermediate))

(defn from-many
  "Given a sequence of arguments, 
   converts them to the intermediate form"
  [context arguments]
  (logging/debug
   "Formatting arguments"
   arguments)
  (let [intermediate-arguments (transform/inner-interleaved
                                (mapv
                                 #(from context %)
                                 arguments)
                                [(token/syntax-element
                                  context
                                  :delimiter
                                  :between-arguments)
                                 (token/whitespace
                                  context
                                  :between-arguments)])
        bracketed [(token/syntax-element
                    context
                    :bracket
                    :opening-arguments)
                   intermediate-arguments
                   (token/syntax-element
                    context
                    :bracket
                    :closing-arguments)]]
    (logging/debug
     "Formatted arguments as"
     bracketed)
    bracketed))
