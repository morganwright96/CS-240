package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class SpellCorrector implements ISpellCorrector{
    private Trie myTrie = new Trie();
    private SortedSet<String> suggestionList = new TreeSet<String>();
    private SortedSet<String> candidateList = new TreeSet<String>();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        try {
            File myFile = new File(dictionaryFileName);
            Scanner myScanner = new Scanner(myFile);
            // Remove the white space
            myScanner.useDelimiter("((\\s+))+");
            while (myScanner.hasNext()) {
                // For each word add the the Trie
                String data = myScanner.next();
                myTrie.add(data);
            }
            myScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //Reset both lists
        suggestionList.clear();
        candidateList.clear();

        // The dictionary only contains lowercase words
        String lowerInputWord = inputWord.toLowerCase();
        if(lowerInputWord.isEmpty()){
            return null;
        }

        // Try to find the word
        if(myTrie.find(lowerInputWord) != null){
            // Word is in the Trie so return the word
            return lowerInputWord;
        }

        // Generate the Candidate list with the Lowercase word
        generateCandidateList(lowerInputWord);

        // Create a Suggestion List
        generateSuggestionList();

        // If the suggestion list is only 1 element long then suggest that word
        if(suggestionList.size() == 1){
            return suggestionList.first();
        }
        // We need to get a second edit distance
        else if(suggestionList.size() == 0){
            // copy the contents of the candidate words to a vector
            Vector<String> myVector = new Vector<String>();
            Iterator setItr = candidateList.iterator();
            while (setItr.hasNext()){
                myVector.add(setItr.next().toString());
            }
            // For each of the elements in the vector generate candidate words
            for(int i = 0; i < myVector.size(); i++){
                generateCandidateList(myVector.elementAt(i));
            }
            // Generate a suggestion list
            generateSuggestionList();
            // If there is only one word in the suggestion list return that word
            if(suggestionList.size() == 1){
                return suggestionList.first();
            }
            // else if there are no suggestions return null
            else if(suggestionList.size() == 0){
                return null;
            }
            else {
                // Check the word count
                return getSuggestion();
            }
        }
        else {
            // Check the word count
            return getSuggestion();
        }
    }

    public void deleteEditDistance(String inputWord){
        // For each of the letters
        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder tempString = new StringBuilder(inputWord);
            // Delete the char and add to the candidate list
            tempString.deleteCharAt(i);
            candidateList.add(tempString.toString());
        }
    }

    public void transpositionEditDistance(String inputWord){
        // For every 2 letters
        for(int i = 0 ; i < inputWord.length() - 1; i++){
            StringBuilder tempString = new StringBuilder(inputWord);
            // Get the char value at the one after the index
            char tempLetter = tempString.charAt(i + 1);
            // Remove the char one after the index
            tempString.deleteCharAt(i + 1);
            // Add the letter back at the index
            tempString.insert(i, tempLetter);
            candidateList.add(tempString.toString());
        }
    }

    public void alterationEditDistance(String inputWord){
        // Grab the char value for the index
        char originalLetter;
        StringBuilder tempWord = new StringBuilder(inputWord);
        // For each of the letters in the word we are trying to find
        for(int i = 0; i < inputWord.length(); i++){
            // Save the original letter
            originalLetter = tempWord.charAt(i);
            for(int j = 0; j < 26; j++){
                // if it's the original letter skip it
                if(originalLetter == j + 'a'){
                    continue;
                }
                // delete and add a new character
                tempWord.deleteCharAt(i);
                tempWord.insert(i, getIndexCharacter(j));
                // add the new word to the candidate list
                candidateList.add(tempWord.toString());
            }
            // Replace the original letter back into the word
            tempWord.deleteCharAt(i);
            tempWord.insert(i, originalLetter);
        }
    }

    public void insertionEditDistance(String inputWord){
        // For each of the positions in the word
        for(int i = 0; i < inputWord.length() + 1; i++){
            StringBuilder tempString = new StringBuilder(inputWord);
            for (int j = 0; j < 26; j++){
                // Add the letter of the Alphabet and add the new word to the candidate list
                tempString.insert(i, getIndexCharacter(j));
                candidateList.add(tempString.toString());
                // Remove the added letter
                tempString.deleteCharAt(i);
            }
        }
    }

    public char getIndexCharacter(int index){
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
    }

    public void generateSuggestionList(){
        // From the list of candidate words find any that are in the dictionary
        Iterator iterator = candidateList.iterator();
        while (iterator.hasNext()){
            String tempWord = iterator.next().toString();
            // If the word is in the dictionary add to the suggestion list
            if(myTrie.find(tempWord) != null){
                suggestionList.add(tempWord);
            }
        }
    }

    public void generateCandidateList(String lowerInputWord){
        // Create the candidate words
        deleteEditDistance(lowerInputWord);
        transpositionEditDistance(lowerInputWord);
        alterationEditDistance(lowerInputWord);
        insertionEditDistance(lowerInputWord);
    }

    // Check all the counts for the suggestions and return the highest count
    public String getSuggestion(){
        Iterator suggestionItr = suggestionList.iterator();
        // Pull out the first word, node, and count from the set
        String currentWord = suggestionItr.next().toString();
        INode currentNode = myTrie.find(currentWord);
        int currentValue = currentNode.getValue();
        // While the is a next value in the iterator
        while (suggestionItr.hasNext()) {
            // Grab the temp values
            String tempWord = suggestionItr.next().toString();
            INode tempNode = myTrie.find(tempWord);
            int tempValue = tempNode.getValue();
            // If the temp count is greater then the current count
            if (currentValue < tempValue) {
                // Replace the current node, count, and word
                currentNode = tempNode;
                currentValue = tempValue;
                currentWord = tempWord;
            }
        }
        // Return the word with the highest count
        return currentWord;
    }
}
