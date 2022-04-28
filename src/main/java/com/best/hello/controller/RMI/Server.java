package com.best.hello.controller.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements Dog {

    protected Server() throws RemoteException {
    }

    public String say() {
        return "RMI test";
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("0.0.0.0", 9999);
            System.out.println("监听9999端口");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
