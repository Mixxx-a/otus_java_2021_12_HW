package ru.sladkov.hw05;

import ru.sladkov.hw05.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestLoggingLoader {

    private TestLoggingLoader() {}

    static TestLoggingInterface createClass(TestLoggingInterface testLoggingImpl) {
        InvocationHandler invocationHandler = new MyInvocationHandler(testLoggingImpl);
        return (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingLoader.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, invocationHandler);
    }

    static class MyInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface testLogging;
        private final List<Method> loggedMethods = new ArrayList<>();

        MyInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;
            Method[] methods = testLogging.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Annotation logAnnotation = method.getDeclaredAnnotation(Log.class);
                if (logAnnotation != null) {
                    this.loggedMethods.add(method);
                }
            }

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method implementedMethod = testLogging.getClass().getDeclaredMethod(method.getName(),
                    method.getParameterTypes());
            if (this.loggedMethods.contains(implementedMethod)) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }
    }
}
