package com.sparta.dtogram.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.lang.System.*;

@Aspect
@Component
public class ParameterAop {
    // com/sparta/dtogram/**/controller 패키지 하위 클래스를 전부 적용하겠다고 지점 설정
    @Pointcut("execution(* com.sparta.dtogram.*.controller..*.*(..))")
    private void cut() {}

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        // 실행되는 함수 이름 가져오고 출력
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        out.println(method.getName() + " 메서드 실행");

        // 메서드에 들어가는 매개변수 배열을 읽어옴
        Object[] args = joinPoint.getArgs();

        // 매개변수 배열의 종류와 값을 출력
        for(Object obj : args) {
            out.println("type : " + obj.getClass().getSimpleName());
            out.println("value : " + obj);
        }
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        out.println("return obj : " + obj);
    }
}
