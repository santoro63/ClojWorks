(ns clojworks.xml
  (:use clojure.contrib.str-utils))

;;==============================================================
;; GENERIC XML PRINTING SUPPORT FUNCTIONS
;;==============================================================

(defn some? [ pred coll ]
  (if (empty? coll)
    false
    (or (pred (first coll)) (some? pred (rest coll)))))

(defn space
  "creates a string consisting of {size} spaces"
  [ size ]
  (if (= 0 size) "" (str " " (space (- size 1)))))

(defn attrib-str [ attr-map ]
  (str-join " " (map #(str % "=\"" (attr-map %) "\"") (keys attr-map))))

(defn open-el [el-name attribs]
  (str "<" el-name
       (if (not (empty? attribs)) (str " " (attrib-str attribs)))))

(defn single-line [indent el]
  (let [ el-name (get el 0)
	attribs (get el 1)
	text (get el 2) ]
    (str (space indent)
	 (open-el el-name attribs) 
	 (if (nil? text) "/>\n" (str ">" text "</" el-name ">\n")))))

(defn get-el-name [ el ]
  (first el))

(defn get-el-attribs [ el ]
  (if (map? (get el 1)) (get el 1) nil))

(defn get-el-text [ el ]
  (cond (string? (get el 1)) (get el 1)
	(string? (get el 2)) (get el 2)
	:default nil))
(defn get-el-children [ el ]
  (filter #(vector? %) el))

(defn el-to-string [ indent el ]
  (let [ name (get-el-name el)
	attribs (get-el-attribs el)
	text (get-el-text el)
	children (get-el-children el)
	prefix (space indent) ]
    (if (empty? children)
      	 (single-line indent [name attribs text ])
	 (str prefix (open-el name attribs) ">\n"
	      (str-join "" (map #(el-to-string (+ 4 indent) %) (filter #(not (empty? %)) children)))
		  prefix "</" name ">\n")))
)
;; pretty prints the xml data to a string according to the following
;; rules:
;;    (1) elements without children or text:
;;             <el attrib1="val1" attrib2="val2" ... />
;;    (2) elements with text:
;;             <el ...>text goes here</el>
;;    (3) elements with children follow the following format:
;;            <el>
;;                <child1>.....
;;                <child2>.....
;;            </el>
;;    (4) elements with children and text:
;;            <el>
;;                text
;;                <child1>....
;;                <child2>....
;;            </el>
;;
;; Children are always indented four spaces with respect to the parent
;;
;; xml-data is expected to be a vector in the following format:
;;    [ el_name attribMapOrNil textOrNil [ child1 ] [ child2 ] ...  ]
(defn pretty-print-xml [ xml-data ]
  (println (el-to-string 0 xml-data)))
