package com.eighttoten.global.constant;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AppConstantValidator {
    @PostConstruct
    public void validateAppConstant(){
        if(AppConstant.WORK_START_TIME.isAfter(AppConstant.WORK_END_TIME)){
            throw new RuntimeException("WORK_START_TIME 은 WORK_END_TIME 보다 이전이어야 합니다.");
        }
        if(AppConstant.WORK_END_TIME.equals(AppConstant.WORK_START_TIME)){
            throw new RuntimeException("WORK_END_TIME 은 WORK_START_TIME 보다 이후여야 합니다.");
        }
    }
}