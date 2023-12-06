package br.com.petshop.system.schedule.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import br.com.petshop.system.schedule.model.enums.Message;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleValidateService {
    Logger log = LoggerFactory.getLogger(ScheduleValidateService.class);
    @Autowired private ScheduleService service;
    @Autowired private ScheduleConverterService convert;


    public List<ScheduleResponse> getByCompanyId(Principal authentication, UUID companyId) {
        try {
            List<ScheduleEntity> entities = new ArrayList<>();
            if (getRole(authentication) == Role.ADMIN)
                entities = service.getAllByCompanyId(companyId);
            else
                entities = service.getByCompanyId(companyId);

            return entities.stream()
                    .map(e -> convert.entityIntoResponse(e))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ERROR_CREATE.get(), ex);
        }
    }

    private Role getRole(Principal authentication) {
        SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());

        return systemUser.getRole();
    }
}
