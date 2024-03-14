(ns gql-fmt.transform)

(set! *warn-on-reflection* true)

(defn inner-interleaved
  "Interleave e between the 
   elements of c1"
  [c1 e]
  (drop-last
   (interleave
    c1
    (repeatedly
     (constantly 
      e)))))
