package me.hjhng125.taxiallocationapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingMemberAspect {

    @Before("execution(* me.hjhng125.taxiallocationapi.taxi.request.*Controller.*(..))")
    public void logRequest(JoinPoint jp) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("<<< Method : {} ", jp.getSignature().getName());
        for (Object object : jp.getArgs()) {
            log.info("<<< Parameter : {} ", object);
        }
    }
}
