package com.account.pro;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExceptionTests {

    /**
     * Exception: Checked Exception : try catch // throws // throw 를 해야하는 경우
     *  RuntimeException: unchecked Exception : try catch // throws // throw 가 필요없다.
     */
    @Test
    public void testException (){

        final String encoding = findFileEncoding("test.txt");
        System.out.println("encoding = [" + encoding + "]");
    }

    private String findFileEncoding(String fileName) {
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException e) { // Checked Exception
            throw new RuntimeException(e);  // Unchecked Exception 으로 변경해서 던져라.
        }
        return reader.getEncoding();
    }
}
