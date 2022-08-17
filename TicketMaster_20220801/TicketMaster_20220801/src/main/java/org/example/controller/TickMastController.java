package org.example.controller;

import org.example.service.TickMastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.servlet.error.ErrorController;

import java.util.HashMap;

/**
 * Implements the TickMastController class, as a @RestController, that is an implementation
 * of the RestController interface from the SpringBoot package "org.springframework.web.bind.annotation".
 * <p>
 * Implements one method for each REST API call, namely @getResponseData for the /data request
 * and @getResponseStat for the /stat request.
 * <p>
 * Additionally, as a derived class from the SpringBoot class @ErrorController, it implements
 * a customized getResponseError method to handle error conditions
 * (e.g. when the REST request is not valid, or the parameters are not valid)
 * managing the /error case of the REST API call.
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.example
 */

@RestController
public class TickMastController implements ErrorController {

    @Autowired
    TickMastService moService;

    ResponseEntity moResponseEntity;

    @RequestMapping("/data")
    public ResponseEntity<Object> getResponseData(@RequestParam HashMap<String, String> dataArgs) {
        System.out.printf("**** TickMastController::getResponseData -- %s \n", dataArgs.toString());
        System.out.printf("getResponseData \n");
        System.out.printf("%s \n", dataArgs.toString());
        try {
            moService.checkDataArgs(dataArgs);
            moResponseEntity = new ResponseEntity<>(moService.searchData(dataArgs), HttpStatus.OK);
            System.out.printf("getResponseData OK \n");
            // System.out.printf(".... %s \n", moResponseEntity.toString());
            return moResponseEntity;
        }
        catch ( Exception e ) {
            moResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            System.out.printf("getResponseData EXC \n");
            System.out.printf(".... %s \n", moResponseEntity.toString());
            System.out.printf(".... %s \n", e.toString());
            return moResponseEntity;
        }
    }

    @RequestMapping("/stat")
    public ResponseEntity<Object> getResponseStat(@RequestParam HashMap<String, String> statArgs) {
        System.out.printf("**** TickMastController::getResponseStat -- %s \n", statArgs.toString());
        System.out.printf("getResponseStat \n");
        System.out.printf("%s \n", statArgs.toString());
        try {
            moService.checkStatArgs(statArgs);
            moResponseEntity = new ResponseEntity<>(moService.searchStat(statArgs), HttpStatus.OK);
            System.out.printf("getResponseStat OK \n");
            // System.out.printf(".... %s \n", moResponseEntity.toString());
            return moResponseEntity;
        }
        catch ( Exception e ) {
            moResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            System.out.printf("getResponseStat EXC \n");
            System.out.printf(".... %s \n", moResponseEntity.toString());
            System.out.printf(".... %s \n", e.toString());
            return moResponseEntity;
        }
    }

    @RequestMapping("/error")
    public ResponseEntity<Object> getResponseErro(@RequestParam HashMap<String, String> statArgs) {
        try {
            moResponseEntity = null;
            System.out.printf("getResponseErro ** \n");
            throw new Exception("wrong request -- valid requests are /data or /stat, followed by ?countrycode=xx and filters &city and/or &weekday");
            // System.out.printf(".... %s \n", moResponseEntity.toString());
        }
        catch ( Exception e ) {
            moResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            System.out.printf("getResponseErro EXC \n");
            System.out.printf(".... %s \n", moResponseEntity.toString());
            System.out.printf(".... %s \n", e.toString());
            return moResponseEntity;
        }
    }

}
