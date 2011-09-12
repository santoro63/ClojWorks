;; prints the difference between two jars
(import (java.util.jar JarFile))

(defn entry-size-map [ jar-file ]
  (let [ jar (new JarFile jar-file)
	entries (enumeration-seq (.entries jar))]
  (reduce #( merge %1 { (.getName %2) (.getSize %2) } ) { } entries)))

(defn only-in-first-map [m1 m2]
  (clojure.set/difference (set (.keySet m1)) (set (.keySet m2))))

(defn different-values-in-map [ m1 m2 ]
  (let [ common-keys (clojure.set/intersection (set (.keySet m1)) (set (.keySet m2))) ]
    (filter #(not (= (m1 %1) (m2%1))) common-keys)))

(defn print-list-with-header [ header print-body-func data-list ]
  (if (not (empty? data-list))
	   (do (println header)
	       (doseq [ x data-list ] (print-body-func x)))))

(def l1 (entry-size-map (first *command-line-args*)))
(def l2 (entry-size-map (first (rest *command-line-args*))))

(print-list-with-header "only in l1"
  #(println "---" %)
  (only-in-first-map l1 l2))
(print-list-with-header "only in l2"
  #(println "---" %)
  (only-in-first-map l2 l1))
(print-list-with-header "different"
  #(println "---" % " : " (l1 %) " <> " (l2 %))
  (different-values-in-map l1 l2))


