package com.example.bpr.Objects;

import androidx.room.Entity;

import java.util.ArrayList;

@Entity
public class Location {
    public String type;
    public ArrayList<Double> coordinates;
}
