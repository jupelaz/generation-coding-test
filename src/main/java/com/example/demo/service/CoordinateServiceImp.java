package com.example.demo.service;

import com.example.demo.model.Coordinates;
import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.model.dto.CoordinateResponse;
import com.example.demo.model.dto.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class CoordinateServiceImp implements CoordinateService {

    final ObjectMapper objectMapper;
    Path coordinatesPath = Paths.get("./coordinates.json");
    Path originPath = Paths.get("./sample.json");
    public GenericResponse createFile() throws IOException {
        Files.copy(originPath, coordinatesPath, StandardCopyOption.REPLACE_EXISTING);
        return GenericResponse.builder().message("Coordinates file has been created correctly").build();
    }
    public void updateFile(CoordinateRequest request) throws IOException {

        var coordinates = objectMapper.readValue(coordinatesPath.toFile(), Coordinates.class);
        coordinates.setValueX(request.getX());
        coordinates.setValueY(request.getY());
        objectMapper.writeValue(coordinatesPath.toFile(), coordinates);
    }
    public CoordinateResponse sendFile() throws IOException {
        var coordinates = objectMapper.readValue(coordinatesPath.toFile(), Coordinates.class);
        return CoordinateResponse
                .builder()
                .x(coordinates.getValueX())
                .y(coordinates.getValueY())
                .result(coordinates.getValueX() + coordinates.getValueY())
                .build();
    }

    public void deleteFile() throws IOException {
        var file = Paths.get("./coordinates.json");
        Files.delete(file);
    }
}
