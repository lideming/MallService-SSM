package site.linkway.utils;

import java.util.Random;

public class RandomString {
    public static String CreateVerificationCode(){
        String verificationCode="";
        String  allCh="1234567890zxcvbnmasdfghjklqwertyuiop";
        Random ran = new Random();
        for(int i=0;i<4;i++){
            int index = ran.nextInt(allCh.length());
            char ch = allCh.charAt(index);
            verificationCode+=ch;
        }
        return verificationCode;
    }
    private static final String cookieAllCh="ZXCVBNMASDFGHJKLQWERTYUIOP1234567890zxcvbnmasdfghjklqwertyuiop";
    public static String cookieStringCreator(){
        String cookie="";
        Random ran = new Random();
        for(int i=0;i<125;i++){
            int index = ran.nextInt(cookieAllCh.length());
            char ch = cookieAllCh.charAt(index);
            cookie+=ch;
        }
        return cookie;
    }
}
