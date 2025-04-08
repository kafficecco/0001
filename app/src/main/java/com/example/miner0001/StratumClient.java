package com.example.miner0001;

import java.io.*;
import java.net.*;
import android.webkit.WebView;

public class StratumClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private WebView webView;

    public StratumClient(WebView webView) {
        this.webView = webView;
    }

    public void connect(String pool, String user, String password) {
        try {
            String[] poolData = pool.split(":");
            String host = poolData[0].replace("stratum+tcp://", "");
            int port = Integer.parseInt(poolData[1]);
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("{\"id\": 1, \"method\": \"mining.subscribe\", \"params\": [\"cpuminer-multi-js/1.0\"]}");
            out.println("{\"id\": 2, \"method\": \"mining.authorize\", \"params\": [\"" + user + "\", \"" + password + "\"]}");

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        webView.post(() -> webView.evaluateJavascript("logMessage('Respuesta: " + line + "');", null));
                        if (line.contains("\"result\": true")) {
                            webView.post(() -> webView.evaluateJavascript("logMessage('Conexión establecida');", null));
                        }
                    }
                } catch (IOException e) {
                    webView.post(() -> webView.evaluateJavascript("logMessage('Error: " + e.getMessage() + "');", null));
                }
            }).start();

        } catch (IOException e) {
            webView.post(() -> webView.evaluateJavascript("logMessage('Error de conexión: " + e.getMessage() + "');", null));
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            webView.post(() -> webView.evaluateJavascript("logMessage('Minería detenida');", null));
        } catch (IOException e) {
            webView.post(() -> webView.evaluateJavascript("logMessage('Error al desconectar: " + e.getMessage() + "');", null));
        }
    }
}
