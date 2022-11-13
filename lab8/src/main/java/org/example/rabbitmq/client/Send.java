package org.example.rabbitmq.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.*;
import org.example.data.City;
import org.example.data.Country;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static Channel channel;
    private static Connection connection;
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            Scanner scanner = new Scanner(System.in);
            System.out.println(getOptions());
            while (true) {
                System.out.println("Enter option:");
                int option = readInt(scanner);
                String response = switch (option) {
                    case 1 -> {
                        Country country = readCountry(scanner);
                        yield call("insertCountry " + new Gson().toJson(country));
                    }
                    case 2 -> {
                        Country country = readCountry(scanner);
                        yield call("updateCountry " + new Gson().toJson(country));
                    }
                    case 3 -> {
                        System.out.print("Enter country id: ");
                        int id = readInt(scanner);
                        yield call("deleteCountry " + id);
                    }
                    case 4 -> {
                        System.out.print("Enter country id: ");
                        int id = readInt(scanner);
                        yield call("getCountry " + id);
                    }
                    case 5 -> {
                        System.out.println("Countries: ");
                        yield call("getCountries");
                    }
                    case 6 -> {
                        City city = readCity(scanner);
                        yield call("insertCity " + new Gson().toJson(city));
                    }
                    case 7 -> {
                        City city = readCity(scanner);
                        yield call("updateCity " + new Gson().toJson(city));
                    }
                    case 8 -> {
                        System.out.print("Enter city id: ");
                        int id = readInt(scanner);
                        yield call("deleteCity " + id);
                    }
                    case 9 -> {
                        System.out.print("Enter city id: ");
                        int id = readInt(scanner);
                        yield call("getCity " + id);
                    }
                    case 10 -> {
                        System.out.print("Enter country id: ");
                        int id = readInt(scanner);
                        yield call("getCountryCities " + id);
                    }
                    case 11 -> call("clearCountries");
                    case 12 -> "Stop";
                    default -> "Wrong command";
                };
                if(response.equals("Stop")) {
                    break;
                }
                parseMessage(response);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException | TimeoutException ignored) {
            }
        }
    }

    public static String call(String message) throws IOException, InterruptedException, ExecutionException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes("UTF-8"));

        final CompletableFuture<String> response = new CompletableFuture<>();

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.get();
        channel.basicCancel(ctag);
        return result;
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

    private static void call(BufferedWriter bufferedWriter, String message) throws IOException {
        bufferedWriter.write(message + "\n");
        bufferedWriter.flush();
    }
}
