(ns app.views.users
  (:require [uix.core :as uix :refer [defui $]]
            [app.hooks :as hooks]
            [app.views.styles :as styles]
            [app.views.admin-panel :as admin-panel]
            [app.subs :as subs]
            [app.navigation :as nav]
            [re-frame.core :as rf]))

;; Create User Form Component
(defui create-user [{:keys [success-callback]}]
  (let [[name set-name!] (uix/use-state "")
        [email set-email!] (uix/use-state "")
        [role set-role!] (uix/use-state "student")]
    ($ :div {:style styles/form-style}
      ($ :h3 "Create New User")
      ;; Name input
      ($ :label {:for "name" :style styles/label-style} "Name:")
      ($ :input {:type "text" :id "name" :value name :onChange #(set-name! (.. % -target -value)) :style styles/input-style})
      ;; Email input
      ($ :label {:for "email" :style styles/label-style} "Email:")
      ($ :input {:type "email" :id "email" :value email :onChange #(set-email! (.. % -target -value)) :style styles/input-style})
      ;; Role selection
      ($ :label {:for "role" :style styles/label-style} "Role:")
      ($ :select {:id "role" :value role :onChange #(set-role! (.. % -target -value)) :style styles/select-style}
        ($ :option {:value "student"} "Student")
        ($ :option {:value "teacher"} "Teacher"))
      ;; Create Button
      ($ :button {:style   styles/button-style
                  :onClick #(do
                              (rf/dispatch [:create-user {:name  name
                                                          :email email
                                                          :role  role}])
                              (success-callback))} "Create User"))))


(defui edit-user [{:keys [success-callback]}]
  (let [editing-user-id (hooks/use-subscribe [:editing-user-id])
        {:keys [user-id] :as editing-user} (hooks/use-subscribe [:user-by-id editing-user-id])
        [name set-name!] (uix/use-state (:name editing-user))
        [email set-email!] (uix/use-state (:email editing-user))
        [role set-role!] (uix/use-state (:role editing-user))]
    ($ :div {:style styles/form-style}
      ($ :h3 "Edit User")
      ;; Name input
      ($ :label {:for "name" :style styles/label-style} "Name:")
      ($ :input {:type "text" :id "name" :value name :onChange #(set-name! (.. % -target -value)) :style styles/input-style})
      ;; Email input
      ($ :label {:for "email" :style styles/label-style} "Email:")
      ($ :input {:type "email" :id "email" :value email :onChange #(set-email! (.. % -target -value)) :style styles/input-style})
      ;; Role selection
      ($ :label {:for "role" :style styles/label-style} "Role:")
      ($ :select {:id "role" :value role :onChange #(set-role! (.. % -target -value)) :style styles/select-style}
        ($ :option {:value "student"} "Student")
        ($ :option {:value "teacher"} "Teacher"))
      ;; Update Button
      ($ :button {:style   styles/button-style
                  :onClick #(do
                              (rf/dispatch [:update-user {:user-id user-id
                                                          :name    name
                                                          :email   email
                                                          :role    role}])
                              (success-callback))} "Update User"))))


(defui user-list [{:keys [set-current-view!]}]
  (let [users (hooks/use-subscribe [:users])]
    ;($ admin-panel/admin-panel-container)

    ($ :div {:style styles/panel-container-style}
      ($ :table {:style styles/table-style}
        ($ :thead
          ($ :tr
            ($ :th {:style styles/th-style} "Name")
            ($ :th {:style styles/th-style} "Email")
            ($ :th {:style styles/th-style} "Role")
            ($ :th {:style styles/th-style} "Date Registered")
            ($ :th {:style styles/th-style} "Actions")))
        ($ :tbody
          (for [{:keys [user-id name email role] :as user} users] ;; Iterate over users to create table rows.
            ($ :tr {:key user-id}
              ($ :td {:style styles/td-style} name)         ;; Display user's name.
              ($ :td {:style styles/td-style} email)        ;; Display user's email.
              ($ :td {:style styles/td-style} role)         ;; Display user's role.
              ($ :td {:style styles/td-style} (styles/format-date (:date user))) ;; Format and display registration date.
              ($ :td {:style styles/td-style}
                ;; Update and Delete buttons
                ($ :button {:style   styles/button-style
                            :onClick #(rf/dispatch [:editing-user-id user-id (fn [] (set-current-view! "edit"))])} "Update") ;; Switch to edit view.
                ($ :button {:style   styles/button-style
                            :onClick #(rf/dispatch [:delete-user user-id])} "Delete"))))))
      ($ :div
        ($ :button {:style   styles/button-style
                    :onClick #(set-current-view! "create")} ;; Switch to create view.
          "Create")))))
