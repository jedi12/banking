package ru.demo.banking.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;

@Aspect
@Component
public class LogErrorToConsoleAspect {

    @Around("execution(void ru.demo.banking.components.OperationCommand.execute(..))")
    public Object logError(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception e) {
            System.out.printf("%n%s%s%s%n", AnsiEscapeCode.RED, e.getMessage(), AnsiEscapeCode.RESET);
            return null;
        }
    }
}
