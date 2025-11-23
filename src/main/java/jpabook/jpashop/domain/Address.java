package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

//jpa의 내장타입이 뭐지?
@Embeddable
@Getter
public class Address {

    private  String city;
    private String street;
    private String zipcode;

}
