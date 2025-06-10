package common;

import java.io.Serializable;

public class TreeNodeDTO implements Serializable {
    public Object value;
    public TreeNodeDTO left;
    public TreeNodeDTO right;

    public TreeNodeDTO(Object value, TreeNodeDTO left, TreeNodeDTO right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
}
