package com.yorosoft.evoicemailapi.repository;

import com.yorosoft.evoicemailapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerById(Long customerId);
    Customer findCustomersByLastNameAndFirstName(String lastName, String firstName);
    Customer findCustomersByPhoneNumber(String phoneNumber);
}
