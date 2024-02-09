opt/bitnami/kafka/bin/kafka-acls.sh --authorizer kafka.security.authorizer.AclAuthorizer --authorizer-properties zookeeper.connect=zookeeper:2181 --add --allow-principal User:espiel_order_service --operation Write --topic active-orders-count
opt/bitnami/kafka/bin/kafka-acls.sh --authorizer kafka.security.authorizer.AclAuthorizer --authorizer-properties zookeeper.connect=zookeeper:2181 --add --allow-principal User:espiel_customer_service --operation Read --topic active-orders-count --group customer-service-gr1
opt/bitnami/kafka/bin/kafka-acls.sh --authorizer kafka.security.authorizer.AclAuthorizer --authorizer-properties zookeeper.connect=zookeeper:2181 --add --allow-principal User:espiel_customer_service --operation Write --topic active-orders-count-replies
opt/bitnami/kafka/bin/kafka-acls.sh --authorizer kafka.security.authorizer.AclAuthorizer --authorizer-properties zookeeper.connect=zookeeper:2181 --add --allow-principal User:espiel_order_service --operation Read --topic active-orders-count-replies --group order-service-gr1
