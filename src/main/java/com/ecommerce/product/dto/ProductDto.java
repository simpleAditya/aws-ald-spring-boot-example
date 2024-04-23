package com.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private String id;

    private String name;

    private String description;

    private String type;

    private Double price;

    private String currency;

    private String createdAt;

    private String updatedAt;

    private String status;

    private String errorMessage;
}
