package com.onecab.domain.entity

/***
 * Сущность для данных по корректировке накладной.
 * Просто преврахается  просто в json  с данными что скорректировали (до/после)
 */

data class AdjustmentsDataEntity(
    val orderId:         String = "",
    val commodityId:     String = "",
    val beforeQuantity:  Double = 0.0,
    val afterQuantity:   Double = 0.0,
    val modifyDate:      String = "",
)
