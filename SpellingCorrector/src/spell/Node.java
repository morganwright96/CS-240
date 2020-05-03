package spell;

public class Node implements INode{
    public int count = 0;
    Node nodeList[] = new Node[26];

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count += 1;
    }

    @Override
    public INode[] getChildren() {
        return nodeList;
    }
}
