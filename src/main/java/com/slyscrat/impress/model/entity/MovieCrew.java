package com.slyscrat.impress.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieCrew implements Serializable {
    private int crew;
    private int movie;
}
