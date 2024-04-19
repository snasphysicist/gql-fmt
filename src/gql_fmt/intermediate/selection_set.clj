(ns gql-fmt.intermediate.selection-set
  (:require [clojure.string :as string]
            [gql-fmt.indent :as indent]
            [gql-fmt.intermediate.argument :as argument]
            [gql-fmt.intermediate.token :as token]
            [taoensso.timbre :as logging]))

(set! *warn-on-reflection* true)

(declare from)

(defn ^:private from-fragment
  "Converts a fragment seletion 
   into intermediate form"
  [context selection]
  (assert
   (not
    (string/blank?
     (:alumbra/fragment-name
      selection))))
  (let [context (indent/increase context)]
    [(token/indent context)
     (token/syntax-element
      context
      :prefix
      :before-fragment-name)
     (token/string-literal
      (:alumbra/fragment-name selection))]))

(defn ^:private from-field
  "Converts a field seletion 
   into intermediate form"
  [context selection]
  (let [context (indent/increase context)
        {field-name :alumbra/field-name
         sub-selection-set :alumbra/selection-set
         arguments :alumbra/arguments} selection
        name field-name
        has-sub-selection-set? (some? (seq sub-selection-set))
        has-arguments? (some? (seq arguments))]
    (logging/debug
     "Formatting selection"
     selection
     "name"
     name
     "with sub selection set?"
     has-sub-selection-set?
     "with arguments?"
     has-arguments?)
    (let [intermediate-arguments (when has-arguments?
                                   [(token/whitespace
                                     context
                                     :after-field-name)
                                    (argument/from-many
                                     context
                                     arguments)])
          intermediate-sub-selection-set (when has-sub-selection-set?
                                           (let [initial-whitespace (if has-arguments?
                                                                      (token/whitespace
                                                                       context
                                                                       :after-arguments)
                                                                      (token/whitespace
                                                                       context
                                                                       :after-field-name))]
                                             [initial-whitespace
                                              (token/syntax-element
                                               context
                                               :bracket
                                               :opening-selection-set)
                                              (token/whitespace
                                               context
                                               :after-opening-selection-set)
                                              (from context sub-selection-set)]))
          intermediate-selection [(token/indent context)
                                  (token/string-literal name)
                                  intermediate-arguments
                                  intermediate-sub-selection-set]]
      (logging/debug
       "Formatted field selection as"
       intermediate-selection)
      intermediate-selection)))

(defn ^:private from-selection
  "Produces the intermediate form corresponding 
   to the provided Alumbra selection"
  [context selection]
  (cond
    (not
     (string/blank?
      (:alumbra/fragment-name
       selection)))
    (from-fragment context selection)

    (not
     (string/blank?
      (:alumbra/field-name
       selection)))
    (from-field context selection)

    :else
    (throw
     (Exception.
      (str
       "Cannot handle selection "
       selection)))))

(defn from
  "Produces the intermediate form corresponding 
   to the provided Alumbra selection-set"
  [context selection-set]
  (logging/debug
   "Formatting"
   (count selection-set)
   "selections")
  (let [intermediate (mapv
                      #(from-selection context %)
                      selection-set)
        with-newlines (vec
                       (interleave
                        intermediate
                        (repeat (token/whitespace
                                 context
                                 :between-selections))))
        with-closer (conj
                     with-newlines
                     (token/indent context)
                     (token/syntax-element
                      context
                      :bracket
                      :closing-selection-set))]
    with-closer))
