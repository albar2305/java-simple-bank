package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping(
      path = "/api/accounts",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<AccountResponse> register(@RequestBody CreateAccountRequest request){
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    AccountResponse accountResponse = accountService.create(username, request);
    return WebResponse.<AccountResponse>builder().data(accountResponse).build();
  }

  @GetMapping(
      path = "/api/accounts",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<List<AccountResponse>> list(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    PagingRequest request = PagingRequest.builder().page(page).size(size).build();
    Page<AccountResponse> accountResponses = accountService.list(username, request);
    return WebResponse.<List<AccountResponse>>builder()
        .data(accountResponses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(accountResponses.getNumber())
            .totalPage(accountResponses.getTotalPages())
            .size(accountResponses.getSize())
            .build())
        .build();
  }
}
