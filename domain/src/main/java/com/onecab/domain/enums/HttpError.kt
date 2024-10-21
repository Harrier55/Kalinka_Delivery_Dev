package com.onecab.domain.enums


enum class HttpError(val code: Int, val message: String) {
    BadRequest(400, "Неправильный запрос"),
    Unauthorized(401, "Пользователь не неаутентифицирован"),
    Forbidden(403, "Пользователь неавторизован"),
    NotFound(404, "Приложение не может найти запрошенный ресурс"),
    ServerError(500, "На сервере произошла ошибка, в результате которой он не может успешно обработать запрос"),
    InternalServerError(501, "Метод запроса не поддерживается сервером и поэтому он не может быть обработан"),
    ServiceUnavailable(503, "Сервер не готов обработать запрос в данный момент");


}

fun getErrorName(code: Int): String? {
    return HttpError.entries.find { it.code == code }?.message
}