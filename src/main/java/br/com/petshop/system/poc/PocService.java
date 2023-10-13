package br.com.petshop.system.poc;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.employee.model.enums.Message;
import br.com.petshop.utils.PetGeometry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PocService {

    @Autowired private ModelMapper mapper;
    @Autowired private PocRepository repository;

    @Autowired private PetGeometry geometry;

    public PocEntity create(PocDto request) {
        try {
            PocEntity newEntity = mapper.map(request, PocEntity.class);
            newEntity.setGeom((Point)
                    geometry.getPoint(newEntity.getAddress().getLat(), newEntity.getAddress().getLon()));


            newEntity = repository.save(newEntity);

            return newEntity;

        } catch (GenericAlreadyRegisteredException ex) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_CREATE.get(), ex);
        }
    }

    public List<PocDto> findAround(Double lat, Double lon, Double radius) {
        try {
            Point point = geometry.getPoint(lat, lon);
            List<PocEntity> entities = repository.findAround(point, radius);

//            List<PocEntity> pocs = repository.findAll();
            return entities.stream().map(p -> mapper.map(p, PocDto.class)).collect(Collectors.toList());

        } catch (GenericAlreadyRegisteredException ex) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_CREATE.get(), ex);
        }
    }

    public PocDto patchMethod(UUID id, JsonPatch patch) {
        try {
            PocEntity entity = repository.findById(id).orElseThrow(GenericNotFoundException::new);
            entity = applyPatchToCustomer(patch, entity);
            entity = repository.save(entity);
            return  mapper.map(entity, PocDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            return null;
        } catch (GenericNotFoundException e) {
            return null;
        }
    }

@Autowired private ObjectMapper omapper;

    private PocEntity applyPatchToCustomer(
            JsonPatch patch, PocEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(omapper.convertValue(entity, JsonNode.class));
        return omapper.treeToValue(patched, PocEntity.class);
    }
}
