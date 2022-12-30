package org.novinomad.picasso.aop.aspects.logging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.services.auth.UserService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggableAspect {
    private final UserService userService;

    @Around("@annotation(org.novinomad.picasso.aop.annotations.logging.Loggable) || @within(org.novinomad.picasso.aop.annotations.logging.Loggable)")
    public Object methodLevelAnnotationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> methodClass = joinPoint.getTarget().getClass();

        if(method.getAnnotation(LogIgnore.class) == null) {

            Loggable loggable = method.getAnnotation(Loggable.class) != null ?
                    method.getAnnotation(Loggable.class) :
                    methodClass.getAnnotation(Loggable.class);

            LogModel logModel = new LogModel();
            logModel.setMethodClassName(getClassCanonicalShortName(methodClass));
            logModel.setMethodName(method.getName());
            logModel.setParameterMap(buildParametersMap(method.getParameters(), joinPoint.getArgs()));
            logModel.setIgnoreReturnValue(loggable.ignoreReturnValue());
            logModel.setMethodReturnType(method.getReturnType());

            final StopWatch stopWatch = new StopWatch();
            try {
                stopWatch.start();

                Object retVal = joinPoint.proceed();

                stopWatch.stop();

                logModel.setMethodReturnValue(retVal);

                return retVal;

            } catch (Throwable e) {
                stopWatch.stop();

                logModel.setThrowable(e);

                throw new RuntimeException(e);

            } finally {
                logModel.setExecutionTimeMillis(stopWatch.getTotalTimeMillis());

                String logMessage = logModel.buildLogMessage();

                if(logModel.getThrowable() != null) {
                    log.error(logMessage, logModel.getThrowable());
                } else {
                    switch (loggable.level()) {
                        case WARN -> log.warn(logMessage);
                        case DEBUG -> log.debug(logMessage);
                        case TRACE -> log.trace(logMessage);
                        case INFO -> log.info(logMessage);
                    }
                }
            }
        } else {
            return joinPoint.proceed();
        }
    }

    private static Map<Parameter, Object> buildParametersMap(Parameter[] parameters, Object[] parameterValues) {
        Map<Parameter, Object> parametersMap = new LinkedHashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            parametersMap.put(parameters[i], parameterValues[i]);
        }
        return parametersMap;
    }

    private static String getClassCanonicalShortName(Class<?> clazz) {
        String canonicalName = clazz.getCanonicalName();

        String[] split = canonicalName.split("\\.");

        if(split.length < 2) {
            return canonicalName;
        } else {
            StringBuilder canonicalShortNameBuilder = new StringBuilder();

            for (int i = 0; i < split.length; i++) {
                String n = split[i];

                if(i < split.length - 1) {
                    canonicalShortNameBuilder.append(n.charAt(0)).append(".");
                } else {
                    canonicalShortNameBuilder.append(n);
                    break;
                }
            }
            return canonicalShortNameBuilder.toString();
        }
    }

    @Setter
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class LogModel {

        boolean ignoreReturnValue;
        String methodClassName;
        String methodName;
        Map<Parameter, Object> parameterMap;

        Class<?> methodReturnType;
        Object methodReturnValue;
        Long executionTimeMillis;
        Throwable throwable;


        public String buildLogMessage() {
            StringBuilder logMessageBuilder = new StringBuilder();

            if(executionTimeMillis != null) {
                logMessageBuilder.append(executionTimeMillis).append(" ms. ");
            }
            logMessageBuilder.append("[").append(userService.getCurrentUserName()).append("] ")
                    .append(methodClassName).append(".").append(methodName).append("(");

            if(!CollectionUtils.isEmpty(parameterMap)) {
                AtomicInteger i = new AtomicInteger(1);

                parameterMap.forEach((parameter, parameterValue) -> {
                    boolean notIgnore = Arrays.stream(parameter.getAnnotations())
                            .noneMatch(annotation -> LogIgnore.class.equals(annotation.annotationType()));

                    if(notIgnore) {
                        logMessageBuilder.append(parameter.getName()).append(" = ").append(parameterValue);

                        if(i.get() != parameterMap.size()) {
                            logMessageBuilder.append(", ");
                        }
                    }
                    i.getAndIncrement();
                });
            }
            logMessageBuilder.append(")");

            logMessageBuilder.append(" : ").append(methodReturnType.getSimpleName());

            if(!Void.TYPE.equals(methodReturnType) && !ignoreReturnValue && throwable == null){
                logMessageBuilder.append(" => ").append(methodReturnValue);
            }
            return logMessageBuilder.toString();
        }
    }
}
