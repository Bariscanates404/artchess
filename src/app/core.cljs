(ns app.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [app.hooks :as hooks]
    [re-frame.core :as re-frame]
    [uix.core :as uix :refer [defui $]]
    [uix.dom]
    [app.events]
    [app.subs :as subs]
    [app.navigation :as nav]
    [app.routes :as routes]
    [app.views.dashboard :as dash]
    [app.http-client :as h]
    ["react" :as react]))

(enable-console-print!)

(defn render []
  (uix.dom/render-root ($ react/StrictMode ($ dash/app))
    (uix.dom/create-root (js/document.getElementById "root"))))


(def initial-users
  {1 {:user-id 1 :name "Barış Can Ateş" :email "bariscanates@gmail.com" :role "teacher" :date "2024-09-21"}
   2 {:user-id 2 :name "John Doe" :email "john@example.com" :role "student" :date "2024-08-01"}})

(comment
  (mapv (fn [[key val]] val) initial-users)

  )
(def initial-classes
  {1 {:class-id 1 :user-id 2 :class "Barış Can Ateş" :email "bariscanates@gmail.com" :role "teacher" :date "2024-09-21"}
   2 {:user-id 2 :name "John Doe" :email "john@example.com" :role "student" :date "2024-08-01"}})

(defn ^:export init []
  (rf/dispatch-sync [:app/init-db {:users initial-users}])
  (nav/init-routes! (nav/make-router routes/routes))
  (render)
  (rf/dispatch-sync [::nav/push-state :dashboard]))


(comment
  (re-frame/subscribe [::subs/current-view])
  @(re-frame/subscribe [:db])
  )
