package com.example.demo.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CoordinateResponse {
    private int x;
    private int y;
    private int result;
}
