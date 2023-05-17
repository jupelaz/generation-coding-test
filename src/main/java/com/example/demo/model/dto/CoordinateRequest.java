package com.example.demo.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CoordinateRequest {
    private int x;
    private int y;
}
