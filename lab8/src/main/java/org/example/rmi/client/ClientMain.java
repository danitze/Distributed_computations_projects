package org.example.rmi.client;

import org.example.data.City;
import org.example.data.Country;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {
    public static final String UNIQUE_BINDING_NAME = "server.dbmanager";

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final Registry registry = LocateRegistry.getRegistry(2732);
        DbManager dbManager = (DbManager) registry.lookup(UNIQUE_BINDING_NAME);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(getOptions());
            int option = readInt(scanner);
            switch (option) {
                case 1 -> {
                    Country country = readCountry(scanner);
                    dbManager.insertCountry(country);
                }
                case 2 -> {
                    Country country = readCountry(scanner);
                    dbManager.updateCountry(country);
                }
                case 3 -> {
                    System.out.print("Enter country id: ");
                    int id = readInt(scanner);
                    dbManager.deleteCountry(id);
                }
                case 4 -> {
                    System.out.print("Enter country id: ");
                    int id = readInt(scanner);
                    Country country = dbManager.getCountry(id);
                    System.out.println(country);
                }
                case 5 -> {
                    System.out.println("Countries: ");
                    dbManager.getCountries().forEach(System.out::println);
                }
                case 6 -> {
                    City city = readCity(scanner);
                    dbManager.insertCity(city);
                }
                case 7 -> {
                    City city = readCity(scanner);
                    dbManager.updateCity(city);
                }
                case 8 -> {
                    System.out.print("Enter city id: ");
                    int id = readInt(scanner);
                    dbManager.deleteCity(id);
                }
                case 9 -> {
                    System.out.print("Enter city id: ");
                    int id = readInt(scanner);
                    City city = dbManager.getCity(id);
                    System.out.println(city);
                }
                case 10 -> {
                    System.out.print("Enter country id: ");
                    int id = readInt(scanner);
                    dbManager.getCountryCities(id).forEach(System.out::println);
                }
                case 11 -> {
                    dbManager.clearCountries();
                    System.out.println("Cleared successfully)");
                }
                case 12 -> {
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
        if (num == 0) {
            return false;
        }
        if (num == 1) {
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
        if (population < 0) {
            throw new RuntimeException();
        }
        System.out.print("Enter city is capital (1 for yes, 0 for no): ");
        isCapital = readBoolean(scanner);
        return new City(id, countryId, name, population, isCapital);
    }
}
