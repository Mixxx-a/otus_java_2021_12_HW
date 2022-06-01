package ru.sladkov.hw05;

import ru.sladkov.hw05.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class TestLoggingLoader {

    private TestLoggingLoader() {
    }

    static TestLoggingInterface createClass(TestLoggingInterface testLoggingInterface) throws NoSuchMethodException {
        InvocationHandler invocationHandler = new MyInvocationHandler(testLoggingInterface);
        return (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingLoader.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, invocationHandler);
    }

    static class MyInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface testLoggingInterface;
        private final Set<Method> loggedMethods = new HashSet<>();

        MyInvocationHandler(TestLoggingInterface testLoggingInterface) throws NoSuchMethodException {
            this.testLoggingInterface = testLoggingInterface;
            Method[] methods = testLoggingInterface.getClass().getDeclaredMethods();

            Class<?>[] interfaces = testLoggingInterface.getClass().getInterfaces();
            Optional<Class<?>> optionalTestLoggingInterfaze = Arrays.stream(interfaces)
                    .filter(interfaze -> interfaze.getName().equals(TestLoggingInterface.class.getName()))
                    .findFirst();
            if (optionalTestLoggingInterfaze.isPresent()) {
                Class<?> testLoggingInterfaze = optionalTestLoggingInterfaze.get();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Log.class)) {
                        Method loggedMethod = testLoggingInterfaze.getMethod(method.getName(), method.getParameterTypes());
                        loggedMethods.add(loggedMethod);
                    }
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.contains(method)) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }
            return method.invoke(testLoggingInterface, args);
        }
    }
}
