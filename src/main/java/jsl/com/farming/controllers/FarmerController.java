package jsl.com.farming.controllers;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.entities.Farmer;
import jsl.com.farming.service.FarmerServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/farmer")
public class FarmerController {
    private final FarmerServices farmerServices;

    public FarmerController(FarmerServices farmerServices) {
        this.farmerServices = farmerServices;
    }

    @PostMapping
    public ResponseEntity<Void> createFarmer(@RequestBody Farmer farmer, UriComponentsBuilder uriComponentsBuilder) {
        var savedFarmer = farmerServices.saveFarmer(farmer);
        URI location = uriComponentsBuilder.path("api/farmer/{id}").buildAndExpand(savedFarmer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{farmerId}/farm")
    public ResponseEntity<Void> addFarm(@PathVariable String farmerId, @RequestBody Farm farm) {
        var farmer = farmerServices.addFarm(farmerId, farm);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/{farmerId}")
    public ResponseEntity<Void> updateFarmer(@PathVariable String farmerId, @RequestBody Farmer farmer) {
        var farmerUpdated = farmerServices.updateFarmer(farmerId, farmer);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{farmerId}")
    public ResponseEntity<Farmer> getFarmer(@PathVariable String farmerId) {
        var farmer = farmerServices.findFarmerById(farmerId);
        return ResponseEntity.of(farmer);
    }

    @GetMapping
    public ResponseEntity<List<Farmer>> getAllFarmers(@RequestParam int page, @RequestParam int size) {
        var farmers = farmerServices.findAll(page, size);
        return ResponseEntity.ok(farmers);
    }

    @DeleteMapping("{farmerId}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String farmerId) {
        farmerServices.deleteFarmerById(farmerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

