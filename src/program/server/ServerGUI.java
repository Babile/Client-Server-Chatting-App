package program.server;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ServerGUI extends Application{
    private Server server;
    private Scene sceneServer;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Server");

        setScene();

        primaryStage.setScene(this.sceneServer);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        if(this.server != null){
            this.server.setRunServer(false);
            this.server.closeConn();
        }
    }

    private void setScene(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);

        Button buttonStart = new Button("Start Server");
        buttonStart.setOnAction(event -> {
            startServer();
        });
        Button buttonStop = new Button("Stop Server");
        buttonStop.setOnAction(event -> {
            stopServer();
        });

        hBox.getChildren().addAll(buttonStart, buttonStop);

        this.sceneServer = new Scene(hBox, 300, 250);
    }

    private void startServer(){
        try{
            this.server = new Server(9000);
            Thread thread = new Thread(this.server);
            thread.start();
        }
        catch(Exception e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }

    private void stopServer(){
        if(this.server != null){
            this.server.setRunServer(false);
            this.server.closeConn();
        }
    }
}