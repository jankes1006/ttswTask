package com.ttsw.task.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserUpdateAdminDTO {
    private Long id;
    private String role;
}
