package src.test1;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AudioCallHandler {

    private final String serverIp;
    private final int sendPort;
    private final int receivePort;  // Cổng nhận
    private boolean isCalling;

    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;

    public AudioCallHandler(String serverIp, int sendPort, int receivePort) {
        this.serverIp = serverIp;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
        this.isCalling = false;
    }
    public void startCall() {
        isCalling = true;
        sendDummyPacket();
        startSending();
        startReceiving();
    }

    public void stopCall() {
        isCalling = false;
        if (audioInput != null && audioInput.isOpen()) {
            audioInput.stop();
            audioInput.close();
        }
        if (audioOutput != null && audioOutput.isOpen()) {
            audioOutput.stop();
            audioOutput.close();
        }
        if (sendSocket != null && !sendSocket.isClosed()) sendSocket.close();
        if (receiveSocket != null && !receiveSocket.isClosed()) receiveSocket.close();
    }

    private void startSending() {
        new Thread(() -> {
            try {
                sendSocket = new DatagramSocket();
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                audioInput = (TargetDataLine) AudioSystem.getLine(info);
                audioInput.open(format);
                audioInput.start();

                byte[] buffer = new byte[1024];
                InetAddress serverAddress = InetAddress.getByName(serverIp);

                while (isCalling) {
                    int bytesRead = audioInput.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        DatagramPacket packet = new DatagramPacket(buffer, bytesRead, serverAddress, sendPort);
                        sendSocket.send(packet);

                        System.out.println("Sent packet size:" + bytesRead);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startReceiving() {
        new Thread(() -> {
            try {
                receiveSocket = new DatagramSocket(receivePort);  // Sử dụng cổng nhận đã cung cấp
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                audioOutput = (SourceDataLine) AudioSystem.getLine(info);
                audioOutput.open(format);
                audioOutput.start();

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Log trong startReceiving
                System.out.println("Listening on port: " + receivePort);
                System.out.println("Received packet from: " + packet.getAddress() + ":" + packet.getPort());


                while (isCalling) {
                    receiveSocket.receive(packet);
                    audioOutput.write(packet.getData(), 0, packet.getLength());
                    System.out.println("Received packet size:" + packet.getLength());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 44100.0f;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void sendDummyPacket() {
        try {
            sendSocket = new DatagramSocket(); // Tạo socket gửi
            byte[] dummyData = "INIT".getBytes();
            DatagramPacket dummyPacket = new DatagramPacket(dummyData, dummyData.length, InetAddress.getByName(serverIp), sendPort);
            sendSocket.send(dummyPacket);
            System.out.println("Dummy packet sent to " + serverIp + ":" + sendPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
