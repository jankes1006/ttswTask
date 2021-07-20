package com.ttsw.task.domain.user;

import com.ttsw.task.enumVariable.user.ModifyFields;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserUpdateUserDTO {
    private ModifyFields modifyFields;
    private String newValue;
    private String password;
}
