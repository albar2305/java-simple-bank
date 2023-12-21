package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.entitiy.Account;
import albaradimassuntoro.simplebank.model.AccountResponse;
import albaradimassuntoro.simplebank.model.CreateAccountRequest;
import albaradimassuntoro.simplebank.model.PagingRequest;
import albaradimassuntoro.simplebank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{

  @Autowired
  public AccountServiceImpl(AccountRepository accountRepository, ValidationService validationService) {
    this.accountRepository = accountRepository;
    this.validationService = validationService;
  }

  private final AccountRepository accountRepository;


  private final ValidationService validationService;

  @Override
  public AccountResponse create(String username, CreateAccountRequest request) {
    validationService.validate(request);

    Account account = new Account();
    account.setId(UUID.randomUUID().toString());
    account.setOwner(username);
    account.setBalance(0L);
    account.setCurrency(request.getCurrency());
    account.setCreatedAt(new Timestamp(new Date().getTime()));
    accountRepository.save(account);

    return AccountResponse.builder().id(account.getId()).createdAt(account.getCreatedAt()).balance(account.getBalance()).currency(account.getCurrency()).owner(account.getOwner()).build();
  }

  private AccountResponse toAccountResponse(Account account){
    return AccountResponse.builder().currency(account.getCurrency()).balance(account.getBalance()).createdAt(account.getCreatedAt()).id(account.getId()).owner(account.getOwner()).build();
  }

  @Override
  public Page<AccountResponse> list(String username, PagingRequest request) {
    validationService.validate(request);
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Account> accounts = accountRepository.findAllByOwner(username, pageable);
    List<AccountResponse> responseList = accounts.getContent().stream().map(this::toAccountResponse).toList();
    return new PageImpl<>(responseList,pageable,accounts.getTotalElements());
  }
}
