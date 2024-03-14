(ns gql-fmt.intermediate.print 
  (:require [clojure.string :as string]))

(set! *warn-on-reflection* true)

(defn- token->string
  "Stringify an intermediate form token"
  [context token]
  (let [type (:token token)]
    (case type
      
      :string-literal
      (:content token)
      
      :indent
      (string/join 
       (repeat 
        (:level token) 
        (-> context :whitespace :indent)))

      :whitespace
      (-> context 
          :whitespace 
          (get (:location token)))

      :syntax-element
      (-> context 
          (get (:category token)) 
          (get (:location token))))))

(defn ->string
  "Given a sequence of intermediate form
   tokens, converts them to strings"
  [context intermediate]
  (string/join
   (map
    #(token->string context %)
    intermediate)))
