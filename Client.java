package send.file.to.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Client");
        Label sourceLable = new Label("Source File");
        TextField sourceTxt = new TextField();
        sourceTxt.setMaxSize(400, 200);

        Label targetIPLable = new Label("IP Destination");
        TextField targetIPTxt = new TextField();
        targetIPTxt.setMaxSize(400, 200);

        Button sendBtn = new Button("Send");

        Label resultLable = new Label("Result");
        TextField result = new TextField();
        result.setMaxSize(200, 200);
        result.setDisable(true);

        Button clearBtn = new Button("Clear");

        VBox vBox = new VBox(sourceLable, sourceTxt, targetIPLable, targetIPTxt, sendBtn, resultLable, result, clearBtn);
        Scene scene = new Scene(vBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ipStr = targetIPTxt.getText();
                try {
                    String host = ipStr;
                    int port = 8888;

                    Socket socket = new Socket(ipStr, port);

                    File file = new File(sourceTxt.getText());
                    DataInputStream reader = new DataInputStream(new FileInputStream(file));

                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(file.getName());

                    byte[] buffer = new byte[1024];
                    while ((reader.read(buffer)) != -1) {
                        outputStream.write(buffer);
                    }
                    reader.close();
                    outputStream.close();
                    result.setText("Successfully sent");

                } catch (IOException ex) {
                    ex.printStackTrace();
                    result.setText("Failed");
                }
            }
        });

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sourceTxt.setText("");
                targetIPTxt.setText("");
                result.setText("");
            }
        });
    }

    public static void main(String[] args) throws IOException {

        Application.launch(args);
    }


}
