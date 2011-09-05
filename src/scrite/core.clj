(ns scrite.core
  (:use [scrite.shoot :only (start-shooting)]))

(defn -main
  []
  (start-shooting)
  ; Following is required to workaround a bug in clojure+leiningen,
  ; see http://stackoverflow.com/questions/7259072/clojure-java-shell-sh-throws-rejectedexecutionexception-when-run-in-a-new-thread/7293889#7293889
  @(promise))
