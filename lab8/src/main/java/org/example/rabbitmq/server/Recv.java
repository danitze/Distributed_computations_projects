package org.example.rabbitmq.server;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.example.data.City;
import org.example.data.Country;
import org.example.service.DbService;
import org.example.service.Service;

import java.util.List;

public class Recv {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static final Service service = new DbService();

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.queuePurge(RPC_QUEUE_NAME);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            String response = "";
            try {
                String command = new String(delivery.getBody(), "UTF-8");
                response = process(command);
            } catch (RuntimeException e) {
                System.out.println(" [.] " + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));
    }

    private static String process(String command) {
        if (command.contains("insertCountry")) {
            Country country = new Gson().fromJson(command.split(" ")[1], Country.class);
            service.insertCountry(country);
            return "Country inserted";
        } else if (command.contains("updateCountry")) {
            Country country = new Gson().fromJson(command.split(" ")[1], Country.class);
            service.updateCountry(country);
            return "Country updated";
        } else if (command.contains("deleteCountry")) {
            int id = Integer.parseInt(command.split(" ")[1]);
            service.deleteCountry(id);
            return "Country deleted";
        } else if (command.contains("getCountry")) {
            int id = Integer.parseInt(command.split(" ")[1]);
            Country country = service.getCountry(id);
            return "country " + new Gson().toJson(country);
        } else if (command.contains("getCountries")) {
            List<Country> countries = service.getCountries();
            return "countries " + new Gson().toJson(countries);
        } else if (command.contains("insertCity")) {
            City city = new Gson().fromJson(command.split(" ")[1], City.class);
            service.insertCity(city);
            return "City inserted";
        } else if (command.contains("updateCity")) {
            City city = new Gson().fromJson(command.split(" ")[1], City.class);
            service.updateCity(city);
            return "City updated";
        } else if (command.contains("deleteCity")) {
            int id = Integer.parseInt(command.split(" ")[1]);
            service.deleteCity(id);
            return "City deleted";
        } else if (command.contains("getCity")) {
            int id = Integer.parseInt(command.split(" ")[1]);
            City city = service.getCity(id);
            return "city " + new Gson().toJson(city);
        } else if (command.contains("getCountryCities")) {
            int countryId = Integer.parseInt(command.split(" ")[1]);
            List<City> countryCities = service.getCountryCities(countryId);
            return "cities " + new Gson().toJson(countryCities);
        } else if (command.contains("clearCountries")) {
            service.clearCountries();
            return "Countries cleared";
        }
        return "";
    }
}
