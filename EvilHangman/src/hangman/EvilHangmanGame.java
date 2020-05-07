package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private SortedSet<Character> guessedLetters = new TreeSet<>();
    private Set<String> possibleWords = new TreeSet<>();
    private Map<String, Set<String>> wordPartitions = new TreeMap<>();
    String wordPattern = "";

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        // Start with a new game and no values
        guessedLetters.clear();
        possibleWords.clear();
        wordPartitions.clear();
        try {
            if (dictionary.length() == 0) {
                throw new EmptyDictionaryException("The dictionary is empty. Please use a valid dictionary");
            }
            if (wordLength <= 0){
                throw new EmptyDictionaryException("The word length can't be negative or zero");
            }
            // Scan the contents of the file
            Scanner myScanner = new Scanner(dictionary);
            // Remove the white space
            myScanner.useDelimiter("((\\s+))+");
            // While there are words in the file
            while (myScanner.hasNext()) {
                // Get the word
                String word = myScanner.next();
                // If the word length is equal to the given input length
                if(word.length() == wordLength){
                    // Add the word the possible words in lowercase only
                    possibleWords.add(word.toLowerCase());
                }
            }
            if(possibleWords.size() == 0){
                throw new EmptyDictionaryException("The word size you entered does not match any words in the dictionary");
            }
            myScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        // Start with clear partitions and word for each guess
        wordPartitions.clear();
        wordPattern = "";
        // Make sure that the character is lowercase
        guess = Character.toLowerCase(guess);
        // If the user has already made a guess
        if(guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException("You have already made this guess!");
        }
        guessedLetters.add(guess);

        // Generate the partitions
        generatePartitions(guess);

        // Find the largest set if there is one
        String tempPattern = getLargestSet();
        // If there is a set with the largest word count
        if(tempPattern != ""){
            // set the possible words and return them
            possibleWords = wordPartitions.get(tempPattern);
            wordPattern = tempPattern;
            return possibleWords;
        }

        // Check to see if any of the patterns contain the guessed letter
        for (Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            tempPattern = entry.getKey();
            // If there is a pattern that does not have the guessed letter
            if(!tempPattern.contains(Character.toString(guess))){
                // set the possible words to the set of words and return them
                possibleWords = entry.getValue();
                wordPattern = entry.getKey();
                return possibleWords;
                //System.out.print("Has a key that does not contain the letters" + "\n");
            }
            //System.out.print("Only has patterns that contain the letter" + "\n");
        }
        // Reset the tempPattern
        tempPattern = "";
        // Check for the least occurrences of the guess in the pattern
        tempPattern = getLeastOccurrences(guess);

        if(tempPattern != ""){
            // set the possible words and return them
            possibleWords = wordPartitions.get(tempPattern);
            wordPattern = tempPattern;
            return possibleWords;
        }

        return possibleWords;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void generatePartitions(char guess){
        Iterator setItr = possibleWords.iterator();
        StringBuilder pattern = new StringBuilder();
        // While there is values in the possible words
        while (setItr.hasNext()){
            // Get the Word that appears next in the set
            String word = setItr.next().toString();
            // For each char in the word build the pattern
            for(int i = 0; i < word.length(); i++){
                // If the guess is in the word add the letter to the pattern
                if(word.charAt(i) == guess){
                    pattern.append(guess);
                }
                // Else add a blank spot
                else {
                    pattern.append("_");
                }
            }
            if(wordPartitions.containsKey(pattern.toString())){
                // Get the set of values from the map
                Set tempSet = wordPartitions.get(pattern.toString());
                // Add the value to the set
                tempSet.add(word);
                // Replace the set
                wordPartitions.replace(pattern.toString(), tempSet);
            }
            else {
                Set<String> tempSet = new TreeSet<>();
                tempSet.add(word);
                wordPartitions.put(pattern.toString(), tempSet);
            }
            pattern.setLength(0);
        }
        return;
    }

    public String getLargestSet(){

        int wordCount = 0;
        String tempPattern = "";
        // Check for largest group
        for (Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            int tempCount = entry.getValue().size();
            // If the counts ever become equal then we need perform a different list determiner
            if(tempCount == wordCount){
                tempPattern = "";
                break;
            }
            if(tempCount > wordCount){
                wordCount = tempCount;
                tempPattern = entry.getKey();
            }
        }

        return tempPattern;
    }

    public String getLeastOccurrences(char guess){
        int numOccurerences = 0;
        String tempPattern = "";
        // Check for least occurrences
        for (Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            String tempKey = entry.getKey();
            int tempOccurrences = 0;
            for(int i = 0; i < tempKey.length(); i++){
                // Each time the guess is found in the word increase the temp count
                if(tempKey.charAt(i) == guess){
                    tempOccurrences++;
                }
            }
            // if the number of occurrences is zero then set the occurrences
            if(numOccurerences == 0){
                tempPattern = tempKey;
                numOccurerences = tempOccurrences;
            }
            // the current pattern has less of the guessed letter
            if(numOccurerences > tempOccurrences){
                tempPattern = tempKey;
                numOccurerences = tempOccurrences;
            }
        }
        return tempPattern;
    }

    public String getWordPattern() {
        return wordPattern;
    }

    public String getFirstWord(){
        // Return the first word in the possible words
        Iterator setItr = possibleWords.iterator();
        return setItr.next().toString();
    }
}
