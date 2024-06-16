package com.tuskers.backend.email.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    private String toEmail;
    private String subject;
    private String content;
}
