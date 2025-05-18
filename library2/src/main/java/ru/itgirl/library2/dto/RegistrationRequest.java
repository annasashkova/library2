package ru.itgirl.library2.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RegistrationRequest {
    private String username;
    private String password;
}
