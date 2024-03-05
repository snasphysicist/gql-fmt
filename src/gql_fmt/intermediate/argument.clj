(ns gql-fmt.intermediate.argument 
  (:require [taoensso.timbre :as logging]
            [gql-fmt.transform :as transform]
            [gql-fmt.intermediate.token :as token]))

(defn- from
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
                      (token/syntax-element
                       context
                       :prefix
                       :before-variables)
                      (token/string-literal variable-name)]]
    (assert 
     (= type :variable)
     (str 
      "Don't know what to do with arguments of type "
      type))
    (logging/debug
     "Formatting argument"
     argument
     "with name"
     argument-name
     "with type"
     type
     "with variable name"
     variable-name
     "as"
     intermediate)
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
                                (repeat
                                 [(token/syntax-element
                                   context
                                   :delimiter
                                   :between-arguments)
                                  (token/whitespace
                                   context
                                   :between-arguments)]))
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
