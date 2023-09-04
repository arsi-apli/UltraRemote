package sk.arsi.saturn.ultra.sender.httpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import sk.arsi.saturn.ultra.sender.httpserver.constant.ServerConstant;
import sk.arsi.saturn.ultra.sender.httpserver.enums.HttpMethod;
import sk.arsi.saturn.ultra.sender.httpserver.utils.ServerUtil;

public class ServerResourceHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ServerResourceHandler.class.getName());

    private final String pathToRoot;
    private final boolean gzippable;
    private final boolean cacheable;
    private final Map<String, Resource> resources = new HashMap<>();

    public ServerResourceHandler(String pathToRoot, boolean gzippable, boolean cacheable) throws IOException {
        this.pathToRoot = pathToRoot.endsWith(ServerConstant.FORWARD_SINGLE_SLASH) ? pathToRoot
                : pathToRoot + ServerConstant.FORWARD_SINGLE_SLASH;
        this.gzippable = gzippable;
        this.cacheable = cacheable;

        File[] files = new File(pathToRoot).listFiles();
        if (files == null) {
            throw new IllegalStateException("Couldn't find webroot: " + pathToRoot);
        }
        for (File f : files) {
            processFile("", f, gzippable);
        }
    }

    public void refresh() {
        resources.clear();
        File[] files = new File(pathToRoot).listFiles();
        if (files == null) {
            throw new IllegalStateException("Couldn't find webroot: " + pathToRoot);
        }
        for (File f : files) {
            try {
                processFile("", f, gzippable);
            } catch (IOException ex) {
                Logger.getLogger(ServerResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getRawQuery();
        // Correct HEAD behavior per Java bug #6886723
        if (httpExchange.getRequestMethod().equals("HEAD")) {
            String requestPath = httpExchange.getRequestURI().getPath();
            httpExchange.getRequestBody().close();
            httpExchange.getResponseHeaders().add("Server", "Apache/2.4.52 (Ubuntu)");
            httpExchange.getResponseHeaders().add("Accept-Ranges", "bytes");
            requestPath = requestPath.substring(1);
            requestPath = requestPath.replaceAll(ServerConstant.FORWARD_DOUBLE_SLASH, File.separator);
            requestPath = pathToRoot + requestPath;
            long filesize = Files.size(Paths.get(requestPath));
            httpExchange.getResponseHeaders().add("Content-Length", "" + filesize);
            httpExchange.sendResponseHeaders(200, -1);
            return;
        }
        String requestPath = httpExchange.getRequestURI().getPath();

        LOGGER.info("Requested Path: " + requestPath);

        serveResource(httpExchange, requestPath);
    }

    private class Resource {

        public final byte[] content;

        public Resource(byte[] content) {
            this.content = content;
        }
    }

    private void processFile(String path, File file, boolean gzippable) throws IOException {
        if (!file.isDirectory()) {
            resources.put(path + file.getName(), new Resource(readResource(new FileInputStream(file), gzippable)));
        }

        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                processFile(path + file.getName() + ServerConstant.FORWARD_SINGLE_SLASH, sub, gzippable);
            }
        }
    }

    private byte[] readResource(final InputStream in, final boolean gzip) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStream gout = gzip ? new GZIPOutputStream(bout) : new DataOutputStream(bout);
        byte[] bs = new byte[4096];
        int r;
        while ((r = in.read(bs)) >= 0) {
            gout.write(bs, 0, r);
        }
        gout.flush();
        gout.close();
        in.close();
        return bout.toByteArray();
    }

    private void serveResource(HttpExchange httpExchange, String requestPath) throws IOException {
        requestPath = requestPath.substring(1);
        requestPath = requestPath.replaceAll(ServerConstant.FORWARD_DOUBLE_SLASH, ServerConstant.FORWARD_SINGLE_SLASH);
        if (requestPath.length() == 0) {
            requestPath = "index.html";
        }
        serveFile(httpExchange, pathToRoot + requestPath);
    }

    private void serveFile(HttpExchange httpExchange, String resourcePath) throws IOException {
        File file = new File(resourcePath);
        if (file.exists()) {
            InputStream in = new FileInputStream(resourcePath);

            Resource res = null;

            if (cacheable) {
                if (resources.get(resourcePath) == null) {
                    res = new Resource(readResource(in, gzippable));
                } else {
                    res = resources.get(resourcePath);
                }
            } else {
                res = new Resource(readResource(in, gzippable));
            }

            if (gzippable) {
                httpExchange.getResponseHeaders().set(ServerConstant.CONTENT_ENCODING, ServerConstant.ENCODING_GZIP);
            }

            String mimeType = ServerUtil.getFileMime(resourcePath);
            writeOutput(httpExchange, res.content.length, res.content, mimeType);
        } else {
            showError(httpExchange, 404, "The requested resource was not found on server");
        }
    }

    private void writeOutput(HttpExchange httpExchange, int contentLength, byte[] content, String contentType)
            throws IOException {
        if (HttpMethod.HEAD.getName().equals(httpExchange.getRequestMethod())) {
            Set<Map.Entry<String, List<String>>> entries = httpExchange.getRequestHeaders().entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries) {
                response += entry.toString() + "\n";
            }
            httpExchange.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
            httpExchange.sendResponseHeaders(200, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
            httpExchange.getResponseBody().close();
        } else {
            httpExchange.getResponseHeaders().add("Accept-Ranges", "bytes");
            httpExchange.getResponseHeaders().add("Server", "Apache/2.4.52 (Ubuntu)");
            httpExchange.sendResponseHeaders(200, contentLength);
            httpExchange.getResponseBody().write(content);
            httpExchange.getResponseBody().close();
        }
    }

    private void showError(HttpExchange httpExchange, int respCode, String errDesc) throws IOException {
        String message = "HTTP error " + respCode + ": " + errDesc;
        byte[] messageBytes = message.getBytes(ServerConstant.ENCODING_UTF8);

        httpExchange.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
        httpExchange.sendResponseHeaders(respCode, messageBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(messageBytes);
        os.close();
    }

}
