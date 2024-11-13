(ns app.subs
  (:require [re-frame.core :as rf]))

;; The `app.subs` namespace is responsible for defining and registering subscriptions
;; that allow components to query the application's local database (app state).
;; Subscriptions are the primary means by which components obtain reactive access to the state.

;; General Usage:
;; To create a new subscription, use the `rf/reg-sub` function, which takes a subscription ID and a
;; subscription handler function. The subscription handler function receives the application state
;; (db) and an optional query vector, and it returns the value to be accessed by the components.
;;
;; Example:
;; (rf/reg-sub :example-subscription
;;             (fn [db _]
;;               (:example-key db)))
;;
;; This example registers a subscription with the ID `:example-subscription` that returns the value
;; associated with the `:example-key` in the application state.

;; Subscription: :db
;; Description: Provides the entire application state.
(rf/reg-sub :db
            (fn [db _]
              db))
(comment
  (rf/subscribe [:db])

  )

(rf/reg-sub
  :health-response
  (fn [db _]
    (:health-response db)))


(rf/reg-sub ::current-view
            (fn [db]
              (-> db :current-route :data :view)))

(rf/reg-sub
  :users
  (fn [db _]
    (mapv val (:users db))))

(rf/reg-sub
  :editing-user-id
  (fn [db _]
    (:editing-user-id db)))

(rf/reg-sub
  :editing-user
  :<- [:editing-user-id]
  :<- [:users]
  (fn [[editing-user-id users] _]
    (get users editing-user-id)))

(rf/reg-sub
  :user-by-id
  :<- [:users]
  (fn [[users] [_ user-id]]
    (get users user-id)))

(comment
  (def editing-user-id 1)

  (re-frame.core/clear-subscription-cache!)
  (rf/dispatch [:editing-user-id 1])
  @(rf/subscribe [:db])
  @(rf/subscribe [:users])
  @(rf/subscribe [:editing-user-id])
  @(rf/subscribe [:editing-user])

  (-> @(rf/subscribe [:db])
      :editing-user-id)

  )

