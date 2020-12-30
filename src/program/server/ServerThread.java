package program.server;

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
    private static int IDClient = 1;

    public ServerThread(Socket socket){
        this.isRunServerThread = true;
        this.socket = socket;
        try{
            this.fromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.toClient = new PrintWriter(this.socket.getOutputStream(), true);
        }
        catch(IOException e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }

    @Override
    public void run(){
        boolean register = false;
        while(this.isRunServerThread){
            try{
                String[] rows = this.fromClient.readLine().split(",");
                System.out.println("[Server] Received message from Client");

                if(rows[0].equals("exit")){
                    System.out.println("[Server] Client exits.");
                    this.isRunServerThread = false;
                }
                else if(!register){
                    register = true;
                    sendMessageToClient("[Server] Client accepted" + "," + IDClient++);
                }
                else {

                }
            }
            catch(IOException e){
                System.out.println("[Server] error msg: " + e.getMessage());
            }
        }
        closeConn();
    }

    public void closeConn(){
        try{
            this.isRunServerThread = false;
            this.toClient.close();
            this.fromClient.close();
            this.socket.close();
        } catch(IOException e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }

    private void sendMessageToClient(String msg){
        this.toClient.println(msg);
        System.out.println("[Server] Message send to Client.");
    }
}
