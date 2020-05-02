package spell;

public class Trie implements ITrie{
    private int hashCodeVal = 0;
    private Node root = new Node();
    private int nodeCount = 1;
    private int wordCount = 0;

    @Override
    public void add(String word) {
        Node currentNode = root;
        int index;
        hashCodeVal += getHashVal(word);
        //for each of the characters in the word
        for (int i = 0; i < word.length(); i++)
        {
            index = word.charAt(i) - 'a';
            // If there is not a value in the position for the letter
            if (currentNode.nodeList[index] == null){
                // Create a new node in the index and modify the total number of nodes
                currentNode.nodeList[index] = new Node();
                nodeCount += 1;
            }
            currentNode = currentNode.nodeList[index];
        }
        if(currentNode.getValue() == 0){
            wordCount += 1;
        }
        currentNode.incrementValue();

    }

    @Override
    public INode find(String word) {
        int index;

        //set the current node equal to root
        Node currentNode = root;
        //for each of the letters in the word
        for (int i = 0; i < word.length(); i++)
        {
            // Get the letter index
            index = word.charAt(i) - 'a';
            // If the next letter of the word is not in the Trie
            if (currentNode.nodeList[index] == null){
                return null;
            }
            currentNode = currentNode.nodeList[index];
        }
        // If the count of the current node is 0 then the current path is not a word
        if(currentNode.getValue() == 0) {
            return null;
        }
        else {
            return currentNode;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() { return ""; }

    @Override
    public int hashCode() {
        // Return the full hash code value of all strings in dictionary
        return hashCodeVal;
    }

    public int getHashVal(String word){
        // Calculate the hash value of a given word
        return 31 * word.hashCode();
    }

    @Override
    public boolean equals(Object o) { return false; }
}
