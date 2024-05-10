(ns gql-fmt.intermediate.token
  "The set of tokens which consitute
   the intermediate form, each of which
   represents an item to be printed
   (including whitespace, brackets, etc...)
   in the formatted output"
  (:require
   [clojure.string :as string]))

(set! *warn-on-reflection* true)

(defn string-literal
  "Represents a string literal
   that should be printed 'as is'"
  [s]
  (assert (string? s))
  {:token :string-literal
   :content s})

(defn indent
  "Represents an indent at the
   level found in the context"
  [context]
  (assert
   (number? (:indent-level context)))
  {:token :indent
   :level (:indent-level context)})

(defn whitespace
  "Represents a region of whitespace
   between two other elements. `location`
   should correspond to a keyword in the
   :whitespace context entry.
   
   Optional `count` and `column` must
   be given for whitespace entries where
   there is the option to break or not
   break, e.g. long variable lists.
   `count` is the number of entities
   (e.g. number of variables), `column`
   is the start column of the first of
   those entities."
  ([context location]
   (whitespace context location 0 0))
  ([context location count column]
   (assert
    (contains?
     (:whitespace context)
     location)
    location)
   {:token :whitespace
    :location location
    :count count
    :column column}))

(defn syntax-element
  "Represents a syntax element meaningful
   in GraphQL, e.g. brackets or $ or !."
  [context category location]
  (assert
   (contains?
    context
    category)
   (string/join
    " "
    [category location]))
  (assert
   (contains?
    (category context)
    location)
   (string/join
    " "
    [category location]))
  {:token :syntax-element
   :category category
   :location location})
