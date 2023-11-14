package com.example.springcommerce.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CategoryRequest {
    private String name;
    private String description;
}
