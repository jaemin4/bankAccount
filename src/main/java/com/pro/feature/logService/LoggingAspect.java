package com.pro.feature.logService;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String THREAD_ID_KEY = "currentThreadId";
    private static final String LAYER_KEY = "layer";
    private static final String INDENTATION_KEY = "indent";

    @Around("execution(* com.pro.feature.bankService.controller..*(..)) || " +
            "execution(* com.pro.feature.bankService.service..*(..)) || " +
            "execution(* com.pro.feature.bankService.repository..*(..))")
    public Object logExecutionLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String layer = getLayer(joinPoint.getSignature().getDeclaringTypeName());

        boolean isRepositoryLayer = "Repository".equals(layer);
        boolean isServiceLayer = "Service".equals(layer);
        boolean isError = false;
        boolean isControllerLayer = "Controller".equals(layer);


        try {
            if (MDC.get(TRACE_ID_KEY) == null) {
                MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString().substring(0, 8));
                MDC.put(THREAD_ID_KEY, String.valueOf(Thread.currentThread().threadId()));
                MDC.put(INDENTATION_KEY, "");
            }

            String indentation = MDC.get(INDENTATION_KEY);

            if (isRepositoryLayer) {
                MDC.put(INDENTATION_KEY, indentation);
            } else {
                MDC.put(INDENTATION_KEY, indentation + "    ");
            }

            if ("Controller".equals(layer)) {
                String requestUrl = getRequestUrl();
                log.info("{}[{}] {} - {}", indentation, MDC.get(TRACE_ID_KEY), layer, requestUrl);
            } else {
                log.info("{}[{}] {} - {}", indentation, MDC.get(TRACE_ID_KEY), layer, getMethodPath(joinPoint));
            }

            Object result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            isError = true;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            String indentation = MDC.get(INDENTATION_KEY);

            if (isServiceLayer) {
                MDC.put(INDENTATION_KEY, indentation.length() > 4 ? indentation.substring(0, indentation.length() - 4) : "");
                indentation = MDC.get(INDENTATION_KEY);
            }

            if (isControllerLayer) {
                MDC.put(INDENTATION_KEY, indentation.length() > 4 ? indentation.substring(0, indentation.length() - 4) : "");
                indentation = MDC.get(INDENTATION_KEY);
            }
            log.info("{}[{}] {} - ({}ms){}", indentation, MDC.get(TRACE_ID_KEY), layer, elapsedTime, isError ? " [ERROR]" : "");
            removeLastLayer();
        }
    }

    private String getRequestUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getMethod() + " " + request.getRequestURI();
        }
        return "Unknown URL";
    }

    private String getMethodPath(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    }

    private String getLayer(String className) {
        if (className.contains("controller")) return "Controller";
        if (className.contains("service")) return "Service";
        if (className.contains("repository")) return "Repository";
        return "Unknown";
    }

    private void removeLastLayer() {
        List<String> layers = getLayers();
        if (!layers.isEmpty()) {
            layers.remove(layers.size() - 1);
            MDC.put(LAYER_KEY, String.join(" -> ", layers));
        }
    }

    private List<String> getLayers() {
        String currentLayers = MDC.get(LAYER_KEY);
        return currentLayers != null ? new ArrayList<>(List.of(currentLayers.split(" -> "))) : new ArrayList<>();
    }
}
