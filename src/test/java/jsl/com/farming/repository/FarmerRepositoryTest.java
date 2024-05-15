package jsl.com.farming.repository;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.entities.FarmType;
import jsl.com.farming.entities.Farmer;
import jsl.com.farming.entities.Location;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FarmerRepositoryTest {
    @Autowired
    FarmerRepository farmerRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static Farmer farmer = new Farmer();
    private static Location location;

    @BeforeEach
    void setUpFarmer() {
        location = new Location("United State", "Georgia", "Atlanta", "123 Mountain View");
        farmer = new Farmer(24, "test name", location);
    }

    @Test
    @DisplayName("Saving farmer in database")
    public void saveFarmer(TestReporter reporter) {
        var savedFarmer = farmerRepository.save(farmer);
        reporter.publishEntry(savedFarmer.toString());
        assertAll(
                () -> assertNotNull(savedFarmer.getId()),
                () -> assertThat(savedFarmer.getFarms()).isEmpty()
        );
    }

    @Test
    @DisplayName("Save farmer and add farms")
    public void saveFarmerAndFarm(TestReporter reporter) {
        Farm farm = new Farm(FarmType.SMALL, 2450.5, location);
        var savedFarmer = farmerRepository.save(farmer);
        savedFarmer.addFarm(farm);
        var farmerUpdatedWithFarm = farmerRepository.save(savedFarmer);

        assertAll(
                () -> assertEquals(savedFarmer.getId(), farmerUpdatedWithFarm.getId()),
                () -> assertNotNull(farmerUpdatedWithFarm.getFarms().getFirst().getId()),
                () -> assertThat(1).isEqualTo(farmerUpdatedWithFarm.getFarms().size()),
                () -> assertNotNull(farmerUpdatedWithFarm.getFarms().getFirst().getFarmer())
        );

        reporter.publishEntry(farmerUpdatedWithFarm.getFarms().toString());
    }

    @Test
    @DisplayName("Find farmer by id")
    public void findById() {
        var savedFarmer = farmerRepository.save(farmer);
        var foundFarmer = farmerRepository.findById(savedFarmer.getId());
        assertAll(
                () -> assertTrue(foundFarmer.isPresent()),
                () -> assertEquals(savedFarmer, foundFarmer.orElse(null))
        );
    }

    @Test
    @DisplayName("Delete by farmer by id")
    public void deleteById() {
        var savedFarmer = farmerRepository.save(farmer);
        farmerRepository.deleteById(savedFarmer.getId());
        assertFalse(farmerRepository.findById(savedFarmer.getId()).isPresent());
    }

    @Test
    @DisplayName("Add more farms for farmer")
    public void addMoreFarms() {
        List<Farm> farms = List.of(
                new Farm(FarmType.SMALL, 2450.5, location),
                new Farm(FarmType.LARGE, 2450.5, location),
                new Farm(FarmType.SMALL, 2450.5, location)
        );

        var savedFarmer = farmerRepository.save(farmer);
        for (Farm farm: farms) savedFarmer.addFarm(farm);
        var saveFarmerWithFarms = farmerRepository.save(savedFarmer);
        assertAll(
                () -> assertEquals(savedFarmer.getId(), saveFarmerWithFarms.getId()),
                () -> assertThat(3).isEqualTo(saveFarmerWithFarms.getFarms().size())
        );
    }
}
