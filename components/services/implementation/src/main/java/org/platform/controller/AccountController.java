package org.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.platform.constants.RoutConstants;
import org.platform.exceptions.ExceptionResponse;
import org.platform.model.Account;
import org.platform.model.User;
import org.platform.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.VERSION + RoutConstants.ACCOUNTS)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Account createAccount(@RequestBody Account account) {
        log.info("Received request to create account.");
        Account createdAccount = accountService.createAccount(account);
        log.info("Account created.");
        return createdAccount;
    }


    @Operation(summary = "Get list of accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error occurred while getting account.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @GetMapping()
    public @ResponseBody List<Account> getAccounts() {
        log.info("Received request to get list of users.");
       List<Account> accounts = accountService.getAccounts();
        log.info("Users found");
        return accounts;
    }

    @GetMapping("/{id}")
    public @ResponseBody Account getAccountById(@PathVariable UUID id) {
          return accountService.getAccount(id);
    }


    @Operation(summary = "Update account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "Account not found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error occurred while updating user.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Account updateAccount(@PathVariable UUID id, @RequestBody @Valid Account account) {
        log.info("Received request to update account.");
        Account newAccount = accountService.updateAccount(id, account);
        log.info("Account updated Successfully.");
        return newAccount;
    }

    @Operation(summary = "Delete account by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error occurred while deleting user.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable UUID accountId) {
            accountService.deleteAccount(accountId);
    }
}
