package track.messenger.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.mockito.internal.util.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.messenger.User;
import track.messenger.messages.Message;

/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session {

    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;
    private InputStream in;
    private OutputStream out;
    // сокет на клиента
    private Socket socket;
    private Protocol protocol;
    private static final int MAX_MSG_SIZE = 32 * 1024;
    static Logger log = LoggerFactory.getLogger(Session.class);

    public Session(Socket clientSocket) throws IOException {
        try {
            socket = clientSocket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            protocol = new StringProtocol();

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     * С каждым сокетом связано 2 канала in/out
     */

    public void send(Message msg) throws ProtocolException, IOException {
        // TODO: Отправить клиенту сообщение
        if (msg.getSenderId() != null) {
            log.info("sent to: " + msg.getSenderId().toString());
        } else {
            log.info("sent to: " + null);
        }
        out.write(protocol.encode(msg));
        out.flush();
    }

    public void onMessage(Message msg) throws IOException, ProtocolException {
        // TODO: Пришло некое сообщение от клиента, его нужно обработать
    }

    public Message getMessage() throws IOException, ProtocolException {

        byte[] buf = new byte[MAX_MSG_SIZE];
        int read = 0;
        Message ret = null;
        try {
            read = in.read(buf, 0, MAX_MSG_SIZE);
            if (read > 0) {
                ret = protocol.decode(buf);
            }
        } catch (SocketTimeoutException ste) {
            ret = null;
        }
        return ret;
    }

    public void close() {
        IOUtil.closeQuietly(in);
        IOUtil.closeQuietly(out);
        IOUtil.closeQuietly(socket);
        // TODO: закрыть in/out каналы и сокет. Освободить другие ресурсы, если необходимо
    }
}