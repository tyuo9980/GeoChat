package jpdk.geochat;

public class Message {

    public String user;
    public String message;
    public String id;
    public double lat;
    public double lng;
    public String sentAt;
    public String sendAt;
    public int duration;

    public Message(String u, String m, String i, double lt, double lg, String st, String sd, int d){
        user = u;
        message = m;
        id = i;
        lat = lt;
        lng = lg;
        sentAt = st;
        sendAt = sd;
        duration = d;
    }
}
