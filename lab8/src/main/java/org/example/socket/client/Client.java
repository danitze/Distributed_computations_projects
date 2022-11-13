package org.example.socket.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.data.City;
import org.example.data.Country;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                Scanner scanner = new Scanner(System.in);
                System.out.println(getOptions());
                while (true) {
                    System.out.println("Enter option:");
                    int option = readInt(scanner);
                    switch (option) {
                        case 1 -> {
                            Country country = readCountry(scanner);
                            writeMessage(out, "insertCountry " + new Gson().toJson(country));
                        }
                        case 2 -> {
                            Country country = readCountry(scanner);
                            writeMessage(out, "updateCountry " + new Gson().toJson(country));
                        }
                        case 3 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            writeMessage(out, "deleteCountry " + id);
                        }
                        case 4 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            writeMessage(out, "getCountry " + id);
                        }
                        case 5 -> {
                            System.out.println("Countries: ");
                            writeMessage(out, "getCountries");
                        }
                        case 6 -> {
                            City city = readCity(scanner);
                            writeMessage(out, "insertCity " + new Gson().toJson(city));
                        }
                        case 7 -> {
                            City city = readCity(scanner);
                            writeMessage(out, "updateCity " + new Gson().toJson(city));
                        }
                        case 8 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            writeMessage(out, "deleteCity " + id);
                        }
                        case 9 -> {
                            System.out.print("Enter city id: ");
                            int id = readInt(scanner);
                            writeMessage(out, "getCity " + id);
                        }
                        case 10 -> {
                            System.out.print("Enter country id: ");
                            int id = readInt(scanner);
                            writeMessage(out, "getCountryCities " + id);
                        }
                        case 11 -> {
                            writeMessage(out, "clearCountries");
                        }
                        case 12 -> {
                            writeMessage(out, "stop");
                            return;
                        }
                        default -> System.out.println("Wrong command");
                    }
                    parseMessage(in.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private static void parseMessage(String message) {
        if(message.contains("country")) {
            System.out.println(new Gson().fromJson(message.split(" ")[1], Country.class));
        } else if(message.contains("countries")) {
            Type listType = new TypeToken<List<Country>>(){}.getType();
            List<Country> countries = new Gson().fromJson(message.split(" ")[1], listType);
            System.out.println(countries);
        } else if(message.contains("city")) {
            System.out.println(new Gson().fromJson(message.split(" ")[1], City.class));
        } else if(message.contains("cities")) {
            Type listType = new TypeToken<List<City>>(){}.getType();
            List<City> cities = new Gson().fromJson(message.split(" ")[1], listType);
            System.out.println(cities);
        } else {
            System.out.println(message);
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

    private static void writeMessage(BufferedWriter bufferedWriter, String message) throws IOException {
        bufferedWriter.write(message + "\n");
        bufferedWriter.flush();
    }
}
