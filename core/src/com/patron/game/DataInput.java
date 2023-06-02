package com.patron.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class DataInput {

    public static Long getLong() throws IOException{
        String s = getString();
        Long value = Long.valueOf(s);
        return value;
    }

    public static char getChar() throws IOException{
        String s = getString();
        return s.charAt(0);
    }

    public static Integer getInt(){
        String s = "";
        Integer value = -1;
        try {
            s = getString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            value = Integer.valueOf(s);
        } catch (NumberFormatException e){
            System.out.println("Please try again");
            return -404404;
        }
        return value;

    }

    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }

}