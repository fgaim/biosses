package semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod;

import semanticSimilaritySystems.core.Pair;
import semanticSimilaritySystems.core.SimilarityMeasure;
import similarityMeasures.CosineSimilarity;
import slib.utils.ex.SLIB_Exception;

import java.io.IOException;
import java.lang.invoke.SwitchPoint;
import java.util.*;

/**
 * Created by orhan on 31.01.2016.
 */
public class CombinedOntologyMethod implements SimilarityMeasure{

    private UmlsSimilarity umls_similarity_score;
    private WordNetSimilarity wordnet_similarity_score;

    public static HashSet<String> addSentenceToDictionary(HashSet<String> dictionary, String sentence){
        String[] split = sentence.toLowerCase().split("\\s+");
        for(String word:split){
            if(!dictionary.contains(word))
                dictionary.add(word);
        }

        return dictionary;
    }
    public static HashSet<String> constructDictionary(String sentence1, String sentence2){
        HashSet<String> dictionary = new HashSet<>();

        dictionary = addSentenceToDictionary(dictionary, sentence1);

        dictionary = addSentenceToDictionary(dictionary, sentence2);

        return dictionary;
    }

    public static double calculateSimilarityScore(String word1 , String word2) throws SLIB_Exception {

        /*
        *EGER WORDNETTE ISTENILEN SKOR YOKSA, SIFIR DONDU ISE UMLS-SIM CALISTIRILMALI.
         * WEIGHTED SCORE YAPILABILIR, EGER UMLS'DE MATCH YAPILDIYSA AGIRLIGI DAHA FAZLA OLMALI IKI KAT FALAN
          *
          * IDF SKORLARI HESAA KATILABILIR
          * AYNI ZAMANDA MAKALEDEKI YONTEME BENZER SEKILDE
          * KELIME ORDERLARI DA HESABA KATILABILIR
          * EGER BASARILI CIKARSA,
          * KELIME ORDERLARINI SUPERVISED YAKLASIMIMIZDA DENEYECEGIZ
        *
        * */


        UmlsSimilarity umls_similarity_measure = new UmlsSimilarity();
        double umls_similarity_score = umls_similarity_measure.getSimilarity(word1, word2);

        WordNetSimilarity wordNet_similarity_measure = new WordNetSimilarity();
        double wordNet_similarity_score = wordNet_similarity_measure.getSimilarity(word1,word2);


        return wordNet_similarity_score;
    }
    public static Vector<Double> constructVectorForSentence(String sentence, HashSet<String> dictionary) throws SLIB_Exception {

        /*LISTEYI DOGRU SORT EDEMEDIK DUZELTME VE KONTROL YAP!!
         *  */
        Vector<Double> vector = new Vector<>();
        String[] split = sentence.toLowerCase().split("\\s+");
        int vectorIndex = 0;
        for(String word: dictionary){
            List<Double> scoresList = new ArrayList<>();
            for(String s: split){
                scoresList.add(calculateSimilarityScore(s, word));
            }
            Arrays.sort(scoresList.toArray());
            vector.add(vectorIndex, scoresList.get(0));
            vectorIndex++;
        }

        return  vector;
    }


    @Override
    public double getSimilarity(String sentence1, String sentence2) throws SLIB_Exception, IOException {

        HashSet<String> dictionary  = constructDictionary(sentence1, sentence2);

        Vector<Double> vector1 = constructVectorForSentence(sentence1, dictionary);
        Vector<Double> vector2 = constructVectorForSentence(sentence2, dictionary);

        CosineSimilarity similarityMeasure = new CosineSimilarity(vector1, vector2);
        Double similarityScore = similarityMeasure.calculateDistanceAmongVectors();
        return similarityScore;
    }
}
