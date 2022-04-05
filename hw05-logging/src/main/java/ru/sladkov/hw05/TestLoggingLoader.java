package ru.sladkov.hw05;

import ru.sladkov.hw05.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class TestLoggingLoader {

    private TestLoggingLoader() {}

    static TestLoggingInterface createClass() {
        InvocationHandler invocationHandler = new MyInvocationHandler(new TestLoggingImpl());
        return (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingLoader.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, invocationHandler);
    }

    static class MyInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface testLogging;

        MyInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method implementedMethod = testLogging.getClass().getDeclaredMethod(method.getName(),
                    method.getParameterTypes());
            Annotation[] annotations = implementedMethod.getDeclaredAnnotations();
            if (Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType() == Log.class)) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }
    }
}
