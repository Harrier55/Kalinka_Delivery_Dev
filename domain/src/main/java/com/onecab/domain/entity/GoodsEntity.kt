package com.onecab.domain.entity

data class GoodsEntity(
    val commodityId:         String = "",
    val commodityName:       String = "name",
    val unitName:            String = "",
    val quantity:            Double = 0.0,  // оригинальное значение
    val price:               Double = 0.0,
    val amount:              Double = 0.0,
    val vatAmount:           Double = 0.0,

    val goodHasBeenModified:     Boolean = false,    // флаг того, что этот товар модифицирован
    val modifyDate:              String = "",        // дата модификации в формате yyyyMMdd
    val modifyQuantity:          Double = 0.0,       // измененное значение
)