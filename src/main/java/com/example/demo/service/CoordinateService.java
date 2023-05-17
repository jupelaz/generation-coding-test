package com.example.demo.service;

import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.model.dto.CoordinateResponse;
import com.example.demo.model.dto.GenericResponse;

import java.io.IOException;

public interface CoordinateService {
    GenericResponse createFile() throws IOException;
    void updateFile(CoordinateRequest request) throws IOException;
    CoordinateResponse sendFile() throws IOException ;
    void deleteFile() throws IOException;
}
