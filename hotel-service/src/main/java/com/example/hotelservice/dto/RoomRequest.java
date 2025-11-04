package com.example.hotelservice.dto;

import lombok.Data;

@Data
public class RoomRequest {
    private Long hotelId;
    private String number;
}