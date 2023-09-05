package com.springboot.advanced_jpa.data.entity;


import com.springboot.advanced_jpa.data.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void auditingTest(){
        Product product = new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(100);

        Product savedProduct = productRepository.save(product);

        System.out.println("Product Name : " + savedProduct.getName());
        System.out.println("createdAt :" + savedProduct.getCreatedAt());
    }
}