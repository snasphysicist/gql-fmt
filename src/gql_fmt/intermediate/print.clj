(ns gql-fmt.intermediate.print
  (:require [clojure.string :as string]))

(set! *warn-on-reflection* true)

(defn- whitespace->string
  [context token]
  (let [whitespace (:whitespace context)
        configuration (get whitespace (:location token))
        count (:count token)
        break-at (:break-at configuration)
        column (:column token)]
    (cond
      (string? configuration)
      configuration

      (not (and count break-at))
      (assert (and count break-at))

      (<= break-at count)
      (string/join
       [(:broken configuration)
        (string/join (repeatedly column (constantly " ")))])

      :else
      (:unbroken configuration))))

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
      (whitespace->string context token)

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
