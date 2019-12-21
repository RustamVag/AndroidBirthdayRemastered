package com.dom.rustam.androidbirthdayremastered;

import java.util.Date;

/**
 * Created by Рустам on 03.10.2016.
 */
public class Birthday {

    private Integer id;

    private String name;

    private Integer day;

    private Integer month;

    private Double latitude;

    private Double longitude;


    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getDay()
    {
        return this.day;
    }

    public void setDay(Integer day)
    {
        this.day = day;
    }

    public Integer getMonth()
    {
        return this.month;
    }

    public void setMonth(Integer month)
    {
        this.month = month;
    }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLatitude() { return this.latitude; }

    public void setLongitude(Double longitude) {this.longitude = longitude; }

    public Double getLongitude() {return this.longitude; }


    // Конструкторы
    public Birthday(String name, Integer day, Integer month)
    {
        this.name = name;
        this.day = day;
        this.month = month;
        this.id = 0;
    }

    public Birthday()
    {
        this.name = "";
        this.day = 1;
        this.month = 0;
        this.id = 0;
    }

    public Birthday(Integer id, String name, Integer day, Integer month)
    {
        this.name = name;
        this.day = day;
        this.month = month;
        this.id = id;
    }

    public Birthday(Integer id, String name, Integer day, Integer month, Double latitude, Double longitude)
    {
        this.name = name;
        this.day = day;
        this.month = month;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}

