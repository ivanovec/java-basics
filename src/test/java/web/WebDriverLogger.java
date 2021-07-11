package web;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.UUID;

import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

@Aspect
public class WebDriverLogger {
    private static AllureLifecycle lifecycle;

    public static AllureLifecycle getLifecycle(){
        if(lifecycle == null) lifecycle = Allure.getLifecycle();
        return lifecycle;
    }

    @Pointcut("call(* org.openqa.selenium.*.findElement*(..))")
    public void find(){}
    @Pointcut("call(* org.openqa.selenium.support.ui.*.until(..))")
    public void until(){}

    @Around("find()")
    public Object findStep(ProceedingJoinPoint joinPoint) throws Throwable {
        return addStep("Trying to find element ", joinPoint);
    }

    @Around("until()")
    public Object untilStep(ProceedingJoinPoint joinPoint) throws Throwable {
        return addStep("Wait until element presence ", joinPoint);
    }

    private Object addStep(String name, ProceedingJoinPoint joinPoint) throws Throwable{
        String uuid = UUID.randomUUID().toString();
        getLifecycle().startStep(uuid, new StepResult().setName(name + joinPoint.getArgs()[0]));

        try{
            Object proceed = joinPoint.proceed();
            getLifecycle().updateStep(uuid, s -> s.setStatus(Status.PASSED));
            return proceed;
        } catch (Throwable throwable) {
            getLifecycle().updateStep(uuid, s -> s
                    .setStatus(getStatus(throwable).orElse(Status.BROKEN))
                    .setStatusDetails(getStatusDetails(throwable).orElse(null)));
            throw throwable;
        } finally {
            getLifecycle().stopStep(uuid);
        }
    }
}
