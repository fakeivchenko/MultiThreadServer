import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    static ExecutorService executorService = Executors.newFixedThreadPool(100);
    private static final int port = 3128;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(port);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                Socket client = server.accept();
                ClientsDB.add(new ClientHandler(client));
                executorService.execute(ClientsDB.get(client));
            } while (!server.isClosed());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void consolePrint(Object obj) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        System.out.println("[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + obj);
    }
}
