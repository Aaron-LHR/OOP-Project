package Client;

import java.io.IOException;

public class client1 {
    public static void main(String[] args) throws IOException {
        Client client = new Client("39.97.126.242",1111);
        client.Login();
        client.init();
    }
}
