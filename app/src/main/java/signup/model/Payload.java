package signup.model;

public class Payload {

    private String To;
    private String Channel;

    public Payload(String to, String channel) {
        To = to;
        Channel = channel;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }
}
