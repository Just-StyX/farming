package jsl.com.farming.service;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.entities.Farmer;
import jsl.com.farming.repository.FarmerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerServices {
    private final FarmerRepository farmerRepository;

    public FarmerServices(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    public Farmer saveFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public Optional<Farmer> findFarmerById(String farmerId) {
        return farmerRepository.findById(farmerId);
    }

    public Farmer updateFarmer(String farmerId, Farmer farmer) {
        var oldFarmer = farmerRepository.findById(farmerId);
        if (oldFarmer.isPresent()) {
            var foundFarmer = oldFarmer.get();
            if (farmer.getAge() != 0) foundFarmer.setAge(farmer.getAge());
            if (farmer.getName() != null) foundFarmer.setName(farmer.getName());
            if (!farmer.getFarms().isEmpty()) {
                for (Farm farm: farmer.getFarms()) foundFarmer.addFarm(farm);
            }
            if (farmer.getLocation() != foundFarmer.getLocation()) foundFarmer.setLocation(farmer.getLocation());
            return farmerRepository.save(foundFarmer);
        }
        return null;
    }

    public Farmer addFarm(String farmerId, Farm farm) {
        var farmer = farmerRepository.findById(farmerId);
        if (farmer.isPresent()) {
            var foundFarmer = farmer.get();
            foundFarmer.addFarm(farm);
            return farmerRepository.save(foundFarmer);
        }
        return null;
    }

    public List<Farmer> findAllFarmers() {
        return farmerRepository.findAll();
    }
    public List<Farmer> findAll(int page, int size) {
        Sort.TypedSort<Farmer> sort = Sort.sort(Farmer.class);
        return farmerRepository
                .findAllFarmers(PageRequest.of(page, size, sort.by(Farmer::getName))).getContent();
    }

    public void deleteFarmerById(String farmerId) {
        farmerRepository.deleteById(farmerId);
    }
}
