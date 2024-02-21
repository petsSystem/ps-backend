package br.com.petshop.appointment.service.appTeste;

import br.com.petshop.schedule.model.dto.Times;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.method.ParameterList;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class mapTest {

    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
        map.registerModule(new JavaTimeModule());
        map.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String bd = "[{\"times\": [\"{\\n    \\\"time\\\": \\\"09:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"10:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"10:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"11:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"11:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"12:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"12:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"13:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"13:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"14:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"14:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"15:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"15:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"16:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"16:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"17:00\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\", \"{\\n    \\\"time\\\": \\\"17:30\\\",\\n    \\\"scheduleIds\\\": [\\\"fdc234e8-7117-4921-861b-964680fc1a2a\\\"]\\n}\"], \"weekday\": \"WEDNESDAY\"}]";


        List<String> sms = map.readValue(bd, new TypeReference<List<String>>(){});

        List<Times> tms = map.readValue(sms.toString(), new TypeReference<List<Times>>(){});


        List<Times> timesList = new ArrayList<>();
        Times t = Times.builder()
                .time(LocalTime.parse("09:00"))
                .scheduleIds(List.of(UUID.randomUUID()))
                .build();
        timesList.add(t);

        System.out.println(timesList.get(0));

        objtest ot = objtest.builder().nome(LocalTime.parse("09:00")).build();





//        String aa = map.convertValue(ot, String.class);

        List<objtest> listOb = new ArrayList<>();

        objtest ott = objtest.builder()
                .nome(LocalTime.parse("09:00"))
                .ids(List.of(UUID.randomUUID()))
                .build();
        listOb.add(ott);

        ott = objtest.builder()
                .nome(LocalTime.parse("09:00"))
                .ids(List.of(UUID.randomUUID()))
                .build();
        listOb.add(ott);

        List<String> banco = new ArrayList<>();

        for(objtest oj : listOb) {
            banco.add(oj.toString());
        }

        System.out.println(banco);

        List<objtest> ltt = map.readValue(banco.toString(), new TypeReference<List<objtest>>(){});
        System.out.println(ltt);

        List<objtest> loj = new ArrayList<>();
        for(String ojs : banco) {
            objtest tt = map.readValue(ojs, objtest.class);
            loj.add(tt);
        }

//

        System.out.println(loj);


        Map<DayOfWeek, Map<LocalTime, List<UUID>>> mapSchedule = new HashMap<>();

        String time1 = "09:00";
        String time2 = "09:30";
        String time3 = "10:00";
        String time4 = "10:30";

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        TreeMap<LocalTime, List<UUID>> scheduleTime1 = new TreeMap<>();
        scheduleTime1.put(LocalTime.parse(time1), List.of(UUID.randomUUID()));
        scheduleTime1.put(LocalTime.parse(time2), List.of(UUID.randomUUID()));
        scheduleTime1.put(LocalTime.parse(time3), List.of(UUID.randomUUID()));

//        List<String> times = new ArrayList<>();
//        for (LocalTime hour : scheduleTime1.keySet()) {
//            List<UUID> scheduleIds = scheduleTime1.get(hour);
//
//            Times time = Times.builder()
//                    .time(hour)
//                    .scheduleIds(scheduleIds)
//                    .build();
//
//            System.out.println();
////            times.add(time.toString());
//        }




        mapSchedule.put(DayOfWeek.WEDNESDAY, scheduleTime1);

//        mapSchedule.forEach((k, v) -> availability(k, v));

        System.out.println(available);

//
//        scheduleTime1.values()
//                .stream()
//                .flatMap(List::stream)
//                .forEach(f -> getCount(f));
//
//        System.out.println(count);

//        Map<LocalTime, List<Integer>> scheduleTime2 = new HashMap<>();
//        scheduleTime2.put(LocalTime.parse(time1), List.of(2));
//        scheduleTime2.put(LocalTime.parse(time2), List.of(2));
//        scheduleTime2.put(LocalTime.parse(time4), List.of(2));
//
//        Map<LocalTime, List<Integer>> res =
//                Stream.concat(scheduleTime1.entrySet().stream(), scheduleTime2.entrySet().stream())
//                        .collect(Collectors.groupingBy(e-> e.getKey(),
//                                Collectors.flatMapping(e -> e.getValue().stream(), Collectors.toList())));
//
//        System.out.println(res);
//        mapper.map(request, entity);

    }
    static Map<DayOfWeek, Integer> available = new HashMap<>();
    public static void availability(DayOfWeek k, Map<LocalTime, List<Integer>> v) {
        Integer count = available.get(k);
        if (count == null)
            count = 0;
        count = count + v.size();
        available.put(k, count);
    }
}
