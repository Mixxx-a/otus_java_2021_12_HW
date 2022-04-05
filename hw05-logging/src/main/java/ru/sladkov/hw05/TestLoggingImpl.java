package ru.sladkov.hw05;

import ru.sladkov.hw05.annotation.Log;

public class TestLoggingImpl implements TestLoggingInterface {
    @Override
    @Log
    @Deprecated // Test other annotations
    public void calculation(int param) {}

    @Override
    public void calculation(int param1, int param2) {}

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {}
}
