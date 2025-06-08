package common;

import java.io.Serializable;

public class Response implements Serializable {
    public String message;
    public String treeOutput;

    public Response(String message, String treeOutput) {
        this.message = message;
        this.treeOutput = treeOutput;
    }
}
