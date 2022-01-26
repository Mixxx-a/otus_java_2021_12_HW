package ru.sladkov.hw03;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestsExecutor {

    public static void executeTests(Class<?> clazz) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        int passedTests = 0;
        int failedTests = 0;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                switch (annotation.toString()) {
                    case "@ru.sladkov.hw03.annotations.Test()" -> testMethods.add(method);
                    case "@ru.sladkov.hw03.annotations.Before()" -> beforeMethods.add(method);
                    case "@ru.sladkov.hw03.annotations.After()" -> afterMethods.add(method);
                }
            }
        }

        System.out.println(clazz.getName() + ":");
        for (Method testMethod : testMethods) {
            boolean isTestPassed = executeTest(clazz, testMethod, beforeMethods, afterMethods);
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

    private static boolean executeTest(Class<?> clazz, Method testMethod, List<Method> beforeMethods,
                                       List<Method> afterMethods) {
        Object classInstance;
        try {
            classInstance = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Can't instantiate new test class object" + e.toString());
            return false;
        }

        try {
            executeMethods(classInstance, beforeMethods);
        } catch (Exception e) {
            System.out.println("Can't execute @Before methods:" + e.toString());
            executeSafeAfters(classInstance, afterMethods);
            return false;
        }

        try {
            executeMethod(classInstance, testMethod);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            System.out.println("Error in initializing test: " + e.toString());
            executeSafeAfters(classInstance, afterMethods);
            return false;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            targetException.printStackTrace(pw);
            System.out.println("Exception: " + sw.toString());
            executeSafeAfters(classInstance, afterMethods);
            return false;
        }

        try {
            executeMethods(classInstance, afterMethods);
        } catch (Exception e) {
            System.out.println("Can't execute @After methods:" + e.toString());
            return false;
        }

        return true;
    }

    private static void executeMethods(Object classInstance, List<Method> methods) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(classInstance);
        }
    }

    private static void executeMethod(Object classInstance, Method method) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        method.invoke(classInstance);
    }

    private static void executeSafeAfters(Object classInstance, List<Method> afterMethods) {
        try {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(classInstance);
            }
        } catch (Exception e) {
            System.out.println("Exception in callAfters()");
        }
    }
}
