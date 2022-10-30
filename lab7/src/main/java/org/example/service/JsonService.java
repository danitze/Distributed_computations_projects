package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.City;
import org.example.data.Country;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonService implements Service {

    private final String countryFileName;
    private final String cityFileName;

    private final ObjectMapper objectMapper;

    private List<Country> countries = null;
    private List<City> cities = null;

    public JsonService(String countryFileName, String cityFileName) throws IOException {
        this.countryFileName = countryFileName;
        this.cityFileName = cityFileName;
        this.objectMapper = new ObjectMapper();

        readData();
    }

    public void readData() throws IOException {
        File file = new File(countryFileName);
        if(file.exists()) {
            countries = objectMapper.readValue(file, new TypeReference<>() {});
        } else {
            countries = new LinkedList<>();
        }

        file = new File(cityFileName);
        if(file.exists()) {
            cities = objectMapper.readValue(file, new TypeReference<>() {});
        } else {
            cities = new LinkedList<>();
        }
    }

    public void writeData() throws IOException {
        File file = new File(countryFileName);
        objectMapper.writeValue(file, countries);

        file = new File(cityFileName);
        objectMapper.writeValue(file, cities);
    }

    @Override
    public void insertCountry(Country country) {
        if(countries.stream().anyMatch(listedCountry -> listedCountry.getId() == country.getId())) {
            return;
        }
        countries.add(country);
    }

    @Override
    public void updateCountry(Country country) {
        Country oldCountry = countries.stream()
                .filter(listedCountry -> listedCountry.getId() == country.getId())
                .findFirst().orElseThrow();

        oldCountry.copy(country);
    }

    @Override
    public void deleteCountry(int id) {
        countries.stream()
                .filter(listedCountry -> listedCountry.getId() == id)
                .findFirst().ifPresent(countries::remove);
        cities.stream()
                .filter(listedCity -> listedCity.getCountryId() == id)
                .forEach(cities::remove);
    }

    @Override
    public Country getCountry(int id) {
        return countries.stream()
                .filter(listedCountry -> listedCountry.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public List<Country> getCountries() {
        return countries;
    }

    @Override
    public void insertCity(City city) {
        if(cities.stream().anyMatch(listedCity -> listedCity.getId() == city.getId())) {
            return;
        }
        if(countries.stream().noneMatch(listedCountry -> listedCountry.getId() == city.getCountryId())) {
            throw new RuntimeException("No country with id " + city.getCountryId());
        }
        cities.add(city);
    }

    @Override
    public void updateCity(City city) {
        City oldCity = cities.stream()
                .filter(listedCity -> listedCity.getId() == city.getId())
                .findFirst().orElseThrow();

        oldCity.copy(city);
    }

    @Override
    public void deleteCity(int id) {
        cities.stream()
                .filter(listedCity -> listedCity.getId() == id)
                .findFirst().ifPresent(cities::remove);
    }

    @Override
    public City getCity(int id) {
        return cities.stream()
                .filter(listedCity -> listedCity.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public List<City> getCountryCities(int countryId) {
        return cities.stream()
                .filter(listedCity -> listedCity.getCountryId() == countryId)
                .collect(Collectors.toList());
    }

    @Override
    public void clearCountries() {
        countries.clear();
        cities.clear();
    }
}
