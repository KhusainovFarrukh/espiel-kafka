package espiel.kafka.customerservice.kafka.consumer.orderscount.model;

public record ActiveOrdersCountMessage(
    Long customerId,
    Long newOrdersCount
) {

}
