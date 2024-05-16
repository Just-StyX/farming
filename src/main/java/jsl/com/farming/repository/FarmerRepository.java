package jsl.com.farming.repository;

import jsl.com.farming.entities.Farmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FarmerRepository extends JpaRepository<Farmer, String> {
    @Query("select f FROM Farmer f")
    Page<Farmer> findAllFarmers(Pageable pageable);
}
