(ns app.views.dashboard
  (:require [uix.core :as uix :refer [defui $]]
            [app.hooks :as hooks]
            [app.subs :as subs]
            [app.navigation :as nav]
            [app.views.styles :as styles]
            [re-frame.core :as rf]
            [app.views.module.sidebar :as sidebar]))




;;#####################################################################################################################

(defui app-header []
  ($ :header {:style styles/header-style}
    ($ :div {:style {:display "flex" :alignItems "center"}}
      ($ :img {:src "artchesslogo.jpg" :alt "Logo" :style styles/logo-style})
      ($ :h1 {:style {:margin "0"}} "Art Chess Academy"))
    ($ :div {:style {:marginTop "10px" :fontSize "18px" :fontWeight "bold"}}
      "Excellence in Chess Education.")))


(defui checkbox-component []
  (let [is-clicked (hooks/use-subscribe [:is-checkbox-clicked])]
    ($ :div
      ($ :label
        ($ :input {:type    "checkbox"
                   :checked is-clicked
                   :onClick #(do
                               (js/console.log %)
                               (rf/dispatch [:set-is-checkbox-clicked (-> % .-target .-checked)]))})
        " Click me")
      (if is-clicked
        ($ :p "Checkbox is checked.")
        ($ :p "Checkbox is not checked.")))))

(defui app-footer []
  ($ :footer {:style styles/footer-style}
    ($ :p "© 2024 Art Chess Academy. All rights reserved.")))

(defui home-page []
  ($ :div {:style styles/page-style}
    ($ :h2 "Welcome to Art Chess Academy")
    ($ :p "At Art Chess Academy, we provide top-notch chess education for all ages and skill levels.")
    ($ :p "Explore our courses, meet our instructors, and join our community to enhance your chess skills.")))


(defui about-us-page []
  ($ :div {:style styles/page-style}
    ($ :h2 "About Us")
    ($ :p "Art Chess Academy is dedicated to fostering a love for chess among students of all ages.")
    ($ :p "Our experienced instructors focus on developing strategic thinking and problem-solving skills through chess."))
  ($ :p "Join us to learn more about our philosophy and teaching methods."))


(defui courses-page []
  ($ :div {:style styles/page-style}
    ($ :h2 "Our Courses")
    ($ :p "We offer a variety of chess classes tailored to different skill levels, from beginners to advanced players.")
    ($ :p "Check out our schedule for detailed information on class timings and subjects."))
  ($ :h3 "Special Courses")
  ($ :ul {:style {:listStyleType "none" :paddingLeft "20px"}}
    ($ :li "Logic Games")
    ($ :li "Philosophy for Children")
    ($ :li "Programming and Algorithms")))


(defui contact-us-page []
  ($ :div {:style styles/page-style}
    ($ :h2 "Contact Us")
    ($ :p "We'd love to hear from you! Whether you have questions about our courses or want to register, feel free to reach out.")
    ($ :p "Email: contact@artchessacademy.com")
    ($ :p "Phone: (123) 456-7890")))


(defui debug []
  (let [editing-user-id (hooks/use-subscribe [:editing-user-id])]
    ($ :div#debug
      ($ :p (str "editing-user-id " editing-user-id)))))

(defui app []
  (let [current-view (hooks/use-subscribe [::subs/current-view])]
    ($ :div {:style {:display       "flex"
                     :flexDirection "column"
                     :height        "100vh"}}
      ($ debug)
      ($ app-header)
      ($ :div {:style {:display "flex" :flex "1"}}
        ($ sidebar/app-sidebar)
        (when current-view
          ($ current-view)))
      ($ app-footer))))
