package com.ead.authuser.dto;

import com.ead.authuser.validation.UserNameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements UserView {

    private UUID userId;

    @JsonView(UserView.RegistrationPost.class)
    //@NotBlank(groups = UserView.RegistrationPost.class, message = "Field cannot be empty")
    //@Size(min = 8, max = 20, groups = UserView.RegistrationPost.class)
    @UserNameConstraint(groups = UserView.RegistrationPost.class)
    private String username;

    @JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class, message = "Field cannot be empty")
    @Email
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @NotBlank(groups = UserView.PasswordPut.class, message = "Field cannot be empty")
    @Size(min = 6, max = 20, groups = UserView.PasswordPut.class, message = "Password must have be between 8 and 20 characteres")
    private String password;

    @JsonView(UserView.PasswordPut.class)
    @NotBlank(groups = UserView.PasswordPut.class, message = "Field cannot be empty")
    @Size(min = 6, max = 20, groups = UserView.PasswordPut.class, message = "Password must have be between 8 and 20 characteres")
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Field cannot be empty")
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Field cannot be empty")
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Field cannot be empty")
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    @NotBlank(groups = UserView.ImagePut.class, message = "Field cannot be empty")
    private String imageUrl;
}
