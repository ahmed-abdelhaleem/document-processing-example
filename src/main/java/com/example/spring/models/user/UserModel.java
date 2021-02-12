package com.example.spring.models.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public class UserModel {

    private Long id;

    private String name;

    private UUID sid;

}
