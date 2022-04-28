package com.best.hello.controller.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Dog extends Remote {
    String say() throws RemoteException;
}
