package com.ttsw.task.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserToSendDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
}
