package org.example.repository;

import org.example.data.City;
import org.example.data.Country;
import org.example.service.DbService;
import org.example.service.JsonService;
import org.example.service.Service;

import java.io.IOException;
import java.util.List;

public class Repository {

    private final Service dbService;
    private final Service jsonService;

    public Repository() throws IOException {
        dbService = new DbService();
        String COUNTRY_FILE_NAME = "country.json";
        String CITY_FILE_NAME = "city.json";
        jsonService = new JsonService(COUNTRY_FILE_NAME, CITY_FILE_NAME);
        addShutdownHook();
    }

    //Db
    public void insertCountryDb(Country country) {
        insertCountry(dbService, country);
    }

    public void updateCountryDb(Country country) {
        updateCountry(dbService, country);
    }

    public void deleteCountryDb(int id) {
        deleteCountry(dbService, id);
    }

    public Country getCountryDb(int id) {
        return getCountry(dbService, id);
    }

    public List<Country> getCountriesDb() {
        return getCountries(dbService);
    }

    public void insertCityDb(City city) {
        insertCity(dbService, city);
    }

    public void updateCityDb(City city) {
        updateCity(dbService, city);
    }

    public void deleteCityDb(int id) {
        deleteCity(dbService, id);
    }

    public City getCityDb(int id) {
        return getCity(dbService, id);
    }

    public List<City> getCountryCitiesDb(int countryId) {
        return getCountryCities(dbService, countryId);
    }

    public void clearCountriesDb() {
        clearCountries(dbService);
    }

    //Json
    public void insertCountryJson(Country country) {
        insertCountry(jsonService, country);
    }

    public void updateCountryJson(Country country) {
        updateCountry(jsonService, country);
    }

    public void deleteCountryJson(int id) {
        deleteCountry(jsonService, id);
    }

    public Country getCountryJson(int id) {
        return getCountry(jsonService, id);
    }

    public List<Country> getCountriesJson() {
        return getCountries(jsonService);
    }

    public void insertCityJson(City city) {
        insertCity(jsonService, city);
    }

    public void updateCityJson(City city) {
        updateCity(jsonService, city);
    }

    public void deleteCityJson(int id) {
        deleteCity(jsonService, id);
    }

    public City getCityJson(int id) {
        return getCity(jsonService, id);
    }

    public List<City> getCountryCitiesJson(int countryId) {
        return getCountryCities(jsonService, countryId);
    }

    public void clearCountriesJson() {
        clearCountries(jsonService);
    }

    //Util methods
    private void insertCountry(Service service, Country country) {
        service.insertCountry(country);
    }

    private void updateCountry(Service service, Country country) {
        service.updateCountry(country);
    }

    private void deleteCountry(Service service, int id) {
        service.deleteCountry(id);
    }

    private Country getCountry(Service service, int id) {
        return service.getCountry(id);
    }

    private List<Country> getCountries(Service service) {
        return service.getCountries();
    }

    private void insertCity(Service service, City city) {
        service.insertCity(city);
    }

    private void updateCity(Service service, City city) {
        service.updateCity(city);
    }

    private void deleteCity(Service service, int id) {
        service.deleteCity(id);
    }

    private City getCity(Service service, int id) {
        return service.getCity(id);
    }

    private List<City> getCountryCities(Service service, int countryId) {
        return service.getCountryCities(countryId);
    }

    private void clearCountries(Service service) {
        service.clearCountries();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ((JsonService) jsonService).writeData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
