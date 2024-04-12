(ns gql-fmt.core
  (:gen-class)
  (:require [gql-fmt.format :as format]))

(set! *warn-on-reflection* true)

(defn -main
  [& _]
  (println (format/reformat "query { hello_world }")))
