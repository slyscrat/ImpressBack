package com.slyscrat.impress.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameRate implements Serializable {
    private int user;
    private int game;
}
