package com.spring.boot.example.core.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.core.convert.converter.Converter;

import java.time.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSR310DateConverters {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

        @Override
        public Date convert(ZonedDateTime source) {
            return Date.from(source.toInstant());
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

        @Override
        public ZonedDateTime convert(Date source) {
            return ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }

    }

}
