package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.model.CreateTransferRequest;
import albaradimassuntoro.simplebank.model.TransferResponse;

public interface TransferService {

  TransferResponse createTransfer(CreateTransferRequest request, String username);

}
