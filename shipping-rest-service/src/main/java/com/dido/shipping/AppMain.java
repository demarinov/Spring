package com.dido.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*   given the shipping company offices :70115,70508,70811 ->
     find the zip code of the office within min distance
     source locations to test: 70785; 70533; 39059 or 39859
 */
@SpringBootApplication
public class AppMain {

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }
}
