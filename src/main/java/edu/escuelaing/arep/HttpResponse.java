package edu.escuelaing.arep;


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse() {
        this.statusCode = 200; 
        this.statusMessage = "OK"; 
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void send(PrintWriter out) {
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        headers.forEach((key, value) -> out.println(key + ": " + value));
        out.println();
        if (body != null) {
            out.println(body);
        }
        out.flush();
    }
}
