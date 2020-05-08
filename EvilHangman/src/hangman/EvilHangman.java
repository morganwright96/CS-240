package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        // Check for 3 valid arguments
        if(args.length != 3){
            if(args.length == 1){
                System.out.print("You have entered " + args.length + " argument please provide 3 valid arguments");
                return;
            }
            System.out.print("You have entered " + args.length + " arguments please provide 3 valid arguments");
            return;
        }

        // Get the arguments from the command line
        String fileName = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangmanGame newGame = new EvilHangmanGame();
        // Try to start a new game
        try {
            File myDictionary = new File(fileName);
            newGame.startGame(myDictionary, wordLength);
        }
        catch (EmptyDictionaryException e){
            System.out.print(e.getMessage());
            return;
        }
        // Allows for the input of user input
        Scanner in = new Scanner(System.in);
        String userInput = "";
        // Initialize a word with no characters
        StringBuilder currentWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++){
            currentWord.append("_");
        }

        // Until the user runs out of guesses or there is a completed word
        for(int i = guesses; i > 0; i--){
            System.out.print("You have " + i + " guesses left\n");
            // Print out the used letters replacing any charters
            System.out.print("Used letters: " + newGame.getGuessedLetters().toString()
                    .replace(",", "")
                    .replace("[", "")
                    .replace("]", "") + "\n");
            // Print the representation of the word
            System.out.print("Word: " + currentWord + "\n");

            // Allow the user to guess a letter
            System.out.print("Enter a Guess: ");
            userInput = in.nextLine();
            userInput = userInput.toLowerCase();
            // Perform userInput error checking
            if(userInput.length() == 0){
                i++;
                System.out.print("You must guess at least one character!\n\n");
                continue;
            }
            if(userInput.contains("exit")){
                System.out.print("Thanks for playing!\n\n");
                return;
            }
            // If the user enters more then 1 character
            if(userInput.length() > 1){
                i++;
                System.out.print("You can only enter one character!\n\n");
                continue;
            }
            // Convert the string to a char value
            char charInput = userInput.charAt(0);
            // If the input is a number
            if(Character.isDigit(charInput)){
                i++;
                System.out.print("You can not enter numbers!\n\n" );
                continue;
            }
            if(Character.isSpaceChar(charInput)){
                i++;
                System.out.print("You can not enter a space!\n\n" );
                continue;
            }
            // If the input value is anything else beside a letter
            if(!Character.isLetter(charInput)){
                i++;
                System.out.print("You can not enter special characters!\n\n");
                continue;
            }
            // Try to call the guess function
            try{
                newGame.makeGuess(charInput);
            } catch (GuessAlreadyMadeException e){
                // Display that they already made that guess
                System.out.print(e.getMessage() + "\nPlease enter a new guess!\n\n");
                // Add one to the guess count
                i++;
                continue;
            }

            String tempPattern = newGame.getWordPattern();
            if(tempPattern.contains(userInput)){
                int letterCount = 0;
                for(int j = 0; j < tempPattern.length(); j++){
                    if(tempPattern.charAt(j) != '_'){
                        // Replace the underscore with the guessed letter increase the count
                        currentWord.replace(j,j+1,Character.toString(tempPattern.charAt(j)));
                        letterCount++;
                    }
                }
                // Show them that there is a letter and the number of them
                System.out.print("Yes, there is " + letterCount + " " + userInput +"\n");
                // Give them an extra guess when they get the letter right
                i++;
            }
            else {
                // Say that it did not contain the letter
                System.out.print("Sorry, there are no " + userInput + "\'s\n");
            }
            if(!currentWord.toString().contains("_")){
                System.out.print("You win! The word was: " + currentWord + "\n");
                return;
            }
            if(i == 1){
                System.out.print("You lose!\nThe word was: " + newGame.getFirstWord());
            }
            System.out.print("\n");

        }
    }

}
