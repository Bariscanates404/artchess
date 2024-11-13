(ns app.views.register-user
  (:require [uix.core :as uix :refer [defui $]]))

;; Styles for form container and elements
(def form-container-style
  {:display         "flex"                                  ;; Use flexbox for layout
   :justifyContent  "flex"                                  ;; Center the items horizontally
   :alignItems      "flex"                                  ;; Center the items vertically
   :minHeight       "100vh"                                 ;; Make the container take the full height of the viewport
   :backgroundColor "#f4f4f4"                               ;; Light grey background color
   :textAlign       "center"})                              ;; Center the text in the container

(def form-style
  {:backgroundColor "#fff"                                  ;; White background for the form
   :padding         "30px"                                  ;; Add padding inside the form
   :borderRadius    "8px"                                   ;; Rounded corners for the form
   :boxShadow       "0 4px 8px rgba(0, 0, 0, 0.1)"          ;; Subtle shadow for depth
   :width           "400px"                                 ;; Fixed width for the form
   :display         "flex"                                  ;; Use flexbox for internal layout
   :flexDirection   "column"                                ;; Stack children vertically
   :gap             "15px"})                                ;; Space between child elements

(def label-style
  {:fontWeight "bold"})                                     ;; Bold font for labels

(def input-style
  {:padding      "10px"                                     ;; Padding inside input fields
   :border       "1px solid #ccc"                           ;; Light grey border
   :borderRadius "4px"                                      ;; Rounded corners for inputs
   :fontSize     "14px"                                     ;; Font size for input text
   :width        "100%"})                                   ;; Make inputs full width

(def select-style
  {:padding      "10px"                                     ;; Padding inside select dropdowns
   :border       "1px solid #ccc"                           ;; Light grey border
   :borderRadius "4px"                                      ;; Rounded corners for selects
   :fontSize     "14px"                                     ;; Font size for select text
   :width        "100%"})                                   ;; Make selects full width

(def button-style
  {:padding         "12px"                                  ;; Padding inside buttons
   :backgroundColor "#4CAF50"                               ;; Green background color
   :color           "white"                                 ;; White text color
   :border          "none"                                  ;; Remove default border
   :borderRadius    "5px"                                   ;; Rounded corners for buttons
   :fontWeight      "bold"                                  ;; Bold font for button text
   :cursor          "pointer"                               ;; Pointer cursor on hover
   :textAlign       "center"                                ;; Center text in button
   :width           "100%"})                                ;; Make buttons full width

(def user-info-style
  {:marginTop       "20px"                                  ;; Margin above the user info section
   :backgroundColor "#e0f0ff"                               ;; Light blue background for user info
   :padding         "20px"                                  ;; Padding inside the user info box
   :borderRadius    "5px"                                   ;; Rounded corners for user info box
   :boxShadow       "0 2px 4px rgba(0, 0, 0, 0.1)"})        ;; Subtle shadow for depth

;; Define the main registration component
(defui register-page []
       (let [[name set-name!] (uix/use-state "")            ;; State for user name input
             [email set-email!] (uix/use-state "")          ;; State for user email input
             [password set-password!] (uix/use-state "")    ;; State for user password input
             [role set-role!] (uix/use-state "student")     ;; State for user role selection, default is "student"
             [user-info set-user-info!] (uix/use-state nil)] ;; State for storing submitted user info

         ;; Main render function
         ($ :div {:style form-container-style}              ;; Main container with styles
            ($ :div {:style form-style}                     ;; Form container with styles
               ($ :h2 {:style {:textAlign "center" :marginBottom "20px"}} "Register") ;; Header for the form

               ;; Name input
               ($ :label {:for "name" :style label-style} "Name:") ;; Label for the name input
               ($ :input {:type     "text"                  ;; Input type is text
                          :id       "name"                  ;; ID for the input
                          :value    name                    ;; Bind value to state
                          :onChange #(set-name! (.. % -target -value)) ;; Update state on change
                          :style    input-style})           ;; Input styles

               ;; Email input
               ($ :label {:for "email" :style label-style} "Email:") ;; Label for the email input
               ($ :input {:type     "email"                 ;; Input type is email
                          :id       "email"                 ;; ID for the input
                          :value    email                   ;; Bind value to state
                          :onChange #(set-email! (.. % -target -value)) ;; Update state on change
                          :style    input-style})           ;; Input styles

               ;; Password input
               ($ :label {:for "password" :style label-style} "Password:") ;; Label for the password input
               ($ :input {:type     "password"              ;; Input type is password
                          :id       "password"              ;; ID for the input
                          :value    password                ;; Bind value to state
                          :onChange #(set-password! (.. % -target -value)) ;; Update state on change
                          :style    input-style})           ;; Input styles

               ;; Role selection (Teacher or Student)
               ($ :label {:for "role" :style label-style} "Role:") ;; Label for the role selection
               ($ :select {:id       "role"                 ;; ID for the select
                           :value    role                   ;; Bind value to state
                           :onChange #(set-role! (.. % -target -value)) ;; Update state on change
                           :style    select-style}          ;; Select styles
                  ($ :option {:value "student"} "Student")  ;; Option for student
                  ($ :option {:value "teacher"} "Teacher")) ;; Option for teacher

               ;; Register button
               ($ :button {:style   button-style            ;; Button styles
                           :onClick #(set-user-info! {:name name :email email :password password :role role})} "Register") ;; On click, set user info state

               ;; Show the user information after submission
               (when user-info                              ;; Only show this section if user-info is not nil
                 ($ :div {:style user-info-style}           ;; Container for user info
                    ($ :h3 "User Information")              ;; Header for user info display
                    ($ :p ($ :strong "Name: ") (:name user-info)) ;; Display name
                    ($ :p ($ :strong "Email: ") (:email user-info)) ;; Display email
                    ($ :p ($ :strong "Password: ") (:password user-info)) ;; Display password
                    ($ :p ($ :strong "Role: ") (:role user-info)))))))) ;; Display role
