(ns quick-db.db
  (:require [toucan.db :as tdb]
            [toucan.models :refer [defmodel IModel]]
            [toucan.hydrate :refer [hydrate] :as hydrate]
            [clojure.java.jdbc :as jdbc]
            [quick-db.kafka :as kafka]))

(tdb/set-default-db-connection!
 {:classname   "org.postgresql.Driver"
  :subprotocol "postgresql"
  :subname     "//localhost:5432/postgres"
  :user        "postgres"
  :password    "postgres"})

(def pg-db {:dbtype "postgresql"
            :dbname "postgres"
            :host "localhost"
            :user "postgres"
            :password "postgres"
            ;:ssl true
            ;:sslfactory "org.postgresql.ssl.NonValidatingFactory"
            })

(jdbc/query pg-db ["select * from inventory.customers;"])

(jdbc/insert! pg-db "inventory.customers" 
              {:id 1011, 
               :first_name "Rose", 
               :last_name "Mary", 
               :email "admin@co-iter.com"})

;(defmodel PhoneNumber :phone_numbers
;  IModel
;  (primary-key [_] :number))

(defmodel Customers :customers 
  IModel 
  (primary-key [_] :customer_id)
  (hydration-keys [_]
                  [:customer]))

(tdb/insert! Customers :customer_name "Tommy")
(tdb/insert! Customers :customer_name "Mary")

(Customers 2)

(defmodel StoreItem :store_item
  IModel
  (primary-key [_] :store_item_id)
  (hydration-keys [_]
                  [:store_item]))

(defn add-store-item [name desc]
  (tdb/insert! StoreItem 
               :store_item_name name
               :store_item_description desc))

(add-store-item "Plush Toy" "Relax")
(add-store-item "Bath Tub" "Large and shiny")
(add-store-item "Spoon" "Spoon")
(add-store-item "Shoe" "a pair and made from recycleables")
(add-store-item "Mattress" "For winter")

(defmodel Shipping :shipping
  IModel
  (primary-key [_] :shipping_id)
  (hydration-keys [_]
                  [:shipping]))

(defmodel Order :order
  IModel
  (primary-key [_] :order_id)
  (hydration-keys [_]
                  [:order]))

(defn create-empty-order [cust {:keys [contact phone email addr]}] 
  (let [shipping (tdb/insert! Shipping
                              :contact_name contact
                              :phone phone
                              :email email
                              :shipping_address addr)]
    (tdb/insert! Order
                 :customer_id cust
                 :shipping_id (:shipping_id shipping))))

(create-empty-order 2 {:contact "Anderson" :phone "31152768"})

(hydrate (Order 1) :customer :shipping)

(defmodel OrderItem :order_item
  IModel
  (primary-key [_] :order_item_id))

(defn add-item-to-order [order-id {:keys [id qty total-price]}]
  (tdb/insert! OrderItem
               :store_item_id id
               :quantity qty
               :total_price total-price
               :order_id order-id))

(add-item-to-order 1 {:id 3 :qty 4 :total-price 20})
(add-item-to-order 1 {:id 5 :qty 1 :total-price 120})
(add-item-to-order 1 {:id 1 :qty 3 :total-price 180})

(tdb/select OrderItem :order_id 1)

(defn ^:hydrate order-items [{:keys [order_id]}]
  (tdb/select OrderItem :order_id order_id))

; need vec for nested hydration
(hydrate (Order 1) :customer :shipping [:order-items :store_item])

(hydrate {:store_item_id 2} :store_item)

(toucan.hydrate/flush-hydration-key-caches!)

(defmodel TransactionReq :transaction_req
  IModel
  (primary-key [_] :transaction_req_id)
  (hydration-keys [_] [:task_data]))

(defmodel Outbox :outbox
  IModel
  (primary-key [_] :message_id))

(defn create-order-trans! [order_id]
  (tdb/transaction
   (tdb/insert! TransactionReq
                :order_id order_id
                :transaction_req_status 0)
   (tdb/insert! Outbox
                :task_data order_id
                :task_type 10
                :task_status 0)))


(def worker (future (kafka/run-application "localhost:9092" 
                                           "dbserver1.inventory.customers" 
                                           "test.dummy2"
                                           "testing_group2")))

(future-cancel worker)

(future-cancelled? worker)

(shutdown-agents)
