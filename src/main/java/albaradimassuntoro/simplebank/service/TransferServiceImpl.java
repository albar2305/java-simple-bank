package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.entitiy.Account;
import albaradimassuntoro.simplebank.entitiy.Entry;
import albaradimassuntoro.simplebank.entitiy.Transfer;
import albaradimassuntoro.simplebank.model.AccountResponse;
import albaradimassuntoro.simplebank.model.CreateTransferRequest;
import albaradimassuntoro.simplebank.model.TransferResponse;
import albaradimassuntoro.simplebank.repository.AccountRepository;
import albaradimassuntoro.simplebank.repository.EntryRepository;
import albaradimassuntoro.simplebank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {

  private final AccountRepository accountRepository;

  private final TransferRepository transferRepository;

  private final EntryRepository entryRepository;

  private final AccountService accountService;

  @Autowired
  public TransferServiceImpl(AccountRepository accountRepository, TransferRepository transferRepository, EntryRepository entryRepository, AccountService accountService) {
    this.accountRepository = accountRepository;
    this.transferRepository = transferRepository;
    this.entryRepository = entryRepository;
    this.accountService = accountService;
  }


  @Transactional
  @Override
  public TransferResponse createTransfer(CreateTransferRequest request, String username) {
    TransferResponse transferResponse = new TransferResponse();
    Account fromAccount = validAccount(request.getFromAccountId(), request.getCurrency());
    if (!fromAccount.getOwner().equals(username)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "from account doesn't belong to the authenticated user");
    }

    validAccount(request.getToAccountId(), request.getCurrency());
    Transfer transfer = new Transfer();
    transfer.setId(UUID.randomUUID().toString());
    transfer.setAmount(request.getAmount());
    transfer.setCreatedAt(new Timestamp(new Date().getTime()));
    transfer.setFromAccount(request.getFromAccountId());
    transfer.setToAccount(request.getToAccountId());
    transferResponse.setTransfer(transferRepository.save(transfer));

    Entry fromEntry = new Entry();
    fromEntry.setId(UUID.randomUUID().toString());
    fromEntry.setCreatedAt(new Timestamp(new Date().getTime()));
    fromEntry.setAmount(-request.getAmount());
    fromEntry.setAccountId(request.getFromAccountId());
    transferResponse.setFromEntry(entryRepository.save(fromEntry));

    Entry toEntry = new Entry();
    toEntry.setId(UUID.randomUUID().toString());
    toEntry.setCreatedAt(new Timestamp(new Date().getTime()));
    toEntry.setAmount(request.getAmount());
    toEntry.setAccountId(request.getToAccountId());
    transferResponse.setFromEntry(entryRepository.save(toEntry));

    if (request.getFromAccountId().compareTo(request.getToAccountId()) < 0) {
      transferResponse.setFromAccount(addMoney(request.getFromAccountId(), -request.getAmount()));
      transferResponse.setToAccount(addMoney(request.getToAccountId(), request.getAmount()));
    } else {
      transferResponse.setFromAccount(addMoney(request.getFromAccountId(), request.getAmount()));
      transferResponse.setToAccount(addMoney(request.getToAccountId(), -request.getAmount()));
    }

    return transferResponse;
  }

  private Account validAccount(String accountId, String currency) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));
    if (!account.getCurrency().equals(currency)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "account" + account.getId() + " currency mismatch: " + account.getCurrency() + " : " + currency);
    }
    return account;
  }

  private AccountResponse addMoney(String accountId, Long amount) {
    return accountService.addAccountBalance(amount, accountId);
  }
}
