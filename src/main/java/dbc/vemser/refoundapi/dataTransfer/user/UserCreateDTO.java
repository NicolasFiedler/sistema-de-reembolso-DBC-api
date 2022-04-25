package dbc.vemser.refoundapi.dataTransfer.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateDTO {

    @NotEmpty
    @Size(min = 2, max = 50, message = "o nome deve conter entre 2 e 40 caracteres")
    private String name;
    @Email
    private String email;
    @NotNull
    private String password;

    private String image;
}