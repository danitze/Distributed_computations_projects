package org.example;

import org.example.data.City;
import org.example.data.Country;
import org.example.repository.Repository;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Repository repository = new Repository();
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            System.out.println("Enter 1 for db, 2 for json, 3 for exit");
            int num = readInt(scanner);
            switch (num) {
                case 1 -> {
                    System.out.println(getOptions());
                    int option = readInt(scanner);
                    switch (option) {
                        case 1 -> {
                            Country country = readCountry(scanner);
                            repository.insertCountryDb(country);
                        }
                        case 2 -> {
                            Country country = readCountry(scanner);
                            repository.updateCountryDb(country);
                        }
                        case 3 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            repository.deleteCountryDb(id);
                        }
                        case 4 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            Country country = repository.getCountryDb(id);
                            System.out.println(country);
                        }
                        case 5 -> {
                            System.out.println("Countries: ");
                            repository.getCountriesDb().forEach(System.out::println);
                        }
                        case 6 -> {
                            City city = readCity(scanner);
                            repository.insertCityDb(city);
                        }
                        case 7 -> {
                            City city = readCity(scanner);
                            repository.updateCityDb(city);
                        }
                        case 8 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            repository.deleteCityDb(id);
                        }
                        case 9 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            City city = repository.getCityDb(id);
                            System.out.println(city);
                        }
                        case 10 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            repository.getCountryCitiesDb(id).forEach(System.out::println);
                        }
                        case 11 -> {
                            repository.clearCountriesDb();
                            System.out.println("Cleared successfully)");
                        }
                        case 12 -> {
                            return;
                        }
                        default -> System.out.println("Wrong command");
                    }
                }
                case 2 -> {
                    System.out.println(getOptions());
                    int option = readInt(scanner);
                    switch (option) {
                        case 1 -> {
                            Country country = readCountry(scanner);
                            repository.insertCountryJson(country);
                        }
                        case 2 -> {
                            Country country = readCountry(scanner);
                            repository.updateCountryJson(country);
                        }
                        case 3 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            repository.deleteCountryJson(id);
                        }
                        case 4 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            Country country = repository.getCountryJson(id);
                            System.out.println(country);
                        }
                        case 5 -> {
                            System.out.println("Countries: ");
                            repository.getCountriesJson().forEach(System.out::println);
                        }
                        case 6 -> {
                            City city = readCity(scanner);
                            repository.insertCityJson(city);
                        }
                        case 7 -> {
                            City city = readCity(scanner);
                            repository.updateCityJson(city);
                        }
                        case 8 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            repository.deleteCityJson(id);
                        }
                        case 9 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            City city = repository.getCityJson(id);
                            System.out.println(city);
                        }
                        case 10 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            repository.getCountryCitiesJson(id).forEach(System.out::println);
                        }
                        case 11 -> {
                            repository.clearCountriesJson();
                            System.out.println("Cleared successfully)");
                        }
                        case 12 -> {
                            return;
                        }
                        default -> System.out.println("Wrong command");
                    }
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("Wrong command");
            }
        }
    }

    private static String getOptions() {
        return "Enter 1 for insert country, 2 for update country, 3 for delete country, 4 for get country, 5 " +
                "for get countries, 6 for insert city, 7 for update city, 8 for delete city, 9 for get city, " +
                "10 for get country cities, 11 for clear countries, 12 for exit";
    }
    private static int readInt(Scanner scanner) {
        return Integer.parseInt(scanner.nextLine());
    }

    private static boolean readBoolean(Scanner scanner) {
        int num = readInt(scanner);
        if(num == 0) {
            return false;
        }
        if(num == 1) {
            return true;
        }
        throw new RuntimeException();
    }

    private static Country readCountry(Scanner scanner) {
        int id;
        String name;
        System.out.print("Enter country id: ");
        id = readInt(scanner);
        System.out.print("Enter country name: ");
        name = scanner.nextLine();
        return new Country(id, name);
    }

    private static City readCity(Scanner scanner) {
        int id;
        int countryId;
        String name;
        int population;
        boolean isCapital;
        System.out.print("Enter city id: ");
        id = readInt(scanner);
        System.out.print("Enter country id: ");
        countryId = readInt(scanner);
        System.out.print("Enter city name: ");
        name = scanner.nextLine();
        System.out.print("Enter city population: ");
        population = readInt(scanner);
        if(population < 0) {
            throw new RuntimeException();
        }
        System.out.print("Enter city is capital (1 for yes, 0 for no): ");
        isCapital = readBoolean(scanner);
        return new City(id, countryId, name, population, isCapital);
    }
}