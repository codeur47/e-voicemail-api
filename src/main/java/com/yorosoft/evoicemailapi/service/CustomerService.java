package com.yorosoft.evoicemailapi.service;

import com.yorosoft.evoicemailapi.exception.domain.BlankValueException;
import com.yorosoft.evoicemailapi.exception.domain.CustomerFoundException;
import com.yorosoft.evoicemailapi.exception.domain.PhoneNumberExistException;
import com.yorosoft.evoicemailapi.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(String firstName, String lastName, String phoneNumber) throws BlankValueException, CustomerFoundException, PhoneNumberExistException;

    List<Customer> findAll();

    void deleteCustomer(Long id) throws BlankValueException, CustomerFoundException;

    Customer findById(Long id) throws CustomerFoundException;

    Customer update(Long id,String firstName, String lastName, String phoneNumber) throws BlankValueException, CustomerFoundException;
}
