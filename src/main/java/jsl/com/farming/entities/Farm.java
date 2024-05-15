package jsl.com.farming.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "farm")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "farm_type", nullable = false)
    private FarmType farmType;

    @Column(name = "farm_size", nullable = false)
    private double farmSize;

    @Embedded
    private Location location;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    public Farm() {}
    public Farm(FarmType farmType, double farmSize, Location location) {
        this.location = location;
        this.farmSize = farmSize;
        this.farmType = farmType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Farm farm = (Farm) object;
        return Double.compare(farmSize, farm.farmSize) == 0 && Objects.equals(id, farm.id) && farmType == farm.farmType && Objects.equals(location, farm.location) && Objects.equals(farmer, farm.farmer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, farmType, farmSize, location, farmer);
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id='" + id + '\'' +
                ", farmType=" + farmType +
                ", farmSize=" + farmSize +
                ", location=" + location +
                ", farmer=" + farmer +
                '}';
    }
}