package leeshani.com.appchat;

public class Message {
    private String message;
    private boolean isInput;

    public Message(String message, boolean isInput) {
        this.message = message;
        this.isInput = isInput;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        isInput = input;
    }
}
