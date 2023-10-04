package br.com.petshop.system.company.repository;

import br.com.petshop.system.company.model.entity.CompanyEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
    Optional<CompanyEntity> findById(String companyId);
    Optional<CompanyEntity> findByIdAndActiveIsTrue(String companyId);
    Optional<CompanyEntity> findByCnpjAndActiveIsTrue(String companyId);
    @Query(value = "SELECT *, ST_DistanceSphere(geom, :p) from sys_company where ST_DistanceSphere(geom, :p) < :distance AND active is true", nativeQuery = true)
    List<CompanyEntity> findNearWithinDistance(Point p, Double distance);

    @Query(value = "SELECT ST_DistanceSphere(geom, :p) from sys_company where id = :id AND active is true", nativeQuery = true)
    Double getDistance(Point p, String id);
}
