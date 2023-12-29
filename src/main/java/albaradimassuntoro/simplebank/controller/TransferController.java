package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

  private final TransferService transferService;

  @Autowired
  public TransferController(TransferService transferService) {
    this.transferService = transferService;
  }

  @PostMapping(
      path = "/api/transfers",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<TransferResponse> register(@RequestBody CreateTransferRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    TransferResponse transfer = transferService.createTransfer(request, username);
    return WebResponse.<TransferResponse>builder().data(transfer).build();
  }
}
