(ns app.views.admin-panel
  (:require [uix.core :as uix :refer [defui $]]
            [app.hooks :as hooks]
            [app.views.styles :as styles]
            [app.subs :as subs]
            [app.navigation :as nav]
            [re-frame.core :as rf]))


;; Define the main admin panel component.
(defui admin-panel-home [{:keys [current-view]}]
  ($ :div "admin-panel-home"))

;; Define the main admin panel component.
(defui admin-panel-container [{:keys [children]}]
  ;; Main Panel Layout
  ($ :div {:style styles/panel-container-style}
    ;; Header
    ($ :h2 "User Admin Panel")
    children))


