package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    Main moMain = new Main();

    @BeforeEach
    void setUp() {
        System.out.printf("#### MainTest setUp \n");
    }

    @AfterEach
    void tearDown() {
        System.out.printf("#### MainTest tearDown \n");
    }

    @Test
    @Order(1)
    void main1() {
        String[] aArgs = {"GET", "uk,de,fr,it,es,pt", "/data"};
        System.out.printf("#### MainTest main1 %s \n", aArgs[2]);
        try {
            moMain.main(aArgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void main2() {
        String[] aArgs = {"GET", "uk,de,fr,it,es,pt", "/data?filter={\"city\":\"Hamburg\"}"};
        System.out.printf("#### MainTest main2 %s \n", aArgs[2]);
        try {
            moMain.main(aArgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void main3() {
        String[] aArgs = {"GET", "uk,de,fr,it,es,pt", "/data?filter={\"weekday\":\"Friday\"}"};
        System.out.printf("#### MainTest main3 %s \n", aArgs[2]);
        try {
            moMain.main(aArgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void main4() {
        String[] aArgs = {"GET", "uk,de,fr,it,es,pt", "/stats?field=\"city\""};
        System.out.printf("#### MainTest main4 %s \n", aArgs[2]);
        try {
            moMain.main(aArgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void main5() {
        String[] aArgs = {"GET", "uk,de,fr,it,es,pt", "/stats?field=\"weekday\""};
        System.out.printf("#### MainTest main5 %s \n", aArgs[2]);
        try {
            moMain.main(aArgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
