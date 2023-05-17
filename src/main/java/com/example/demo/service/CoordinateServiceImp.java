package com.example.demo.service;

import com.example.demo.model.Coordinates;
import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.model.dto.CoordinateResponse;
import com.example.demo.model.dto.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class CoordinateServiceImp implements CoordinateService {

    public GenericResponse createFile() throws IOException {
        var destinyFile = Paths.get("./coordinates.json");
        var originFile = Paths.get("./sample.json");
        Files.copy(originFile,destinyFile, StandardCopyOption.REPLACE_EXISTING);
        return GenericResponse.builder().message("Coordinates file has been created correctly").build();
    }
    public void updateFile(CoordinateRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var coordinates = objectMapper.readValue(new File("./coordinates.json"), Coordinates.class);
        coordinates.setValueX(request.getX());
        coordinates.setValueY(request.getY());
        objectMapper.writeValue(new File("./coordinates.json"), coordinates);
    }
    public CoordinateResponse sendFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var coordinates = objectMapper.readValue(new File("./coordinates.json"), Coordinates.class);
        return CoordinateResponse
                .builder()
                .x(coordinates.getValueX())
                .y(coordinates.getValueY())
                .build();
    }

    public void deleteFile() throws IOException {
        var file = Paths.get("./coordinates.json");
        Files.delete(file);
    }
}
