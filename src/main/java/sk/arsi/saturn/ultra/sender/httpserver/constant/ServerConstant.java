package sk.arsi.saturn.ultra.sender.httpserver.constant;

import java.util.HashMap;
import java.util.Map;

public final class ServerConstant {

	private ServerConstant() {
	}

	public static final String FORWARD_SINGLE_SLASH = "/";
	public static final String FORWARD_DOUBLE_SLASH = "//";
	public static final String WEBAPP_DIR = "webapp";
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 8000;
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String TEXT_PLAIN = "text/plain; charset=utf-8";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String ENCODING_GZIP = "gzip";
	public static final String ENCODING_UTF8 = "UTF-8";

	public static final Map<String, String> MIME_MAP = new HashMap<>();
	static {
		MIME_MAP.put("appcache", "text/cache-manifest");
		MIME_MAP.put("css", "text/css");
		MIME_MAP.put("asc", "text/plain");
		MIME_MAP.put("gif", "image/gif");
		MIME_MAP.put("htm", "text/html");
		MIME_MAP.put("html", "text/html");
		MIME_MAP.put("java", "text/x-java-source");
		MIME_MAP.put("js", "application/javascript");
		MIME_MAP.put("json", "application/json");
		MIME_MAP.put("jpg", "image/jpeg");
		MIME_MAP.put("jpeg", "image/jpeg");
		MIME_MAP.put("mp3", "audio/mpeg");
		MIME_MAP.put("mp4", "video/mp4");
		MIME_MAP.put("m3u", "audio/mpeg-url");
		MIME_MAP.put("ogv", "video/ogg");
		MIME_MAP.put("flv", "video/x-flv");
		MIME_MAP.put("mov", "video/quicktime");
		MIME_MAP.put("swf", "application/x-shockwave-flash");
		MIME_MAP.put("pdf", "application/pdf");
		MIME_MAP.put("doc", "application/msword");
		MIME_MAP.put("ogg", "application/x-ogg");
		MIME_MAP.put("png", "image/png");
		MIME_MAP.put("svg", "image/svg+xml");
		MIME_MAP.put("xlsm", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		MIME_MAP.put("xml", "application/xml");
		MIME_MAP.put("zip", "application/zip");
		MIME_MAP.put("m3u8", "application/vnd.apple.mpegurl");
		MIME_MAP.put("md", "text/plain");
		MIME_MAP.put("txt", "text/plain");
		MIME_MAP.put("php", "text/plain");
		MIME_MAP.put("ts", "video/mp2t");
	};

}
