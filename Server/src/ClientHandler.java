import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientDialog;
    private DataInputStream input;
    private DataOutputStream output;
    private String clientName;
    private String login;

    public Socket getClientDialog() { return clientDialog; }

    public DataInputStream getInput() { return input; }

    public DataOutputStream getOutput() { return output; }

    public String getLogin() { return login; }


    public ClientHandler(Socket clientDialog) {
        this.clientDialog = clientDialog;
    }

    @Override
    public void run() {
        try {
            input = new DataInputStream(clientDialog.getInputStream());
            output = new DataOutputStream(clientDialog.getOutputStream());
            clientName = clientDialog.getLocalAddress().toString() + ':' + clientDialog.getPort();
            MultiThreadServer.consolePrint("Client " + clientName + " was connected!");
            do {
                String cmd = input.readUTF();
                MultiThreadServer.consolePrint("Client " + clientName + " printed: " + cmd);
                if (cmd.equalsIgnoreCase("login")) loginHandler();
                else commandHandler(cmd);
                output.flush();
            } while (!clientDialog.isClosed());
            input.close();
            output.close();
            clientDialog.close();
        } catch (IOException e) {
            ClientsDB.remove(this);
            MultiThreadServer.consolePrint("Client " + clientName + " was disconnected! (" + e.getMessage() + ").");
        }
    }

    private void loginHandler() throws IOException {
        if (this.login == null) {
            output.writeUTF("Введите свой логин: ");
            login = input.readUTF();
            if (login.isEmpty()) { output.writeUTF("Введен неверный логин! Попробуйте еще раз.\n"); }
            this.login = login;
            output.writeUTF("Логин успешно сохранен!\n");
            writeInChat(login + " присоединился к нам!\n");
        }
        else output.writeUTF("Повторный вход не требуется.\n");
    }

    private void writeInChat(String str) throws IOException {
        for (ClientHandler client : ClientsDB.toArray()) {
            if(client.login != null) client.getOutput().writeUTF(str);
        }
    }

    private void commandHandler(String cmd) throws IOException {
        if (this.login == null) { output.writeUTF("Сначала нужно войти (команда login).\n"); return; }
        switch (cmd.toLowerCase()) {
            case "list":
                String[] logins = ClientsDB.getLoginsList();
                output.writeUTF("Сейчас на сервере " + logins.length + " активных пользователя:\n");
                for (String login : logins) {
                    output.writeUTF(login + '\n');
                }
                break;
            case "message":
                output.writeUTF("Введите свое сообщение: ");
                writeInChat(this.login + ": " + input.readUTF() + '\n');
                break;
            case "logout":
                input.close();
                output.close();
                clientDialog.close();
                ClientsDB.remove(this);
                MultiThreadServer.consolePrint("Client " + clientName + " was disconnected! (logout).");
                break;
        }
    }
}
