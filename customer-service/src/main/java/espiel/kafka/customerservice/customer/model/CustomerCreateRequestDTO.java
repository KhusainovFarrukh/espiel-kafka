package espiel.kafka.customerservice.customer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CustomerCreateRequestDTO(
    @NotBlank
    @Size(max = 100)
    String firstName,

    @NotBlank
    @Size(max = 100)
    String lastName,

    @NotBlank
    @Size(max = 50)
    String email,

    @NotNull
    @Past
    LocalDate birthDate,

    @Size(max = 200)
    String address,

    @Size(max = 50)
    String phone
) {

}
