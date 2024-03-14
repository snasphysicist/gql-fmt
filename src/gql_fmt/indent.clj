(ns gql-fmt.indent)

(set! *warn-on-reflection* true)

(defn increase 
  "Increase the current indent level by one"
  [context]
  (update-in
   context
   [:indent-level]
   inc))

(defn decrease
  "Decrease the current indent level by one"
  [context]
  (update-in
   context
   [:indent-level]
   dec))
