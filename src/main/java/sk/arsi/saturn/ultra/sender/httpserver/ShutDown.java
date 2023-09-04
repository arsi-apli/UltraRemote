package sk.arsi.saturn.ultra.sender.httpserver;

public class ShutDown extends Thread {

	@Override
	public void run() {
		SimpleHttpServer.shutDown();
	}
}
