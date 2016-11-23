package track.messenger.messages;

import track.messenger.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ivan on 21.11.16.
 */
public class ChatListResultMessage extends Message{
    private List<Integer> chatIds;
    public ChatListResultMessage() {
        super(null);
    }
    public List<Integer> getChatIds() {
        return chatIds;
    }
}
