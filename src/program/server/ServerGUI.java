package program.server;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ServerGUI extends Application{
    private Server server;
    private Scene sceneServer;
    public static TextArea clients;

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
            this.server.shutDown();
        }
    }

    private void setScene(){
        clients = new TextArea();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);

        Button buttonStop = new Button("Stop Server");
        Button buttonStart = new Button("Start Server");
        buttonStop.setDisable(true);

        buttonStart.setOnAction(event -> {
            buttonStop.setDisable(false);
            buttonStart.setDisable(true);
            startServer();
        });

        buttonStop.setOnAction(event -> {
            buttonStop.setDisable(true);
            buttonStart.setDisable(false);
            stopServer();
        });

        hBox.getChildren().addAll(buttonStart, buttonStop);

        clients.setEditable(false);
        clients.setFont(new Font(15));
        clients.setPrefWidth(100);
        clients.setPrefHeight(120);

        Label listClients = new Label("Connected clients:");
        listClients.setFont(new Font(15));

        GridPane gridPaneButton = new GridPane();

        gridPaneButton.setAlignment(Pos.CENTER);
        gridPaneButton.setHgap(20);
        gridPaneButton.setVgap(10);

        gridPaneButton.add(hBox, 0, 0);
        gridPaneButton.add(listClients, 0, 1);
        gridPaneButton.add(clients, 0, 2);

        this.sceneServer = new Scene(gridPaneButton, 300, 250);
    }

    private void startServer(){
        try{
            this.server = new Server(9000);
            Thread thread = new Thread(this.server);
            thread.setDaemon(true);
            thread.start();
        }
        catch(Exception e){
            System.out.println("[Server] error msg: " + e.getMessage());
        }
    }

    private void stopServer(){
        if(this.server != null){
            this.server.setRunServer(false);
            this.server.shutDown();
        }
    }
}