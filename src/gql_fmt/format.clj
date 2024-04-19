(ns gql-fmt.format
  (:require
   [alumbra.parser :as parser]
   [clojure.string :as string]
   [gql-fmt.configuration :as configuration]
   [gql-fmt.intermediate.fragment :as intermediate.fragment]
   [gql-fmt.intermediate.operation :as intermediate.operation]
   [gql-fmt.intermediate.print :as print]
   [taoensso.timbre :as logging]))

(set! *warn-on-reflection* true)

(defn ^:private set-log-level!
  "Sets the log level from the environment,
   if given, else defaults to :error"
  []
  (let [from-environment (System/getenv
                          "GQL_FMT_LOG_LEVEL")
        level (if (string/blank? from-environment)
                "error"
                from-environment)
        as-keyword (-> level
                       string/lower-case
                       keyword)]
    (assert
     (#{:trace :debug :info :warn :error :fatal :report}
      as-keyword))
    (logging/set-min-level! as-keyword)))

(set-log-level!)

(defn reformat
  "Reformat given string query `q`
   into configured standard format"
  [q]
  (let [parsed (parser/parse-document q)
        fragments (:alumbra/fragments parsed)
        operations (:alumbra/operations parsed)
        _ (assert
           (< (count fragments) 2)
           (str
            "0-1 top level fragments assumed, found "
            (count fragments)))
        _ (assert
           (< (count operations) 2)
           (str
            "0-1 top level operations assumed, found "
            (count operations)))
        context {:indent-level 0}
        context (configuration/with-configuration
                  context)
        intermediate (intermediate.fragment/from
                      context
                      fragments)
        intermediate (concat
                      intermediate
                      (intermediate.operation/from
                       context
                       operations))
        intermediate (flatten intermediate)
        intermediate (remove nil? intermediate)]
    (logging/debug
     "Intermediate form"
     intermediate)
    (print/->string context intermediate)))
