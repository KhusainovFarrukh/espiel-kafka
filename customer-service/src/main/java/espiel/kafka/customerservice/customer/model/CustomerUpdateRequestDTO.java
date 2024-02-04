package espiel.kafka.customerservice.customer.model;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CustomerUpdateRequestDTO(
    @Size(max = 100)
    String firstName,

    @Size(max = 100)
    String lastName,

    @Size(max = 50)
    String email,

    @Past
    LocalDate birthDate,

    @Size(max = 200)
    String address,

    @Size(max = 50)
    String phone
) {

}
