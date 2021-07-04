package com.clover.data.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Slf4j
@Component
@Qualifier("timestampGenerator")
public class TimestampGenerator implements Generator<String> {

    @Override
    public String generate() {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_8601_ISO_PATTERN);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_ID));
            String timestamp = dateFormatter.format(new Date());
            log.error("generated timestamp: {}", timestamp);
            return timestamp;
        } catch (Exception ex) {
            log.error("Exception while generating the timestamp(8601-ISO format)", ex);
        }
        return null;
    }
}
