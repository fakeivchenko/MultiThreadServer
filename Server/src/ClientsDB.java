import java.net.Socket;
import java.util.ArrayList;

public class ClientsDB {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static boolean add(ClientHandler e) {
        if (clients.add(e)) return true;
        else return false;
    }

    public static boolean remove(ClientHandler e) {
        if (clients.remove(e)) return true;
        else return false;
    }

    public static ClientHandler get(Socket clientDialog) {
        for (ClientHandler client : clients) {
            if(client.getClientDialog() == clientDialog) return client;
        }
        return null;
    }

    public static int getSize() { return clients.size(); }

    public static ClientHandler[] toArray() {
        ClientHandler[] array = new ClientHandler[clients.size()];
        clients.toArray(array);
        return array;
    }

    public static String[] getLoginsList() {
        ArrayList<String> tempList = new ArrayList<>();
        for (ClientHandler client : clients) {
            if (client.getLogin() != null) tempList.add(client.getLogin());
        }
        String[] logins = new String[tempList.size()];
        tempList.toArray(logins);
        return logins;
    }
}
