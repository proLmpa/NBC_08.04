package com.sparta.dtogram.user.aop;

import com.sparta.dtogram.user.dto.PasswordRequestDto;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.repository.PasswordHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PasswordHistoryAOP {

    private final PasswordHistoryRepository passwordHistoryRepository;

    public PasswordHistoryAOP(PasswordHistoryRepository passwordHistoryRepository) {
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    @Pointcut("execution(public * com.sparta.dtogram.user.controller.UserController.signup(com.sparta.dtogram.user.dto.SignupRequestDto))")
    private void signupPassword() {}

    @Pointcut("execution(public * com.sparta.dtogram.user.controller.ProfileController.editPassword(com.sparta.dtogram.user.dto.PasswordRequestDto))")
    private void editPassword() {}

//    @AfterReturning("signupPassword() || editPassword()")
//    public void savePasswordHistory(SignupRequestDto signupRequestDto, PasswordRequestDto passwordRequestDto) {
//        passwordHistoryRepository.save(signupRequestDto.getPassword())
//    }
}
