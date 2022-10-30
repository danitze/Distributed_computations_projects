package org.example.service;

import org.example.data.City;
import org.example.data.Country;

import java.util.List;

public interface Service {
    public void insertCountry(Country country);

    public void updateCountry(Country country);

    public void deleteCountry(int id);

    public Country getCountry(int id);

    public List<Country> getCountries();

    public void insertCity(City city);

    public void updateCity(City city);

    public void deleteCity(int id);

    public City getCity(int id);

    public List<City> getCountryCities(int countryId);

    public void clearCountries();
}
