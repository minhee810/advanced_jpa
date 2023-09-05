package com.springboot.advanced_jpa.data.dto;

public class ChangeProductNameDto {

    private Long number;
    private String name;

    public ChangeProductNameDto(){      // 주입 받지 않음. 따라서 생성자 오버로딩!

    }

    public ChangeProductNameDto(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
