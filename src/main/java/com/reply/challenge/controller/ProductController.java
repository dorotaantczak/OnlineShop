package com.reply.challenge.controller;

import com.reply.challenge.exception.CategoryNotFound;
import com.reply.challenge.model.Category;
import com.reply.challenge.model.Product;
import com.reply.challenge.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        super();
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProduct() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    @GetMapping("searchProductByName/{name}") //api/v1/products/searchProductByName/{name}
    private ResponseEntity<Product> getProductName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.searchProductByName(name));
    }

    @GetMapping("searchProductByCategory") //api/v1/products/searchProductByCategory{category}
    private ResponseEntity<List<Product>> getProductCategory(@RequestParam("category") String categoryString) {
        Category category;
        try {
            category = Category.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            throw new CategoryNotFound("Category " + categoryString + " not found.");
        }

        List<Product> products = productService.searchProductByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.searchProductByCategory(category));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        productService.addProduct(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()         // http://localhost:80xx/api/v1/products
                .path("/{id}")                // http://localhost:80xx/api/v1/products/{id}
                .buildAndExpand(product.getId()) // http://localhost:80xx/api/v1/products/4
                .toUri();
        return ResponseEntity
                .created(location)
                .body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product, @PathVariable int id) {
        return ResponseEntity.ok(productService.updateProductById(product, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProductByPriceOrAmount(@PathVariable int id, @RequestBody Product updatedProduct) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getAmountInStock() != null) {
            existingProduct.setAmountInStock(updatedProduct.getAmountInStock());
        }

        return ResponseEntity.ok(productService.updateProductById(existingProduct, id));
    }

}