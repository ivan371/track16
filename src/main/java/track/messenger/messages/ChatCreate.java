package track.messenger.messages;

import track.messenger.User;

import java.io.Serializable;

/**
 * Created by ivan on 21.11.16.
 */
public class ChatCreate extends Message implements Serializable {
    private String[] tokens;
    public ChatCreate() {
        super(null);
    }

    public void setIds(String[] tokens) {
        this.tokens = tokens;
    }

    public String[] getTokens() {
        return tokens;
    }
}
