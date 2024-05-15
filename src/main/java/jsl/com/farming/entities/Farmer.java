package jsl.com.farming.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "farmer")
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Location location;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true,
            mappedBy = "farmer", fetch = FetchType.LAZY)
    private List<Farm> farms = new ArrayList<>();

    public Farmer() {}
    public Farmer(int age, String name, Location location) {
        this.age = age;
        this.name = name;
        this.location = location;
    }

    public void addFarm(Farm farm) {
        this.getFarms().add(farm);
        farm.setFarmer(this);
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
