package sixgaezzang.sidepeek.common.util.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class DateTimeProvider {

    /**
     * 현재 날짜를 반환하는 메서드
     *
     * @return {@code LocalDate}
     */
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 현재 날짜, 시간를 반환하는 메서드
     *
     * @return {@code LocalDateTime}
     */
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
