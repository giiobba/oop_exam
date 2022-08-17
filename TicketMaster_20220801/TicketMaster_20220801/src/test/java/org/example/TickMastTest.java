package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TickMastTest {

    TickMastService moService = new TickMastService();

    @BeforeEach
    void setUp() {
        System.out.printf("#### TickMastTest setUp \n");
    }

    @AfterEach
    void tearDown() {
        System.out.printf("#### TickMastTest tearDown \n");
    }

    @Test
    @Order(1)
    void service1() {
        HashMap<String, String> param = new HashMap<>();
        param.put("city", "");
        System.out.printf("#### TickMastTest service1 %s \n", param.toString());
        try {
            moService.searchData(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service1 -- exception %s", e.toString());
        }
    }

    @Test
    @Order(2)
    void service2() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "ua");
        System.out.printf("#### TickMastTest service2 %s \n", param.toString());
        try {
            moService.searchData(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service2 -- exception %s", e.toString());
        }
    }

    @Test
    @Order(3)
    void service3() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "it");
        System.out.printf("#### TickMastTest service3 %s \n", param.toString());
        try {
            moService.searchData(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service3 -- exception %s ", e.toString());
        }
    }

    @Test
    @Order(4)
    void service4() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "es");
        param.put("city", "");
        System.out.printf("#### TickMastTest service4 %s \n", param.toString());
        try {
            moService.searchData(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service4 -- exception %s ", e.toString());
        }
    }

    @Test
    @Order(5)
    void service5() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "be");
        param.put("city", "");
        System.out.printf("#### TickMastTest service5 %s \n", param.toString());
        try {
            moService.searchStat(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service5 -- exception %s ", e.toString());
        }
    }

    @Test
    @Order(6)
    void service6() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "at");
        param.put("weekday", "");
        System.out.printf("#### TickMastTest service6 %s \n", param.toString());
        try {
            moService.searchStat(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service6 -- exception %s ", e.toString());
        }
    }

    @Test
    @Order(7)
    void service7() {
        HashMap<String, String> param = new HashMap<>();
        param.put("countrycode", "de");
        param.put("city", "");
        param.put("weekday", "");
        System.out.printf("#### TickMastTest service7 %s \n", param.toString());
        try {
            moService.searchStat(param);
        } catch (Exception e) {
            System.out.printf("@@@@ TickMastTest service7 -- exception %s ", e.toString());
        }
    }

}
