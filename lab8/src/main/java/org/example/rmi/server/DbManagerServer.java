package org.example.rmi.server;

import org.example.data.City;
import org.example.data.Country;
import org.example.rmi.client.DbManager;
import org.example.service.DbService;
import org.example.service.Service;

import java.rmi.RemoteException;
import java.util.List;

public class DbManagerServer implements DbManager {

    private static Service service = new DbService();

    @Override
    public void insertCountry(Country country) throws RemoteException {
        service.insertCountry(country);
    }

    @Override
    public void updateCountry(Country country) throws RemoteException {
        service.updateCountry(country);
    }

    @Override
    public void deleteCountry(int id) throws RemoteException {
        service.deleteCountry(id);
    }

    @Override
    public Country getCountry(int id) throws RemoteException {
        return service.getCountry(id);
    }

    @Override
    public List<Country> getCountries() throws RemoteException {
        return service.getCountries();
    }

    @Override
    public void insertCity(City city) throws RemoteException {
        service.insertCity(city);
    }

    @Override
    public void updateCity(City city) throws RemoteException {
        service.updateCity(city);
    }

    @Override
    public void deleteCity(int id) throws RemoteException {
        service.deleteCity(id);
    }

    @Override
    public City getCity(int id) throws RemoteException {
        return service.getCity(id);
    }

    @Override
    public List<City> getCountryCities(int countryId) throws RemoteException {
        return service.getCountryCities(countryId);
    }

    @Override
    public void clearCountries() throws RemoteException {
        service.clearCountries();
    }
}
