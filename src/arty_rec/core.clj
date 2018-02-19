(ns arty-rec.core
  (:refer-clojure :exclude [flatten])
  (:require [clojure.string :as string]))

(defrecord Symbol [target])
(defrecord Choice [targets])

(defn- interleave-long [x y]
  (let [min-length (min (count x) (count y))]
    (concat (interleave (take min-length x) (take min-length y))
            (drop min-length x) (drop min-length y))))

(defn- parse-string [s]
  (let [parts (string/split s #"#")
        plains (keep-indexed #(when (even? %1) (not-empty %2)) parts)
        symbols (keep-indexed #(when (odd? %1) (->Symbol %2)) parts)]
    (interleave-long plains symbols)))

(defn- parse [v]
  (cond
    (coll? v) [(->Choice (map parse v))]
    (string? v) (parse-string v)
    :else
    (throw (ex-info (str "Unable to parse " (pr-str v))
                    {:type ::parse-error
                     :value v}))))

(defn create
  "Create a grammar object from a Tracery grammar."
  [input]
  (into {} (for [[k v] input] [k (parse v)])))

(declare flatten flatten-value)

(defn- flatten-item [grammar value]
  (cond
    (string? value) value
    (instance? Symbol value) (flatten grammar (:target value))
    (instance? Choice value) (flatten-value grammar (rand-nth (:targets value)))
    :else
    ;; This is not supposed to happen.
    (throw (AssertionError. (str "Unable to process value " (pr-str value))
                            nil))))

(defn- flatten-value [grammar value]
  (apply str (map (partial flatten-item grammar) value)))

(defn generate
  "Generate a string from a grammar starting from the given symbol.

  The default symbol is `origin`."
  ([grammar] (flatten grammar "origin"))
  ([grammar target]
   (if-let [value (get grammar target)]
     (flatten-value grammar value)
     (throw (ex-info (str "Unknown target: " (pr-str target))
                     {:type ::unknown-target
                      :target target
                      :known-targets (keys grammar)})))))
