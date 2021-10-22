package com.yorosoft.evoicemailapi.controller;

import com.yorosoft.evoicemailapi.dto.CustomerRequest;
import com.yorosoft.evoicemailapi.dto.CustomerResponse;
import com.yorosoft.evoicemailapi.exception.domain.*;
import com.yorosoft.evoicemailapi.mapper.CustomerMapper;
import com.yorosoft.evoicemailapi.model.Customer;
import com.yorosoft.evoicemailapi.service.CustomerService;
import com.yorosoft.evoicemailapi.utility.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {  "/customers"})
public class CustomerController {

    public static final String CUSTOMER_DELETED_SUCCESSFULLY = "Customer deleted successfully";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping()
    public ResponseEntity<CustomerResponse> save(@RequestBody CustomerRequest userRequest) throws UserNotFoundException, UsernameExistException, BlankValueException, CustomerFoundException, PhoneNumberExistException {
        Customer customer = customerService.create(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getPhoneNumber());
        return new ResponseEntity<>(customerMapper.mapCustomerToCustomerResponse(customer), OK);
    }

    @PutMapping ()
    public ResponseEntity<CustomerResponse> update(@RequestBody CustomerRequest customerRequest) throws BlankValueException, CustomerFoundException {
        Customer customer = customerService.update(customerRequest.getId(),customerRequest.getFirstName(),customerRequest.getLastName(),customerRequest.getPhoneNumber());
        return new ResponseEntity<>(customerMapper.mapCustomerToCustomerResponse(customer), OK);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAll() {
        List<Customer> customers = customerService.findAll();
        return new ResponseEntity<>(customerMapper.mapCustomerListToCustomerResponseList(customers), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) throws BlankValueException, CustomerFoundException {
        customerService.deleteCustomer(id);
        return response(OK, CUSTOMER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
