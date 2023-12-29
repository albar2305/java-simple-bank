package albaradimassuntoro.simplebank.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferRequest {

  private String fromAccountId;

  private String toAccountId;

  @NotNull
  @Min(value = 1)
  private Long amount;

  @NotBlank
  @Pattern(regexp = "^(USD|IDR|GBP|EUR)$", message = "Currency should be one of: USD, RP, GBP, EUR")
  private String currency;

}
