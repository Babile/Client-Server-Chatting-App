package program.client;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable{
    private boolean isRunClientThread;
    private Socket socket;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private String name;
    private boolean connected;

    public ClientThread(String name){
        try{
            this.connected = false;
            this.isRunClientThread = true;
            this.name = name;
            this.socket = new Socket("127.0.0.1", 9000);
            this.fromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.toServer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch(IOException e){
            System.out.println("[Client] error msg: ");
            e.printStackTrace();
        }
    }

    public void closeConn(){
        try{
            if(this.fromServer != null || this.toServer != null || this.socket != null){
                System.out.println("[Client] Exit message send to Server.");
                this.toServer.println("exit," + socket.getPort());
                this.fromServer.close();
                this.toServer.close();
                this.socket.close();
                this.isRunClientThread = false;
            }
        }
        catch(IOException e){
            System.out.println("[Client] error msg: ");
            e.printStackTrace();
        }
    }

    public void sendMsgToServer(String msg){
        System.out.println("[Client] Message send to Server.");
        this.toServer.println(msg);
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public void run(){
        while(this.isRunClientThread){
            try{
                System.out.println("[Client] Message receive from Server.");
                String[] msgs = this.fromServer.readLine().split(",");

                if(msgs[0].equals("[Server] Client accepted")){
                    this.connected = true;
                }
                else if(msgs[0].equals("message")){
                    Platform.runLater(() -> {
                        ClientGUI.messages.appendText(msgs[1] + ": " + msgs[2] + "\n");
                    });
                }
            } catch(IOException e){
                System.out.println("[Client] error msg: ");
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected(){
        return this.connected;
    }
}
