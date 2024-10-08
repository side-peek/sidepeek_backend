package sixgaezzang.sidepeek.projects.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    /**
     * {@code  localDate} 기준으로 지난 주 월요일을 찾는 메서드.
     * <p>
     * 기준 날짜의 요일이 [화, 수, 목, 금]이라면 {@link DateUtils#getEndDayOfLastWeek}를
     * 두 번 호출하여 지난 주 월요일 날짜를 얻는다.
     * @param localDate 기준이 되는 날짜
     * @return 지난 주 월요일 날짜를 {@code  LocalDate}으로 반환한다.
     */
    public static LocalDate getStartDayOfLastWeek(LocalDate localDate) {
        if (!localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            localDate = getNearestPastDayOfWeek(localDate, DayOfWeek.MONDAY);
        }

        return getNearestPastDayOfWeek(localDate, DayOfWeek.MONDAY);
    }

    /**
     * {@code  localDate} 기준으로 지난 주 일요일을 찾는 메서드.
     * @param localDate 기준이 되는 날짜
     * @return 지난 주 일요일 날짜를 {@code  LocalDate}으로 반환한다.
     */
    public static LocalDate getEndDayOfLastWeek(LocalDate localDate) {
        return getNearestPastDayOfWeek(localDate, DayOfWeek.SUNDAY);
    }

    public static LocalDate getLastDayOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 오늘이 일요일이면 그대로 반환
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return date;
        }

        int daysUntilSunday = DayOfWeek.SUNDAY.getValue() - dayOfWeek.getValue();
        return date.plusDays(daysUntilSunday); // 오늘로부터 일요일까지의 날짜를 더한다.
    }

    /**
     * {@code  localDate} 기준으로 {@code  localDate}에 해당하는 요일인
     * 가장 가까운 과거 날짜를 찾는 메서드
     * @param localDate 기준이 되는 날짜
     * @param dayOfWeek 찾고자 하는 가까운 과거 날짜의 요일
     * @return 가장 가까운 과거 요일 날짜를 {@code  LocalDate}로 반환한다.
     */
    public static LocalDate getNearestPastDayOfWeek(LocalDate localDate, DayOfWeek dayOfWeek) {
        return localDate.with(TemporalAdjusters.previous(dayOfWeek));
    }
}
