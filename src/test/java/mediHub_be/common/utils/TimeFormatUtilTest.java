//package mediHub_be.common.utils;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("[TimeFormatUtil 테스트]")
//class TimeFormatUtilTest {
//
//    @Test
//    @DisplayName("년, 월 format 테스트")
//    void testYearAndMonth() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30);
//        String expected = "2024-06";
//
//        // when
//        String result = TimeFormatUtil.yearAndMonth(date).format(TimeFormatUtil.yMFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("년, 월, 일 format 테스트")
//    void testYearAndMonthDay() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30);
//        String expected = "2024-06-01";
//
//        // when
//        String result = TimeFormatUtil.yearAndMonthDay(date).format(TimeFormatUtil.yMDFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("년, 월, 일, 시, 분 format 테스트")
//    void testYearAndMonthDayHM() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30);
//        String expected = "2024-06-01 12:30";
//
//        // when
//        String result = TimeFormatUtil.yearAndMonthDayHM(date).format(TimeFormatUtil.yMDHMFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("년, 월, 일, 시, 분, 초 format 테스트")
//    void testYearAndMonthDayHMS() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30, 45);
//        String expected = "2024-06-01 12:30:45";
//
//        // when
//        String result = TimeFormatUtil.yearAndMonthDayHMS(date).format(TimeFormatUtil.yMDHMSFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("월, 일, 시, 분, 초 format 테스트")
//    void testMonthAndDayHM() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30, 45);
//        String expected = "06-01 12:30";
//
//        // when
//        String result = TimeFormatUtil.monthAndDHM(date).format(TimeFormatUtil.mDHMFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("시, 분 format 테스트")
//    void testHourAndMinute() {
//        // given
//        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 30);
//        String expected = "12:30";
//
//        // when
//        String result = TimeFormatUtil.hourAndM(date).format(TimeFormatUtil.hMFormatter);
//
//        // then
//        assertThat(result).isEqualTo(expected);
//    }
//}
