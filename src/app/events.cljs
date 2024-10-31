(ns app.events
  (:require [re-frame.core :as rf]
            [app.navigation :as nav]))

;; Documentation for `app.events` namespace

;; The `app.events` namespace is responsible for defining and registering events
;; that trigger changes to the application's local database (also known as the app state).
;; Events are the primary means by which the state of the application is updated
;; in response to user actions or other triggers.

;; General Usage:
;; To create a new event, use the `rf/reg-event-db` function, which takes an event ID and an
;; event handler function. The event handler function receives the current application state
;; (db) and an event vector. The event vector's first element is the event ID, and subsequent
;; elements are the event's parameters. The event handler function returns the updated state.
;;
;; Example:
;; (rf/reg-event-db
;;   :example-event
;;   (fn [db [_ param1 param2]]
;;     (assoc db :example-key (do-something-with param1 param2))))
;;
;; This example registers an event with the ID `:example-event` that updates the state by
;; associating a new value with the `:example-key` based on `param1` and `param2`.
(rf/reg-event-db
  :health-ok
  (fn [db [_ response]]
    ;; (js/console.log "Health OK response:" (clj->js response))
    (assoc db :health-response response)))

(rf/reg-event-db
  :health-failed
  (fn [db [_ response]]
    ;; (js/console.log "Health Failed response:" (clj->js response))
    (assoc db :health-response :failure)))

(rf/reg-event-fx
  :get-server-hello
  (fn [_]
    (js/console.log "Dispatching health check request")
    {:http-request {:uri        "/health"
                    :method     :get
                    :on-success [:health-ok]
                    :on-failure [:health-failed]}}))

(rf/reg-event-db :app/init-db
  (fn [db [_ initial-db]]
    (merge db initial-db)))



;; ####################################################################################################
#_(rf/reg-fx
    :user-operation-success
    (fn [success-callback]
      (success-callback)))

(rf/reg-event-db
  :create-user
  (fn [db [_ user]]
    (update db :users conj user)))

(rf/reg-event-db
  :delete-user
  (fn [db [_ index]]
    (update db :users #(vec (remove (fn [user] (= (nth % index) user)) %)))))
#_(rf/reg-event-db
    :update-user
    (fn [db [_ index updated-user]]
      (assoc-in db [:users index] updated-user)))           ;; Update user at the specified index.

(rf/reg-event-fx
  :save-user-success
  (fn [_]
    {:dispatch [::nav/push-state :admin-panel]}))

(rf/reg-event-fx
  :save-user-failure
  (fn [_]
    ;; When failed alert the user
    {:dispatch [::nav/push-state :admin-panel]}))

(rf/reg-event-db
  :local-update-user
  (fn [db [_ {:keys [user-id] :as updated-user}]]
    (assoc-in db [:users user-id] updated-user)))

(rf/reg-event-fx
  :update-user
  (fn [_ [_ updated-user]]
    {:http-request {:uri        "/api/save-user"
                    :method     :post
                    :params     updated-user
                    :on-success [:save-user-success]
                    :on-failure [:save-user-failure]}
     :dispatch     [:local-update-user updated-user]}))     ;; Update user at the specified index.

(comment
  @(rf/subscribe [:db])
  @(rf/subscribe [:users])
  (:users (assoc-in @(rf/subscribe [:db]) [:users 1] "BLA"))
  )

(rf/reg-fx :run-fn
  (fn [exec-fn!]
    (exec-fn!)))

(rf/reg-event-fx :event-set-current-view!
  (fn [_ [_ set-current-view!]]
    (println "set-current-view! " set-current-view!)
    (println (type set-current-view!))
    {:run-fn set-current-view!}))

(rf/reg-event-fx
  :editing-user-id
  (fn [db [_ user-id set-current-view!]]
    {:db (assoc db :editing-user-id user-id)
     :dispatch [:event-set-current-view! set-current-view!]}))                   ;; Set user to be edited
