package org.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private static final String NAME_EMPTY_MSG = "Name cannot be empty!";
    public static final String  NAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]*$";
    public static final String  NAME_REGEX_MSG = "Name of user can contain letters and hyphen";

    private static final String SURNAME_EMPTY_MSG = "Surname cannot be empty!";
    public static final String  SURNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]*$";
    public static final String  SURNAME_REGEX_MSG = "Surname of user can contain letters and hyphen";

    public static final String EMAIL_EMPTY_MSG = "Email cannot be empty!";
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Schema(description = "unique identifier of the user", hidden = true)
    private UUID userId;

    @NotEmpty(message = NAME_EMPTY_MSG)
    @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MSG)
    @Schema(description = "Name of user", defaultValue = "John")
    private String name;

    @NotEmpty(message = SURNAME_EMPTY_MSG)
    @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MSG)
    @Schema(description = "Surname of user", defaultValue = "Lennon")
    private String surname;

    @NotEmpty(message = EMAIL_EMPTY_MSG)
    @Pattern(regexp = EMAIL_REGEX)
    @Schema(description = "Email of user", defaultValue = "jon@gmail.com")
    private String email;

    @Hidden
    private String password;

    private UUID accountId;

    private Address address;

    @Hidden
    public UUID getAccountId() {
        return accountId;
    }

}
