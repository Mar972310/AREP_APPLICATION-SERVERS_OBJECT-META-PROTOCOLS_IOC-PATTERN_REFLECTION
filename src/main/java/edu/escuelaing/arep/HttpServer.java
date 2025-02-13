package edu.escuelaing.arep;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.net.URL;


import edu.escuelaing.arep.annotation.GetMapping;
import edu.escuelaing.arep.annotation.PostMapping;
import edu.escuelaing.arep.annotation.RestController;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */

public class HttpServer {
    private static final int PORT = 35000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private static String ruta = "src/main/java/edu/escuelaing/arep/resources";
    static HashMap<String, Method> servicesGet = new HashMap<>();
    static HashMap<String,Method> servicesPost = new HashMap<>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer server = new HttpServer();
        System.out.println(ruta);
        server.loadComponents();
        server.startServer();
    }

    public void startServer() throws IOException, URISyntaxException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Failed to start server on port: " + PORT);
            throw e;
        }

        while (running) {
            try {
                System.out.println("Ready to receive ...");
                Socket clientSocket = serverSocket.accept();
                HttpRequestHandler requestHandler = new HttpRequestHandler(clientSocket,ruta);
                requestHandler.handlerRequest();
            } catch (IOException e) {
                if (!running) {
                    System.out.println("Server stopped.");
                    break;
                }
                e.printStackTrace();
            }
        }
    }

    public static void loadComponents() throws URISyntaxException  {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = "edu.escuelaing.arep.controller"; 
        URL resource = classLoader.getResource(packagePath.replace(".", "/")); 
    
        if (resource == null) {
            System.err.println("No se encontró el paquete: " + packagePath);
            return; 
        }
    
        File classes = new File(resource.toURI());
        if (classes.exists() && classes.isDirectory()){
            try {
                for (File file : classes.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        String className = packagePath + "." + file.getName().replace(".class", "");
                        Class<?> controllerClass = Class.forName(className);
                        if (!controllerClass.isAnnotationPresent(RestController.class)){
                            continue;
                        }
                        for(Method m: controllerClass.getDeclaredMethods()){
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                GetMapping a = m.getAnnotation(GetMapping.class);
                                servicesGet.put(a.value(), m);
                            } else if (m.isAnnotationPresent(PostMapping.class)) { // Corregido
                                PostMapping a = m.getAnnotation(PostMapping.class);
                                servicesPost.put(a.value(), m);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error cargando componentes", e);
            }
        } else {
            System.err.println("El directorio de clases no existe o no es válido.");
        }
    }
    
    
    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped successfully.");
            } catch (IOException e) {
                System.err.println("Error closing server: " + e.getMessage());
            }
        }
    }
}
