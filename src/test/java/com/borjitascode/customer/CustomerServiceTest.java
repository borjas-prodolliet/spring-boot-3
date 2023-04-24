package com.borjitascode.customer;

import com.borjitascode.customer.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {;
        underTest = new CustomerService(customerRepository);
    }

    @Test
    void canGetAllCustomers() {
        // when
        underTest.getAllCustomers();

        // then
        verify(customerRepository).findAll();
    }

    @Test
    void canAddCustomer() {
        // given
        Customer customer = new Customer(
                "Test User", "test@test.com", 27
        );

        // when
        underTest.addCustomer(customer);

        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer).isEqualTo(customer);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Customer customer = new Customer(
                "Test User", "test@test.com", 27
        );

        given(customerRepository.selectExistsEmail(anyString())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addCustomer(customer))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("Email %s already taken", customer.getEmail()));

        verify(customerRepository, never()).save(any());
    }
}