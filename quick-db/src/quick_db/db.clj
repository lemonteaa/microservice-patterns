(ns quick-db.db
  (:require [toucan.db :as tdb]
            [toucan.models :refer [defmodel IModel]]
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
  (primary-key [_] :customer_id))

(tdb/insert! Customers :customer_name "Tommy")
(tdb/insert! Customers :customer_name "Mary")

(Customers 2)

(def worker (future (kafka/run-application "localhost:9092" 
                                           "dbserver1.inventory.customers" 
                                           "test.dummy2"
                                           "testing_group2")))

(future-cancel worker)

(future-cancelled? worker)

(shutdown-agents)
