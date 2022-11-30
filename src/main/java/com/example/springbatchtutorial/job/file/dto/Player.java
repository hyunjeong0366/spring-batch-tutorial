package com.example.springbatchtutorial.job.file.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class Player {
    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
}

