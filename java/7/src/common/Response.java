package common;

import java.io.Serializable;

public class Response implements Serializable {
    public String message;
    public String treeOutput;
    public TreeNodeDTO treeData;

    public Response(String message, String treeOutput, TreeNodeDTO treeData) {
        this.message = message;
        this.treeOutput = treeOutput;
        this.treeData = treeData;
    }

    public Response(String message, String treeOutput) {
        this(message, treeOutput, null);
    }
}
