(defproject quick-db "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.postgresql/postgresql "42.5.0"]
                 [toucan "1.18.0"]
                 [org.apache.kafka/kafka-clients "3.0.0"]
                 [org.apache.kafka/kafka_2.12 "3.0.0"]
                 [org.testcontainers/testcontainers "1.15.3"]
                 [org.testcontainers/kafka "1.15.3"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :main ^:skip-aot quick-db.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
