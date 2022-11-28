package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(String source) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        return StringUtils.hasLength(source) ? LocalTime.parse(source, formatter) : null;
    }
}
