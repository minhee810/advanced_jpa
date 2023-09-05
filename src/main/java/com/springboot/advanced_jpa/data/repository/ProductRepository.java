package com.springboot.advanced_jpa.data.repository;

import com.springboot.advanced_jpa.data.entity.Product;
import com.springboot.advanced_jpa.data.repository.support.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

  /*

    List<T> findAll(); ==> select * from

    List<T> findAll(Sort sort); ==> select * from order by

    List<T> findAllById(Iterable<ID> ids); ==> select * from where

    <S extends T> saveAll(Iterable<S> entities);  ==> insert, update, delete 에서 사용됨.

    */

    // 쿼리메서드 생성
    // (리턴타입) + {주제 + 서술(속성) } 구조의 메서드

    // find...By
    Optional<Product> findByNumber(Long number);
    List<Product> findAllByName(String name);
    Product queryByNumber(Long number);

    // exists...By : 존재하는지 여부를 boolean값으로 표현
    // boolean existsByNumber(Long number);

    // count...By : 조회 쿼리 수행 후, 결과로 나온 레코드 개수 리턴
    long countByName(String name);

    // delete...By, remove...By : 삭제 void 혹은 삭제한 횟수 리턴
    void deleteByNumber(Long number);
    long removeByName(String name);

    //...First<number>..., ...Top<number>... : 쿼리를 통해 조회된 결과값의 개수를 제한
    List<Product> findFirst5ByName(String name);
    List<Product> findTop10ByName(String name);

    // ## 쿼리메서드의 조건자 키워드##

    // findByNumber 메서드와 동일하게 동작   :
     Product findByNumberIs(Long number);
     Product findByNumberEquals(Long number);

     // (is) Not : 다르다
    Product findByNumberIsNot(Long number);
    Product findByNumberNot(Long number);

    // (is) Null, (is) NotNull  : null값 여부
    List<Product> findByUpdatedAtNull();
    List<Product> findByUpdatedAtIsNull();
    List<Product> findByUpdatedAtNotNull();
    List<Product> findByUpdatedAtIsNotNull();

    // (is)True, (is)False  : 지정된 컬럼 값을 확인
   /* Product findByisActiveTrue();
    Product findByisActiveIsTrue();
    Product findByisActiveFalse();
    Product findByisActiveIsFalse();*/


    // And, Or (여러 조건을 묶을 때 사용)
    Product findByNumberAndName(Long number, String name);
    Product findByNumberOrName(Long number, String name);

    // GreaterThan, LessThan, BetWeen : 크다, 작다, 중간값
    List<Product> findByPriceIsGreaterThan(Long price);
    List<Product> findByPriceGreaterThan(Long price);
    List<Product> findByPriceGreaterThanEqual(Long price);
    List<Product> findByPriceIsLessThan(Long price);
    List<Product> findByPriceIsLessThanEqual(Long price);
    List<Product> findByPriceIsBetween(Long lowPrice, Long highPrice);
    List<Product> findByPriceBetween(Long lowPrice, Long highPrice);

    // StartingWith, EndingWith, Containing, Like : 시작하는, 끝나는, 포함하는
    // (컬럼값의 일치 여부를 확인, SQL의 % 의 키워드와 동일

    List<Product> findByNameLike(String name);
    List<Product> findByNameIsLike(String name);

    List<Product> findByNameContains(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameIsContaining(String name);

    // 문자열의 앞
    List<Product> findByNameStartsWith(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameIsStartingWith(String name);

    // 문자열의 뒤
    List<Product> findByNameEndsWith(String name);
    List<Product> findByNameEndingWith(String name);
    List<Product> findByNameIsEndingWith(String name);



    /* 정렬 처리하기 */

    // Asc : 오름차순, Desc : 내림차순 (예제 8.13)
    List<Product> findByNameOrderByNumberAsc(String name);
    List<Product> findByNameOrderByNumberDesc(String name);

    // 여러 정렬 기준 사용하기, And를 붙이지 않음 (예제 8.14)
    List<Product> findByNameOrderByPriceAscStockDesc(String name);

    // 매개변수를 활용한 정렬 방법 (예제 8.15)
    List<Product> findByName(String name, Sort sort);

    /* 페이징 처리하기 */
    // 예제 8.18
    // 리턴타입 : Page, 매개변수 : Pageable 타입의 객체를 정의.
    Page<Product> findByName(String name, Pageable pageable);

    /* @Query 어노테이션을 활용한 메소드 작성 */
    // @Query 어노테이션 : 직접 해당 데이터베이스에 특화된 SQL을 작성할 경우
    // 예제 8.21 쿼리 어노테이션 밑에는 반드시 메서드가 있어야 한다. 
    // From 뒤에 엔티티 타입 지정 및 별칭설정. (AS는 생략 가능)
    // ?1 : 첫번째 파라미터를 의미 
    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    List<Product> findByName(String name);

    // 예제 8.22
    // 파라미터가 여러개일 경우, 순서가 바뀌었을 때 오류가 발생하는 것을 방지하기 위해 
    // 주로 @Param을 사용 
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByNameParam(@Param("name") String name);

    // 예제 8.23
    // 특정 칼럼의 값만 추출도 가능.
    // 해당 필드의 타입이 모두 다르니까 object[] 로 받기
    // 일부 컬럼만 조회하고 싶을 경우
    @Query("SELECT p.name, p.price, p.stock FROM Product p WHERE p.name = :name")
    List<Object[]> findByNameParam2(@Param("name") String name);


}
