package br.com.petshop.appointment.service.appTeste;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class teste {

    public static void main(String[] args) {

        String date = "2024-02-21";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFormatted = LocalDate.parse(date, dateFormatter);

        System.out.println(dateFormatted);




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

        System.out.println("______________________________________");

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

        String date1 = "17/02/2024";
        date1 = date1.replaceAll("/", "-");
        String time1 = "18:23";

        System.out.println("Date from FRONT: " + date1);
        System.out.println("Time from FRONT: " + time1);

        LocalDate date3 = LocalDate.parse(date1, formatterDate);
        LocalTime time3 = LocalTime.parse(time1, formatterTime);

        System.out.println("Date DB " + date3);
        System.out.println("Time DB: " + time3);

        String date4 = date3.format(formatterDate);
        date4 = date4.replaceAll("-", "/");
        String time4 = time3.format(formatterTime);

        System.out.println("Date to FRONT: " + date4);
        System.out.println("Time to FRONT: " + time4);




    }
    /**
     * 2024-02-14T17:58:28.184306
     * LocalDateTime formatado: 14/02/2024 05:58
     */
}
