package org.example.mapper;

import org.example.data.City;
import org.example.data.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mappers {
    public static Country mapToCountry(ResultSet resultSet) {
        try {
            return new Country(
                    resultSet.getInt("id"),
                    resultSet.getString("country_name")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static City mapToCity(ResultSet resultSet) {
        try {
            return new City(
                    resultSet.getInt("id"),
                    resultSet.getInt("country_id"),
                    resultSet.getString("city_name"),
                    resultSet.getInt("population"),
                    resultSet.getBoolean("is_capital")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
