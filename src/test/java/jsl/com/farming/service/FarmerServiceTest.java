package jsl.com.farming.service;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.entities.FarmType;
import jsl.com.farming.entities.Farmer;
import jsl.com.farming.entities.Location;
import jsl.com.farming.repository.FarmerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FarmerServiceTest {
    @Mock
    FarmerRepository farmerRepository;

    @InjectMocks
    FarmerServices farmerServices;

    private Farmer farmer;
    private List<Farm> farms;
    private Location location;

    @BeforeEach
    void setFarmerRepository() {
        location = new Location("United State", "Georgia", "Atlanta", "123 Mountain View");
        farmer = new Farmer(24, "test name", location);
        farms = List.of(
                new Farm(FarmType.SMALL, 2450.5, location),
                new Farm(FarmType.LARGE, 2450.5, location),
                new Farm(FarmType.SMALL, 2450.5, location)
        );
    }

    @Test
    @DisplayName("Service layer saving")
    public void saveFarmer() {
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer);

        var savedFarmer = farmerServices.saveFarmer(farmer);
        assertNull(savedFarmer.getId());
        verify(farmerRepository).save(farmer);
    }

    @Test
    @DisplayName("find farmer by id")
    public void findFarmerById() {
        when(farmerRepository.findById(anyString())).thenReturn(Optional.ofNullable(farmer));

        var foundFarmer = farmerServices.findFarmerById(anyString());
        assertTrue(foundFarmer.isPresent());
        verify(farmerRepository).findById(anyString());
    }

    @Test
    @DisplayName("Update farmer")
    public void updateFarmer() {
        var newFarmer = new Farmer(27, "update name", location);
        newFarmer.setFarms(farms);

        when(farmerRepository.findById(anyString())).thenReturn(Optional.ofNullable(farmer));
        when(farmerRepository.save(any(Farmer.class))).thenReturn(newFarmer);

        var updatedFarmer = farmerServices.updateFarmer("abc", newFarmer);

        assertEquals(27, updatedFarmer.getAge());

        InOrder order = inOrder(farmerRepository);
        order.verify(farmerRepository).findById(anyString());
        order.verify(farmerRepository).save(any(Farmer.class));
    }

    @Test
    @DisplayName("Add a new farm")
    public void addANewFarm() {
        when(farmerRepository.findById(anyString())).thenReturn(Optional.ofNullable(farmer));
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer);

        var farmAdded = farmerServices.addFarm("abc", farms.getFirst());

        assertEquals(1, farmAdded.getFarms().size());

        InOrder order = inOrder(farmerRepository);
        order.verify(farmerRepository).findById(anyString());
        order.verify(farmerRepository).save(any(Farmer.class));
    }

    @Test
    @DisplayName("Find all farmers")
    public void findAllFarmers() {
        var farmer1 = farmer;
        var farmer2 = new Farmer(27, "update name", location);
        List<Farmer> farmers = List.of(farmer1, farmer2);
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer1, farmer2);
        when(farmerRepository.findAll()).thenReturn(farmers);

        farmerServices.saveFarmer(farmer1);
        farmerServices.saveFarmer(farmer2);

        var listFarmers = farmerServices.findAllFarmers();

        assertEquals(2, listFarmers.size());

        InOrder order = inOrder(farmerRepository);;
        order.verify(farmerRepository, times(2)).save(any(Farmer.class));
        order.verify(farmerRepository).findAll();
    }

    @Test
    @DisplayName("Delete farmer")
    public void delete() {
        farmerServices.deleteFarmerById(anyString());
        verify(farmerRepository).deleteById(anyString());
    }
}
