(ns gql-fmt.intermediate.type
  (:require
   [gql-fmt.intermediate.token :as token]
   [taoensso.timbre :as logging]))

(declare from)

(defn ^:private from-named-type
  "Convert an alumbra named type to intermediate form"
  [context t]
  (let [type-name (:alumbra/type-name t)
        non-null? (:alumbra/non-null? t)
        null-bang (when non-null?
                    (token/syntax-element
                     context
                     :suffix
                     :after-non-null-arguments))]
    (logging/debug
     "Named type -> intermediate"
     "with name"
     type-name
     "non null?"
     non-null?
     "full"
     t)
    [(token/string-literal
      type-name)
     null-bang]))

(defn ^:private from-list-type
  "Convert an alumbra list type to intermediate form"
  [context t]
  (let [element-type (:alumbra/element-type t)
        non-null? (:alumbra/non-null? t)
        null-bang (when non-null?
                    (token/syntax-element
                     context
                     :suffix
                     :after-non-null-arguments))]
    (logging/debug
     "List type -> intermediate"
     "non null?"
     non-null?
     "full"
     t)
    [(token/syntax-element
      context
      :bracket
      :opening-list)
     (from context element-type)
     (token/syntax-element
      context
      :bracket
      :closing-list)
     null-bang]))

(defn from
  "Converts and :alumbra/type from a variable
   into the intermediate form"
  [context t]
  (let [type-class (:alumbra/type-class t)]
    (cond
      (= :named-type type-class)
      (from-named-type context t)

      (= :list-type type-class)
      (from-list-type context t)

      :else
      (throw
       (Exception.
        (str "Unable to handle type class "
             type-class))))))
