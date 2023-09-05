package com.springboot.advanced_jpa.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "name") // 멤버변수의 모든 값들을 문자열로 리턴할 수 있게 만들어줌
@Builder
@Table(name = "product")
public class Product extends BaseTimeEntity {      // product 파일 : 테이블 생성되는 클래스 @Entity
                            // Table : 테이블 명 명시

    // @NoArgsConstructor  : 기본 생성자 자동으로 만들어주는 어노테이션
    // @AllArgsConstructor : 생성자 대신에 자동으로 생성  모든 아규먼트를 외부에서 전달받는 생성자

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 (자동생성 방식 결정)
    private Long number;        // 식별값 --> Long의 개수만큼 Entity 들이 만들어 질 수 있음. 따라서Repository 식별 타입 <Long>
    // Big int == Long
    @Column(nullable = false)           // Null값 허용 안함. null 허용하겠다. 하면 @Column 제거 해도 됨. 변수명으로 자동 매핑 됨.
    private String name;
    // oracle 일 경우 varchar2
    @Column(nullable = false)           // Null값 허용 안함.
    private Integer price;

    @Column(nullable = false)           // Null값 허용 안함.
    private Integer stock;

   /* private LocalDateTime createdAt;       // Null값 허용 따라서 @없음.

    private LocalDateTime updatedAt;*/

}
