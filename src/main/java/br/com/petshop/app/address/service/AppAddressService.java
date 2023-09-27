package br.com.petshop.app.address.service;

import br.com.petshop.app.address.model.dto.request.AppAddressCreateRequest;
import br.com.petshop.app.address.model.dto.request.AppAddressUpdateRequest;
import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.address.model.entity.AppAddressEntity;
import br.com.petshop.app.address.model.enums.Message;
import br.com.petshop.app.address.repository.AppAddressRepository;
import br.com.petshop.app.user.model.entity.AppUserEntity;
import br.com.petshop.app.user.service.AppUserService;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppAddressService {

    Logger log = LoggerFactory.getLogger(AppAddressService.class);
    @Autowired private AppUserService appUserService;
    @Autowired private AppAddressRepository addressRepository;
    @Autowired private AppAddressConverterService convert;

    public AppAddressResponse create(Principal authentication, AppAddressCreateRequest request) {
        try {
            Optional<AppAddressEntity> opEntity = addressRepository.findByStreetAndNumber(request.getStreet(), request.getNumber());
            if (opEntity.isPresent())
                throw new GenericAlreadyRegisteredException(Message.ADDRESS_ALREADY_REGISTERED.get());

            AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
            AppAddressEntity addressEntity = convert.addressCreateRequestIntoEntity(request);
            addressEntity.setAppUser(userEntity);
//            Set<AddressEntity> entities = userEntity.getAppUserAddresses();
//            if (entities == null)
//                entities = new HashSet<>();
//
//            entities.add(addressEntity);
//            userEntity.setAppUserAddresses(entities);
            addressEntity = addressRepository.save(addressEntity);
            return convert.addressEntityIntoResponse(addressEntity);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_CREATE.get(), ex);
        }
    }

    public Set<AppAddressResponse> get(Principal authentication) {
        try {
            List<AppAddressEntity> address = addressRepository.findByAppUser_Email(authentication.getName());

            return address.stream()
                    .map(a -> convert.addressEntityIntoResponse(a))
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_GET.get(), ex);
        }
    }

    public AppAddressResponse update(String addressId, AppAddressUpdateRequest request) {
        try {
            AppAddressEntity addressEntity = findById(addressId);
            addressEntity = convert.addressUpdateRequestIntoEntity(request, addressEntity);
            addressEntity = addressRepository.save(addressEntity);
//            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
//            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
//            userEntity.setAppUserAddresses(addressEntities);
//            save(userEntity);
            return convert.addressEntityIntoResponse(addressEntity);
        } catch (GenericNotFoundException ex) {
            log.error(Message.ADDRESS_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.ADDRESS_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_UPDATE.get(), ex);
        }
    }

    public void delete(String addressId) {
        try {
            AppAddressEntity addressEntity = findById(addressId);
            addressRepository.delete(addressEntity);
//            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
//            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
//            userEntity.setAppUserAddresses(addressEntities);
//
//            save(userEntity);
//            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (GenericNotFoundException ex) {
            log.error(Message.ADDRESS_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.ADDRESS_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_DELETE.get(), ex);
        }
    }

    public AppAddressEntity findById(String addressId) {
        return addressRepository.findByAddressId(addressId)
                .orElseThrow(GenericNotFoundException::new);
    }
}