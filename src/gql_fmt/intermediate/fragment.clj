(ns gql-fmt.intermediate.fragment
  (:require [gql-fmt.intermediate.selection-set :as selection-set]
            [gql-fmt.intermediate.token :as token]))

;; #:alumbra{:fragments 
;;  [
;;   #:alumbra{:fragment-name "names", 
;;    :metadata {:row 0, :column 9, :index 9}, 
;;    :type-condition #:alumbra{
;;        :type-name "Person", 
;;        :metadata {:row 0, :column 18, :index 18}
;;    }, 
;;    :selection-set [
;;       #:alumbra{:field-name "first", 
;;                 :metadata {:row 0, :column 27, :index 27}}
;;       #:alumbra{:field-name "last", 
;;                 :metadata {:row 0, :column 33, :index 33}}
;;                   ]}
;;   ], 
;;           :metadata {:row 0, :column 0, :index 0}}

(defn ^:private from-one
  "Converts a single fragment
   into intermediate form"
  [context fragment]
  (concat
   [(token/string-literal "fragment")
    (token/whitespace
     context
     :between-fragment-strings)
    (token/string-literal
     (:alumbra/fragment-name
      fragment))
    (token/whitespace
     context
     :between-fragment-strings)
    (token/string-literal "on")
    (token/whitespace
     context
     :between-fragment-strings)
    (token/string-literal
     (-> fragment
         :alumbra/type-condition
         :alumbra/type-name))
    (token/whitespace
     context
     :after-fragment)
    (token/syntax-element
     context
     :bracket
     :opening-fragment)
    (token/whitespace
     context
     :after-opening-selection-set)
    (selection-set/from
     context
     (:alumbra/selection-set fragment))]))

(defn from
  "Converts a sequence of fragments
   into intermediate form"
  [context fragments]
  (when (= 1 (count fragments))
    (from-one context (first fragments))))
