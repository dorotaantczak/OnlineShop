package com.reply.challenge.controller;

import com.reply.challenge.model.AccountType;
import com.reply.challenge.model.Customer;
import com.reply.challenge.model.ProfileType;
import com.reply.challenge.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        super();
        this.customerService = customerService;
    }

    @Operation(
            summary = "Retrieving all customers resources",
            method = "GET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Json list of customers resources in body of response.",
                            content = {@Content(mediaType = "application/json")}
                    )
            }
    )

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomer() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getAllCustomers());
    }

    @Operation(
            summary = "Finds and returns a single customer resource by the supplied id.",
            method = "GET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer resource successfully found and returned as json object in."
                                    + " Body of the response.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer resource not found.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getCustomerById(id));
    }

    @Operation(
            summary = "Finds and returns array of customers resource by the supplied attribute which is name.",
            method = "GET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer resource successfully found and returned as json object in."
                                    + " Body of the response.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer with specific name not found.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )


    @GetMapping("searchCustomerByName/{name}") //api/v1/customers/searchCustomerByName/{name}
    private ResponseEntity<Customer> getCustomerName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.searchCustomerByName(name));
    }

    @Operation(
            summary = "Finds and returns array of customers resource by the supplied attribute which is profile type and account type.",
            method = "GET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer resource successfully found and returned as json object in."
                                    + " Body of the response.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer with specific profile type and account type not found.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @GetMapping("searchCustomerByProfileTypeAndAccountType")
    private ResponseEntity<List<Customer>> getCustomerProfileAndAccountType(
            @RequestParam("profile") ProfileType profileType,
            @RequestParam("account") AccountType accountType) {
        List<Customer> customers = customerService.searchCustomerByProfileTypeAndAccountType(profileType, accountType);
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.searchCustomerByProfileTypeAndAccountType(profileType, accountType));
    }

    @Operation(
            summary = "Creating a new customer resource.",
            description = "Accepts and validates a customer object passed via body of the request. Checks that the id "
                    + "is not already present in persistence context. Then saves new customer resource.",
            method = "POST",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer created.",
                            content = {@Content(mediaType = "application/json")},
                            headers = {@Header(
                                    name = "Location",
                                    description = "URI of new customer resource."
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Customer with such name is already present.",
                            content = {@Content(mediaType = "text/plain")}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Supplied customer is invalid, returns CSV string with validation errors.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer) {
        customerService.addCustomer(customer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(customer);
    }

    @Operation(
            summary = "Updating a new customer resource.",
            description = "Accepts and validates a customer object passed via body of the request. Checks that the id "
                    + "is not already present in persistence context. Then saves updated customer resource.",
            method = "PUT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer updated.",
                            content = {@Content(mediaType = "application/json")}

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Supplied employeee is invalid, returns CSV string with validation errors.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customer, @PathVariable int id) {
        return ResponseEntity.ok(customerService.updateCustomerById(customer, id));
    }

    @Operation(
            summary = "Deleting existing customer resource.",
            description = "Accepts and validates a customer object passed via body of the request. Checks id.",
            method = "DELETE",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer deleted.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer resource not found.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(
            summary = "Finds and returns an updated single customer (profile type and number identification) resource by the supplied id.",
            method = "PATCH",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer resource successfully found and returned as json object in."
                                    + " Body of the response.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Supplied profile type is invalid, returns CSV string with validation errors.",
                            content = {@Content(mediaType = "text/plain")}
                    )
            }
    )

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer updatedCustomer) {
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedCustomer.getProfileType() != null) {
            existingCustomer.setProfileType(updatedCustomer.getProfileType());
        }
        if (updatedCustomer.getNumberIdentification() != null) {
            existingCustomer.setNumberIdentification(updatedCustomer.getNumberIdentification());
        }

        return ResponseEntity.ok(customerService.updateCustomerById(existingCustomer, id));
    }

}