
package edu.escuelaing.arep;
import java.net.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import edu.escuelaing.arep.annotation.RequestParam;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */
public class HttpRequestHandler {
    private final Socket clientSocket;
    private String ruta;
    PrintWriter out ;
    BufferedReader in ;
    BufferedOutputStream bodyOut ;
    
    
    public HttpRequestHandler(Socket clientSocket, String ruta){
        this.clientSocket = clientSocket;
        this.ruta = ruta;
    }
    
    public void handlerRequest() throws IOException, URISyntaxException{
        
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        bodyOut = new BufferedOutputStream(clientSocket.getOutputStream());
        
        String inputLine;
        boolean isFirstLine = true;
        String file = "";
        String method = "";
        while ((inputLine = in.readLine()) != null) {
            if (isFirstLine) {
                file = inputLine.split(" ")[1];
                method = inputLine.split(" ")[0];
                isFirstLine = false; 
            }
            if (!in.ready()) {
                break;
            }
        }
        rediretMethod(method,file);
        out.close();
        bodyOut.close();
        in.close();
        clientSocket.close();  
    }  

    //falta arreglar esto :) y html
    public void rediretMethod(String method, String file) throws IOException, URISyntaxException{
        URI requestFile = new URI(file);
        String fileRequest = requestFile.getPath();
        String queryRequest = Optional.ofNullable(requestFile.getQuery()).orElse("");
        String contentType = getContentType(fileRequest);
        if(fileRequest.startsWith("/app")){
            handlerRequestApp(method,fileRequest,queryRequest);
        }else{
            requestStaticHandler(ruta + file, contentType); 
        }
    }
    
    public void handlerRequestApp(String method, String fileRequest, String queryRequest){
        String endpoint = fileRequest.substring(4);
        Method service = null;
        String code = "404";
        String outputLine = " ";
        System.out.println(endpoint);
        if(method.equals("GET")){
            service = HttpServer.servicesGet.get(endpoint);
            code = "200";
        }else if(method.equals("POST")){
            service = HttpServer.servicesPost.get(endpoint);
            code = "201";
        }
        if(service != null){
            outputLine = invokeHandler(service,queryRequest);
            outputLine = "{\"response\":\"" + outputLine + "\"}";                
        }else{
            outputLine = "{\"response\":Method not supported}"; ;
            code = "404";
        }
        String responseHeader = requestHeader("text/json", outputLine.length(), code);
        out.println(responseHeader);  
        out.flush();
        out.println(outputLine);  
        out.flush();
    }

    public String invokeHandler(Method service, String query){
        String response = "";
        try {
            Map<String, String> queryParams = queryParams(query);
            Object[] parameters = new Object[service.getParameterCount()];
            Class<?>[] parameterTypes = service.getParameterTypes();
            Annotation[][] annotations = service.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) annotation;
                        String paramName = requestParam.value();
                        String paramValue = queryParams.get(paramName);
                        if (paramValue == null || paramValue.isEmpty()) {
                            paramValue = requestParam.defaultValue();
                        }
                        if (paramValue != null) {
                            if (parameterTypes[i] == int.class) {
                                parameters[i] = Integer.parseInt(paramValue);
                            } else if (parameterTypes[i] == double.class) {
                                parameters[i] = Double.parseDouble(paramValue);
                            } else {
                                parameters[i] = paramValue;
                            }
                        } else {
                            parameters[i] = null;
                        }
                    }
                }
            }
            Object instance = service.getDeclaringClass().getDeclaredConstructor().newInstance();
            response = (String) service.invoke(instance, parameters);
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error executing service method: " + e.getMessage());
        }
        return response;
    }
    

    public Map<String, String> queryParams(String query){
        Map<String, String> queryParams = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return queryParams;
        }
        String[] values = query.split("&");
        for(String s: values){
            String[] valueM = s.split("=",2);
            String key = valueM[0].trim();
            String value = valueM.length > 1 ? valueM[1].trim() : "";
            queryParams.put(key, value);
        }
        return queryParams;
    }

    public void requestStaticHandler(String file, String contentType) throws IOException{
        if(fileExists(file)){
            byte[] requestfile = readFileData(file);
            String requestHeader = requestHeader(contentType,requestfile.length,"200");
            out.println(requestHeader);
            out.flush();
            bodyOut.write(requestfile);
            bodyOut.flush();
        }else{
            out.println(notFound());
        } 
    }

    public byte[] readFileData(String requestFile) throws IOException {
        File file = new File(requestFile);

        if (file.isDirectory()) {
            throw new FileNotFoundException("La ruta solicitada es un directorio, no un archivo: " + requestFile);
        }
        if (!fileExists(requestFile)) {
            throw new FileNotFoundException("Archivo no encontrado: " + requestFile);
        }
        
        int fileLength = (int) file.length();
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
        return fileData;
    }
    

    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
    
    public String getContentType(String requestFile){
        String contentType = " ";
        if (requestFile.endsWith(".html")){
            contentType = "text/html";
        }else if (requestFile.endsWith(".css")){
            contentType = "text/css";
        }else if (requestFile.endsWith(".js")){
            contentType = "application/javascript";
        }else if (requestFile.endsWith(".png")){
            contentType = "image/png";
        }else if (requestFile.endsWith(".jpg") || requestFile.endsWith(".jpeg")){
            contentType = "image/jpeg";
        }else{
            contentType = "text/plain";
        }
        return contentType;  
    }
    
    public String requestHeader(String contentType, int contentLength, String code){
        String outHeader = "HTTP/1.1 " + code + " OK\r\n"
                    + "Content-Type: " + contentType + "\r\n"
                    + "Content-Length: " + contentLength + "\r\n";     
        return outHeader;
    }
    
    public static String notFound(){
        String outputLine = "HTTP/1.1. 404 Not Found\r\n"
                        +"Content-type: text/html\r\n"
                        +"\r\n"
                        +"<!DOCTYPE html>\n"
                        + "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <title>404 - File Not Found</title>\n"
                        + "    <style>\n"
                        + "        body {\n"
                        + "            font-family: Arial, sans-serif;\n"
                        + "            background-color: #ded5fa;\n"
                        + "            display: flex;\n"
                        + "            justify-content: center;\n"
                        + "            align-items: center;\n"
                        + "            height: 100vh;\n"
                        + "            margin: 0;\n"
                        + "        }\n"
                        + "        .container {\n"
                        + "            text-align: center;\n"
                        + "            background-color: #c2aaeb;\n"
                        + "            padding: 50px;\n"
                        + "            border-radius: 8px;\n"
                        + "            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\n"
                        + "        }\n"
                        + "        h1 {\n"
                        + "            font-size: 5em;\n"
                        + "            color: #f7755b;\n"
                        + "        }\n"
                        + "        p {\n"
                        + "            font-size: 1.2em;\n"
                        + "            color: #555;\n"
                        + "        }\n"
                        + "    </style>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "    <div class=\"container\">\n"
                        + "        <h1>404</h1>\n"
                        + "        <p>Oops! The file you're looking for cannot be found.</p>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";
        return outputLine;
    }    
}
