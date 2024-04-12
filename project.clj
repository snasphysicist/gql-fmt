(defproject pw.snas/gql-fmt "0.2.0-SNAPSHOT"
  :description "A GraphQL query formatter" 
  :url "https://github.com/snasphysicist/gql-fmt"
  :license {:name "MIT"
            :url "https://mit-license.org"}
  :dependencies [[alumbra/parser "0.1.7"]
                 [com.taoensso/timbre "6.3.1"]
                 [org.clojure/clojure "1.11.1"]]
  :main gql-fmt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
