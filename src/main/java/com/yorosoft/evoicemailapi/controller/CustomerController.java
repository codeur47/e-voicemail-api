package com.yorosoft.evoicemailapi.controller;

import com.yorosoft.evoicemailapi.dto.CustomerRequest;
import com.yorosoft.evoicemailapi.dto.CustomerResponse;
import com.yorosoft.evoicemailapi.exception.domain.*;
import com.yorosoft.evoicemailapi.mapper.CustomerMapper;
import com.yorosoft.evoicemailapi.model.Customer;
import com.yorosoft.evoicemailapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {  "/customers"})
public class CustomerController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping()
    public ResponseEntity<CustomerResponse> register(@RequestBody CustomerRequest userRequest) throws UserNotFoundException, UsernameExistException, BlankValueException, CustomerFoundException, PhoneNumberExistException {
        Customer customer = customerService.create(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getPhoneNumber());
        return new ResponseEntity<>(customerMapper.mapCustomerToCustomerResponse(customer), OK);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return new ResponseEntity<>(customerMapper.mapCustomerListToCustomerResponseList(customers), OK);
    }
}
