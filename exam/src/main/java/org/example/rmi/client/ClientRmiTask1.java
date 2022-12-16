package org.example.rmi.client;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRmiTask1 {
    public static final String UNIQUE_BINDING_NAME = "server.students";

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final Registry registry = LocateRegistry.getRegistry(2732);
        Manager manager = (Manager) registry.lookup(UNIQUE_BINDING_NAME);
        System.out.println(manager.getStudentsByGroup("IPS-31"));
    }

}
