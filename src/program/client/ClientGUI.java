package program.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class ClientGUI extends Application{
    private Stage ChatStage;
    private Scene scene;
    private ClientThread clientThread;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Client");
        this.ChatStage = primaryStage;

        setScene();

        primaryStage.setScene(this.scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setScene(){
        Label labelRegister = new Label("Registration");
        labelRegister.setStyle("-fx-font-weight: bold;");
        Label name = new Label("Name:");
        labelRegister.setFont(new Font(18));

        TextField textAvatar = new TextField();

        Button buttonConnect = new Button("Send");

        buttonConnect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 1){
                        onButtonConnectClick(textAvatar.getText());
                    }
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(20);

        gridPane.add(labelRegister, 1, 0);
        gridPane.add(name, 0, 1);
        gridPane.add(textAvatar, 1, 1);
        gridPane.add(buttonConnect, 1, 2);

        this.scene = new Scene(gridPane, 300, 250);
    }

    @Override
    public void stop() throws Exception{
        if(this.clientThread != null){
            this.clientThread.closeConn();
        }
    }

    private void onButtonConnectClick(String name){
        AtomicReference<String> anwserFromServer = new AtomicReference<>("");
        try{
            this.clientThread = new ClientThread(name);
            this.clientThread.sendMsgToServer(name);
            anwserFromServer.set(this.clientThread.getMsgFromServer());
        }
        catch(Exception e){
            System.out.println("[Client] error msg: " + e.getMessage());
        }

        String[] temp = anwserFromServer.get().split(",");

        if(!anwserFromServer.get().isEmpty()){
            if(temp[0].equals("[Server] Client accepted")){
                Stage stageConnected = new Stage();
                stageConnected.setTitle("Registration");

                GridPane gridPaneButton = new GridPane();

                gridPaneButton.setAlignment(Pos.CENTER);
                gridPaneButton.setHgap(20);
                gridPaneButton.setVgap(10);

                Label label = new Label("Registration saucerful.");
                VBox vboxButton = new VBox();
                Button buttonClose = new Button("OK");

                buttonClose.setOnAction(event -> {
                    TextArea messages = new TextArea();
                    messages.setEditable(false);
                    messages.setFont(new Font(15));
                    messages.setPrefWidth(300);
                    messages.setPrefHeight(220);

                    Label labelMsg = new Label("Message: ");
                    TextField textFieldMsgToServer = new TextField();
                    Button buttonClick = new Button("Send");
                    buttonClick.setMinWidth(97);

                    anwserFromServer.set("");

                    buttonClick.setOnAction(event1 -> {
                        try{
                            this.clientThread.sendMsgToServer("message," + this.clientThread.getName() + "," + textFieldMsgToServer.getText());
                            textFieldMsgToServer.setText("");
                        }
                        catch(Exception e){
                            System.out.println("[Client] error msg: " + e.getMessage());
                        }
                    });

                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(labelMsg, textFieldMsgToServer,buttonClick);

                    VBox vbox = new VBox();
                    vbox.getChildren().addAll(messages, hBox);

                    Scene sceneChat = new Scene(vbox, 300, 250);
                    this.ChatStage.setScene(sceneChat);
                    stageConnected.close();
                });

                vboxButton.getChildren().addAll(label, buttonClose);
                gridPaneButton.add(vboxButton, 1, 0);

                Scene sceneConnected = new Scene(gridPaneButton, 240, 100);
                stageConnected.setScene(sceneConnected);
                stageConnected.showAndWait();
            }
        }
        else{
            Stage stageFailedConnection = new Stage();
            stageFailedConnection.setTitle("Registracija");

            GridPane gridPaneButton = new GridPane();
            gridPaneButton.setAlignment(Pos.CENTER);

            Label label = new Label("Registracija nije uspela.");
            VBox vboxButton = new VBox();
            Button buttonClose = new Button("OK");

            buttonClose.setOnAction(event -> {
                stageFailedConnection.close();
            });

            vboxButton.getChildren().addAll(label, buttonClose);
            gridPaneButton.setHgap(20);
            gridPaneButton.setVgap(10);
            gridPaneButton.add(vboxButton, 1, 0);

            Scene sceneFailedConnection = new Scene(gridPaneButton, 240, 100);
            stageFailedConnection.setScene(sceneFailedConnection);
            stageFailedConnection.showAndWait();
        }
    }
}
