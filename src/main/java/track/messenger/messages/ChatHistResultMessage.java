package track.messenger.messages;

import track.messenger.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ivan on 21.11.16.
 */
public class ChatHistResultMessage extends Message{
    List<String> history;
    public ChatHistResultMessage() {
        super(null);
    }
    public List<String> getHistory() {
        return history;
    }
}
