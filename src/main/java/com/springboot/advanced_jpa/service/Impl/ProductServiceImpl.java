package com.springboot.advanced_jpa.service.Impl;

import com.springboot.advanced_jpa.data.dto.ProductDto;
import com.springboot.advanced_jpa.data.dto.ProductResponseDto;
import com.springboot.advanced_jpa.data.entity.Product;
import com.springboot.advanced_jpa.data.repository.ProductRepository;
import com.springboot.advanced_jpa.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service // Entity class : Product
public class ProductServiceImpl implements ProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto getProduct(Long number) {
        LOGGER.info("[getProduct] input number : {}", number);
        Product product = productRepository.findById(number).get();

        LOGGER.info("[getProduct] input number : {}, name:{}", product.getNumber(),
                product.getName());

        // Entity 객체가 반환됨. 따라서 일반 DTO 객체로 변환
        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setNumber(product.getNumber());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setStock(product.getStock());

        return productResponseDto;

    }

    @Override
    public ProductResponseDto saveProduct(ProductDto productDto) {
        LOGGER.info("[saveProduct] productDTO : {}", productDto.toString());
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        LOGGER.info("[saveProduct] savedProduct :{}", savedProduct);

        ProductResponseDto productResponseDto = new ProductResponseDto();    // Entity 객체가 반환됨. 따라서 일반 DTO 객체로 변환
        productResponseDto.setNumber(savedProduct.getNumber());      // presentation 계층으로 다시 보내기 위해 다시 변환 작업을 함.
        productResponseDto.setName(savedProduct.getName());
        productResponseDto.setPrice(savedProduct.getPrice());
        productResponseDto.setStock(savedProduct.getStock());

        return productResponseDto;
    }

    // private final ProductDAO productDAO;



   /* @Autowired  // : DAO레이어 생략 service에서 바로 Repository 사용
       public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }*/

    /* @Override*/
  /*  public ProductResponseDto getProduct(Long number) {
  // service를 기준으로 db에 접근 entity 설계도 참고해서 만들었음.
        Product product = productDAO.selectProduct(number);
        // Entity 객체가 반환됨. 따라서 일반 DTO 객체로 변환

        ProductResponseDto productResponseDto = new ProductResponseDto();    // Entity 객체가 반환됨. 따라서 일반 DTO 객체로 변환

        productResponseDto.setNumber(product.getNumber());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setStock(product.getStock());

        return productResponseDto;
    }*/
      /*  @Override
        public ProductResponseDto saveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productDAO.insertProduct(product);

        ProductResponseDto productResponseDto = new ProductResponseDto();    // Entity 객체가 반환됨. 따라서 일반 DTO 객체로 변환
        productResponseDto.setNumber(savedProduct.getNumber());      // presentation 계층으로 다시 보내기 위해 다시 변환 작업을 함.
        productResponseDto.setName(savedProduct.getName());
        productResponseDto.setPrice(savedProduct.getPrice());
        productResponseDto.setStock(savedProduct.getStock());

        return productResponseDto;
    }*/

/*    @Override
    public ProductResponseDto changeProductName(Long number, String name) throws Exception {

        Product changedProduct = productDAO.updateProduct(number, name);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setNumber(changedProduct.getNumber());
        productResponseDto.setName(changedProduct.getName());
        productResponseDto.setPrice(changedProduct.getPrice());
        productResponseDto.setStock(changedProduct.getStock());
        return productResponseDto;
    }*/
@Override
public ProductResponseDto changeProductName(Long number, String name) throws Exception {
    Product foundProduct = productRepository.findById(number).get();
    foundProduct.setName(name);
    Product changedProduct = productRepository.save(foundProduct);

    ProductResponseDto productResponseDto = new ProductResponseDto();
    productResponseDto.setNumber(changedProduct.getNumber());
    productResponseDto.setName(changedProduct.getName());
    productResponseDto.setPrice(changedProduct.getPrice());
    productResponseDto.setStock(changedProduct.getStock());

    return productResponseDto;
}

/*    @Override
    public void deleteProduct(Long number) throws Exception {  // 리턴값이 없기 때문에 간결.
        productDAO.deleteProduct(number);
    }*/
@Override
public void deleteProduct(Long number) throws Exception {  // 리턴값이 없기 때문에 간결.
    productRepository.deleteById(number);
}

}
