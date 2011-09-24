(ns clojworks.xml
  (:use clojure.contrib.str-utils))

;;==============================================================
;; GENERIC XML PRINTING SUPPORT FUNCTIONS
;;==============================================================

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
  (let [ el-name (el 0)
	attribs (el 1)
	text (el 2) ]
    (str (space indent)
	 (open-el el-name attribs) 
	 (if (nil? text) " />\n" (str ">" text "</" el-name ">\n")))))

(defn el-to-string
  ( [indent el-name]
      (single-line indent [el-name nil nil] ))
  ( [indent el-name attribs ]
      (single-line indent [el-name  attribs nil nil] ))
  ( [indent el-name attribs text]
      (single-line indent [el-name attribs text]))
  ( [indent el-name attribs text & children]
    (let [ prefix (space indent) ]
      (if (empty? children)
        (single-line indent [el-name attribs text] )
        (str prefix (open-el el-name attribs) ">\n"
             (str-join "\n" (map #(apply el-to-string (cons (+ 4 indent) %)) (filter #(not (empty? %)) children)))
	     prefix "</" el-name ">\n"))))
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
;;    [ el_name attribMapOrNil textOrNil [ child1 child2 ... ] ]
