package albaradimassuntoro.simplebank.model;

import albaradimassuntoro.simplebank.entitiy.Account;
import albaradimassuntoro.simplebank.entitiy.Entry;
import albaradimassuntoro.simplebank.entitiy.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {

  private Transfer transfer;

  private AccountResponse fromAccount;

  private AccountResponse toAccount;

  private Entry fromEntry;

  private Entry toEntry;
}
