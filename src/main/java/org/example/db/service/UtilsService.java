package org.example.db.service;

public class UtilsService {
    public final static String SLASH = "/";
    public static String extractValueFromUrl(String s) {
        return s.substring(s.lastIndexOf(SLASH)+1);
    }
}
