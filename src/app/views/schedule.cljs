(ns app.views.schedule
  (:require [uix.core :as uix :refer [defui $]]
            [re-frame.core :as rf]))

(defn schedule-table-style []
  #js {:width          "100%"
       :borderCollapse "collapse"})

(defn th-td-style []
  #js {:border    "1px solid #ddd"
       :padding   "8px"
       :textAlign "center"})

(defn th-style []
  (merge (th-td-style)
         #js {:backgroundColor "#f2f2f2"
              :color           "#333"}))

(defn even-tr-style []
  #js {:backgroundColor "#f9f9f9"})

(defn hover-tr-style []
  #js {:backgroundColor "#ddd"})

(defn td-style []
  (merge (th-td-style)
         #js {:cursor "pointer"}))

(defn clicked-td-style []
  (merge (td-style)
         #js {:backgroundColor "#d1e7dd"}))

(defui weekly-schedule []
       ($ :table {:style (schedule-table-style)}
          ($ :thead
             ($ :tr
                ($ :th {:style (th-style)} "Time")
                (for [day ["Monday" "Tuesday" "Wednesday" "Thursday" "Friday" "Saturday" "Sunday"]]
                  ($ :th {:key day :style (th-style)} day))))
          ($ :tbody
             (for [hour (range 9 24)
                   minute (range 0 60 30)]
               (let [time-label (str (if (< hour 10) "0" "") hour ":" (if (= minute 0) "00" minute))]
                 ($ :tr {:key (str hour "-" minute) :style (when (even? hour) (even-tr-style))}
                    ($ :td {:style (th-td-style)} time-label)
                    (for [day ["Monday" "Tuesday" "Wednesday" "Thursday" "Friday" "Saturday" "Sunday"]]
                      ($ :td {:key     (str day "-" time-label)
                              :style   (if (rf/subscribe [:slot-clicked? day time-label])
                                         (clicked-td-style)
                                         (td-style))
                              :onClick #(rf/dispatch [:slot-click day time-label])}))))))))
