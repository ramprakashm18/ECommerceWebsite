package com.project.ecom_project.controller;

import com.project.ecom_project.model.Product;
import com.project.ecom_project.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

//    @GetMapping("/")
//    public String greet(){
//        return "Hello!";
//    }
//

    private ProductService service;
    public ProductController(ProductService service){
        this.service = service;
    }

//    @GetMapping("/products")
//    public List<Product> getAllProducts(){
//        return service.getAllProducts();
//    }
//
//    @GetMapping("/products/{id}")
//    public Product getProductById(@PathVariable int id){
//        return service.getProductById(id);
//    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {

        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try{
            Product prod = service.addProduct(product,imageFile);
            return new ResponseEntity<>(prod,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id){
        Product prod = service.getProductById(id);
        byte[] imageFile = prod.getImageData();

        if (prod == null || prod.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id
            ,@RequestPart Product product
            , @RequestPart MultipartFile imageFile){
        Product prod = null;
        try {
            prod = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to Update.",HttpStatus.BAD_REQUEST);
        }

        if(prod!=null){
            return new ResponseEntity<>("Updated Successfully.",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed to Update.",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        Product prod = service.getProductById(id);

        if(prod!=null){
            service.deleteProductById(id);
            return new ResponseEntity<>("Product Deleted.",HttpStatus.OK);
        }else return new ResponseEntity<>("Product Not Found.",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        System.out.println("Searching with "+keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
