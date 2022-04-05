package ru.sladkov.hw03;

import ru.sladkov.hw03.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestsExecutor {

    public static void executeTests(Class<?> clazz) {
        int passedTests = 0;
        int failedTests = 0;

        //Get @Before, @Test and @After methods
        List<Method> beforeMethods = getAnnotatedMethods(clazz, Before.class);
        List<Method> afterMethods = getAnnotatedMethods(clazz, After.class);

        //Execute tests
        System.out.println("\n" + clazz.getName() + ":");
        for (Method testMethod : getAnnotatedMethods(clazz, Test.class)) {
            System.out.println("Executing " + testMethod.getName() + ":");
            boolean isTestPassed = executeTest(clazz, testMethod, beforeMethods,
                    afterMethods);
            if (isTestPassed) {
                passedTests++;
                System.out.println(testMethod.getName() + " -> passed");
            } else {
                failedTests++;
                System.out.println(testMethod.getName() + " -> FAILED! See details above");
            }
            System.out.println("------------------------------------------------------------------");
        }

        System.out.println("Out of " + (passedTests + failedTests) + " tests, " + passedTests + " passed and " +
                failedTests + " failed");
    }

    private static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    private static boolean executeTest(Class<?> clazz, Method testMethod, List<Method> beforeMethods,
                                       List<Method> afterMethods) {
        Object classInstance = createObjectOrNull(clazz);
        if (classInstance != null) {
            try {
                executeMethods(classInstance, beforeMethods.toArray(new Method[0]));
                executeMethods(classInstance, testMethod);
            } catch (InvocationTargetException | IllegalAccessException e) {
                printCauseStackTrace(e);
                return false;
            } finally {
                try {
                    executeMethods(classInstance, afterMethods.toArray(new Method[0]));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    printCauseStackTrace(e);
                    return false;
                }
            }
            return true;
        } else return false;
    }

    private static Object createObjectOrNull(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException e) {
            System.out.println("Can't create Test class object, please fix your test class!");
            return null;
        } catch (InvocationTargetException | IllegalAccessException e) {
            printCauseStackTrace(e);
            return null;
        }
    }

    private static void executeMethods(Object classInstance, Method... methods) throws InvocationTargetException,
            IllegalAccessException {
        for (Method method : methods) {
            method.invoke(classInstance);
        }
    }

    private static void printStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        System.out.println("Exception: " + sw);
    }

    private static void printCauseStackTrace(Exception exception) {
        Throwable targetException = exception.getCause();
        printStackTrace(targetException);
    }
}
