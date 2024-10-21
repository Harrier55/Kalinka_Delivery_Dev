package com.onecab.core.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import kotlin.math.abs

object DateFormatterMyApplication {

    private const val PATTERN_INPUT = "yyyy-MM-dd'T'HH:mm:ss"
    private const val PATTERN_1 = "dd.MM.yyyy"
    private const val PATTERN_2 = "dd.MM.yyyy 'в' HH:mm"
    private const val PATTERN_3 = "yyyyMMdd"
    private const val PATTERN_4 = "yyyyMMddHHmmss" // 20240912070000

    fun countdownTimer(dateFromDb: String, increaseMin: Long): String {
        /*
        к значению времени из базы данных dateFromDb прибавляем требуемое количетсво
        минут - increaseMin и вычисляем значение разницы
        если время еще осталось - вернется оставшееся время, если не осталось
        вернется ноль
         */

        val currentDate =  LocalDateTime.now()
        val inputDate = LocalDateTime.parse(
            dateFromDb,
            DateTimeFormatter.ofPattern(PATTERN_INPUT)
        )
        // Добавляем превышение минут к дате
        val newDate = inputDate.plus(increaseMin, ChronoUnit.MINUTES)
        // определяем разницу между датами
        val duration = Duration.between(newDate, currentDate)

        val min = abs(duration.toMinutes() % 60)
        val sec = abs(duration.seconds % 60).toString().padStart(2,'0')
        val difference = duration.toMillis()

        return if (difference < 0) "$min : $sec" else ""
    }

    //Преобразует дату из формата "yyyy-MM-dd'T'HH:mm:ss" в формат 20.01.2024
    fun String?.date_formatter_1(): String {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern(PATTERN_INPUT)
            val outputFormatter = DateTimeFormatter.ofPattern(PATTERN_1)
            val date = LocalDate.parse(this, inputFormatter)
            return date.format(outputFormatter)
        } catch (e: Exception) {
            Log.e("DateTransformation", "formatter_1: $e")
            return ""
        }
    }

    //вПреобразует дату из формата "yyyy-MM-dd'T'HH:mm:ss" в формат 20.01.2024 в 10:51
    fun String?.date_formatter_2(): String {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern(PATTERN_INPUT)
            val outputFormatter = DateTimeFormatter.ofPattern(PATTERN_2)
            val dateTime = LocalDateTime.parse(this, inputFormatter)
            return dateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.e("DateTransformation", "formatter_2: $e")
            return ""
        }
    }

    //Преобразует дату из формата "yyyy-MM-dd'T'HH:mm:ss" в формат yyyyMMdd
    fun String?.date_formatter_3(): String {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern(PATTERN_INPUT)
            val outputFormatter = DateTimeFormatter.ofPattern(PATTERN_3)
            val dateTime = LocalDateTime.parse(this, inputFormatter)
            return dateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.e("DateTransformation", "formatter_3: $e")
            return ""
        }
    }

    // Преобразует Long из диалога в дату вида 12.11.2021
    @SuppressLint("SimpleDateFormat")
    fun Long.date_formatter_lond_to_string_4(): String {
        try {
            val dateFormat = SimpleDateFormat(PATTERN_1)
            val date = Date(this)
            return dateFormat.format(date)

        } catch (e: Exception) {
            Log.e("DateTransformation", "date_formatter_lond_to_string_4: $e")
            return ""
        }
    }

    // Преобразует Long из диалога в дату вида "yyyyMMdd"
    @SuppressLint("SimpleDateFormat")
    fun Long.date_formatter_lond_to_string_5(): String {
        try {
            val dateFormat = SimpleDateFormat(PATTERN_3)
            val date = Date(this)
            return dateFormat.format(date)

        } catch (e: Exception) {
            Log.e("DateTransformation", "date_formatter_lond_to_string_4: $e")
            return ""
        }
    }

    // Преобразует дату из формата "yyyy-MM-dd'T'HH:mm:ss" в формат 20240912070000
    @SuppressLint("SimpleDateFormat")
    fun String.date_formatter_6(): String {
        try {
            val inputFormatter = DateTimeFormatter.ofPattern(PATTERN_INPUT)
            val outputFormatter = DateTimeFormatter.ofPattern(PATTERN_4)
            val dateTime = LocalDateTime.parse(this, inputFormatter)
            return dateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.e("DateTransformation", "date_formatter_6: $e")
            return ""
        }
    }

    // Преобразует дату из формата Long в формат "yyyy-MM-dd'T'HH:mm:ss"
    @SuppressLint("SimpleDateFormat")
    fun Long.date_formatter_lond_to_string_7(): String {
        try {
            val dateFormat = SimpleDateFormat(PATTERN_INPUT)
            val date = Date(this)
            return dateFormat.format(date)
        } catch (e: Exception) {
            Log.e("DateTransformation", "date_formatter_7: $e")
            return ""
        }
    }

    // Возвращает текущую дату в формате "20.01.2024"
    fun getCurrentDateForCalendarButton(): String {
        return getCurrentLocalDateTime().date_formatter_1()
    }

    // Вернет текущую дату в формате yyyyMMdd.  Используется для запросов на сервер
    fun getCurrentDateForRequest(): String {
        return getCurrentLocalDateTime().date_formatter_3()
    }

    // Предоставляе текущую дату в формате "yyyy-MM-dd'T'HH:mm:ss"
    fun getCurrentLocalDateTime(): String {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(PATTERN_INPUT)
        return currentDate.format(formatter)
    }
}