package semanticSimilaritySystems.supervisedMethod;

import com.google.common.io.Resources;
import semanticSimilaritySystems.core.FileOperations;
import semanticSimilaritySystems.core.Pair;
import semanticSimilaritySystems.supervisedMethod.features.FeatureExtractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gizem on 17.04.2016.
 */
public class test {

    public static List<String> readStopWordsList() throws IOException {
        List<String> stopWordsList = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(Resources.getResource("stopWords/stop_words.txt").getFile()));
        String line;

        while ((line = bufferedReader.readLine())!=null){
            if(!stopWordsList.contains(line))
                stopWordsList.add(line.toLowerCase());
        }

        return stopWordsList;

    }
    public static void main(String[] args) throws IOException {

        LinkedList<Pair> pairList;
        FileOperations operations = new FileOperations();
        pairList = operations.readPairsFromFile("sentencePairsData/pairs_test.txt");

        List<String> stopWordsList = readStopWordsList();
        FeatureExtractor featureExtractor = new FeatureExtractor(pairList,stopWordsList);
        featureExtractor.createArffFileFromInstances();
     //   featureExtractor.createSVMFileFormatInstances();

    }
}
