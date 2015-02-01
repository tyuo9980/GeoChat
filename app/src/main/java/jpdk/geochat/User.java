package jpdk.geochat;

public class User {
    public String id;
    public String name;
    public String fbid;

    public User(String id, String fbid, String name){
        this.id = id;
        this.fbid = fbid;
        this.name = name;
    }
}
