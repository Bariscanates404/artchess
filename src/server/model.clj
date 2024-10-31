(ns server.model
  ;; Require necessary namespaces
  (:require [clojure.java.jdbc :as j]                       ;; `clojure.java.jdbc` is used for interacting with a SQL database
            [honey.sql :as sql]                             ;; `honey.sql` are used for building SQL queries
            [honey.sql.helpers :as h]
            [clojure.data.json :as json]
            [aero.core :as aero]
            ))

;; Define the connection string for connecting to the PostgreSQL database
(def conn-str (:db-conn (:db-conn-config (aero/read-config (clojure.java.io/resource "config.edn") {:profile :dev}))))

;; Alias for the `honey.sql/format` function, which converts HoneySQL data structures to SQL strings
(def fmt sql/format)

;; Define formatting parameters for the HoneySQL `format` function
(def format-params {:checking :strict
                    :params   {}
                    :pretty   true
                    :quoted   nil
                    :dialect  :ansi})

(defn execute! [q]
  ;; TODO: use proper log mechanism.
  (let [sql-string (fmt q format-params)]
    (println q)
    (println sql-string)
    (j/db-do-prepared conn-str sql-string)))

(defn query! [q]
  (let [sql-string (fmt q format-params)]
    (println q)
    (println sql-string)
    (j/query conn-str sql-string)))


;; TODO: Match the columns with the payload from user-save form
;; Define a HoneySQL data structure representing the DDL (Data Definition Language) for creating the `web_user` table
(def user-table-ddl {:create-table [:web-user]              ;; Define a `CREATE TABLE` statement for the `web_user` table
                     :with-columns [[:user-id :serial :unique] ;; Define `user-id` as a unique serial (auto-increment) column
                                    [:name :text]           ;; Define `name` as a text column
                                    [:email :text]
                                    [:phone-number :text]
                                    ;[:role :integer]
                                    [[:primary-key :user-id]]]}) ;; Define `user-id` as the primary key of the table


(def lesson-table-ddl {:create-table [:lesson]
                       :with-columns [[:lesson-id :serial :unique]
                                      [:lesson-name :text]
                                      [:teacher-id :int]
                                      [:student-id :int]
                                      ;[:role :integer]
                                      [[:primary-key :lesson-id]]]})











(comment
  (DANGER-create-data-model)

  )

(defn DANGER-create-data-model []
  (j/db-do-prepared conn-str "DROP TABLE web_user")
  (j/db-do-prepared conn-str "DROP TABLE lesson")
  (j/db-do-prepared conn-str (fmt user-table-ddl) format-params)
  (j/db-do-prepared conn-str (fmt lesson-table-ddl) format-params)
  )

(defn save-user! [user]
  (let [save-ok? (execute! {:insert-into :web-user
                            :values      [user]})])
  ;(if save-ok?
  ;  (get-users )
  ; error)
  ;

  ;; TODO: After saving the entity user on the table, get the user in a query to be sure to return latest data correct to API.
  )

(defn get-all-users []
  (query! {:select [:*] :from [:web-user]}))


(defn extract-body-ctx [request]
  (-> request
    :body
    slurp
    (json/read-str :key-fn keyword)))



(comment
  (j/db-do-prepared conn-str (fmt user-table-ddl))

  "DATA DEFINITION LANGUAGE - Creating tables in the db SCHEMA"
  ;; Format the `user-table-ddl` HoneySQL data structure into a SQL string
  (fmt user-table-ddl)
  (query! {:select [:*]
           :from   [:web-user]})

  ;; Example of dropping the `web_user` table if it exists
  (j/db-do-prepared conn-str "DROP TABLE web_user")
  (j/db-do-prepared conn-str "DROP TABLE lesson")

  ;; Example of creating the `web_user` table using the formatted SQL string from the `user-table-ddl` definition
  (j/db-do-prepared conn-str (fmt user-table-ddl) format-params)
  (j/db-do-prepared conn-str (fmt lesson-table-ddl) format-params)

  )
