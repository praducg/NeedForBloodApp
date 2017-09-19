package com.example.loadcombo.loaddropdownexamples;

/**
 * Created by Pradeep on 8/20/2017.
 */

public class WorldDetails {

    private String country;
    private String state;
    private String toponymName;//This  is for the city name
    private String population;
    private String geoNameId;
    public String getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(String geoNameId) {
        this.geoNameId = geoNameId;
    }



    public String getToponymName() {
        return toponymName;
    }

    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    public String getState() {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }



}
