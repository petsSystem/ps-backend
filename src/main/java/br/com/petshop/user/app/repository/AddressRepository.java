package br.com.petshop.user.app.repository;

import br.com.petshop.user.app.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    Optional<AddressEntity> findByAddressId(String addressId);
    List<AddressEntity> findByAppUser_Email(String email);
    Optional<AddressEntity> findByStreetAndNumber(String street, String number);
}
