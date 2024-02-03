package espiel.kafka.customerservice.customer.model;

import java.time.LocalDate;

public record CustomerResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    LocalDate birthDate,
    Integer activeOrdersCount,
    String address,
    String phone,
    LocalDate createdAt,
    LocalDate updatedAt
) {

}
