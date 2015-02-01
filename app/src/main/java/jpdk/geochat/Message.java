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

    public void speak(){
        System.out.println("user " + user);
        System.out.println("msg " + message);
        System.out.println("id " + id);
        System.out.println("lat " + lat);
        System.out.println("lng " + lng);
        System.out.println("sent " + sentAt);
        System.out.println("send " + sendAt);
        System.out.println("dur " + duration);
    }
}

