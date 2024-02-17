package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppointmentBusinessUtils {

    public ScheduleFilterRequest getScheduleFilter(AppointmentFilterRequest filter) {
        return ScheduleFilterRequest.builder()
                .companyId(filter.getCompanyId())
                .productId(filter.getProductId())
                .userId(filter.getUserId())
                .build();
    }

    public Map<String, List<AppointmentResponse>> createMap(ProductResponse product, List<ScheduleResponse> schedules) {

        Integer intervalMinutes = product.getIntervalMinutes();

//        for(ScheduleResponse sch : schedules) {
//
//            LocalTime initialTime = LocalTime.parse(sch.getDays().ge)
//
//        }
        Map<String, List<AppointmentResponse>> scheduleMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        for (int i = 0; i < 6; i++) {
            String key = getKey(today.plusMonths(i));
            scheduleMap.put(key, new ArrayList<>());
            System.out.println(key);
        }

        return scheduleMap;
    }

    public String getKey(LocalDate date) {
        String month = String.valueOf(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        return month.concat("-").concat(year);
    }
}
