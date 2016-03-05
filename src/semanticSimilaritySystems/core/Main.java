package semanticSimilaritySystems.core;

import semanticSimilaritySystems.baseline.BaselineMethod;
import semanticSimilaritySystems.baseline.SimMetricFunctions;
import semanticSimilaritySystems.unsupervisedMethod.LSA.LsaDocumentSimilarity;
import semanticSimilaritySystems.unsupervisedMethod.paragraphVector.ParagraphVector;
import semanticSimilaritySystems.unsupervisedMethod.paragraphVector.ParagraphVectorModel;
import slib.utils.ex.SLIB_Exception;


import java.io.*;
import java.util.HashSet;
import java.util.List;

/**
 * Created by orhan on 07.02.2016.
 */
public class Main {

    public static HashSet<String> addSentenceToDictionary(HashSet<String> dictionary, String sentence){
        String[] split = sentence.toLowerCase().split("\\s+");
        for(String word:split){
            if(!dictionary.contains(word))
                dictionary.add(word);
        }

        return dictionary;
    }
    public static HashSet<String> constructDictionary(List<Pair> pairList){
        HashSet<String> dictionary = new HashSet<String>();
        for(Pair current: pairList){

            dictionary = addSentenceToDictionary(dictionary, current.getSentence1());

            dictionary = addSentenceToDictionary(dictionary,current.getSentence2());

        }
        return dictionary;
    }
    public static void calculateSimilarityScoreAmongSentences(List<Pair> pairList) throws SLIB_Exception, IOException {

        /****************************BASELINE**********************************************/
        SimilarityMeasure measure = new BaselineMethod(constructDictionary(pairList));
      //  for(Pair currentPair: pairList){
          //  double similarityScore =measure.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
           // System.out.println(similarityScore);
       // }

        measure = new SimMetricFunctions();
        for(Pair currentPair: pairList){
              double similarityScore =measure.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
             System.out.println(similarityScore);
        }

        /**********************************************************************************/




        /********************************PARAGRAPH VECTOR**********************************/

        measure = new ParagraphVector(new ParagraphVectorModel("paragraphVector/sentence_vectors.txt"));
//        for(Pair currentPair: pairList){
//       //     double similarityScore =measure.getSimilarity(currentPair.getPairId(), currentPair.getPairId());
//     //       System.out.println(similarityScore);
//        }
        /**********************************************************************************/



        /***********************************LSA********************************************/

        measure = new LsaDocumentSimilarity();
//        for(Pair currentPair: pairList){
//           // double similarityScore =measure.getSimilarity(currentPair.getSentence1(), currentPair.getSentence2());
//           // System.out.println(similarityScore);
//        }
        /**********************************************************************************/
    }


    public static void main(String[] args) throws SLIB_Exception, IOException {
        List<Pair> pairList;
        FileOperations operations = new FileOperations();
        pairList = operations.readPairsFromFile("sentencePairsData/pairs.txt");

        calculateSimilarityScoreAmongSentences(pairList);


    }
}
