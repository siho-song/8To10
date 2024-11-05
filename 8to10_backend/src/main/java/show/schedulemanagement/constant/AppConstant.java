package show.schedulemanagement.constant;

import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstant {
    public static final LocalTime WORK_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime WORK_END_TIME = LocalTime.of(22, 0);
}
