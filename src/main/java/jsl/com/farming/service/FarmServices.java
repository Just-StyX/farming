package jsl.com.farming.service;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.repository.FarmRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmServices {
    private final FarmRepository farmRepository;

    public FarmServices(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    public Optional<Farm> findFarmerById(String farmId) {
        return farmRepository.findById(farmId);
    }

    public Farm updateFarm(String farmId, Farm farm) {
        var oldFarm = farmRepository.findById(farmId);
        if (oldFarm.isPresent()) {
            var foundFarm = oldFarm.get();
            if (farm.getFarmType() != null) foundFarm.setFarmType(farm.getFarmType());
            if (farm.getFarmSize() != 0) foundFarm.setFarmSize(farm.getFarmSize());
            if (farm.getLocation() != foundFarm.getLocation()) foundFarm.setLocation(farm.getLocation());
            return farmRepository.save(foundFarm);
        }
        return null;
    }

    public void deleteFarm(String farmId) {
        farmRepository.deleteById(farmId);
    }

}
