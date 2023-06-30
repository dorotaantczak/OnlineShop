package com.reply.challenge.repository;

import com.reply.challenge.model.Customer;
import com.reply.challenge.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findCustomerByName (String name);

}
