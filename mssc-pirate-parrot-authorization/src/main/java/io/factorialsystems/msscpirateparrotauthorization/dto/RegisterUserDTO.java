package io.factorialsystems.msscpirateparrotauthorization.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;
}
