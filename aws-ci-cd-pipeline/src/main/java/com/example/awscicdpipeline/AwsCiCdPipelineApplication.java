package com.example.awscicdpipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("/products")
public class AwsCiCdPipelineApplication {

	@Autowired
	private ProductDao productDao;

	@GetMapping
	public List<ProductDto> fetchProducts() {
		return productDao.getProducts().stream().
				sorted(Comparator.comparing(ProductDto::getPrice)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsCiCdPipelineApplication.class, args);
	}

}