package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class appointment {

    public static void main(String[] args) {
        System.out.println("oi");

        Map<LocalDate, List<AppointmentResponse>> scheduleMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        for (int i = 0; i < 6; i++) {
            String month = String.valueOf(today.plusMonths(i).getMonthValue());
            String year = String.valueOf(today.plusMonths(i).getYear());
            String key = month.concat("-").concat(year);
            System.out.println(key);
        }


    }
}
