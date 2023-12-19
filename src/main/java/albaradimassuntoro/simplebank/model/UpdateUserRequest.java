package albaradimassuntoro.simplebank.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

  @NotBlank
  @Size(max = 100)
  private String password;

  @NotBlank
  @Size(max = 100)
  private String fullName;
}