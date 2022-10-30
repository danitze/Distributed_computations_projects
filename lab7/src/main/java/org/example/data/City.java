package org.example.data;

public class City {
    private int id;
    private int countryId;
    private String name;
    private int population;
    private boolean isCapital;

    public City(int id, int countryId, String name, int population, boolean isCapital) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.population = population;
        this.isCapital = isCapital;
    }

    public City() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public boolean isCapital() {
        return isCapital;
    }

    public void setCapital(boolean capital) {
        isCapital = capital;
    }

    public void copy(City city) {
        this.countryId = city.countryId;
        this.name = city.name;
        this.population = city.population;
        this.isCapital = city.isCapital;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", isCapital=" + isCapital +
                '}';
    }
}
