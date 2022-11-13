package org.example.socket.server;

import com.google.gson.Gson;
import org.example.data.City;
import org.example.data.Country;
import org.example.service.DbService;
import org.example.service.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketServer {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        Service service = new DbService();
        try {
            server = new ServerSocket(4004);
            try {
                clientSocket = server.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                while (true) {
                    String command = in.readLine();
                    if(command.equals("stop")) {
                        break;
                    } else if(command.contains("insertCountry")) {
                        Country country = new Gson().fromJson(command.split(" ")[1], Country.class);
                        service.insertCountry(country);
                        out.write("Country inserted\n");
                    } else if(command.contains("updateCountry")) {
                        Country country = new Gson().fromJson(command.split(" ")[1], Country.class);
                        service.updateCountry(country);
                        out.write("Country updated\n");
                    } else if(command.contains("deleteCountry")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        service.deleteCountry(id);
                        out.write("Country deleted\n");
                    } else if(command.contains("getCountry")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        Country country = service.getCountry(id);
                        out.write("country " + new Gson().toJson(country) + "\n");
                    } else if(command.contains("getCountries")) {
                        List<Country> countries = service.getCountries();
                        out.write("countries " + new Gson().toJson(countries) + "\n");
                    } else if(command.contains("insertCity")) {
                        City city = new Gson().fromJson(command.split(" ")[1], City.class);
                        service.insertCity(city);
                        out.write("City inserted\n");
                    } else if(command.contains("updateCity")) {
                        City city = new Gson().fromJson(command.split(" ")[1], City.class);
                        service.updateCity(city);
                        out.write("City updated\n");
                    } else if(command.contains("deleteCity")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        service.deleteCity(id);
                        out.write("City deleted\n");
                    } else if(command.contains("getCity")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        City city = service.getCity(id);
                        out.write("city " + new Gson().toJson(city) + "\n");
                    } else if(command.contains("getCountryCities")) {
                        int countryId = Integer.parseInt(command.split(" ")[1]);
                        List<City> countryCities = service.getCountryCities(countryId);
                        out.write("cities " + new Gson().toJson(countryCities) + "\n");
                    } else if(command.contains("clearCountries")) {
                        service.clearCountries();
                        out.write("Countries cleared\n");
                    }
                    out.flush();
                }
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
