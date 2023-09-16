package br.com.petshop.user.app.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.user.app.model.dto.request.AddressCreateRequest;
import br.com.petshop.user.app.model.dto.request.AddressUpdateRequest;
import br.com.petshop.user.app.model.dto.response.AddressResponse;
import br.com.petshop.user.app.model.entity.AddressEntity;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.user.app.repository.AddressRepository;
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
public class AddressService {

    Logger log = LoggerFactory.getLogger(AddressService.class);
    @Autowired private AppUserService appUserService;
    @Autowired private AddressRepository addressRepository;
    @Autowired private ConvertService convert;

    public AddressResponse create(Principal authentication, AddressCreateRequest request) {
        try {
            Optional<AddressEntity> opEntity = addressRepository.findByStreetAndNumber(request.getStreet(), request.getNumber());
            if (opEntity.isPresent())
                throw new GenericAlreadyRegisteredException("Endereço já existe.");

            AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
            AddressEntity addressEntity = convert.convertAddressCreateRequestIntoEntity(request);
            addressEntity.setAppUser(userEntity);
//            Set<AddressEntity> entities = userEntity.getAppUserAddresses();
//            if (entities == null)
//                entities = new HashSet<>();
//
//            entities.add(addressEntity);
//            userEntity.setAppUserAddresses(entities);
            addressEntity = addressRepository.save(addressEntity);
            return convert.convertAddressEntityIntoResponse(addressEntity);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already Registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do endereço do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public Set<AddressResponse> get(Principal authentication) {
        try {
            List<AddressEntity> address = addressRepository.findByAppUser_Email(authentication.getName());

            return address.stream()
                    .map(a -> convert.convertAddressEntityIntoResponse(a))
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar endereço do cadastro. Tente novamente mais tarde.", ex);
        }
    }

    public AddressResponse update(String addressId, AddressUpdateRequest request) {
        try {
            AddressEntity addressEntity = findById(addressId);
            addressEntity = convert.convertAddressUpdateRequestIntoEntity(request, addressEntity);
            addressEntity = addressRepository.save(addressEntity);
//            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
//            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
//            userEntity.setAppUserAddresses(addressEntities);
//            save(userEntity);
            return convert.convertAddressEntityIntoResponse(addressEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar endereço do cadastro. Tente novamente mais tarde.", ex);
        }
    }

    public void delete(String addressId) {
        try {
            AddressEntity addressEntity = findById(addressId);
            addressRepository.delete(addressEntity);
//            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
//            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
//            userEntity.setAppUserAddresses(addressEntities);
//
//            save(userEntity);
//            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir endereço do cadastro. Tente novamente mais tarde.", ex);
        }
    }

    public AddressEntity findById(String addressId) {
        return addressRepository.findByAddressId(addressId)
                .orElseThrow(GenericNotFoundException::new);
    }
}
