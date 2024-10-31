(ns server.utils
  (:require [clojure.walk :as walk]
            [clojure.string :as str]
            ))

(defn ->kebab-case [m]
  (let [kebab-case-key (fn [k]
                         (if (keyword? k)
                           (-> k
                               name
                               (str/replace "_" "-")
                               keyword)
                           k))]
    (walk/postwalk
      (fn [x]
        (if (map? x)
          (into {} (map (fn [[k v]] [(kebab-case-key k) v]) x))
          x))
      m)))


;; Generate a UUID using JavaScript's `crypto` API
(defn generate-uuid []
      (let [template "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx"]
           (.replace template
                     #"[xy]"
                     (fn [c]
                         (let [r (bit-and (rand-int 16) (if (= c "x") 15 3))]
                              (.toString (bit-or r (if (= c "x") 0 8)) 16))))))