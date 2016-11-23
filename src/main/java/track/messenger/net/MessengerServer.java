package track.messenger.net;

import org.mockito.internal.util.io.IOUtil;
import track.messenger.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class MessengerServer {

    private ServerSocket serverSocket;
    private LinkedBlockingQueue<Session> sessions;
    private int port;
    private int nthreads;

    public void start() throws Exception{
        serverSocket = null;
        sessions = new LinkedBlockingQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(nthreads);
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting...");
            listen();
            while (true) {
                Session session = sessions.take();
                Message msg = session.getMessage();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            IOUtil.closeQuietly(serverSocket);
        }

    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setNthreads(int nthreads) {
        this.nthreads = nthreads;
    }


    private void listen() {
        Thread listenerThread = new Thread(() -> {
            System.out.println("Listening...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    sessions.put(new Session(clientSocket));
                } catch (Exception e) {
                    System.out.println("listen: " + e.toString() + " error");
                    Thread.currentThread().interrupt();
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
}
