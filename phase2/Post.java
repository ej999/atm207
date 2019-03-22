package phase2;

import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Post {


    static void post() {
        try {
            String url = "https://csc207-atm-project.firebaseio.com/customers/";

            Scanner reader = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = reader.next();
            System.out.print("Enter password: ");
            String password = reader.next();

            url += username + ".json?print=pretty";

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.setRequestMethod("PUT");

            String userpass = "user" + ":" + "pass";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
            conn.setRequestProperty ("Authorization", basicAuth);

            String data =  "{\"password\":\"" + password +"\"}";
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.close();

            new InputStreamReader(conn.getInputStream());

            //read
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream(), "UTF-8"))) {
//                String parsed = null;
//                for (String line; (line = reader.readLine()) != null;) {
//                    System.out.println(line);
//                    parsed = line;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}