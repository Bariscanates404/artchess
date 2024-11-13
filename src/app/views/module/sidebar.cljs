(ns app.views.module.sidebar
  (:require [uix.core :as uix :refer [defui $]]
            [app.hooks :as hooks]
            [app.subs :as subs]
            [app.navigation :as nav]
            [app.views.styles :as styles]
            [re-frame.core :as rf]))

(def menu-items
  [{:title      "Home"
    :nav-target :dashboard}
   {:title      "About Us"
    :nav-target :about-us}
   {:title      "Courses"
    :nav-target :courses}
   {:title      "Contact Us"
    :nav-target :contact-us}
   {:title      "Admin Panel"
    :nav-target :admin-panel}
   {:title      "User List"
    :nav-target :user-list}
   {:title      "Register Page"
    :nav-target :register-user}
   ])

(defui app-sidebar []
       ($ :aside {:style styles/sidebar-style}
          ($ :nav {:style {:flex "1"}}
             ($ :ul {:style {:listStyleType "none" :padding "0" :marginTop "20px"}}
                (map (fn [{:keys [title nav-target] :as item}]
                       ($ :li {:key item :style styles/li-margin-style}
                          ($ :a {:href    "#" :style styles/link-style
                                 :onClick #(rf/dispatch [::nav/push-state nav-target])} title)))
                     menu-items)))))
