package com.yorosoft.evoicemailapi.service.impl;

import com.yorosoft.evoicemailapi.exception.domain.BlankValueException;
import com.yorosoft.evoicemailapi.exception.domain.CustomerFoundException;
import com.yorosoft.evoicemailapi.exception.domain.PhoneNumberExistException;
import com.yorosoft.evoicemailapi.model.Customer;
import com.yorosoft.evoicemailapi.repository.CustomerRepository;
import com.yorosoft.evoicemailapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(String firstName, String lastName, String phoneNumber) throws BlankValueException, CustomerFoundException, PhoneNumberExistException {
        validateCustomerInfo(firstName, lastName, phoneNumber);
        verifyCustomerInDatabase(firstName,lastName);
        verifyCustomerPhoneNumber(phoneNumber);
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    private void validateCustomerInfo(String firstName, String lastName, String phoneNumber) throws BlankValueException {
        if (!StringUtils.isNotBlank(firstName) || !StringUtils.isNotBlank(lastName) || !StringUtils.isNotBlank(phoneNumber))
            throw new BlankValueException("Values must not null or blank");
    }

    private void verifyCustomerInDatabase(String firstName, String lastName) throws CustomerFoundException {
        if (customerRepository.findCustomersByLastNameAndFirstName(lastName,firstName) != null)
            throw new CustomerFoundException("Customer already exist in database");
    }

    private void verifyCustomerPhoneNumber(String phoneNumber) throws PhoneNumberExistException {
        if (customerRepository.findCustomersByPhoneNumber(phoneNumber) != null)
            throw new PhoneNumberExistException("Phone number already exist in database");
    }
}
