package semanticSimilaritySystems.core;
import com.google.common.io.Resources;
import semanticSimilaritySystems.core.Pair;
import java.io.*;
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
                if(count==1)
                    newPair.setSentence1(sentence);
                else newPair.setSentence2(sentence);
                count++;
              //  System.out.println(sentence);
            }
        }
        return newPair;
    }

    public static List<Pair> readPairsFromFile(String filePath) throws IOException {
        List<Pair> pairList = new LinkedList<Pair>();
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
