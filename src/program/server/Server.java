package program.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private boolean isRunServer;
    private int port;
    private ServerSocket serverListener;

    public Server(int port){
        this.port = port;
        this.isRunServer = true;
    }

    @Override
    public void run(){
        try{
            this.serverListener = new ServerSocket(this.port);
            while(this.isRunServer){
                Socket socket = this.serverListener.accept();
                System.out.println("[Server] Client connected! Ip: " + socket.getInetAddress() + ", port: " + socket.getPort());

                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
        catch(IOException e){
            closeConn();
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }
    public boolean getRunServer(){
        return this.isRunServer;
    }

    public void setRunServer(boolean runServer){
        this.isRunServer = runServer;
    }

    public void closeConn(){
        try{
            this.serverListener.close();
        } catch(IOException e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }
}
