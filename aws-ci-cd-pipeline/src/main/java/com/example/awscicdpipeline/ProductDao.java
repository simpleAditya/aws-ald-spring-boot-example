package com.example.awscicdpipeline;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ProductDao {


    public List<ProductDto> getProducts() {
        return Stream.of(
                new ProductDto(101, "Mobile", 1, 30000),
                new ProductDto(38, "Book", 4, 2000),
                new ProductDto(205, "Laptop", 1, 150000),
                new ProductDto(809, "Headset", 1, 1799))
                .collect(Collectors.toList());
    }
}
