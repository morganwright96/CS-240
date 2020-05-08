package spell;

import java.util.Set;
import java.util.TreeSet;

public class Trie implements ITrie{
    private int hashCodeVal = 0;
    private Node root = new Node();
    private int nodeCount = 1;
    private int wordCount = 0;
    private StringBuilder wordList = new StringBuilder("");

    private boolean isEqual = true;

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
        // Only increase the word count if the word dose not exist
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
    public String toString() {
        // Reset the word list each time this is called
        wordList.setLength(0);
        StringBuilder currentWord = new StringBuilder("");
        // For each of the child nodes on the root node
        for(int i = 0; i < root.nodeList.length; i++) {
            // if the node is null skip it
            if(root.nodeList[i] == null){
                continue;
            }
            // Add teh letter and call the helper function
            currentWord.append(Character.toString(i + 'a'));
            toStringHelper(currentWord, root.nodeList[i]);
            // Reset the current word
            currentWord.setLength(0);
        }
        //Remove the last newline from the list of words only if the wordlist is not empty
        if(wordList.length() != 0) {
            wordList.setLength(wordList.length() - 1);
        }
        //System.out.print(wordList.toString());
        return wordList.toString();
    }

    public void toStringHelper(StringBuilder currentWord, Node currentNode){
        // Add the current word if the count is greater than 0
        if(currentNode.getValue() > 0) {
            wordList.append(currentWord + "\n");
        }
        // For each of the child nodes
        for(int i = 0; i < currentNode.nodeList.length; i++) {
            // If the child node is null skip that position
            if (currentNode.nodeList[i] == null) {
                continue;
            }
            // else add the character to the current word and call the toStringHelper function
            currentWord.append(Character.toString(i + 'a'));
            toStringHelper(currentWord, currentNode.nodeList[i]);
        }
        // Remove the last character from current word
        currentWord.setLength(currentWord.length() - 1);
    }

    /*public char getIndexCharacter(int index){
        // Used to convert an index into a letter to build the word
        switch (index){
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
            case 8:
                return 'i';
            case 9:
                return 'j';
            case 10:
                return 'k';
            case 11:
                return 'l';
            case 12:
                return 'm';
            case 13:
                return 'n';
            case 14:
                return 'o';
            case 15:
                return 'p';
            case 16:
                return 'q';
            case 17:
                return 'r';
            case 18:
                return 's';
            case 19:
                return 't';
            case 20:
                return 'u';
            case 21:
                return 'v';
            case 22:
                return 'w';
            case 23:
                return 'x';
            case 24:
                return 'y';
            case 25:
                return 'z';
            default:
                return '0';
        }
    }*/

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
    public boolean equals(Object o) {
        // Makes sure the isEqual is set to true before tying to compare
        isEqual = true;
        // if the o is not an instance of Trie
        if(!(o instanceof Trie)){
            return false;
        }
        // if the object is null
        if(o == null){
            return false;
        }
        // if the word counts are different
        if(((Trie) o).wordCount != this.wordCount){
            return false;
        }
        // if the node counts are different
        if(((Trie) o).nodeCount != this.nodeCount){
            return false;
        }
        equalsHelper(root, ((Trie) o).root);
        // Check the counts for each of the nodes
        return isEqual;
    }

    public void equalsHelper(Node currentNode, Node objectNode){
        for(int i = 0; i < currentNode.nodeList.length; i++) {
            // if the child node is null skip that position
            if(currentNode.nodeList[i] == null && objectNode.nodeList[i] == null){
                continue;
            }
            // if there are nodes that exists in both trie
            else if(currentNode.nodeList[i] != null && objectNode.nodeList[i] != null){
                //if the counts are different then set isEqual to false
                if(currentNode.getValue() != objectNode.getValue()){
                    isEqual = false;
                }
                //else call the equalsHelper
                equalsHelper(currentNode.nodeList[i], objectNode.nodeList[i]);
            }
            else{
                isEqual = false;
            }
        }
    }
}

