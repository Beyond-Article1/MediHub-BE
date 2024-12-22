package mediHub_be.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 년도, 월
 * 년도, 월, 일
 * 년도, 월, 일, 시, 분
 * 년도, 월, 일, 시, 분, 초
 * 월, 일, 시, 분
 * 시, 분
 */
// 시간 포매팅
@Slf4j
public class TimeFormatUtil {

    // 년도, 월
    public static final DateTimeFormatter yMFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    // 년도, 월, 일
    public static final DateTimeFormatter yMDFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 년도, 월, 일, 시, 분
    public static final DateTimeFormatter yMDHMFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // 년도, 월, 일, 시, 분, 초
    public static final DateTimeFormatter yMDHMSFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // 월, 일, 시, 분
    public static final DateTimeFormatter mDHMFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    // 시, 분
    public static final DateTimeFormatter hMFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // === 시간 메서드 === //
    // 년도, 월 만 가져오기
    public static LocalDateTime yearAndMonth(LocalDateTime date){
        date.format(yMFormatter);
        return date;
    }

    // 년도, 월, 일
    public static LocalDateTime yearAndMonthDay(LocalDateTime date){
        date.format(yMDFormatter);
        return date;
    }

    // 년도, 월, 일, 시, 분
    public static LocalDateTime yearAndMonthDayHM(LocalDateTime date){
        date.format(yMDHMFormatter);
        return date;
    }

    // 년도, 월, 일, 시, 분, 초
    public static LocalDateTime yearAndMonthDayHMS(LocalDateTime date){
        date.format(yMDHMSFormatter);
        return date;
    }

    // 월, 일, 시, 분
    public static LocalDateTime monthAndDHM(LocalDateTime date){
        date.format(mDHMFormatter);
        return date;
    }

    // 시, 분
    public static LocalDateTime hourAndM(LocalDateTime date){
        date.format(hMFormatter);
        return date;
    }
}
