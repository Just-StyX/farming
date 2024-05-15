package jsl.com.farming.json;

import jsl.com.farming.entities.Farm;
import jsl.com.farming.entities.FarmType;
import jsl.com.farming.entities.Farmer;
import jsl.com.farming.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class FarmerJsonTest {
    private final JacksonTester<Farm> farmJacksonTester;
    private final JacksonTester<Farmer> farmerJacksonTester;
    private final JacksonTester<List<Farm>> listFarmJacksonTester;
    private List<Farm> farms;
    private final Location location = new Location("United State", "Georgia", "Atlanta", "123 Mountain View");
    private final Farmer farmer = new Farmer(24, "test name", location);
    private final Farm farm = new Farm(FarmType.SMALL, 2450.5, location);

    @Autowired
    public FarmerJsonTest(JacksonTester<Farm> farmJacksonTester, JacksonTester<Farmer> farmerJacksonTester,
                          JacksonTester<List<Farm>> listFarmJacksonTester) {
        this.farmJacksonTester = farmJacksonTester;
        this.farmerJacksonTester = farmerJacksonTester;
        this.listFarmJacksonTester = listFarmJacksonTester;
    }

    @BeforeEach
    void setUp() {
        farms = List.of(
                new Farm(FarmType.SMALL, 2450.5, location),
                new Farm(FarmType.LARGE, 2450.5, location),
                new Farm(FarmType.SMALL, 2450.5, location)
        );
    }

    @Test
    @DisplayName("Serialize farmer")
    public void serializeFarmer() throws IOException {
        var farmerJson = farmerJacksonTester.write(farmer);
        var farmJson = farmJacksonTester.write(farm);
        assertAll(
                () -> assertThat(farmerJson).isEqualToJson("farmer.json"),
                () -> assertThat(farmJson).isEqualToJson("farm.json")
        );
    }

    @Test
    @DisplayName("Adding farms to farmer")
    public void addFarmListToFarmer() throws IOException {
        for (Farm farm1: farms) farmer.getFarms().add(farm1);
        var farmerJson = farmerJacksonTester.write(farmer);
        var farmListJson = listFarmJacksonTester.write(farms);
        assertAll(
                () -> assertThat(farmerJson).isEqualToJson("farmerList.json"),
                () -> assertThat(farmListJson).isEqualToJson("farmList.json")
        );
    }

    @Test
    @DisplayName("from json to farm object")
    public void fromJsonToFarm() throws IOException {
        Farmer newFarmer = new Farmer(24, "test name", location);
        newFarmer.setFarms(farms);
        var expectedFarmer = """
                {
                  "age":24,
                  "name":"test name",
                  "location":{
                    "country":"United State",
                    "state":"Georgia",
                    "city":"Atlanta",
                    "street":"123 Mountain View"
                  }
                }
                """;
        var expectedFarm = """
                {
                  "farmType":"SMALL",
                  "farmSize":2450.5,
                  "location":{
                    "country":"United State",
                    "state":"Georgia",
                    "city":"Atlanta",
                    "street":"123 Mountain View"
                  }
                }
                """;
        var expectedFarmerList = """
                {
                  "age":24,
                  "name":"test name",
                  "location":{
                    "country":"United State",
                    "state":"Georgia",
                    "city":"Atlanta",
                    "street":"123 Mountain View"
                  },
                  "farms": [
                    {
                      "farmType":"SMALL",
                      "farmSize":2450.5,
                      "location":{
                        "country":"United State",
                        "state":"Georgia",
                        "city":"Atlanta",
                        "street":"123 Mountain View"
                      }
                    },
                    {
                      "farmType":"LARGE",
                      "farmSize":2450.5,
                      "location":{
                        "country":"United State",
                        "state":"Georgia",
                        "city":"Atlanta",
                        "street":"123 Mountain View"
                      }
                    },
                    {
                      "farmType":"SMALL",
                      "farmSize":2450.5,
                      "location":{
                        "country":"United State",
                        "state":"Georgia",
                        "city":"Atlanta",
                        "street":"123 Mountain View"
                      }
                    }
                  ]
                }
                """;
        var expectedFarmList = """
                [
                  {
                    "farmType":"SMALL",
                    "farmSize":2450.5,
                    "location":{
                      "country":"United State",
                      "state":"Georgia",
                      "city":"Atlanta",
                      "street":"123 Mountain View"
                    }
                  },
                  {
                    "farmType":"LARGE",
                    "farmSize":2450.5,
                    "location":{
                      "country":"United State",
                      "state":"Georgia",
                      "city":"Atlanta",
                      "street":"123 Mountain View"
                    }
                  },
                  {
                    "farmType":"SMALL",
                    "farmSize":2450.5,
                    "location":{
                      "country":"United State",
                      "state":"Georgia",
                      "city":"Atlanta",
                      "street":"123 Mountain View"
                    }
                  }
                ]
                """;
        assertAll(
                () -> assertThat(farmerJacksonTester.parse(expectedFarmer))
                        .extracting(Object::toString).isEqualTo(farmer.toString()),
                () -> assertThat(farmerJacksonTester.parse(expectedFarmer))
                        .extracting(Object::toString).isEqualTo(newFarmer.toString()),
                () -> assertThat(farmJacksonTester.parse(expectedFarm))
                        .extracting(Object::toString).isEqualTo(farm.toString()),
                () -> assertThat(listFarmJacksonTester.parse(expectedFarmList))
                        .extracting(Object::toString).isEqualTo(farms.toString())
        );
    }
}
