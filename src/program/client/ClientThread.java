package program.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread{
    private Socket socket;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private int ID;

    public ClientThread(){
        try{
            this.socket = new Socket("127.0.0.1", 9000);
            this.fromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.toServer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch(IOException e){
            System.out.println("[Client] error msg: " + e.getMessage());
        }
    }

    public int getID(){
        return this.ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public void closeConn(){
        try{
            if(this.fromServer != null || this.toServer != null || this.socket != null){
                this.fromServer.close();
                this.toServer.close();
                this.socket.close();
            }
        }
        catch(IOException e){
            System.out.println("[Client] error msg: " + e.getMessage());
        }
    }

    public void sendMsgToServer(String msg){
        System.out.println("[Client] Message send to Server.");
        this.toServer.println(msg);
    }

    public String getMsgFromServer(){
        try{
            System.out.println("[Client] Received message from Server.");
            return this.fromServer.readLine();
        } catch(IOException e){
            return "[Client] error msg: " + e.getMessage();
        }
    }
}
