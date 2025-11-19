package com.demoh.demoh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

@SpringBootApplication
public class DemohApplication {

    @RestController
    class HelloworldController {

		@CrossOrigin
        @GetMapping("/")
        String hello() {
            return "hello version 6";
        }

		@CrossOrigin
        @PostMapping("/process")
        public ResponseEntity<ResponseDTO> processInput(@RequestBody RequestDTO request) {

            // 1. Get the value from the request
            String code = request.getInputCode();

            if (code.equals("justreturndemoaccount")) {
                // This returns a fixed account, just for demo purposes
                String hurl = "https://hashscan.io/testnet/transaction/0.0.6214184@1763473252.738000111";
                String accountid = "0.0.7279544";
                String evm = "*****************************************";
                String privatek = "***********************************************";
                String publick = "************************************************";
                return ResponseEntity.ok(new ResponseDTO(accountid, evm, hurl, privatek, publick));
            }

			// only do the task if this real valid password was sent
            if (!code.equals("*********************************************")) {
                // invalid password for service, return empty response
                return ResponseEntity.ok(new ResponseDTO("*", "*", "*", "*", "*"));
            }

            // create account
            System.out.println("***START");
            Client client = null;
            try {

                // Your account ID and private key from string value
                AccountId MY_ACCOUNT_ID = AccountId.fromString("*************************");
                PrivateKey MY_PRIVATE_KEY = PrivateKey.fromStringED25519(
                        "***************************************************************");

                // Pre-configured client for test network (testnet)
                client = Client.forTestnet();

                // Set the operator with the account ID and private key
                client.setOperator(MY_ACCOUNT_ID, MY_PRIVATE_KEY);

                // create account
                //Generate a new key for the account
                PrivateKey accountPrivateKey = PrivateKey.generateECDSA();
                PublicKey accountPublicKey = accountPrivateKey.getPublicKey();

                AccountCreateTransaction txCreateAccount = new AccountCreateTransaction()
                        .setKeyWithAlias(accountPublicKey)
                        //DO NOT set an alias with your key if you plan to update/rotate keys in the future, Use .setKeyWithoutAlias instead 
                        //.setKeyWithoutAlias(accountPublicKey)
                        .setInitialBalance(new Hbar(10));

                //Submit the transaction to a Hedera network
                TransactionResponse txCreateAccountResponse = txCreateAccount.execute(client);

                //Request the receipt of the transaction
                TransactionReceipt receiptCreateAccountTx = txCreateAccountResponse.getReceipt(client);

                //Get the transaction consensus status
                Status statusCreateAccountTx = receiptCreateAccountTx.status;

                //Get the Account ID
                AccountId accountId = receiptCreateAccountTx.accountId;

                //Get the Transaction ID
                String txIdAccountCreated = txCreateAccountResponse.transactionId.toString();

                System.out.println("------------------------------ Create Account ------------------------------ ");
                System.out.println("Receipt status       : " + statusCreateAccountTx.toString());
                System.out.println("Transaction ID       : " + txIdAccountCreated);
                System.out.println("Hashscan URL         : https://hashscan.io/testnet/transaction/" + txIdAccountCreated);
                System.out.println("Account ID           : " + accountId.toString());

                // String accountId, String evmAddress, String hashscanURL, String privateKey, String publicKey
                ResponseDTO response = new ResponseDTO(accountId.toString(), "" + accountPublicKey.toEvmAddress(), "https://hashscan.io/testnet/transaction/" + txIdAccountCreated, accountPrivateKey.toStringRaw(), accountPublicKey.toStringRaw());
                return ResponseEntity.ok(response);
            } catch (Exception ex) {
                ex.printStackTrace();
                ResponseDTO response = new ResponseDTO("***", ex.getMessage(), "**", "**", "**");
                return ResponseEntity.ok(response);
            } finally {
                if (client != null) {
                    try {
                        client.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemohApplication.class, args);
    }

}
