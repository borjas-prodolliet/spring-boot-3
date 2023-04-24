package com.borjitascode.customer;

import com.borjitascode.customer.Customer;
import com.borjitascode.customer.NewCustomerRequest;
import com.borjitascode.customer.CustomerRepository;
import com.borjitascode.customer.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer addCustomer(Customer customer) {
        Boolean existsEmail = customerRepository.selectExistsEmail(customer.getEmail());
        if (existsEmail) {
            throw new BadRequestException(String.format("Email %s already taken", customer.getEmail()));
        }

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
