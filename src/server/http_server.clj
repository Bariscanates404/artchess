(ns server.http-server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test-http]
            [clojure.data.json :as json]
            [io.pedestal.interceptor :as i]
            [clojure.java.io :as io]

            [server.model :as m]))

;; HANDLER
(def health-check
  (i/interceptor
    {:name  :health-check
     :enter (fn [context]
              (let [request      (:request context)
                    client-ip    (:remote-addr request)
                    current-time (System/currentTimeMillis)
                    resp-body    {:message   "Hello HTTP Client I'm Alive!"
                                  :timestamp current-time
                                  :client-ip client-ip}
                    resp         {:status  200
                                  :headers {"Content-Type" "application/json"}
                                  :body    resp-body}]
                (assoc context :response resp)))}))

(defn save-user [request]
  (let [user             (m/extract-body-ctx request)
        save-user-return (m/save-user! user)]
    (println request)
    (println user)
    (if save-user-return
      {:status 200
       :body   save-user-return}
      {:status 403
       :body   "Failed to save user"})))

(defn get-users [request]
  (let [users (json/read-str m/get-all-users)
        resp  {:status 200
               :body   users}]
    (println request)
    (json/read-str resp)))




;;## INDEX
;; Symbol receives a value during a binding function

(defn index-html []
  (slurp (io/resource "public/index.html")))

(def index-interceptor
  (i/interceptor
    {:name :home-page
     :enter
     (fn [context]
       (assoc context
         :response
         {:status  200                                      ;; 404 not found
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :body    (index-html)}))}))


(def login-interceptor
  (i/interceptor
    {:name  :login
     :enter (fn [context]
              (let [response (-> {:status 200
                                  :body   {:hello "world"
                                           :login "true"}}
                               (update :headers assoc "Set-Cookie" (str "Token=" "token" "; Path=/")))]
                (assoc context :response response)
                ))}))
#_(defn check-user-credentials [email password]
    (let [{:keys [user-id economical-group-id] :as user} (first (vec (db/query
                                                                       {:select [:u.tenant-id :u.user-id :u.economical-group-id :u.name :u.email :u.phone :u.password
                                                                                 :u.all-equipments :p.profile]
                                                                        :from   [[:user :u]]
                                                                        :join   [[:profile :p] [:= :u.profile-id :p.profile-id]]
                                                                        :where  [:= :email email]})))
          economical-group-data (first (vec (db/query
                                              {:select :*
                                               :from   :economical-group
                                               :where  [:= :economical-group-id economical-group-id]})))
          user-locations        (vec (db/query {:select [:l.location-id :l.location]
                                                :from   [[:user-in-location :ul]]
                                                :join   [[:location :l] [:= :l.location-id :ul.location-id]]
                                                :where  [:= :user-id user-id]}))

          server-credential     (get user :password)
          match-credential?     (when server-credential (= password server-credential))
          user                  (-> user
                                  (dissoc :password)
                                  (assoc :economical-group economical-group-data
                                         :user-locations user-locations))]
      (when match-credential?
        (assoc user :session (generate-session user)))))
#_(def login-interceptor
    (i/interceptor
      {:name  :login
       :enter (fn [context]
                (let [{:keys [email password]} (u/extract-body-ctx context)
                      session  (check-user-credentials email password)
                      response (if session
                                 (u/response session)
                                 (u/authentication-failed))]
                  (assoc context :response response)))}))


(def json-response-interceptor
  (i/interceptor
    {:name  :json-response
     :leave (fn [context]
              (update-in context [:response :body] json/write-str))}))



;; path method interceptor
(def routes
  (route/expand-routes
    #{["/" :get index-interceptor :route-name :index-page]
      ["/health" :get [health-check json-response-interceptor] :route-name :health-check]
      ["/save-user" :post save-user :route-name :save-user]
      ["/get-user" :get get-users :route-name :get-user]
      ["/api/login" :post login-interceptor :route-name :login]

      }))



(defn create-server []
  (http/create-server
    {::http/routes         routes                           ;; Application Programming Interface
     ::http/type           :jetty
     ::http/host           "0.0.0.0"                        ;; Necessary for Docker
     ::http/port           7777
     ::http/join?          false
     ::http/resource-path  "/public"
     ::http/secure-headers {:content-security-policy-settings {:object-src "none"}}}))

(defonce server (atom nil))

(defn stop []
  (when @server (http/stop @server)))

(defn start []
  (reset! server (http/start (create-server))))

(comment

  (start)
  (stop)
  (test-http/response-for (:io.pedestal.http/service-fn @server) :get "/health")
  (test-http/response-for (:io.pedestal.http/service-fn @server) :get "/get-user")

  (test-http/response-for
    (:io.pedestal.http/service-fn @server)
    :post
    "/save-user"
    :body (json/write-str {:name         "KKKK"
                           :email        "ads@foo.com"
                           :phone-number "asd"})
    :headers {"Content-Type" "application/json"}))




