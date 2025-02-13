package WebServerTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import edu.escuelaing.arep.HttpServer;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */

public class WebServerTest {

    
    private static final String URL = "http://localhost:35000/";
    private static HttpServer server;
    private static Thread serverThread;


    @BeforeClass
    public static void setUp() {
        try {
            server = new HttpServer();
            serverThread = new Thread(() -> {
                try {
                    server.startServer();
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();
            Thread.sleep(1000); 
            System.out.println("Servidor iniciado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void shouldLoadStaticFileHtml() throws Exception {

        String file = "index.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileHtml() throws Exception {
        String file = "web.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileCss() throws Exception {
   
        String file = "style.css";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileCss() throws Exception {
    
        String file = "styles.css";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileJs() throws Exception {
        String file = "script.js";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileJs() throws Exception {
        String file = "prueba.js";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticImagePNG() throws Exception {
        String file = "imagen1.png";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticImageJPG() throws Exception {
        
        String file = "imagen2.jpg";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticImagePNG() throws Exception {
        String file = "imagen8.png";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticImageJPG() throws Exception {
        String file = "imagen5.jpg";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithQuery() throws Exception {
        String file = "app/greeting?name=maria";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Hello maria !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithoutQuery() throws Exception {
        String file = "app/greeting";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Hello world !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadGreetingControllerWithQuery() throws Exception {
        String file = "app/greeting?name=maria";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"Hello juan !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //voy aqui
    @Test
    public void shouldLoadMathControllerPIWithQuery() throws Exception {
        String file = "app/pi?decimals=5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"3.14159\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerPIWithoutQuery() throws Exception {
        String file = "app/pi";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"3.14\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerPIWithQuery() throws Exception {
        String file = "app/pi?decimals=5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"3.141\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerSumWithQuery() throws Exception {
        String file = "app/sum?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerSumWithoutQuery() throws Exception {
        String file = "app/sum";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No ingresó números\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerSumWithQuery() throws Exception {
        String file = "app/sum?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerRestWithQuery() throws Exception {
        String file = "app/rest?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"-4\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerRestWithoutQuery() throws Exception {
        String file = "app/rest";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No ingresó números\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerRestWithQuery() throws Exception {
        String file = "app/rest?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Test
    public void shouldLoadMathControllerMulWithQuery() throws Exception {
        String file = "app/mul?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"30\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerMulWithoutQuery() throws Exception {
        String file = "app/mul";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No ingresó números\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerMul1WithoutQuery() throws Exception {
        String file = "app/mul?number=1";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Faltan números para la multiplicación\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerMulWithQuery() throws Exception {
        String file = "app/mul?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    @Test
    public void shouldLoadMathControllerDivWithQuery() throws Exception {
        String file = "app/div?number=4,2";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"2.0\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDiv2WithQuery() throws Exception {
        String file = "app/div?number=4,0";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No se puede dividir por 0\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDiv1WithoutQuery() throws Exception {
        String file = "app/div?number=1";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Faltan números para la divisón\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDivWithoutQuery() throws Exception {
        String file = "app/div";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No ingresó números\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerDivWithQuery() throws Exception {
        String file = "app/mul?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @AfterClass
    public static void tearDown(){
        try {
            server.stopServer();
            serverThread.join(); 
            System.out.println("Servidor cerrado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

}