import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.util.stream.Collectors.toMap;

public class Dispatcher {
    public static void main(String[] argv){
        String text = inputFile("in.txt");
        WordsPrep wp = new WordsPrep(text);
        String[] words = wp.getReadyWords();
        if (words == null){
            System.out.println("Empty input. Try again...");
            System.exit(1);
        }
        WordsCounter wc = new WordsCounter(words);
        wc.displayWordCount();
    }

    public static String inputFile(String fileName){
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        }
        catch (IOException e){
            e.getMessage();
        }
        return content;
    }
}

class WordsCounter {
    HashMap<String, Integer> map;
    String[] words;

    WordsCounter(String[] words){
        this.words = words;
        map = new HashMap<>();
        if(this.words != null){
            countWords();
            sortWordsDsc();
        }
    }

    private void countWords(){
        for(String word : words)
            map.put(word, map.getOrDefault(word, 0) + 1);

    }

    private void sortWordsDsc() {
        map = map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    public void displayWordCount() {
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public boolean displayWordCount(int wordsNum) {
        if(wordsNum <= map.size() && wordsNum > 0) {
            Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
            for (int i = 0; i < wordsNum; i++) {
                Map.Entry<String, Integer> entry = it.next();
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            return true;
        }
        System.out.println("Number of words is irrelevant. Try again...");
        return false;
    }
}

class WordsPrep {
    String[] words;
    String[] separatedWords;
    String[] readyWords;
    int count = 0;

    public WordsPrep(String rawText) {
        words = rawText.split("(?=[,.])|\\s+");
        removeNonLiteral();
        cutWords();
    }

    private void removeNonLiteral() {
        separatedWords = new String[words.length];
        int j = -1;
        for (int i = 0; i < words.length; i++){
            if(!words[i].isEmpty() && words[i].matches("^[a-zA-Z'\'-]{0,200}$"))
                separatedWords[++j] = words[i].toLowerCase();
        }
        count = words.length - (j+1);
    }

    private void cutWords() {
        if(separatedWords.length == 0)
            return;
        readyWords = new String[separatedWords.length - count];
        for(int i = 0; i < readyWords.length; i++){
            if(separatedWords[i].length() > 30)
                readyWords[i] = separatedWords[i].substring(0, 30);
            else
                readyWords[i] = separatedWords[i];
        }
    }

    public String[] getReadyWords(){
        if(readyWords.length == 0)
            return null;
        return readyWords;
    }
}