(ns gql-fmt.intermediate.selection-set 
  (:require [taoensso.timbre :as logging]
            [gql-fmt.intermediate.token :as token]
            [gql-fmt.intermediate.argument :as argument]
            [gql-fmt.indent :as indent]))

(declare from)

(defn- from-selection
  "Produces the intermediate form corresponding 
   to the provided Alumbra selection"
  [context selection]
  (let [context (indent/increase context)
        {field-name :alumbra/field-name
         sub-selection-set :alumbra/selection-set
         arguments :alumbra/arguments} selection
        has-sub-selection-set? (some? (seq sub-selection-set))
        has-arguments? (some? (seq arguments))]
    (logging/debug
     "Formatting selection"
     selection
     "name"
     field-name
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
          intermediate-selection-set [(token/indent context)
                                      (token/string-literal field-name)
                                      intermediate-arguments
                                      intermediate-sub-selection-set]]
      (logging/debug 
       "Formatted selection set as"
       intermediate-selection-set)
      intermediate-selection-set)))

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
