package src.test1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CallController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    private AudioCallHandler audioCallHandler;


    @FXML
    public void initialize() {
        // Địa chỉ IP của server, cổng gửi có thể là cố định
//        String serverIp = "127.0.0.1";
        String serverIp = "192.168.206.2";

        btnStart.setOnAction(event -> startCall(serverIp));
        btnStop.setOnAction(event -> stopCall());
    }

    private int receiverID;
    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }


    private void startCall(String serverIp) {
        try {
            // Gửi yêu cầu gọi tới server
            Socket socket = new Socket(serverIp, 10397);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu gọi tới người nhận
            out.println("START_CALL:" + receiverID);
            System.out.println("day la o user START_CALL:" + receiverID);

            // Đợi phản hồi từ server (thành công hoặc thất bại)
            String response = in.readLine();
            if (response.startsWith("CALL_INFO:")) {
                String[] parts = response.split(":");
                int localReceivePort = Integer.parseInt(parts[1]);
                String remoteIP = parts[2];
                int remoteSendPort = Integer.parseInt(parts[3]);

                System.out.println("Local UDP Port: " + localReceivePort);
                System.out.println("Remote IP: " + remoteIP + ", Remote UDP Port: " + remoteSendPort);

                audioCallHandler = new AudioCallHandler(remoteIP, remoteSendPort, localReceivePort);
                audioCallHandler.startCall();

                audioCallHandler.sendDummyPacket();

                btnStart.setDisable(true);
                btnStop.setDisable(false);
            } else {
                System.out.println("Call failed: " + response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void stopCall() {
        if (audioCallHandler != null) {
            audioCallHandler.stopCall();
            btnStart.setDisable(false);
            btnStop.setDisable(true);
        }
    }


}
