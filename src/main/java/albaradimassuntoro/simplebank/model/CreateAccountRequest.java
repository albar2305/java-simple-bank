package albaradimassuntoro.simplebank.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

  @NotBlank
  @Pattern(regexp = "^(USD|IDR|GBP|EUR)$", message = "Currency should be one of: USD, RP, GBP, EUR")
  private String currency;
}
