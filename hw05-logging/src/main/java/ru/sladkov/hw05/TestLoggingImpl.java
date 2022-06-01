package ru.sladkov.hw05;

import ru.sladkov.hw05.annotation.Log;

public class TestLoggingImpl implements Cloneable, TestLoggingInterface { //Test with other interface(s)
    @Override
    @Log
    @Deprecated // Test other annotations
    public void calculation(int param) {
    }

    @Override
    public void calculation(int param1, int param2) {
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
    }

    @Override
    public TestLoggingImpl clone() {
        try {
            return (TestLoggingImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
