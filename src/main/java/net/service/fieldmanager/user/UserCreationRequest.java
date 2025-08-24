package net.service.fieldmanager.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private boolean active = true;
}
