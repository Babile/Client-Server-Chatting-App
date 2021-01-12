package program.server;

import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private boolean isRunServer;
    private int port;
    private ServerSocket serverListener;
    public static ArrayList<ServerThread> clientList;

    public Server(int port){
        this.port = port;
        this.isRunServer = true;
        clientList = new ArrayList<ServerThread>();
    }

    @Override
    public void run(){
        try{
            boolean pass = false;
            this.serverListener = new ServerSocket(this.port);
            while(this.isRunServer){
                pass = false;
                Socket socket = this.serverListener.accept();

                System.out.println("[Server] Client connected! Ip: " + socket.getInetAddress() + ", port: " + socket.getPort());
                ServerThread serverThread = new ServerThread(socket);
                clientList.add(serverThread);
                Thread thread = new Thread(serverThread);
                thread.setDaemon(true);
                thread.start();
            }
        }
        catch(IOException e){
            shutDown();
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }
    public boolean getRunServer(){
        return this.isRunServer;
    }

    public void setRunServer(boolean runServer){
        this.isRunServer = runServer;
    }

    public void shutDown(){
        try{
            this.serverListener.close();
        } catch(IOException e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }
}
