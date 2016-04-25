package semanticSimilaritySystems.core;

import com.google.common.io.Resources;
import org.openrdf.query.algebra.Str;
import semanticSimilaritySystems.baseline.BaselineMethod;
import semanticSimilaritySystems.baseline.SimMetricFunctions;
import semanticSimilaritySystems.supervisedMethod.features.FeatureExtractor;
import semanticSimilaritySystems.unsupervisedMethod.LSA.LsaDocumentSimilarity;
import semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod.CombinedOntologyMethod;
import semanticSimilaritySystems.unsupervisedMethod.paragraphVector.ParagraphVector;
import semanticSimilaritySystems.unsupervisedMethod.paragraphVector.ParagraphVectorModel;
import slib.utils.ex.SLIB_Exception;


import java.io.*;
import java.util.*;

/**
 * Created by orhan on 07.02.2016.
 */
public class Main {

    public static String replacePunctuations(String phrase){

        phrase = phrase.trim();
        phrase = phrase.replaceAll("\\.","");
        phrase = phrase.replaceAll(";","");
        phrase = phrase.replaceAll("-","");
        phrase = phrase.replaceAll(":","");
        phrase = phrase.replaceAll(",","");
        phrase = phrase.replaceAll("_","");
        phrase = phrase.replaceAll("!", "");
       // phrase = phrase.replace(" " , "");
        phrase = phrase.replaceAll("\\(", "");
        phrase = phrase.replaceAll("\\)", "");
        phrase = phrase.replaceAll("\\[", "");
        phrase = phrase.replaceAll("\\]", "");
        phrase = phrase.replaceAll("\\*", "");
        phrase = phrase.replaceAll("/", "");
        phrase = phrase.replaceAll("\\?", "");


        return phrase.toLowerCase();
    }


    public static HashSet<String> addSentenceToDictionary(HashSet<String> dictionary, String sentence, List<String> stops){
        String[] split = sentence.toLowerCase().split("\\s+");
        for(String word:split){
            word = replacePunctuations(word);
            word = word.trim();
            if(!dictionary.contains(word) && !stops.contains(word))
                dictionary.add(word);
        }

        return dictionary;
    }
    public static HashSet<String> constructDictionary(List<Pair> pairList, List<String> stops){
        HashSet<String> dictionary = new HashSet<String>();
        for(Pair current: pairList){

            dictionary = addSentenceToDictionary(dictionary, current.getSentence1(),stops);

            dictionary = addSentenceToDictionary(dictionary,current.getSentence2(),stops);

        }
        return dictionary;
    }

    public static String removeStopWordsFromSentence(String sentence, List<String> stopwords){
        String processedS = "";
        String split[] = sentence.split("\\s+");
        for (String s: split){
            if(!stopwords.contains(s)){
                processedS  = processedS +" " +s;
            }

        }

        processedS  =processedS.trim();
        processedS = replacePunctuations(processedS);
        return processedS.trim();
    }
    public static void calculateSimilarityScoreAmongSentencesUsingUnsupervisedMethods(List<Pair> pairList) throws SLIB_Exception, IOException {

        List<String> stopWordsList = readStopWordsList();

        /****************************BASELINE**********************************************/
        SimilarityMeasure measure = new BaselineMethod(constructDictionary(pairList,stopWordsList));
//          for(Pair currentPair: pairList){
//
//          double similarityScore =measure.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
//         System.out.println(similarityScore);
//         }

        /**********************************************************************************/


        /****************************SIMMETRICS***************************************************/

       measure = new SimMetricFunctions();
//        for(Pair currentPair: pairList){
//            String preprocessedS1 = removeStopWordsFromSentence(currentPair.getSentence1(), stopWordsList);
//            String preprocessedS2 = removeStopWordsFromSentence(currentPair.getSentence2(), stopWordsList);
//
//            double similarityScore =measure.getSimilarity(preprocessedS1, preprocessedS2);
//            //System.out.println("Sentence 1: " + preprocessedS1);
//           // System.out.println("Sentence 2: " + preprocessedS2);
//             System.out.println(similarityScore);
//        }

        /**********************************************************************************/




        /********************************PARAGRAPH VECTOR**********************************/

        //   measure = new ParagraphVector(new ParagraphVectorModel("paragraphVector/sentence_vectors.txt"));
//        for(Pair currentPair: pairList){
//       //     double similarityScore =measure.getSimilarity(currentPair.getPairId(), currentPair.getPairId());
//     //       System.out.println(similarityScore);
//        }
        /**********************************************************************************/


        /***********************************LSA********************************************/

//        measure = new LsaDocumentSimilarity();
//        for(Pair currentPair: pairList){
//            double similarityScore =measure.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
//       //     System.out.println(similarityScore);
//        }
        /**********************************************************************************/

        /**************************************COMBINED METHOD**************************************/

        CombinedOntologyMethod measure1 = new CombinedOntologyMethod(stopWordsList);
         for(Pair currentPair: pairList){
            double similarityScore =measure1.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
            System.out.println(similarityScore);
            //break;
        }
//        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("pair_score.txt")));
//
//
//        HashMap<String,Double> pair_scores = measure1.getPair_score();
//        Iterator<String> it = pair_scores.keySet().iterator();
//        while (it.hasNext()){
//            String pairs = it.next();
//            Double sim = pair_scores.get(pairs);
//            writer.write(pairs + " " + sim);
//            writer.newLine();
//        }
//        writer.close();

//        /**********************************************************************************/

    }

    public HashMap<String, Double> readIDFScores() throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(Resources.getResource("idfScores/idf_values_tac_data.txt").getFile()));
        String line;
        HashMap<String , Double> idf_hash = new HashMap<String, Double>();

        while ((line = buffer.readLine())!=null){
            String[] split = line.split("\\s+");
            String word = split[0];
            Double value = Double.valueOf(split[1]);
            if(!idf_hash.containsKey(word))
                idf_hash.put(word, value);
        }


        return idf_hash;

    }
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
    public static void main(String[] args) throws SLIB_Exception, IOException {
        LinkedList<Pair> pairList;
        FileOperations operations = new FileOperations();
        pairList = operations.readPairsFromFile("sentencePairsData/pairs.txt");

        calculateSimilarityScoreAmongSentencesUsingUnsupervisedMethods(pairList);
//        FeatureExtractor featureExtractor = new FeatureExtractor(pairList);
//       // featureExtractor.createArffFileFromInstances();
//        featureExtractor.createSVMFileFormatInstances();
//

    }
}