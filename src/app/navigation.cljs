(ns app.navigation
  (:require [re-frame.core :as rf]
            [reitit.core :as r]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as frontend]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))


;;; Routes ;;;

(defn href
  "Return relative url for given route. Url can be used in HTML links."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (rfe/href k params query)))


(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [::navigated new-match])))

(defn make-router [routes]
  (frontend/router
    routes
    {:data {:coercion rss/coercion}}))

(defn init-routes! [router]
  (js/console.log "initializing routes")
  (rfe/start!
    router
    on-navigate
    {:use-fragment true}))


;;; Effects ;;;

;; Triggering navigation from events.

(rf/reg-fx :push-state
           (fn [route]
             (apply rfe/push-state route)))

;;; Events ;;;

(rf/reg-event-db ::initialize-db
                 (fn [db _]
                   (if db
                     db
                     {:current-route nil})))

(rf/reg-event-fx
  ::push-state (fn [_ [_ & route]]
                 {:push-state route}))

(rf/reg-event-db
  ::navigated (fn [db [_ new-match]]
                (let [old-match (:current-route db)
                      controllers (rfc/apply-controllers (:controllers old-match) new-match)]
                  (js/console.log "Navigated " new-match)
                  (assoc db :current-route (assoc new-match :controllers controllers)))))
