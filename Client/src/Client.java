import java.io.*;
import java.net.Socket;

public class Client {
    private static final int port = 3128;

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            try (Socket server = new Socket("localhost", port);
                 DataOutputStream output = new DataOutputStream(server.getOutputStream());
                 DataInputStream input = new DataInputStream(server.getInputStream());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {
                i = 1;
                do {
                    do {
                        if (reader.ready()) {
                            String str = reader.readLine();
                            output.writeUTF(str);
                            output.flush();
                            if (str.equalsIgnoreCase("logout")) System.exit(0);
                        }
                    } while (input.available() <= 0);
                        if (input.available() >= 0) {
                            System.out.print(input.readUTF());
                        }
                } while (!server.isOutputShutdown());
            } catch (IOException e) { System.out.println("Can't connect to the server, trying again... [" + i + "/10]"); }
        }
        System.out.println("Failed to connect, server not responding.");
    }
}
