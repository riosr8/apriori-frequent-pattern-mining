
import java.util.ArrayList;
import java.util.List;

public class TrieNode {

    private int idNumber;
    private int support;
    private List<TrieNode> children;

    TrieNode() {
        this.idNumber = 0;
        this.support = 0;
        this.children = new ArrayList<>(0);
    }

    TrieNode(int n) {
        this.idNumber = n;
        this.support = 0;
        this.children = new ArrayList<>(0);
    }

    TrieNode(TrieNode old) {
        this.idNumber = old.idNumber;
        this.support = old.support;
        this.children = new ArrayList<>(old.children);
    }

    int getItemLabel() {
        return idNumber;
    }

    int getSupport() {
        return support;
    }

    List<TrieNode> getChildren() {
        return children;
    }

    void incrementNodeCounter() {
        this.support++;
    }

    void addChildNode(TrieNode child) {
        this.children.add(child);
    }

    void addChildAtIndex(TrieNode child, int index) {
        this.children.add(index, child);
    }

    boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        TrieNode temp = (TrieNode)obj;
        return this.getItemLabel() == temp.getItemLabel();
    }

}
