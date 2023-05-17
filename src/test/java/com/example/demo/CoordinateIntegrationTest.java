package com.example.demo;

import com.example.demo.model.Coordinates;
import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.model.dto.CoordinateResponse;
import com.example.demo.model.dto.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Log
public class CoordinateIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String PATH = "/api/coordinates";

    private final Path coordinatesFilePath = Paths.get("./coordinates.json");
    private final Path originFilePath = Paths.get("./sample.json");

    void fileDelete() throws IOException {
        try {
            Files.delete(coordinatesFilePath);
        } catch (NoSuchFileException e){
            log.info("File does not exist");
        }
    }
    void fileCreation() throws IOException {
        Files.copy(originFilePath, coordinatesFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
    @Test
    void whenPost_thenTheFileIsCreated() throws Exception {
        var response = mockMvc.perform(
                        post(PATH))
                .andReturn().getResponse();
        GenericResponse genericResponse = objectMapper
                .readValue(response.getContentAsString(), GenericResponse.class);
        assertTrue(Files.exists(coordinatesFilePath));
        assertEquals(response.getStatus(),HttpStatus.OK.value());
        assertEquals(genericResponse.getMessage(),"Coordinates file has been created correctly");
    }

    @Test
    void whenPost_thenTheFileAlreadyExists() throws Exception {
        fileCreation();
        var response = mockMvc.perform(
                        post(PATH))
                .andReturn().getResponse();
        GenericResponse genericResponse = objectMapper
                .readValue(response.getContentAsString(), GenericResponse.class);
        assertEquals(response.getStatus(),HttpStatus.OK.value());
        assertEquals(genericResponse.getMessage(),"Coordinates file has been created correctly");
    }


    @Test
    public void givenAValidRequest_whenPut_thenTheFileIsUpdated() throws Exception {
        fileCreation();
        var request = CoordinateRequest.builder().x(11).y(22).build();
        var content = objectMapper.writeValueAsString(request);
        mockMvc.perform(
                put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        var coordinates = objectMapper.readValue(coordinatesFilePath.toFile(), Coordinates.class);
        assertEquals(request.getX(),coordinates.getValueX());
        assertEquals(request.getY(),coordinates.getValueY());
    }

    @Test
    public void givenAValidRequestWithTheFileErased_whenPut_thenReturnsAnError() throws Exception {
        fileDelete();
        var request = CoordinateRequest.builder().x(10).y(20).build();
        var response = mockMvc.perform(
                put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andReturn().getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatus());

    }

    @Test
    public void givenAnInvalidRequest_whenPut_thenReturnsAValidationError() throws Exception {
        fileCreation();
        var response = mockMvc.perform(
                put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")).andReturn().getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatus());
    }

    @Test
    public void givenAExistingFile_whenGet_thenReturnsExpectedFile() throws Exception {
        fileCreation();
        var response = mockMvc.perform(get(PATH)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        var coordinates = objectMapper.readValue(coordinatesFilePath.toFile(), Coordinates.class);
        var responseCoordinates = objectMapper.readValue(response.getContentAsString(), CoordinateResponse.class);
        assertEquals(coordinates.getValueX(),responseCoordinates.getX());
        assertEquals(coordinates.getValueY(),responseCoordinates.getY());
        assertEquals(
                coordinates.getValueX() + coordinates.getValueY(),
                responseCoordinates.getResult());
    }
    @Test
    public void givenANotExistingFile_whenGet_thenReturnsExpectedError() throws Exception {
        fileDelete();
        var response = mockMvc.perform(get(PATH)).andReturn().getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatus());
    }

    @Test
    public void givenAnExistingFile_whenDelete_thenTheFileIsDeleted() throws Exception {
        fileCreation();
        var response = mockMvc.perform(delete(PATH)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertFalse(Files.exists(coordinatesFilePath));
    }
    @Test
    public void givenANotExistingFile_whenDelete_thenReturnsExpectedError() throws Exception {
        fileDelete();
        var response = mockMvc.perform(delete(PATH)).andReturn().getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatus());
    }
}
