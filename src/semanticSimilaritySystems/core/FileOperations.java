package semanticSimilaritySystems.core;
import com.google.common.io.Resources;
import semanticSimilaritySystems.core.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileOperations {
    public static BufferedReader openFile(String filePath) throws FileNotFoundException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(Resources.getResource(filePath).getFile())));
        return buffer;
    }

    public static void closeFile(BufferedReader buffer) throws IOException {
        buffer.close();
    }

    public static Pair fillPairProperties(String line){
        Pair newPair = new Pair();
        String[] split = line.split("\\s+");
        newPair.setPairId(split[0]);
        line = line.substring(split[0].length(), line.length());
        split = line.trim().split("\t");
        int count =1;
        for(String sentence: split){
            sentence = sentence.trim();
            if(!sentence.equals("")){
                String[] splitSentence = sentence.split("\\s+");
                List<String> lst = new LinkedList<String>();

                String beginBigram ="\\B"; String endBigram  ="\\E";
                String preWord =beginBigram;
                List<String> bigramlst = new LinkedList<String>();
                for(String word: split){
                    word = word.toLowerCase();
                    word = replacePunctuations(word);
                    lst.add(word);
                    bigramlst.add(preWord +"-"+word);
                    preWord = word;
                }
                bigramlst.add(preWord + "-" + endBigram);
                if(count==1){
                    newPair.setSentence1(sentence);
                    newPair.setPreprocessedWordListForSentence1(lst);
                    newPair.setBigramFeaturesForSentence1(bigramlst);
                }
                else{
                    newPair.setSentence2(sentence);
                    newPair.setPreprocessedWordListForSentence2(lst);
                    newPair.setBigramFeaturesForSentence2(bigramlst);
                }
                count++;
              //  System.out.println(sentence);
            }
        }
        return newPair;
    }

    public static String replacePunctuations(String phrase){

        phrase = phrase.trim();
        phrase = phrase.replaceAll("\\.","");
        phrase = phrase.replaceAll(";","");
        phrase = phrase.replaceAll("-","");
        phrase = phrase.replaceAll(":","");
        phrase = phrase.replaceAll(",","");
        phrase = phrase.replaceAll("_","");
        phrase = phrase.replaceAll("!", "");
        phrase = phrase.replace(" " , "");
        phrase = phrase.replaceAll("\\(", "");
        phrase = phrase.replaceAll("\\)", "");
        phrase = phrase.replaceAll("\\[", "");
        phrase = phrase.replaceAll("\\]", "");
        phrase = phrase.replaceAll("\\*", "");
        phrase = phrase.replaceAll("/", "");
        phrase = phrase.replaceAll("\\?", "");


        return phrase.toLowerCase();
    }


    public static LinkedList<Pair> readPairsFromFile(String filePath) throws IOException {
        LinkedList<Pair> pairList = new LinkedList<Pair>();
        BufferedReader buffer = openFile(filePath);
        String line;
        while((line=buffer.readLine())!=null){
            Pair newPair = fillPairProperties(line);
           // System.out.println(line);
            pairList.add(newPair);
        }
        closeFile(buffer);
        return pairList;
    }
}
