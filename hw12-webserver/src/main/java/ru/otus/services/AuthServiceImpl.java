package ru.otus.services;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    Map<String, String> users = new HashMap<>();

    public AuthServiceImpl() {
        users.put("user", "1234");
    }

    @Override
    public boolean authenticate(String login, String password) {
        String userPassword = users.get(login);
        if (userPassword != null) {
            return userPassword.equals(password);
        } else return false;
    }

}
