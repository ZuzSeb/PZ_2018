package com.zuzseb.learning.utils;

public class StringUtils {

    /**
     * Przycinanie ciągu znaków do podanej długość
     * @param text tekst do przycięcia
     * @param max liczba znaków do jakiej ma zostać przycięty tekst
     * @return
     */
    public static String cut(String text, int max){
        return text.substring(0,max);
    }
}
