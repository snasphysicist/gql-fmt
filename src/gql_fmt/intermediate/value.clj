(ns gql-fmt.intermediate.value
  (:require
   [gql-fmt.intermediate.token :as token]))

(set! *warn-on-reflection* true)

(defn ^:private from-integer
  "Converts an integer value into intermediate form"
  [value]
  (assert
   (=
    :integer
    (:alumbra/value-type value)))
  [(token/string-literal
    (str (:alumbra/integer value)))])

(defn ^:private from-variable
  "Converts a variable value into intermediate form"
  [context value]
  (assert
   (=
    :variable
    (:alumbra/value-type value)))
  (let [variable-name (:alumbra/variable-name
                       value)]
    [(token/syntax-element
      context
      :prefix
      :before-variables)
     (token/string-literal variable-name)]))

(defn from
  [context value]
  (let [value-type (:alumbra/value-type
                    value)]
    (cond
      (= :variable value-type)
      (from-variable context value)

      (= :integer value-type)
      (from-integer value)

      :else
      (throw
       (Exception.
        (str "Unable to handle value type "
             value-type))))))
