package jsl.com.farming.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String country;
    private String state;
    private String city;
    private String street;

    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Location location = (Location) object;
        return Objects.equals(country, location.country) && Objects.equals(state, location.state) && Objects.equals(city, location.city) && Objects.equals(street, location.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, state, city, street);
    }
}
