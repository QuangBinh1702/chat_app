package src.test1;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

public class Server {
    private static final int PORT = 10397;
    private static Map<Integer, ClientHandler> clientMap = new HashMap<>();

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try {
            Properties props = new Properties();
            props.load(Server.class.getResourceAsStream("/config.properties"));
            DB_URL = props.getProperty("DB_URL") + "?useSSL=false";
            DB_USER = props.getProperty("DB_USER");
            DB_PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int userID;


        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);


                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("USER_ID:")) {
                        userID = Integer.parseInt(message.split(":")[1]);
                        System.out.println("UserID:" + userID);
                        synchronized (clientMap) {
                            clientMap.put(userID, this);
                        }
                    } else if (message.startsWith("SELECT_USER")) {
                        // Định dạng của nó là : SELECT_USER:<receiverID>
                        int receiverID = Integer.parseInt(message.split(":")[1]);
                        sendChatHistory(userID, receiverID);
                    } else if (message.startsWith("START_CALL")) {
                        handleStartCall(message);
                    } else {
                        handleMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientMap) {
                    clientMap.remove(userID);
                }
            }
        }

        private void handleMessage(String message) {
            System.out.println(message);
            // Định dạng tin nhắn: "senderID:receiverID:content"
            String[] parts = message.split(":", 3);
            System.out.println(Arrays.toString(parts));
            int senderID = Integer.parseInt(parts[0]);
            int receiverID = Integer.parseInt(parts[1]);
            String content = parts[2];

            if (content.startsWith("image:")) {
                String imagePath = content.substring(6);
                saveMessageToDatabase(senderID, receiverID, imagePath);

                synchronized (clientMap) {
                    ClientHandler receiver = clientMap.get(receiverID);
                    if (receiver != null && receiverID != senderID) {
                        // Gửi hình ảnh đến client
                        // Định dạng gửi hình ảnh : senderID:receiverID:image:imagePath
                        receiver.sendMessage(senderID + ":" + receiverID + ":image:" + imagePath);
                    } else {
                        System.out.println("User " + receiverID + " not connected.");
                    }
                }
            } else if (content.startsWith("emotion:")) {
                String emoji = content.substring(8);
                saveMessageToDatabase(senderID, receiverID, emoji);

                synchronized (clientMap) {
                    ClientHandler receiver = clientMap.get(receiverID);
                    if (receiver != null && receiverID != senderID) {
                        // Gửi emoji đến client
                        // Định dạng gửi emotion : senderID:receiverID:emotion:emoji
                        receiver.sendMessage(senderID + ":" + receiverID + ":emotion:" + emoji);
                    } else {
                        System.out.println("User " + receiverID + " not connected.");
                    }
                }

            } else if (content.startsWith("file:")) {
                String filePath = content.substring(5);
                saveMessageToDatabase(senderID, receiverID, filePath);

                synchronized (clientMap) {
                    ClientHandler receiver = clientMap.get(receiverID);
                    if (receiver != null && receiverID != senderID) {
                        // Gửi emoji đến client
                        // Định dạng gửi file : senderID:receiverID:file:filePath
                        receiver.sendMessage(senderID + ":" + receiverID + ":file:" + filePath);

                    } else {
                        System.out.println("User " + receiverID + " not connected.");
                    }
                }
            } else {
                saveMessageToDatabase(senderID, receiverID, content);

                // Gửi tin nhắn đến người nhận
                synchronized (clientMap) {
                    ClientHandler receiver = clientMap.get(receiverID);
                    System.out.println("senderID: " + senderID + " receiverID: " + receiverID + " content: " + content);
                    if (receiver != null && receiverID != senderID) {
                        System.out.println(senderID + ":" + receiverID);
                        receiver.sendMessage(senderID + ":" + content);
                    } else {
                        System.out.println("User " + receiverID + " not connected.");
                    }
                }
            }
        }


        private void handleStartCall(String message) {
            System.out.println("Received START_CALL request: " + message);
            int receiverID = Integer.parseInt(message.split(":")[1]);

            try {
                DatagramSocket tempSocket = new DatagramSocket(0);
                int callerReceivePort = tempSocket.getLocalPort();
                tempSocket.close();

                ClientHandler receiver = clientMap.get(receiverID);
                if (receiver != null) {
                    String callerIP = socket.getInetAddress().getHostAddress();
                    String receiverIP = receiver.socket.getInetAddress().getHostAddress();

                    // Gửi thông tin đến caller
                    sendMessage("CALL_INFO:" + callerReceivePort + ":" + receiverIP + ":" + callerReceivePort);

                    // Gửi thông tin đến receiver
                    receiver.sendMessage("CALL_INFO:" + callerReceivePort + ":" + callerIP + ":" + callerReceivePort);
                } else {
                    sendMessage("ERROR: Receiver not available.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void sendMessage(String message) {
            out.println(message);
        }

        private void saveMessageToDatabase(int senderID, int receiverID, String content) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "insert into message (SenderID, ReceiverID, MessageContent) values (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, senderID);
                preparedStatement.setInt(2, receiverID);
                preparedStatement.setString(3, content);
                preparedStatement.executeUpdate();
                System.out.println("Message saved to database.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private List<String> getMessagesFromDatabase(int senderID, int receiverID) {
            List<String> messages = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "select SenderID, MessageContent from message " +
                        "where (SenderID = ? and ReceiverID = ?) or (SenderID = ? and ReceiverID = ?)" +
                        " order by TimeStamp ASC";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, senderID);
                preparedStatement.setInt(2, receiverID);
                preparedStatement.setInt(3, receiverID);
                preparedStatement.setInt(4, senderID);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int sender = resultSet.getInt("SenderID");
                    String messageContent = resultSet.getString("MessageContent");
                    messages.add(sender + ":" + messageContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(messages);
            return messages;
        }


        private void sendChatHistory(int senderID, int receiverID) {
            List<String> messages = getMessagesFromDatabase(senderID, receiverID);
            for (String message : messages) {

                out.println("HISTORY:" + message);
            }
            out.println("END_HISTORY");
        }


    }
}
