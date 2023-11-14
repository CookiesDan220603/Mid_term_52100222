package com.example.springcommerce.service;

import com.example.springcommerce.dto.request.ProductRequest;
import com.example.springcommerce.dto.response.ProductResponse;
import com.example.springcommerce.model.Category;
import com.example.springcommerce.model.Product;
import com.example.springcommerce.repository.CategoryRepository;
import com.example.springcommerce.repository.ProductRepository;
import com.example.utils.ProductSpecification;
import com.example.utils.SearchCriteria;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductResponse convertProduct(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public String addProduct(ProductRequest request) {
        var product = productRepository.findByName(request.getName());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        if (product == null){
           if (category != null) {
               Product pro = Product.builder()
                       .name(request.getName())
                       .description(request.getDescription())
                       .price(request.getPrice())
                       .quantity(request.getQuantity())
                       .category(category)
                       .brand(request.getBrand())
                       .color(request.getColor())
                       .build();
               productRepository.save(pro);
               category.getProducts().add(pro);
               categoryRepository.save(category);
               return "success";
           }
        }
        return "failed";
    }

    public ProductResponse updateProduct(ProductRequest request, String id) {
        var product = productRepository.findById(id).orElse(null);
        if (product != null){
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            productRepository.save(product);
            return convertProduct(product);
        }
        return null;
    }

    public ProductResponse deleteProduct(String id) {
        var product = productRepository.findById(id).orElse(null);
        if (product != null){
            productRepository.delete(product);
            return convertProduct(product);
        }
        return null;
    }

    public List<ProductResponse> searchProduct(String filter, Double priceFrom, Double priceTo) {
        if (StringUtils.isNotBlank(filter) && priceFrom != null && priceTo != null) {
            return productRepository.searchAll(filter, priceFrom, priceTo).stream().map(this::convertProduct).toList();
        } else if (StringUtils.isNotBlank(filter) && priceFrom != null) {
            return productRepository.searchAllMinPrice(filter, priceFrom).stream().map(this::convertProduct).toList();
        } else if (StringUtils.isNotBlank(filter) && priceTo != null) {
            return productRepository.searchAllMaxPrice(filter, priceTo).stream().map(this::convertProduct).toList();
        } else if (StringUtils.isNotBlank(filter)) {
            return productRepository.searchAll(filter).stream().map(this::convertProduct).toList();
        } else if (priceFrom != null && priceTo != null) {
            return productRepository.searchAll(priceFrom, priceTo).stream().map(this::convertProduct).toList();
        } else if (priceFrom != null) {
            return productRepository.searchAllMinPrice(priceFrom).stream().map(this::convertProduct).toList();
        } else if (priceTo != null) {
            return productRepository.searchAllMaxPrice(priceTo).stream().map(this::convertProduct).toList();
        }



//        List<Product> result = productRepository.findAll();
//        if (StringUtils.isNotBlank(category)) {
//            List<Product> searchCategory = new ArrayList<>();
//            for (Product product : result) {
//                if (product.getCategory().getId().equalsIgnoreCase(category)) {
//                    searchCategory.add(product);
//                }
//            }
//            if (CollectionUtils.isEmpty(searchCategory)) {
//                return searchCategory;
//            }
//            result = searchCategory;
//        }
//        if (StringUtils.isNotBlank(category)) {
//            return productRepository.findByCategory_id(category);
//        }

//        if (priceFrom != null) {
//            List<Product> searchPriceFrom = new ArrayList<>();
//            for (Product product : result) {
//                if (product.getPrice() >= priceFrom) {
//                    searchPriceFrom.add(product);
//                }
//            }
//            if (CollectionUtils.isEmpty(searchPriceFrom)) {
//                return searchPriceFrom;
//            }
//            result = searchPriceFrom;
//        }
//
//
//        if (priceTo != null) {
//            List<Product> searchPriceTo = new ArrayList<>();
//            for (Product product : result) {
//                if (product.getPrice() <= priceTo) {
//                    searchPriceTo.add(product);
//                }
//            }
//            if (CollectionUtils.isEmpty(searchPriceTo)) {
//                return searchPriceTo;
//            }
//            result = searchPriceTo;
//        }
        return null;
    }

    public ProductResponse getProductById(String id) {
        return convertProduct(productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found")));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::convertProduct).toList();
    }

    public List<String> findAllBrand() {
        return productRepository.findDistinctBrand();
    }

    public List<String> findAllColor() {
        return productRepository.findDistinctColor();
    }

    public List<ProductResponse> filter(String filter) {
        var tabs = List.of(filter.split(";"));

        System.out.println("tabs: " + tabs);
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        tabs.forEach(tab -> {
            var key = tab.split(":")[0];
            var value = tab.split(":")[1];

            switch (key) {
                case "brands" -> {
                    var brands = value.replace("%20", " ").split(",");
                    for (var brand : brands) {
                        searchCriteria.add(SearchCriteria.builder().key("brand").operation(":").value(brand).build());
                    }
                }
                case "colors" -> {
                    var colors = value.split(",");
                    for (var color : colors) {
                        searchCriteria.add(SearchCriteria.builder().key("color").operation(":").value(color).build());
                    }
                }
                case "price" -> {
                    if (value.contains("-")) {
//						two case -153 or 0-300
                        var prices = value.split("-");
                        if (value.startsWith("-")) {
                            System.out.println("co 1 gia tri maxPrice" + "; value: " + value);
                            searchCriteria.add(SearchCriteria.builder().key("price").operation("<").value(Double.parseDouble(prices[1])).build());
                        } else if (value.endsWith("-")) {
                            System.out.println("co 1 gia tri minPrice" + "; value: " + value);
                            searchCriteria.add(SearchCriteria.builder().key("price").operation(">").value(Double.parseDouble(prices[0])).build());
                        } else {
                            System.out.println("co 2 gia tri minPrice va maxPrice" + "; value: " + value);
                            searchCriteria.add(SearchCriteria.builder().key("price").operation("><").value(Double.parseDouble(prices[0])).secondValue(Double.parseDouble(prices[1])).build());
                        }
                    } else {
                        System.out.println("co 1 gia tri minPrice" + "; value: " + value);
                        searchCriteria.add(SearchCriteria.builder().key("price").operation(">").value(value).build());
                    }
                }
                default -> {}
            }
        });

        Specification<Product> spec = Specification.where(null);

        for (var criteria : searchCriteria) {
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                spec = spec.and(new ProductSpecification(criteria));
            } else if (criteria.getOperation().equalsIgnoreCase("><")) {
                spec = spec.and(new ProductSpecification(criteria));
            } else if (criteria.getOperation().equalsIgnoreCase(">")) {
                spec = spec.and(new ProductSpecification(criteria));
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                spec = spec.and(new ProductSpecification(criteria));
            }
        }
        return productRepository.findAll(spec).stream().map(this::convertProduct).toList();
    }
}
