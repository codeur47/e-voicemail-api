package com.yorosoft.evoicemailapi.mapper;

import com.yorosoft.evoicemailapi.dto.CustomerRequest;
import com.yorosoft.evoicemailapi.dto.CustomerResponse;
import com.yorosoft.evoicemailapi.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerRequest mapCustomerToCustomerRequest(Customer customer);
    Customer mapCustomerRequestToCustomer(CustomerRequest customerRequest);

    CustomerResponse mapCustomerToCustomerResponse(Customer customer);
    Customer mapCustomerResponseToCustomer(CustomerResponse customerResponse);

    List<CustomerResponse> mapCustomerListToCustomerResponseList(List<Customer> customerList);
    List<Customer> mapCustomerResponseListToCustomerList(List<CustomerResponse> customerResponseList);
}
