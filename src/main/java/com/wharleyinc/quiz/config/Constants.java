package com.wharleyinc.quiz.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable usernames
    public static final String USERNAME_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    private Constants() {
    }
}
