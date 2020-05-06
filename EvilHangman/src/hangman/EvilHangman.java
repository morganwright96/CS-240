package hangman;

import java.io.File;
import java.io.IOException;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {
        // FIXME actual game logic
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
        File myDictionary = new File(fileName);
        newGame.startGame(myDictionary, wordLength);
        System.out.print("New Game Started");

       /* // If the guess is not a letter then
        if(!Character.isLetter(guess))
        {

        } if( guess more then one char
        */

        // Try to get the guess
        // Catch the Guess Already made Exception
    }

}
