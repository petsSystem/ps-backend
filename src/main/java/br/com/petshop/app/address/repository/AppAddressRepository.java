package br.com.petshop.app.address.repository;

import br.com.petshop.app.address.model.entity.AppAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppAddressRepository extends JpaRepository<AppAddressEntity, Integer> {
    Optional<AppAddressEntity> findByAddressId(String addressId);
    List<AppAddressEntity> findByAppUser_Email(String email);
    Optional<AppAddressEntity> findByStreetAndNumber(String street, String number);
}
