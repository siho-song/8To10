package com.eighttoten.constant;

import static com.eighttoten.constant.AppConstant.WORK_END_TIME;
import static com.eighttoten.constant.AppConstant.WORK_START_TIME;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AppConstantValidator {
    @PostConstruct
    public void validateAppConstant(){
        if(WORK_START_TIME.isAfter(WORK_END_TIME)){
            throw new RuntimeException("WORK_START_TIME 은 WORK_END_TIME 보다 이전이어야 합니다.");
        }
        if(WORK_END_TIME.equals(WORK_START_TIME)){
            throw new RuntimeException("WORK_END_TIME 은 WORK_START_TIME 보다 이후여야 합니다.");
        }
    }
}