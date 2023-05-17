package com.example.demo.service;

import com.example.demo.model.Coordinates;
import com.example.demo.model.dto.CoordinateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log
public class CoordinatesServiceTest {

    @Autowired
    private CoordinateService service;

    @BeforeEach void before() throws IOException {
        Path coordinatesFile = Paths.get("./coordinates.json");
        try {
            Files.delete(coordinatesFile);
        } catch (NoSuchFileException e){
            log.info("File does not exist");
        }
    }
    @Test void postRequest() throws IOException {
        Path original = Paths.get("./sample.json");
        Path copied = Paths.get("./coordinates.json");
        service.createFile();
        assertTrue(Files.exists(copied));
        assertEquals(Files.readAllLines(original),Files.readAllLines(copied));
    }
    @Nested
    public class CoordinatesServiceTestWithFileCreated {

        @BeforeEach void before() throws IOException {
            Path original = Paths.get("./sample.json");
            Path copied = Paths.get("./coordinates.json");
            Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
        }

        @Test void putRequest() throws IOException {
            var request = CoordinateRequest.builder().x(1).y(2).build();
            service.updateFile(request);
            Path file = Paths.get("./coordinates.json");
            assertTrue(Files.exists(file));
            assertEquals(List.of("{\"valueX\":1,\"valueY\":2}"),Files.readAllLines(file));

        }
        @Test void getRequest() throws IOException {
            Path path = Paths.get("./coordinates.json");
            var response = service.sendFile();
            ObjectMapper objectMapper = new ObjectMapper();
            var coordinates = objectMapper.readValue(path.toFile(), Coordinates.class);
            assertAll(
                    () -> assertEquals(coordinates.getValueX(),response.getX()),
                    () -> assertEquals(coordinates.getValueY(),response.getY()),
                    () -> assertEquals(coordinates.getValueX() + coordinates.getValueY(), response.getResult())
            );
        }
        @Test void deleteRequest() throws IOException {
            Path path = Paths.get("./coordinates.json");
            service.deleteFile();
            assertFalse(Files.exists(path));
        }
    }
    @AfterEach void after() throws IOException {
        Path coordinatesFile = Paths.get("./coordinates.json");
        Files.delete(coordinatesFile);
    }
}
