package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private SortedSet<Character> guessedLetters = new TreeSet<>();
    private Set<String> possibleWords = new TreeSet<>();
    private Map<String, Set<String>> wordPartitions = new TreeMap<>();
    String currentPattern = "";

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
        wordPartitions.clear();
        currentPattern = "";
        guess = Character.toLowerCase(guess);

        // Check to see if the user has already guessed this char
        if(guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException("You have already guessed this letter");
        }
        // Add the letter to the guessed letter set
        guessedLetters.add(guess);

        // Generate the partitions
        generatePartitions(guess);

        // Check to see if any given set is larger then the rest
        currentPattern = getLargestSet();
        // If the current pattern is not empty
        if(currentPattern != ""){
            // Return the set with the given pattern
            possibleWords = wordPartitions.get(currentPattern);
            return possibleWords;
        }

        // Check to see if any key does not contain the guess
        for(Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            // If there is a key that does not contain the value
            if (!entry.getKey().contains(Character.toString(guess))){
                possibleWords = wordPartitions.get(entry.getKey());
                return possibleWords;
            }
        }

        // Get the group with least number of occurrences
        currentPattern = getLeastOccurrences(guess);
        if(currentPattern != ""){
            // Return the set with the given pattern
            possibleWords = wordPartitions.get(currentPattern);
            return possibleWords;
        }

        // Find the word list with the letter occurring in the rightmost position
        currentPattern = getRightMostPattern(guess);
        if(currentPattern != ""){
            // Return the set with the given pattern
            possibleWords = wordPartitions.get(currentPattern);
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
        String currentPattern = "";
        int currentSize = 0;
        int tempSize = 0;
        for(Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            // Get the size of the set for a given key
            tempSize = entry.getValue().size();
            // If the counts ever become equal then we need perform a different list determiner
            if(tempSize > currentSize){
                currentPattern = entry.getKey();
                currentSize = tempSize;
                tempSize = 0;
            }
        }
        if(currentSize == tempSize){
            return "";
        }

        // Return the current pattern
        return currentPattern;
    }

    public String getLeastOccurrences(char guess){
        int currentCount = 0;
        String currentPattern = "";

        for (Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            int tempCount = getOccurrences(entry.getKey(), guess);

            // If there is ever an equal number of letters in the count
            if(currentCount == tempCount){
                return "";
            }
            // Get a count for the first entry in the map
            if(currentCount == 0){
                currentCount = tempCount;
                currentPattern = entry.getKey();
            }
            // Check to se if the temp count is less then the current
            if(currentCount > tempCount){
                currentCount = tempCount;
                currentPattern = entry.getKey();
            }
        }
        return currentPattern;
    }

    public int getOccurrences(String word, char guess){
        int currentCount = 0;
        for(int i = 0; i < word.length(); i++){
            // Each time the guess is found in the word increase the temp count
            if(word.charAt(i) == guess){
                currentCount++;
            }
        }
        return currentCount;
    }

    public String getRightMostPattern(char guess){
        String currentPattern = "";
        // For each of the patterns
        for (Map.Entry<String,Set<String>> entry : wordPartitions.entrySet()){
            String tempPattern = entry.getKey();
            // Set the pattern on the first run to first entry
            if(currentPattern == ""){
                currentPattern = entry.getKey();
                continue;
            }
            // Starting at the end of the pattern
            for (int i = tempPattern.length(); i > 0; i--){
                // If the patterns both have the guess in the given position
                if(tempPattern.charAt(i-1) == currentPattern.charAt(i-1) && tempPattern.charAt(i-1) == guess){
                    continue;
                }
                // Both are not the guess
                // If the tempPattern has the guess in the given position
                else if (tempPattern.charAt(i-1) == guess){
                    currentPattern = tempPattern;
                }
                else {
                    break;
                }
            }
        }
        return currentPattern;
    }

    public String getWordPattern(){
        return currentPattern;
    }

    public String getFirstWord(){
        Iterator setItr = possibleWords.iterator();
        return setItr.next().toString();
    }

}
