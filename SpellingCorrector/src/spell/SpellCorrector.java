package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class SpellCorrector implements ISpellCorrector{
    private Trie myTrie = new Trie();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        try {
            File myFile = new File(dictionaryFileName);
            Scanner myScanner = new Scanner(myFile);
            myScanner.useDelimiter("((#[^\\n]*\\n) | (\\s+))+");
            while (myScanner.hasNextLine()) {
                String data = myScanner.nextLine();
                System.out.println(data);
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

        return null;
    }
}
