package com.springboot.advanced_jpa.data.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.advanced_jpa.data.entity.Product;
import com.springboot.advanced_jpa.data.entity.QProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

      @BeforeEach
    void beforeEach(){
        // given
        Product product1 = new Product();
        product1.setName("펜");
        product1.setPrice(1000);
        product1.setStock(100);
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());

        Product product2 = new Product();
        product2.setName("펜");
        product2.setPrice(5000);
        product2.setStock(300);
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());

        Product product3 = new Product();
        product3.setName("펜");
        product3.setPrice(500);
        product3.setStock(50);
        product3.setCreatedAt(LocalDateTime.now());
        product3.setUpdatedAt(LocalDateTime.now());

        Product savedProduct1 = productRepository.save(product1);

        Product savedProduct2 = productRepository.save(product2);

        Product savedProduct3 = productRepository.save(product3);

    }

    @Test
    void sortingAndPagingTest() {
        System.out.println("=================== 테스트 코드 결과 값 확인 ===================");
         System.out.println("== Sort 클래스는 내부 클래스로 정의되어있는 ==");
        System.out.println("== Order 객체를 활용하여 정렬 기준을 생성 ==");
        System.out.println("== Order 객체에는 asc와 desc 메소드가 포함되어있다. ==");
        System.out.println(productRepository.findByNameOrderByNumberAsc("펜"));
        System.out.println(productRepository.findByNameOrderByNumberDesc("펜"));

        System.out.println(productRepository.findByNameOrderByPriceAscStockDesc("펜"));
        System.out.println("===============================================================");

        System.out.println("==다중 정렬을 사용할 경우, comma를 사용하여 구분 ===");
        System.out.println(productRepository.findByName("펜",
                Sort.by(Sort.Order.asc("price"))));
        System.out.println(productRepository.findByName("펜",
                Sort.by(Sort.Order.asc("price"), Sort.Order.desc("stock"))));

        System.out.println("== Sort부분을 하나의 메서드로 분리하여 작성하면 가독성 좋음 ");
        System.out.println(productRepository.findByName("펜", getSort()));
        System.out.println("===============================================================");


        System.out.println("== 리턴타입: Page<Product>==");
        System.out.println("== Pageable파라미터를 전달하기 위해 PageRequest 클래스 사용==");
        System.out.println("== pageRequest는 Pageable의 구현체");
        System.out.println("== 일반적으로  PageRequest는 of()를 통해 PageRequest 객체를 생성 ==");
        System.out.println("== of() : 다양한 형태로 오버로딩 되어있다. ");
        System.out.println("== page객체 자체를 출력하면 몇번째 페이지에 해당하는지만 확인 가능 ");
        System.out.println("==페이징 쿼리 메서드를 호출하는 방법 == ");
        System.out.println(productRepository.findByName("펜",
                PageRequest.of(0, 2)));

        Page<Product> productPage = productRepository.findByName("펜",
                PageRequest.of(0, 2));
        System.out.println("=====" + productPage);

        System.out.println("==각 페이지를 구성하는 세부적인 값을 볼 경우 == ");
        System.out.println(productPage.getContent());

        System.out.println("=================================================================");
        System.out.println(productRepository.findByName("펜",
                PageRequest.of(0,2,Sort.by(Sort.Order.asc("price")))));

        System.out.println(productRepository.findByName("펜", PageRequest.of(0, 2,Sort.by(Sort.Order.asc("price")))).getContent());
    }

    // 예제 8.17 sort 부분을 하나의 메서드로 분리
    private Sort getSort() {
        return Sort.by(
                Sort.Order.asc("price"),
                Sort.Order.desc("stock")
        );
    }

    // ** 쿼리 어노테이션 사용 테스트
    @Test
    public void queryAnnotationTest(){
        System.out.println("==findByName==");
        System.out.println(productRepository.findByName("펜"));
        System.out.println("==findByNameParam==");
        System.out.println(productRepository.findByNameParam("펜"));

        List<Object[]> objects = productRepository.findByNameParam2("펜");
        System.out.println("==findByNameParam2==");
        for (Object[] object : objects) {
            System.out.println(object[0]);
            System.out.println(object[1]);
            System.out.println(object[2]);

        }
    }

    //*Test1*//*
    @Test
    public void findByNumberTest(){
        // given
        Product product = new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(100);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        // When
        Product foundProduct = productRepository.findByNumber(savedProduct.getNumber())
                .orElseThrow(() -> new RuntimeException());

        Assertions.assertThat(savedProduct.getNumber()).isEqualTo(foundProduct.getNumber());
        Assertions.assertThat(savedProduct.getName()).isEqualTo(foundProduct.getName());
        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(foundProduct.getPrice());
        Assertions.assertThat(savedProduct.getStock()).isEqualTo(foundProduct.getStock());

        // clean
        productRepository.delete(foundProduct);
    }

    //*Test2*//*
    @Test
    public void findAllByNameTest(){
        // given
        Product product1 = new Product();
        product1.setName("펜");
        product1.setPrice(1000);
        product1.setStock(100);
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());

        Product product2 = new Product();
        product2.setName("펜");
        product2.setPrice(5000);
        product2.setStock(300);
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product1);
        Product savedProduct1 = productRepository.save(product2);

        // when
        List<Product> foundProducts = productRepository.findAllByName("펜");
        Product foundProduct1 = foundProducts.get(3);
        Product foundProduct2 = foundProducts.get(4);

        // Then
        Assertions.assertThat(foundProduct1.getNumber()).isEqualTo(savedProduct.getNumber());
        Assertions.assertThat(foundProduct1.getName()).isEqualTo(savedProduct.getName());
        Assertions.assertThat(foundProduct1.getPrice()).isEqualTo(savedProduct.getPrice());
        Assertions.assertThat(foundProduct1.getStock()).isEqualTo(savedProduct.getStock());

        Assertions.assertThat(foundProduct2.getNumber()).isEqualTo(savedProduct1.getNumber());
        Assertions.assertThat(foundProduct2.getName()).isEqualTo(savedProduct1.getName());
        Assertions.assertThat(foundProduct2.getPrice()).isEqualTo(savedProduct1.getPrice());
        Assertions.assertThat(foundProduct2.getStock()).isEqualTo(savedProduct1.getStock());

        // clean
        productRepository.delete(foundProduct1);
        productRepository.delete(foundProduct2);
    }

 /* Test3*/
    public void queryByNumberTest() {
        // given
        Product product = new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(100);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        // When
        Product queriesProduct = productRepository.queryByNumber(savedProduct.getNumber());

        // then
        Assertions.assertThat(savedProduct.getNumber()).isEqualTo(queriesProduct.getNumber());


    }

    @PersistenceContext
    EntityManager entityManager;

     //* JQuery를 활용한 QueryDSL 테스트
    @Test
    void queryDslTest(){
        // 1. QueryDsl을 사용하기 위해 JPAQuery 객체 생성 <= EntityMamager를 이용
        // JPAQuery안에 우리가 사용할 수 있는 쿼리들이 존재함.
        // From : 무엇으로 부터 where :조건 , fetch : 다중값 리턴시키기 위한 메서드
        // 문자열 쿼리가 코드로 만들어짐 따라서 조금 더 안정적으로 실행할 수 있음.

        JPAQuery<Product> query = new JPAQuery(entityManager); // 빈 주입 위에서 함. entityManeger를 사용하여 객체 생성
        // 여러가지 메서드 제공 이를 활용해 쿼리문 생성 가능
        // 호출 형태는 빌더 패턴 형태

        QProduct qProduct = QProduct.product; // Qproduct클래스의 static 붙은 상수

        // JPAQuery는 빌더 형식으로 쿼리를 작성
        List<Product> productList = query
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch(); // list타입으로 리턴받기 위한 메서드

        for (Product product : productList) {
            System.out.println("------------");
            System.out.println();
            System.out.println("Product Number : " + product.getNumber());
            System.out.println("product name = " + product.getName());
            System.out.println("product Price = " + product.getPrice());
            System.out.println("product Stock = " + product.getStock());
            System.out.println();
            System.out.println("------------");

        }
    }

    /**JPAQueryFactory를 사용 : JPAQueryFactory는 select절부터 작성가능 **/
    // selectFrom() 을 사용 : 전체 조회 select * from 의미와 동일
    // select() from()를 분리 시키기
    @Test
    void queryDslTest2(){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QProduct qProduct = QProduct.product;

        List<Product> productList = jpaQueryFactory
                .selectFrom(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (Product product : productList) {
            System.out.println("------------");
            System.out.println();
            System.out.println("Product Number : " + product.getNumber());
            System.out.println("product name = " + product.getName());
            System.out.println("product Price = " + product.getPrice());
            System.out.println("product Stock = " + product.getStock());
            System.out.println();
            System.out.println("------------");

        }
    }

    @Test
    void queryDslTest3() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QProduct qProduct = QProduct.product;

        List<String> productList = jpaQueryFactory
                .select(qProduct.name)
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (String product : productList) {
            System.out.println("==============");
            System.out.println("product Name = " + product);
            System.out.println("==============");
        }

        List<Tuple> tupleList = jpaQueryFactory
                .select(qProduct.name, qProduct.price)
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (Tuple product : tupleList) {
            System.out.println("==============");
            System.out.println("product Name = " + product.get(qProduct.name));
            System.out.println("product Name = " + product.get(qProduct.price));
            System.out.println("==============");

        }
    }
    // Tuple select 시 선택해서 가져오는 경우 서로 타입이 다를 경우 tuple에 담아올 수 있음
    // index로 접근이 가능


    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void queryDslTest4(){           //  Bean객체 스프링 컨테이너에서 가져다가 사용
        QProduct qProduct = QProduct.product;

        List<String> productList =jpaQueryFactory
                .select(qProduct.name)
                .from(qProduct)
                .where(qProduct.name.eq("펜"))
                .orderBy(qProduct.price.asc())
                .fetch();

        for (String product : productList){
            System.out.println("-----------");
            System.out.println("product Name4 = " + product);
            System.out.println("-----------");
        }
    }
}