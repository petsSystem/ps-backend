package br.com.petshop.appointment.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class teste {

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String data = "14-02-2024 17:56";

        LocalDateTime dateTime = LocalDateTime.parse(data, formatter);
        System.out.println("LocalDateTime: " + dateTime);

        String str = dateTime.format(formatter);
        System.out.println("String: " + str);

        System.out.println(dateTime.getDayOfMonth());
        System.out.println(dateTime.getMonth().getValue());
        System.out.println(dateTime.getHour());
        System.out.println(dateTime.getMinute());

        formatter = DateTimeFormatter.ofPattern("HH:mm");

        data = "17:56";

        LocalTime time = LocalTime.parse(data, formatter);
        System.out.println("LocalTime: " + time);

        String strTime = time.format(formatter);
        System.out.println("str Time: " + strTime);

    }
    /**
     * 2024-02-14T17:58:28.184306
     * LocalDateTime formatado: 14/02/2024 05:58
     */
}
