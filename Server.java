package send.file.to.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {
    private static Socket socket;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Server");

        TextField textField = new TextField();
        textField.setMaxSize(400, 200);

        TextField resultTxt = new TextField();
        resultTxt.setMaxSize(200, 200);
        resultTxt.setDisable(true);

        MenuItem menuItem1 = new MenuItem("C:");
        MenuItem menuItem2 = new MenuItem("D:");
        MenuItem menuItem3 = new MenuItem("F:");
        Label label = new Label("Disk select?");

        MenuButton menuButton = new MenuButton("Options", null, menuItem1, menuItem2, menuItem3);
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText("C:\\Test\\");
            }
        });
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText("D:\\Test\\");
            }
        });
        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText("F:\\Test\\");
            }
        });
        VBox vBox = new VBox(menuButton, label, resultTxt);

        Scene scene = new Scene(vBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try {
                resultTxt.setText("");
                int port = 8888;
                ServerSocket serverSocket = new ServerSocket(port);
                resultTxt.setText("Server is listening...");

                while (true) {
                    socket = serverSocket.accept();
                    resultTxt.setText("Connected to server...");

                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    String savedDir = label.getText();
                    File file = new File(savedDir + inputStream.readUTF());

                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

                    byte[] buffer = new byte[1024];
                    while (inputStream.read(buffer) != -1) {
                        dataOutputStream.write(buffer);
                    }

                    dataOutputStream.flush();
                    dataOutputStream.close();
                    resultTxt.setText("Successfully received");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultTxt.setText("Failed");
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
