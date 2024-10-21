package com.onecab.domain.enums

enum class QrCodeStatus {
    ACWP,   // завершена успешно
    RCVD,   // не завершена
    RJCT,   // завершена неуспешно
    NTST    // не начата
}