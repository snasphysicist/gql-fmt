(ns gql-fmt.indent)

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
