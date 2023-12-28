package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.model.AccountResponse;
import albaradimassuntoro.simplebank.model.CreateAccountRequest;
import albaradimassuntoro.simplebank.model.PagingRequest;
import org.springframework.data.domain.Page;

public interface AccountService {
  AccountResponse create(String username, CreateAccountRequest request);

  Page<AccountResponse> list(String username, PagingRequest request);

  AccountResponse get(String username, String id);

}
