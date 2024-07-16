package show.schedulemanagement.constant;

import static show.schedulemanagement.constant.AppConstant.*;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AppConstantValidator {
    @PostConstruct
    public void validateAppConstant(){
        if(WORK_START_TIME.isAfter(WORK_END_TIME)){
            throw new RuntimeException("AppConstant Setting Error, WORK_START_TIME must be before WORK_END_TIME");
        }
        if(WORK_END_TIME.equals(WORK_START_TIME)){
            throw new RuntimeException("AppConstant Setting Error, WORK_END_TIME and WORK_START_TIME cannot equal");
        }
    }
}
