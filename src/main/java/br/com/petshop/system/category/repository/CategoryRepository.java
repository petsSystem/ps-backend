package br.com.petshop.system.category.repository;

import br.com.petshop.system.category.model.entity.CategoryEntity;
import br.com.petshop.system.category.model.enums.Category;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findById(UUID categoryId);
    Optional<CategoryEntity> findByTypeAndCompanyId(Category type, UUID companyId);
    List<CategoryEntity> findAll(Specification<CategoryEntity> filter);

//    Optional<CategoryEntity> findByIdAndActiveIsTrue(UUID categoryId);
//
//
//    Optional<CategoryEntity> findByCnpj(String cnpj);
//    @Query(value = "SELECT * from sys_company where ST_DistanceSphere(geom, :p) < :distance AND active is true", nativeQuery = true)
//    List<CategoryEntity> findNearWithinDistance(Point p, Double distance);
//
//    @Query(value = "SELECT ST_DistanceSphere(geom, :p) from sys_company where id = :id AND active is true", nativeQuery = true)
//    Double getDistance(Point p, UUID id);
//    Page<CategoryEntity> findAll(Specification<CategoryEntity> filter, Pageable paging);
////    @Query(value = "SELECT * FROM public.sys_company sc JOIN public.sys_company_employee sce ON sc.id = sce.company_id WHERE sce.employee_id = :employeeId", nativeQuery = true)
//    Page<CompanyEntity> findCompaniesFromEmployeeId(UUID employeeId, Pageable paging);
}
