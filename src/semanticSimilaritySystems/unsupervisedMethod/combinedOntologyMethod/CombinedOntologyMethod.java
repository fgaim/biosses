package semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod;
import semanticSimilaritySystems.core.SimilarityMeasure;
import similarityMeasures.CosineSimilarity;
import slib.utils.ex.SLIB_Exception;

import java.io.*;
import java.util.*;

/**
 * Created by orhan on 31.01.2016.
 */
public class CombinedOntologyMethod implements SimilarityMeasure{

    private UmlsSimilarity umls_similarity_score;
    private WordNetSimilarity wordnet_similarity_score;

    public HashSet<String> addSentenceToDictionary(HashSet<String> dictionary, String sentence){
        String[] split = sentence.toLowerCase().split("\\s+");
        for(String word:split){
            if(!dictionary.contains(word))
                dictionary.add(word);
        }

        return dictionary;
    }
    public String removePunctuations(String word){

        word = word.replaceAll("\\.", "");
        word = word.replaceAll(",", "");
        word = word.replaceAll(";", "");
        word = word.replaceAll(":", "");
        word = word.replaceAll("\\?", "");
        word = word.replaceAll("\\(", "");
        word = word.replaceAll("\\)", "");
        word = word.replaceAll("\\[", "");
        word = word.replaceAll("\\]", "");
        word = word.replaceAll("\\{", "");

        word = word.replaceAll("\\}", "");
        word = word.replaceAll("\\!", "");
        return word;
    }
    public HashSet<String> constructDictionary(String sentence1, String sentence2){
        HashSet<String> dictionary = new HashSet<String>();

        dictionary = addSentenceToDictionary(dictionary, sentence1);

        dictionary = addSentenceToDictionary(dictionary, sentence2);

        return dictionary;
    }
    public static void main(String args[]) throws IOException {
        getMetamapResult("It has recently been shown that Craf is essential for Kras G12D-induced NSCLC." );

        getMetamapResult("Tumorigenesis is a multistage process that involves multiple cell types.");
    }

    public static String getMetamapResult(String sentence) throws IOException {

        BufferedWriter buffer = new BufferedWriter(new FileWriter(new File("input.txt")));
        buffer.write(sentence);
        buffer.close();

        GenericBatchNew batch = new GenericBatchNew();
        String[] args = new String[5];
        args[0] = "--command";
        args[1] = "metamap";
        args[2] = "--email";
        args[3] = "gizemsogancioglu@gmail.com";
        args[4] = "input.txt";
        batch.main(args);

        return "";
    }
    public double calculateOnlyWordnetScores(String word1, String word2){
        double similarityScore = 0.0;

        WordNetSimilarity wordNet_similarity_measure = new WordNetSimilarity();
        double wordNet_similarity_score = wordNet_similarity_measure.getSimilarity(word1, word2);
        if (wordNet_similarity_score > 0)
            similarityScore = wordNet_similarity_score;

        return similarityScore;
    }

    public double calculateOnlyUmlsScores(String word1, String word2) throws SLIB_Exception, IOException {
        double similarityScore = 0.0;
        UmlsSimilarity umls_similarity_measure = new UmlsSimilarity();
        double umls_similarity_score = umls_similarity_measure.getSimilarity(word1, word2);

        if(umls_similarity_score > 0)
            similarityScore = umls_similarity_score;


        return similarityScore;
    }
    public double calculateCombinedSimilarityScore(String word1 , String word2) throws SLIB_Exception, IOException {

        double similarityScore = 0.0;
        /*
        *EGER WORDNETTE ISTENILEN SKOR YOKSA, SIFIR DONDU ISE UMLS-SIM CALISTIRILMALI.
         * WEIGHTED SCORE YAPILABILIR, EGER UMLS'DE MATCH YAPILDIYSA AGIRLIGI DAHA FAZLA OLMALI IKI KAT FALAN
          *
          * IDF SKORLARI HESABA KATILABILIR
          * AYNI ZAMANDA MAKALEDEKI YONTEME BENZER SEKILDE
          * KELIME ORDERLARI DA HESABA KATILABILIR
          * EGER BASARILI CIKARSA,
          * KELIME ORDERLARINI SUPERVISED YAKLASIMIMIZDA DENEYECEGIZ
        *
        * */
        if(word1.equalsIgnoreCase(word2)){
            similarityScore = 1;
        }

        else {
            UmlsSimilarity umls_similarity_measure = new UmlsSimilarity();
            double umls_similarity_score = umls_similarity_measure.getSimilarity(word1, word2);

            if(umls_similarity_score > 0)
                similarityScore = umls_similarity_score;
//            else {
//                WordNetSimilarity wordNet_similarity_measure = new WordNetSimilarity();
//                double wordNet_similarity_score = wordNet_similarity_measure.getSimilarity(word1, word2);
//                if (wordNet_similarity_score > 0)
//                    similarityScore = wordNet_similarity_score;
//            }
        }

        //System.out.println(word1 + " " + word2 + " " +similarityScore);

        return similarityScore;
    }

    public Vector<Double> constructVectorForSentence(String sentence, HashSet<String> dictionary) throws SLIB_Exception, IOException {

        /*LISTEYI DOGRU SORT EDEMEDIK DUZELTME VE KONTROL YAP!!
         *  */

        Vector<Double> vector = new Vector<Double>();
        String[] split = sentence.toLowerCase().split("\\s+");
        int vectorIndex = 0;
        for(String word: dictionary){
            List<Double> scoresList = new ArrayList<Double>();
            for(String s: split){
                scoresList.add(calculateCombinedSimilarityScore(removePunctuations(s), removePunctuations(word)));
            }
            Collections.sort(scoresList);
            vector.add(vectorIndex, scoresList.get(scoresList.size()-1));
            vectorIndex++;
        }

        return  vector;
    }


    public double getSimilarity(String sentence1, String sentence2) throws SLIB_Exception, IOException {

        HashSet<String> dictionary  = constructDictionary(sentence1, sentence2);

        Vector<Double> vector1 = constructVectorForSentence(sentence1, dictionary);
        Vector<Double> vector2 = constructVectorForSentence(sentence2, dictionary);

        CosineSimilarity similarityMeasure = new CosineSimilarity(vector1, vector2);
        double similarityScore = similarityMeasure.calculateDistanceAmongVectors();
        return similarityScore;
    }
}