package com.example.springcommerce.dto.response;

import com.example.springcommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String id;
    private String user_id;
    private Date created_date;
    private Date updated_date;
    private List<ProductResponse> products;
}
