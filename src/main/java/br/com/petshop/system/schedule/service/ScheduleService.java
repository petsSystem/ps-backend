package br.com.petshop.system.schedule.service;

import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import br.com.petshop.system.schedule.model.enums.Message;
import br.com.petshop.system.schedule.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleRepository scheduleRepository;

    public List<UUID> create(CompanyEntity company) {
        try {
            final UUID companyId = company.getId();
//            return company.getCategories().stream()
//                    .map(c -> {
//                        ScheduleEntity entity = ScheduleEntity.builder()
//                                .category(c)
//                                .companyId(companyId)
//                                .active(true)
//                                .build();
//                        entity = scheduleRepository.save(entity);
//                        return entity.getId();
//                    }).collect(Collectors.toList());
            return null;

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ERROR_CREATE.get(), ex);
        }
    }

    public List<ScheduleEntity> getByCompanyId(UUID companyId) {
        try {
            return scheduleRepository.findByCompanyIdAndActiveIsTrue(companyId);

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ERROR_CREATE.get(), ex);
        }
    }

    public List<ScheduleEntity> getAllByCompanyId(UUID companyId) {
        try {
            return scheduleRepository.findByCompanyId(companyId);

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ERROR_CREATE.get(), ex);
        }
    }
}
