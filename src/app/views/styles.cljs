(ns app.views.styles)

;; Function to format a date string into a more user-friendly format.
(defn format-date [date-str]
  (let [date (js/Date. date-str)]                           ;; Create a JavaScript Date object from the date string.
    (str (.toLocaleDateString date (clj->js {:year "numeric" :month "long" :day "numeric"}))))) ;; Format the date.


;; Define styles for various components in the admin panel.
(def panel-container-style
  {:padding         "40px"
   :display         "flex"
   :flexDirection   "column"
   :alignItems      "center"
   :backgroundColor "#f4f4f4"
   :minHeight       "100vh"})

(def table-style
  {:borderCollapse "collapse"
   :width          "100%"
   :marginBottom   "20px"})

(def th-style
  {:border          "1px solid #ddd"
   :padding         "10px"
   :textAlign       "center"
   :backgroundColor "#4CAF50"
   :color           "white"})

(def td-style
  {:border    "1px solid #ddd"
   :padding   "10px"
   :textAlign "center"})

(def button-style
  {:margin          "5px"
   :padding         "10px"
   :backgroundColor "#007BFF"
   :color           "white"
   :border          "none"
   :borderRadius    "5px"
   :cursor          "pointer"})

(def form-style
  {:backgroundColor "#fff"
   :padding         "30px"
   :borderRadius    "8px"
   :boxShadow       "0 4px 8px rgba(0, 0, 0, 0.1)"
   :width           "400px"
   :display         "flex"
   :flexDirection   "column"
   :gap             "15px"})

(def label-style
  {:fontWeight "bold"})

(def input-style
  {:padding      "10px"
   :border       "1px solid #ccc"
   :borderRadius "4px"
   :fontSize     "14px"
   :width        "100%"})

(def select-style
  {:padding      "10px"
   :border       "1px solid #ccc"
   :borderRadius "4px"
   :fontSize     "14px"
   :width        "100%"})


;;#####################################################################################################################

(def header-style #js {:backgroundColor "#333"
                       :color           "white"
                       :padding         "20px"
                       :textAlign       "center"
                       :display         "flex"
                       :justifyContent  "space-between"
                       :alignItems      "center"})

(def logo-style #js {:width       "60px"
                     :height      "auto"
                     :marginRight "10px"})

(def sidebar-style #js {:backgroundColor "#444"
                        :color           "white"
                        :width           "200px"
                        :padding         "20px"
                        :height          "100vh"
                        :display         "flex"
                        :flexDirection   "column"})

(def link-style #js {:color          "white"
                     :textDecoration "none"
                     :padding        "10px"
                     :display        "block"
                     :border         "1px solid #666"
                     :borderRadius   "4px"
                     :transition     "background-color 0.3s ease, border-color 0.3s ease"})

(def footer-style #js {:backgroundColor "#333"
                       :color           "white"
                       :textAlign       "center"
                       :padding         "10px 0"})

(def li-margin-style #js {:marginBottom "10px"})

(def page-style {})
