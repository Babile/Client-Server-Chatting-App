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
    private String name;

    public ClientThread(String name){
        try{
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

    public void getMsgFromServer(){
        try{
            System.out.println("[Client] Message receive from Server.");
            this.fromServer.readLine();
        } catch(IOException e){
            System.out.println("[Client] error msg: ");
            e.printStackTrace();
        }
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
