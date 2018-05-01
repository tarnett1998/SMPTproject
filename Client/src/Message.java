import java.util.ArrayList;


class Message {


private String to = "null1";
private String from = "null1";
private String subject = "null1";
private String message = "null1";
private String ip = "null1";

    ArrayList<String> cc = new ArrayList<String>();

    public Message(String to_, String from_, String subject_, String message_, String ip_) {
    to = to_;
    from = from_;
    subject = subject_;
    message = message_;
    ip = ip_;
    }

    boolean isValid() {

        return true;
    }

    void parseMessage() {
    }

    String getFrom() {
        return from;
    }
    
    String getTo() {
        return to;
    }
    
    String getMessage(){
      return message;
    }

    ArrayList<String> formatMessage() {

        return null;
    }
}
