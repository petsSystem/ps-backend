package br.com.petshop.system.poc;

import br.com.petshop.system.company.model.entity.CompanyEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PocRepository extends JpaRepository<PocEntity, Integer> {
    @Query(value = "SELECT * from sys_poc where ST_DistanceSphere(geom, :p) < :distance AND active is true", nativeQuery = true)
    List<PocEntity> findAround(Point p, Double distance);

    Optional<PocEntity> findById(UUID id);

}

