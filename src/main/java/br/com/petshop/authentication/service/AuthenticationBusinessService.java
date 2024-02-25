package br.com.petshop.authentication.service;

import br.com.petshop.authentication.model.dto.request.AuthenticationForget;
import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.model.enums.AuthType;
import br.com.petshop.authentication.model.enums.Message;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.service.app.CustomerAppBusinessService;
import br.com.petshop.user.service.SysUserBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
* Classe responsável pelas regras de negócio da autenticação
 */
@Service
public class AuthenticationBusinessService {

    private Logger log = LoggerFactory.getLogger(AuthenticationBusinessService.class);
    @Autowired private AuthenticationService service;
    @Autowired private SysUserBusinessService userBusiness;
    @Autowired private CustomerAppBusinessService customerBusiness;

    /**
     * Método que efetua login
     * @param request
     * @return
     */
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

    /**
     * Método que envia email de esquecimento de senha
     * @param request
     * @param type
     */
    public void forget (AuthenticationForget request, AuthType type) {
        if (type == AuthType.SYS)
            userBusiness.forget(request.username());
        else
            customerBusiness.forget(request.username());
    }

    /**
     * Método de criação de cliente do aplicativo
     * @param request
     * @return
     */
    public CustomerResponse create(CustomerAppCreateRequest request) {
        return customerBusiness.create(request);
    }
}