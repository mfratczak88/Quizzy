package com.example.mf.quizzy.Config;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    public static final String URL_LOGIN = "https://users-service-quizzy.herokuapp.com/login/";
    public static final String URL_REGISTER = "https://users-service-quizzy.herokuapp.com/register/";
    public static final Map<String, String> CATEGORIES = new HashMap<>();
    static  {
        CATEGORIES.put("Books", "10");
        CATEGORIES.put("Film", "11");
        CATEGORIES.put("Music", "12");
        CATEGORIES.put("TV", "14");
        CATEGORIES.put("Science", "17");
        CATEGORIES.put("History", "23");
        CATEGORIES.put("Politics", "24");
        CATEGORIES.put("Animals", "27");
        CATEGORIES.put("Geo", "22");
    }
}
