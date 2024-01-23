package br.com.petshop.authentication.service;

import br.com.petshop.customer.model.dto.request.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.service.CustomerFacade;
import br.com.petshop.authentication.model.dto.request.AuthenticationForget;
import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.model.enums.AuthType;
import br.com.petshop.authentication.model.enums.Message;
import br.com.petshop.user.service.SysUserValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationFacade {

    private Logger log = LoggerFactory.getLogger(AuthenticationFacade.class);
    @Autowired private AuthenticationService service;
    @Autowired private SysUserValidateService userFacade;
    @Autowired private CustomerFacade customerFacade;

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            return service.login(request);

        } catch (BadCredentialsException ex) {
            log.error(Message.AUTH_USER_PASSWORD_ERROR.get() + " Error: " +  ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, Message.AUTH_USER_PASSWORD_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.AUTH_LOGIN_ERROR.get() + " Error: " +  ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.AUTH_LOGIN_ERROR.get(), ex);
        }
    }

    public void forget (AuthenticationForget request, AuthType type) {
        if (type == AuthType.SYS)
            userFacade.forget(request.username());
        else
            customerFacade.forget(request.username());
    }

    public CustomerResponse create(CustomerAppCreateRequest request) {
        return customerFacade.createApp(request);
    }
}