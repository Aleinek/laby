package common;

import java.io.Serializable;

public class Request implements Serializable {
    public TreeType type;
    public String command;
    public String value;

    public Request(TreeType type, String command, String value) {
        this.type = type;
        this.command = command;
        this.value = value;
    }
}
