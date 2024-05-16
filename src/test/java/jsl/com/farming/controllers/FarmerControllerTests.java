package jsl.com.farming.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FarmerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Creating a new farmer")
    public void createFarmer(TestReporter reporter) throws Exception {
        var farmerJson = """
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
        ResultActions resultActions = mockMvc.perform(post("/api/farmer")
                .contentType(MediaType.APPLICATION_JSON).content(farmerJson));
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Add a farm to an existing farmer")
    public void addFarm() throws Exception {
        var farm = """
                {
                  "farmType":"SMALL",
                  "farmSize":2450.5,
                  "location":{
                    "country":"United State",
                    "state":"Georgia",
                    "city":"Atlanta",
                    "street":"123 Cabbage Town "
                  }
                }
                """;
        var urlTemplate = "/api/farmer/%s/farm".formatted("88d55042-87b1-4333-85dc-7694c746892a");
        ResultActions resultActions = mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON).content(farm));
        resultActions.andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Updating an already existing farmer")
    public void updateFarmer(TestReporter reporter) throws Exception {
        var farmerJson = """
                {
                  "age":24,
                  "name":"updated name",
                  "location":{
                    "country":"United State",
                    "state":"Georgia",
                    "city":"Atlanta",
                    "street":"123 Alabama View"
                  }
                }
                """;
        ResultActions resultActions = mockMvc.perform(put("/api/farmer/88d55042-87b1-4333-85dc-7694c746892a")
                .contentType(MediaType.APPLICATION_JSON).content(farmerJson));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Get farmer by id")
    public void getFarmerById() throws Exception {
        var farmerId = "88d55042-87b1-4333-85dc-7694c746892a";
        ResultActions result = mockMvc.perform(get("/api/farmer/{farmerId}", farmerId));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(farmerId))
                .andExpect(jsonPath("$.age").value(24))
                .andExpect(jsonPath("$.name").value("updated name"));
    }

    @Test
    @DisplayName("Get all farmers")
    public void getAllFarmers() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/farmer?page=0&size=3"));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete farmer by id")
    public void deleteFarmerById() throws Exception {
        var farmerId = "56ed20cf-9396-4057-8a8d-1df8e0a93a5d";
        var result = mockMvc.perform(delete("/api/farmer/{farmerId}", farmerId));

        result.andExpect(status().isNoContent());
    }
}
