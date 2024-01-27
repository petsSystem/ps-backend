package br.com.petshop.app.address.service;

import br.com.petshop.app.address.model.dto.request.AppAddressCreateRequest;
import br.com.petshop.app.address.model.dto.request.AppAddressUpdateRequest;
import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.address.model.entity.AppAddressEntity;
import br.com.petshop.app.address.model.enums.Message;
import br.com.petshop.app.address.repository.AppAddressRepository;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.service.CustomerService;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.customer.service.GeometryService;
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
    @Autowired private CustomerService appUserService;
    @Autowired private AppAddressRepository addressRepository;
    @Autowired private AppAddressConverterService convert;

    @Autowired private GeometryService geometry;

    public AppAddressResponse create(Principal authentication, AppAddressCreateRequest request) {
        try {
//            Optional<AppAddressEntity> opEntity = addressRepository.findByStreetAndNumber(request.getStreet(), request.getNumber());
//            if (opEntity.isPresent())
//                throw new GenericAlreadyRegisteredException(Message.ADDRESS_ALREADY_REGISTERED.get());

            CustomerEntity userEntity = null;//appUserService.findByEmail(authentication.getName());
            AppAddressEntity addressEntity = convert.addressCreateRequestIntoEntity(request);
            addressEntity.setAppUser(userEntity);
//            addressEntity.setGeom(geometry.getPoint(addressEntity.getLat(), addressEntity.getLon()));
//            Set<AddressEntity> entities = userEntity.getAppUserAddresses();
//            if (entities == null)
//                entities = new HashSet<>();
//
//            entities.add(addressEntity);
//            userEntity.setAppUserAddresses(entities);
            addressEntity = addressRepository.save(addressEntity);

            setPrincipal(authentication, addressEntity.getId());

            return convert.addressEntityIntoResponse(addressEntity);

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

    public AppAddressEntity getPrincipal(Principal authentication) {
        try {
            Optional<AppAddressEntity> address = addressRepository.findByAppUser_EmailAndPrincipalIsTrue(authentication.getName());

            return address.get();
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
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_UPDATE.get(), ex);
        }
    }

    public void setPrincipal (Principal authentication, String addressId) {
        try {
            List<AppAddressEntity> addresses = addressRepository.findByAppUser_Email(authentication.getName());
            addresses.stream().forEach(a -> {
                if (a.getPrincipal()) {
                    a.setPrincipal(false);
                    addressRepository.save(a);
                }
                if (a.getId().equalsIgnoreCase(addressId)) {
                    a.setPrincipal(true);
                    addressRepository.save(a);
                }
            });
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_PRINCIPAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_PRINCIPAL.get(), ex);
        }
    }

    public void delete(Principal authentication, String addressId) {
        try {
            AppAddressEntity addressEntity = findById(addressId);
            addressRepository.delete(addressEntity);

            List<AppAddressEntity> addresses = addressRepository.findByAppUser_Email(authentication.getName());
            if (!addresses.isEmpty()) {
                Optional<AppAddressEntity> entity = addresses.stream()
                        .filter(a -> a.getPrincipal())
                        .findFirst();
                if (entity.isEmpty()) {
                    String id = addresses.get(addresses.size() - 1).getId();
                    addressEntity = findById(id);
                    addressEntity.setPrincipal(true);
                    addressRepository.save(addressEntity);
                }
            }

//            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
//            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
//            userEntity.setAppUserAddresses(addressEntities);
//
//            save(userEntity);
//            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (Exception ex) {
            log.error(Message.ADDRESS_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ADDRESS_ERROR_DELETE.get(), ex);
        }
    }

    public AppAddressEntity findById(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(GenericNotFoundException::new);
    }
}
