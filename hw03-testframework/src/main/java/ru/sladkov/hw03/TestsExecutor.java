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

        //Get @Before, @Test and @After methods
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

        //Execute tests
        System.out.println(clazz.getName() + ":");
        for (Method testMethod : testMethods) {
            System.out.println("Executing " + testMethod.getName() + ":");
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
        //Create new class instance
        Object classInstance;
        try {
            classInstance = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Can't instantiate new test class object" + e.getCause().toString());
            return false;
        }

        //Execute @Before methods
        try {
            executeMethods(classInstance, beforeMethods);
        } catch (Exception e) {
            System.out.println("Can't execute @Before methods:" + e.getCause().toString());
            executeAftersWithoutThrowingException(classInstance, afterMethods);
            return false;
        }

        //Execute current @Test method
        try {
            executeMethod(classInstance, testMethod);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            targetException.printStackTrace(pw);
            System.out.println("Exception: " + sw);
            executeAftersWithoutThrowingException(classInstance, afterMethods);
            return false;
        } catch (Exception e) {
            System.out.println("Error in initializing test: " + e.getCause().toString());
            executeAftersWithoutThrowingException(classInstance, afterMethods);
            return false;
        }

        //Execute @After methods
        try {
            executeMethods(classInstance, afterMethods);
        } catch (Exception e) {
            System.out.println("Can't execute @After methods:" + e.getCause().toString());
            return false;
        }

        return true;
    }

    private static void executeMethods(Object classInstance, List<Method> methods) throws
            InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(classInstance);
        }
    }

    private static void executeMethod(Object classInstance, Method method) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(classInstance);
    }

    //If @Before or @Test threw exception - execute @After anyway
    private static void executeAftersWithoutThrowingException(Object classInstance, List<Method> afterMethods) {
        try {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(classInstance);
            }
        } catch (Exception e) {
            System.out.println("Exception in executeAftersWithoutThrowingException() " + e.getCause().toString());
        }
    }
}
