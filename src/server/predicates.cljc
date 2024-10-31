(ns server.predicates
  (:require
    [lambdaisland.uri :refer [uri]]  ;; For parsing URIs
    [clojure.string :as str]         ;; String utilities
    [nc-platform.schema-validations.country-codes :as cc]) ;; For ISO country code validation
  #?(:clj (:import (java.util UUID)))) ;; Import Java's UUID class in Clojure (server-side)

;; Predicate: Checks if a value matches the format of a valid Clojure symbol.
(defn symbol-string? [val]
  (boolean (re-matches #"[a-zA-Z][_0-9a-zA-Z]*" val))) ;; Regular expression ensures symbols start with a letter.

;; Example:
;; (symbol-string? "foo") ;; => true
;; (symbol-string? "_foo") ;; => false (invalid start)
;; (symbol-string? "foo123") ;; => true

;; Predicate: Checks if a collection contains only unique elements.
(defn unique? [coll]
  (or (empty? coll) (apply distinct? coll))) ;; Returns true if the collection is empty or all elements are distinct.

;; Example:
;; (unique? [1 2 3 4]) ;; => true
;; (unique? [1 2 3 2]) ;; => false

;; Predicate: Checks if a collection is unique by a specific key.
(defn unique-by?-fn [id-key]
  (fn [coll]
    (let [coll (if (map? coll) (vals coll) coll)  ;; If input is a map, extract values (ignoring keys)
          vals (map #(get % id-key) coll)]        ;; Extract the value associated with `id-key`
      (unique? vals))))                          ;; Check if the values are unique.

;; Example:
;; (unique-by?-fn :id [{:id 1} {:id 2} {:id 1}]) ;; => false (id 1 is repeated)
;; (unique-by?-fn :id [{:id 1} {:id 2} {:id 3}]) ;; => true

;; Predicate: Checks if a string is a valid UUID.
(defn uuid-string? [s]
  (try
    (let [parsed #?(:clj  (UUID/fromString s)   ;; In Clojure, parse the string using Java's UUID class.
                    :cljs (parse-uuid s))]     ;; In ClojureScript, use the `parse-uuid` function.
      (and (uuid? parsed)                      ;; Check if the parsed value is a valid UUID
           (= (str parsed) s)))                ;; Ensure the string matches the parsed UUID.
    #?(:clj (catch Exception _ false)           ;; Catch any exceptions during parsing and return false.
       :cljs (catch :default _ false))))

;; Example:
;; (uuid-string? "123e4567-e89b-12d3-a456-426614174000") ;; => true
;; (uuid-string? "invalid-uuid") ;; => false

;; Predicate: Checks if an email address is valid based on a regular expression.
(defn valid-email? [email]
  ;; Regex pattern for basic email validation.
  (let [pattern (re-pattern
                  (str "(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+"
                       "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                       "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
                       "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"))]
    (boolean (and (string? email)                ;; Ensure input is a string.
                  (re-matches pattern email))))) ;; Validate string against the regex pattern.

;; Example:
;; (valid-email? "user@example.com") ;; => true
;; (valid-email? "invalid-email")    ;; => false

;; Predicate: Checks if a string is a valid hex (hexadecimal) string.
(defn hex-string? [s]
  (re-matches #"[0-9A-Fa-f]{2,}" s))  ;; Regex pattern checks if the string contains valid hex characters.

;; Example:
;; (hex-string? "1A3F") ;; => true
;; (hex-string? "ZZZ")  ;; => false

;; Predicate: Checks if elements in a collection are ordered by a specific key.
(defn indexed-ordered-by?-fn [id-key]
  (fn [{:keys [elements order]}]
    (and
      (every? (fn [[k v]] (= k (get v id-key))) elements)  ;; Ensure keys in `elements` match the specified `id-key`.
      (->> elements
           vals
           (map #(get % id-key))      ;; Extract `id-key` from each element.
           set
           (= (set order))))))        ;; Check if the order matches the expected order.

;; Example:
;; (indexed-ordered-by?-fn :id {:elements {:a {:id 1}, :b {:id 2}}, :order [1 2]}) ;; => true
;; (indexed-ordered-by?-fn :id {:elements {:a {:id 2}, :b {:id 1}}, :order [1 2]}) ;; => false

;; Predicate: Checks if a URI-like string is valid.
(defn valid-uri? [uri-like]
  (let [{:keys [scheme host]} (uri uri-like)]   ;; Parse the URI string into components.
    (not (or (str/blank? scheme) (str/blank? host)))))  ;; Ensure that both scheme (e.g., "http") and host are present.

;; Example:
;; (valid-uri? "https://example.com") ;; => true
;; (valid-uri? "invalid-uri")         ;; => false
