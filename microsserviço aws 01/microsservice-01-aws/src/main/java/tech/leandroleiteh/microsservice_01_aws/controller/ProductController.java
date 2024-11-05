package tech.leandroleiteh.microsservice_01_aws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.leandroleiteh.microsservice_01_aws.model.Product;
import tech.leandroleiteh.microsservice_01_aws.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProducts(){
        var productList = productRepository.findAll();
        LOGGER.info("Products all: {}", productList);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable long id){
        var product = productRepository.findById(id);
        if (product.isPresent()){
            LOGGER.info("Product get for Id: {}", product.get());
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/byCode")
    public ResponseEntity<Product> findByCode(@RequestParam String code){
        var product = productRepository.findByCode(code);
        if (product.isPresent()){
            LOGGER.info("Product get for Code: {}", product.get());
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        var productCreate = productRepository.save(product);
        LOGGER.info("Product Created: {}", productCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable long id){
        if (productRepository.existsById(id)){
            product.setId(id);
            LOGGER.info("Product update: {}", product);
            return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id){
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            LOGGER.info("Product deleted: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.notFound().build();
    }

}
