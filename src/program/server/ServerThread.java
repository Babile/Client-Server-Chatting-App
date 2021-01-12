package program.server;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{
    private boolean isRunServerThread;
    private Socket socket;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private String name;

    public ServerThread(Socket socket){
        this.isRunServerThread = true;
        this.socket = socket;
        try{
            this.fromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.toClient = new PrintWriter(this.socket.getOutputStream(), true);
        }
        catch(IOException e){
            System.out.println("[Server] error msg: ");
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        boolean pass = false;
        while(this.isRunServerThread){
            try{
                String[] rows = this.fromClient.readLine().split(",");

                if(!pass){
                    System.out.println("[Server] Received message from Client");
                    Platform.runLater(() -> {
                        ServerGUI.clients.appendText(rows[0] + "\n");
                    });
                    name = rows[0];
                    sendMessageToClient("[Server] Client accepted");
                    pass = true;
                }

                else if(rows[0].equals("exit")){
                    System.out.println("[Server] Client exits.");
                    if(!Server.clientList.isEmpty()){
                        Platform.runLater(() -> {
                            ServerGUI.clients.clear();
                            ServerGUI.clients.setText("");
                        });
                        for(ServerThread client : Server.clientList) {
                            if(client.getPortOfClient() == Integer.parseInt(rows[1])){
                                client.shutDown();
                                Server.clientList.remove(client);
                            }
                            else {
                                Platform.runLater(() -> {
                                    ServerGUI.clients.appendText(client.name + "\n");
                                });
                            }
                        }
                    }
                }
                else if(rows[0].equals("message")) {
                    System.out.println("[Server] Received message from Client");
                    if(!Server.clientList.isEmpty()){
                        for(ServerThread client : Server.clientList) {
                            client.sendMessageToClient(rows[0] + "," + rows[1] + "," + rows[2]);
                        }
                    }
                }
            }
            catch(IOException e){
                System.out.println("[Server] error msg: ");
                e.printStackTrace();
            }
        }
    }

    public void shutDown(){
        try{
            this.isRunServerThread = false;
            this.toClient.close();
            this.fromClient.close();
            this.socket.close();
        } catch(IOException e){
            System.out.println("[Server] error msg: ");
            e.printStackTrace();
        }
    }

    private void sendMessageToClient(String msg){
        this.toClient.println(msg);
        System.out.println("[Server] Message send to Client.");
    }

    public int getPortOfClient(){
        return socket.getPort();
    }

    public boolean isRunServerThread(){
        return this.isRunServerThread;
    }

    public void setRunServerThread(boolean runServerThread){
        this.isRunServerThread = runServerThread;
    }
}
