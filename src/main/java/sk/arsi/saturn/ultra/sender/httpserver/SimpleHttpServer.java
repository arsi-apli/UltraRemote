package sk.arsi.saturn.ultra.sender.httpserver;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.arsi.saturn.ultra.sender.httpserver.constant.ServerConstant;
import sk.arsi.saturn.ultra.sender.httpserver.handler.ServerResourceHandler;

public class SimpleHttpServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SimpleHttpServer.class.getName());

    private static SimpleHttpServer server;
    private HttpServer httpServer;
    private ExecutorService executor;
    private static int port;
    public static String TMP_DIR;

    static {
        try {
            TMP_DIR = Files.createTempDirectory("UltraSender").toFile().getAbsolutePath();
            new File(TMP_DIR + File.separator + ".placeholder").createNewFile();
        } catch (Exception ex) {
            TMP_DIR = null;
        }
    }
    private InetSocketAddress address;
    private ServerResourceHandler resourceHandler;

    public InetSocketAddress getAddress() {
        return address;
    }

    public SimpleHttpServer() {
    }

    public void refresh() {
        if (resourceHandler != null) {
            resourceHandler.refresh();
        }
    }
    @Override
    public void run() {
        try {
            executor = Executors.newFixedThreadPool(10);

            httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 0), 0);
            address = httpServer.getAddress();
            System.out.println("HTTP Server port: " + address);

            try {
                resourceHandler = new ServerResourceHandler(
                        TMP_DIR, false, false);
                httpServer.createContext(ServerConstant.FORWARD_SINGLE_SLASH, resourceHandler);
            } catch (Exception ex) {
                Logger.getLogger(SimpleHttpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            httpServer.setExecutor(executor);

            LOGGER.info("Starting server...");

            httpServer.start();

            LOGGER.info("Server started => " + ServerConstant.DEFAULT_HOST + ":" + port);

            // Wait here until shutdown is notified
            synchronized (this) {
                try {
                    this.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error occurred during server starting..." + e);
        }
    }

    static void shutDown() {
        try {
            LOGGER.info("Shutting down server...");
            server.httpServer.stop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (server) {
            server.notifyAll();
        }
    }

}
