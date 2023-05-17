package com.example.demo.controller;

import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.model.dto.CoordinateResponse;
import com.example.demo.model.dto.GenericResponse;
import com.example.demo.service.CoordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {
    final CoordinateService coordinateService;
    @PostMapping
    public ResponseEntity<GenericResponse> createFile() throws IOException {
        var response = coordinateService.createFile();
        return ResponseEntity.ok(response);
    }
    @PutMapping
    public void updateFile(@Valid CoordinateRequest request) throws IOException {
        coordinateService.updateFile(request);
    }
    @GetMapping
    public ResponseEntity<CoordinateResponse> sendFile() throws IOException {
        var response = coordinateService.sendFile();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping
    public void deleteFile() throws IOException {
        coordinateService.deleteFile();
    }
}
