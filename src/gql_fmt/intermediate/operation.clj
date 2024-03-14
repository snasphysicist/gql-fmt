(ns gql-fmt.intermediate.operation 
  (:require [taoensso.timbre :as logging]
            [gql-fmt.intermediate.token :as token]
            [gql-fmt.intermediate.variables :as variables]
            [gql-fmt.intermediate.selection-set :as selection-set]))

(set! *warn-on-reflection* true)

(defn from 
  "Produces the intermediate form corresponding 
   to the provided Alumbra operation"
  [context o]
  (logging/debug 
   "Operation -> intermediate form:" 
   o) 
  (let [type (:alumbra/operation-type o) 
        o-variables (:alumbra/variables o) 
        has-variables? (some? (seq o-variables))] 
    (flatten 
     [(token/string-literal 
       type) 
      (token/whitespace 
       context 
       :after-operation) 
      (when has-variables? 
        (variables/from context o-variables)) 
      (token/syntax-element 
       context 
       :bracket 
       :opening-operation) 
      (token/whitespace 
       context 
       :after-opening-operation) 
      (selection-set/from 
       context 
       (:alumbra/selection-set o))])))
