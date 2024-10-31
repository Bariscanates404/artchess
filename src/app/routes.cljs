(ns app.routes
  (:require
    [re-frame.core :as rf]
    [app.views.register-user :as register]
    [app.views.admin-panel :as admin-panel]
    [app.views.dashboard :as dashboard]
    [app.views.users :as users]
    [app.events :as events]))

(def routes
  [["dashboard"
    {:name        :dashboard
     :view        dashboard/home-page
     :link-text   "Dashboard"
     :controllers [{:start (fn [& params] (js/console.log "Entering Dashboard"))
                    :stop  (fn [& params] (js/console.log "Leaving Dashboard"))}]}]

   ["about-us"
    {:name        :about-us
     :view        dashboard/about-us-page
     :link-text   "About Us"
     :controllers [{:start (fn [& params] (js/console.log "Entering about us"))
                    :stop  (fn [& params] (js/console.log "Leaving about us"))}]}]
   ["courses"
    {:name        :courses
     :view        dashboard/courses-page
     :link-text   "Courses"
     :controllers [{:start (fn [& params] (js/console.log "Entering courses"))
                    :stop  (fn [& params] (js/console.log "Leaving courses"))}]}]
   ["contact"
    {:name        :contact-us
     :view        dashboard/contact-us-page
     :link-text   "Contact"
     :controllers [{:start (fn [& params] (js/console.log "Entering contact"))
                    :stop  (fn [& params] (js/console.log "Leaving contact"))}]}]
   ["admin-panel"
    {:name        :admin-panel
     :view        admin-panel/admin-panel-home
     :link-text   "admin-panel"
     :controllers [{:start (fn [& params] (js/console.log "Entering admin-panel"))
                    :stop  (fn [& params] (js/console.log "Leaving admin-panel"))}]}]

   ["users-list"
       {:name        :user-list
        :view        users/user-list
        :link-text   "user-list"
        :controllers [{:start (fn [& params] (js/console.log "Entering user-list"))
                       :stop  (fn [& params] (js/console.log "Leaving user-list"))}]}]

   ["register-user"
    {:name        :register-user
     :view        register/register-page
     :link-text   "register-user"
     :controllers [{:start (fn [& params] (js/console.log "Entering register-user"))
                    :stop  (fn [& params] (js/console.log "Leaving register-user"))}]}]



   ;["open-issue"
   ; {:name        :open-issue
   ;  :view        client-issues/open-issue
   ;  :link-text   "Chamado"
   ;  :controllers [{:start (fn [& params] (js/console.log "Entering Abrir Chamado"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Abrir Chamado"))}]}]
   ;["all-issues"
   ; {:name        :list-issues
   ;  :view        client-issues/list-issues
   ;  :link-text   "Chamados"
   ;  :controllers [{:start (fn [& params]
   ;                          (rf/dispatch [::events/fetch-client-issues])
   ;                          (js/console.log "Entering Listar Chamados"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Listar Chamados"))}]}]
   ;["new-issue"
   ; {:name        :new-client-issue
   ;  :view        client-issues/new-issue
   ;  :link-text   "Chamado"
   ;  :controllers [{:start (fn [& params]
   ;                          (rf/dispatch [::events/ensure-draft-issue])
   ;                          (js/console.log "Entering Novo Chamado"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Novo Chamado"))}]}]
   ;["edit-issue"
   ; {:name        :edit-client-issue
   ;  :view        client-issues/edit-issue
   ;  :link-text   "Chamado"
   ;  :controllers [{:start (fn [& params] (js/console.log "Entering Ver Chamado"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Ver Chamado"))}]}]
   ;["users"
   ; {:name        :users
   ;  :view        users-view/list-users
   ;  :link-text   "Usuários"
   ;  :controllers [{:start (fn [& params]
   ;                          (rf/dispatch [::events/fetch-users])
   ;                          (js/console.log "Entering Usuários"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Usuários"))}]}]
   ;["edit-user"
   ; {:name        :edit-user
   ;  :view        users-view/edit-user
   ;  :link-text   "Usuário"
   ;  :controllers [{:start (fn [& params]
   ;                          (js/console.log "Entering Editar Usuário"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Editar Usuário"))}]}]
   ;["new-user"
   ; {:name        :new-user
   ;  :view        users-view/new-user
   ;  :link-text   "Usuário"
   ;  :controllers [{:start (fn [& params]
   ;                          (js/console.log "Entering Novo Usuário"))
   ;                 :stop  (fn [& params] (js/console.log "Leaving Novo Usuário"))}]}]
   ])
