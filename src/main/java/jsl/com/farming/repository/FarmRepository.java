package jsl.com.farming.repository;

import jsl.com.farming.entities.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, String> {
}
