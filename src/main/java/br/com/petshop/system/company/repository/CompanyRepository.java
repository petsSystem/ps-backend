package br.com.petshop.system.company.repository;

import br.com.petshop.system.company.model.entity.CompanyEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
    Optional<CompanyEntity> findById(UUID companyId);
    Optional<CompanyEntity> findByIdAndActiveIsTrue(UUID companyId);
    Optional<CompanyEntity> findByCnpj(String cnpj);
    @Query(value = "SELECT * from sys_company where ST_DistanceSphere(geom, :p) < :distance AND active is true", nativeQuery = true)
    List<CompanyEntity> findNearWithinDistance(Point p, Double distance);

    @Query(value = "SELECT ST_DistanceSphere(geom, :p) from sys_company where id = :id AND active is true", nativeQuery = true)
    Double getDistance(Point p, UUID id);
    Page<CompanyEntity> findAll(Specification<CompanyEntity> filter, Pageable paging);
    @Query(value = "SELECT * FROM public.sys_company sc JOIN public.sys_company_employee sce ON sc.id = sce.company_id WHERE sce.employee_id = :employeeId", nativeQuery = true)
    Page<CompanyEntity> findCompaniesFromEmployeeId(UUID employeeId, Pageable paging);
}
