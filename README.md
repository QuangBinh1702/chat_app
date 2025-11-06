# Chat App with Voice Call

A real-time chat application with voice calling capabilities built using Java and JavaFX.

## Features

- 💬 Real-time text chat between users
- 🎤 Voice call functionality
- 👤 User authentication (Register/Login)
- 😊 Emoji support
- 🌙 Dark mode UI
- 💾 Message history storage

## Technologies Used

- **Backend:** Java 21, Socket Programming
- **Frontend:** JavaFX 21, FXML
- **Database:** SQL
- **Build Tool:** Maven
- **Architecture:** MVC Pattern

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── src/test1/
│   │       ├── controllers/
│   │       ├── AudioCallHandler.java
│   │       ├── CallController.java
│   │       ├── Controller.java
│   │       ├── loginController.java
│   │       ├── Main.java
│   │       ├── registerController.java
│   │       └── Server.java
│   └── resources/
│       └── src/test1/
│           ├── database/
│           ├── emoji/
│           ├── images/
│           ├── styles/
│           └── views/
```

## Prerequisites

- Java Development Kit (JDK) 21
- Maven
- MySQL Server

## Setup and Installation

1. Clone the repository:
```bash
git clone https://github.com/QuangBinh1702/chat_app.git
```

2. Import the database schema:
```bash
mysql -u your_username -p < src/main/resources/src/test1/database/chatapp.sql
```

3. Build the project:
```bash
mvn clean install
```

4. Run the server:
```bash
mvn exec:java -Dexec.mainClass="src.test1.Server"
```

5. Run the client application:
```bash
mvn javafx:run
```

## Screenshots

[You can add screenshots of your application here]

## Contributing

Feel free to fork the project and submit pull requests for any improvements.

## License

[Choose an appropriate license for your project]

## Authors

- [@QuangBinh1702](https://github.com/QuangBinh1702)

## Acknowledgments

- Thanks to everyone who contributed to this project
- Special thanks to the JavaFX community for their resources and support
