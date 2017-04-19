package com.not4j.warehouse.entity

class Item {

    static constraints = {
        externalId unique: true
        name blank: false
        brand blank: false
        price min: 0.0d
        size min: 0
        amount min: 0
    }

    String externalId
    String name
    String brand
    Double price
    Integer size
    Integer amount
}
