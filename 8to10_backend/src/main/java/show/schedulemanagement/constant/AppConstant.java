package show.schedulemanagement.constant;

import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstant {
    public static final LocalTime WORK_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime WORK_END_TIME = LocalTime.of(22, 0);

    public static String imageFilePath;

    @Value("${app.file.image-path}")
    private String filePath;

    @PostConstruct
    void init(){
        AppConstant.imageFilePath = filePath;
    }
}
