package espiel.kafka.orderservice.kafka.producer.order.model;

public record ActiveOrdersCountMessage(
    Long customerId,
    Long newOrdersCount
) {

}
