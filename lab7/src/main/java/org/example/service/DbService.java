package org.example.service;

import org.example.data.City;
import org.example.data.Country;
import org.example.mapper.Mappers;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DbService implements Service {
    @Override
    public void insertCountry(Country country) {
        try (Connection connection = establishConnection()) {
            String query = "INSERT INTO country(id, country_name) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, country.getId());
            preparedStatement.setString(2, country.getName());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCountry(Country country) {
        try(Connection connection = establishConnection()) {
            String query = "UPDATE country SET country_name=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, country.getName());
            preparedStatement.setInt(2, country.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCountry(int id) {
        try(Connection connection = establishConnection()) {
            String query = "DELETE FROM country WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Country getCountry(int id) {
        try(Connection connection = establishConnection()) {
            String query = "SELECT * FROM country WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return Mappers.mapToCountry(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Country> getCountries() {
        try(Connection connection = establishConnection()) {
            List<Country> countries = new LinkedList<>();
            String query = "SELECT * FROM country";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                countries.add(Mappers.mapToCountry(resultSet));
            }
            return countries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertCity(City city) {
        try(Connection connection = establishConnection()) {
            String query = "INSERT INTO city(id, country_id, city_name, population, is_capital) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, city.getId());
            preparedStatement.setInt(2, city.getCountryId());
            preparedStatement.setString(3, city.getName());
            preparedStatement.setInt(4, city.getPopulation());
            preparedStatement.setBoolean(5, city.isCapital());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCity(City city) {
        try(Connection connection = establishConnection()) {
            String query = "UPDATE city SET country_id=?, city_name=?, population=?, is_capital=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, city.getCountryId());
            preparedStatement.setString(2, city.getName());
            preparedStatement.setInt(3, city.getPopulation());
            preparedStatement.setBoolean(4, city.isCapital());
            preparedStatement.setInt(5, city.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCity(int id) {
        try(Connection connection = establishConnection()) {
            String query = "DELETE FROM city WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public City getCity(int id) {
        try(Connection connection = establishConnection()) {
            String query = "SELECT * FROM city WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return Mappers.mapToCity(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<City> getCountryCities(int countryId) {
        try(Connection connection = establishConnection()) {
            List<City> cities = new LinkedList<>();
            String query = "SELECT * FROM city WHERE country_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cities.add(Mappers.mapToCity(resultSet));
            }
            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCountries() {
        try(Connection connection = establishConnection()) {
            String query = "DELETE FROM country";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection establishConnection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost:3306/distributed_comp_lab_7", "root", "");
    }
}
