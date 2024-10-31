(ns app.http-client
  "Port of day8/re-frame-http-fx."
  (:require [ajax.xhrio]
            [ajax.core :as ajax]
            [re-frame.core :as rf]
            [cljs.core.async :as async]
            [cljs-http.client :as http])
  (:import [goog.net ErrorCode XhrIo]))

; see http://docs.closure-library.googlecode.com/git/class_goog_net_XhrIo.html
(defn- failure-details [^XhrIo xhrio response]
  (merge {:uri             (.getLastUri xhrio)
          :last-method     (.-lastMethod_ xhrio)
          :last-error      (.getLastError xhrio)
          :last-error-code (.getLastErrorCode xhrio)
          :debug-message   (-> xhrio .getLastErrorCode (ErrorCode/getDebugMessage))}
         response))

(defn- handle-response
  [xhrio on-success on-failure [success? response]]
  (if success?
    (rf/dispatch (conj on-success response))
    (rf/dispatch (conj on-failure (failure-details xhrio response)))))

(defn- prepare-request [{:keys [on-success on-failure]
                         :or   {on-success [:http-success]
                                on-failure [:http-failure]}
                         :as   request}]
  (let [xhrio (XhrIo.)]
    (-> request
        (assoc :api xhrio
               :format (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :handler (partial handle-response xhrio on-success on-failure))
        (dissoc :on-success :on-failure :on-request))))



(defn- prepare-multipart-request [{:keys [on-success on-failure]
                                   :or   {on-success [:http-success]
                                          on-failure [:http-failure]}
                                   :as   request}]
  (let [xhrio (XhrIo.)]
    (-> request
        (assoc :api xhrio
               ;:format (ajax/json-request-format)

               :response-format (ajax/json-response-format {:keywords? true})
               :handler (partial handle-response xhrio on-success on-failure))
        (dissoc :on-success :on-failure :on-request))))

(defn- send-request! [request]
  (let [xhrio (-> request prepare-request ajax/ajax-request)]
    (when-let [on-request (:on-request request)]
      (rf/dispatch (conj on-request xhrio)))))

(defn- multipart-request! [{:keys [uri data on-success on-failure] :as request}]
  (async/go
    (let [resp (async/<! (http/post uri data))]
      (if (= (:status resp) 200)
        (rf/dispatch (conj on-success resp))
        (rf/dispatch (conj on-failure resp))))))

(defn http-request
  [request]
  (if (sequential? request)
    (doseq [r request]
      (send-request! r))
    (send-request! request)))


;; effects != events
(rf/reg-fx :http-request http-request)
(rf/reg-fx :http-multipart-request multipart-request!)


(comment
  (let [progress-chan (async/chan)
        my-file :my-file]
    (http/post "http://example.com" {:multipart-params [["key1" "value1"] ["my-file" my-file]] :progress progress-chan}))
  "The progress-channel will receive progress events: {:directon dir :loaded uploaded_or_downloaded :total size}
  :direction is :upload or :download
  in some cases :total can be missing"
  ;; How to use a customized backend host:port for requests

  (def api-port 3000)

  (def api-location
    (str "http://localhost:" api-port))
  )
