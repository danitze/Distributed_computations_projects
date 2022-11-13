package org.example.rmi.client;

import org.example.data.City;
import org.example.data.Country;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DbManager extends Remote {
    void insertCountry(Country country) throws RemoteException;

    void updateCountry(Country country) throws RemoteException;

    void deleteCountry(int id) throws RemoteException;

    Country getCountry(int id) throws RemoteException;

    List<Country> getCountries() throws RemoteException;

    void insertCity(City city) throws RemoteException;

    void updateCity(City city) throws RemoteException;

    void deleteCity(int id) throws RemoteException;

    City getCity(int id) throws RemoteException;

    List<City> getCountryCities(int countryId) throws RemoteException;

    void clearCountries() throws RemoteException;
}
