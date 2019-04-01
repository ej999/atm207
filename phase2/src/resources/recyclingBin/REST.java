//package recyclingBin;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//import java.util.Scanner;
//
//
///**
// * A class that interacts with our cloud database - Firebase Database, by using its URL as a REST1 endpoint.
// */
//class REST {
//
//    private static String parsed = null;
//    private ScriptEngine engine;
//
//    REST() {
//        initEngine();
//    }
//
//    static void saveData() throws IOException {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Enter username: ");
//        String username = reader.next();
//        System.out.print("Enter password: ");
//        String password = reader.next();
//
//
//        URL url = new URL("https://csc207-atm-project.firebaseio.com/" + username + ".json?print=pretty");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
////        conn.setRequestMethod("PUT");
////
////        String userpass = "user" + ":" + "pass";
////        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
////        conn.setRequestProperty ("Authorization", basicAuth);
//
//        String data = "{\"password\":\"" + password + "\"}";
//        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
//        out.write(data);
//        out.close();
//    }
//
//    private void initEngine() {
//        ScriptEngineManager sem = new ScriptEngineManager();
//        this.engine = sem.getEngineByName("javascript");
//    }
//
//    void parseJson() throws IOException, ScriptException {
//        String script = "Java.asJSONCompatible(" + parsed + ")";
//        Map result = (Map) this.engine.eval(script);
//        result.forEach((t, u) -> {
//            System.out.println(t);
//        });
//    }
//
//    void retrieveData() throws IOException {
//        // Firebase Database URL; add argument "print=pretty" for clean format.
//        URL url = new URL("https://csc207-atm-project.firebaseio.com/.json");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
//        String parsed = reader.readLine();
//        System.out.println(parsed);
//        System.out.println(parsed.substring(1, parsed.length() - 1));
//
//        REST.parsed = parsed;
//
//    }
//}