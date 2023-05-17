package com.example.demo.controller;

import com.example.demo.model.dto.CoordinateRequest;
import com.example.demo.service.CoordinateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CoordinatesControllerTest {
    @Mock
    CoordinateService service;
    @InjectMocks
    CoordinatesController controller;


    @Test
    void createFile() throws IOException {
        controller.createFile();
        verify(service).createFile();
    }

    @Test
    void updateFile() throws IOException {
        var request = mock(CoordinateRequest.class);
        controller.updateFile(request);
        verify(service).updateFile(request);
    }

    @Test
    void sendFile() throws IOException {
        controller.sendFile();
        verify(service).sendFile();
    }

    @Test
    void deleteFile() throws IOException {
        controller.deleteFile();
        verify(service).deleteFile();
    }
}
