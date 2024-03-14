(ns gql-fmt.configuration
  "Tools to provide/load formatting configuration")

(set! *warn-on-reflection* true)

(defn ^:private bracket
  "Configuration for brackets/parentheses
   used in formatted output"
  []
  {:closing-arguments ")"
   :closing-selection-set "}"
   :closing-variables ")"
   :opening-arguments "("
   :opening-operation "{"
   :opening-selection-set "{"
   :opening-variables "("})

(defn ^:private whitespace 
  "Configuration for whitespace between
   elements in formatted output"
  []
  {:after-arguments " "
   :after-field-name " "
   :after-opening-selection-set "\n"
   :after-opening-operation "\n"
   :after-operation " "
   :after-variables " "
   :between-arguments " "
   :between-selections "\n"
   :between-variables " "
   :indent "  "})

(defn ^:private delimiter 
  "Configuration of delimiters between
   elements in formatted output"
  []
  {:between-arguments ","
   :between-argument-and-type ":"
   :between-variables ","
   :between-variable-and-argument ":"})

(defn ^:private prefix
  "Configuration of prefixes prepended to
   elements in formatted output"
  []
  {:before-variables "$"})

(defn ^:private suffix
  "Configuration of suffixes appended to
   elements in formatted output"
  []
  {:after-non-null-arguments "!"})

(defn with-configuration
  "Adds configuration into context"
  [context]
  (merge 
   context
   {:bracket (bracket)
    :delimiter (delimiter)
    :newline (newline)
    :prefix (prefix)
    :suffix (suffix)
    :whitespace (whitespace)}))
