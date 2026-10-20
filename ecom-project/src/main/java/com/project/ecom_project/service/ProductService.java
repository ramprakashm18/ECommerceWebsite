package com.project.ecom_project.service;

import com.project.ecom_project.model.Product;
import com.project.ecom_project.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private ProductRepository repo;
    public ProductService(ProductRepository repo){
        this.repo = repo;
    }
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

//    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageData(imageFile.getBytes());
//        return repo.save(product);
//    }
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {

        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        if (product.getStockQuantity() == 0) {
            product.setProductAvailable(false);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
        }

        return repo.save(product);
    }

//    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageData(imageFile.getBytes());
//        return repo.save(product);
//    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile)
            throws IOException {

        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Product existingProduct = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setId(id);

        if (product.getStockQuantity() == 0) {
            product.setProductAvailable(false);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
        } else {
            product.setImageName(existingProduct.getImageName());
            product.setImageType(existingProduct.getImageType());
            product.setImageData(existingProduct.getImageData());
        }

        return repo.save(product);
    }

    public void deleteProductById(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
