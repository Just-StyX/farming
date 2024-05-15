package jsl.com.farming.repository;

import jsl.com.farming.entities.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, String> {
}
