package com.example.demo.model.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateRequest {
    private int x;
    private int y;
}
