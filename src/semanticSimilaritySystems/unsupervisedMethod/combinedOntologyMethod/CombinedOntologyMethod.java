package semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod;
import org.openrdf.query.algebra.Str;
import semanticSimilaritySystems.core.Pair;
import semanticSimilaritySystems.core.Sentence;
import semanticSimilaritySystems.core.SimilarityMeasure;
import semanticSimilaritySystems.core.Word;
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
    static HashSet<String> stringDict;

    public HashSet<Word> addSentenceToDictionary(HashSet<Word> dictionary,  Sentence sentence){
         for(Word word:sentence.getWords()){
            if(!stringDict.contains(word.getWord().toLowerCase())){
                dictionary.add(word);
                stringDict.add(word.getWord().toLowerCase());
            }
        }
        return dictionary;
    }

    public HashSet<Word> constructDictionary(Sentence sentence1, Sentence sentence2){
        HashSet<Word> dictionary = new HashSet<Word>();
        stringDict = new HashSet<String>();
        dictionary = addSentenceToDictionary(dictionary, sentence1);
        dictionary = addSentenceToDictionary(dictionary, sentence2);

        return dictionary;
    }

    public static void main(String[] args) throws IOException {
        getMetamapResult("Tumorigenesis is a multistage process that involves multiple cell types Non Small Cell Lung Carcinoma.");
    }

    public static Sentence fillSentenceWithMetamapResults(String results){
        Sentence mappedSentence = new Sentence(); mappedSentence.setWords(new LinkedList<Word>());
        mappedSentence.setStringWords(new LinkedList<String>());
        boolean metamapping = false;
        boolean newPhrase = false;
        String[] split = results.split("\n");
        String preTerm="";
        int wordIndex = 0;
        for(String s: split){
            if(!s.equals("")){
                if(s.contains("Phrase:") ){
                    //SAVE PREVIOUS ONE
                    if(newPhrase && !metamapping) {
                   //     System.out.println("Not umls word: " + preTerm);
                        preTerm = replacePunctuations(preTerm);
                        if(!preTerm.equals("")) {
                            Word word = new Word();
                            word.setWord(preTerm);
                            word.setInUmls(false);
                            mappedSentence.getWords().add(wordIndex, word);
                            mappedSentence.getStringWords().add(wordIndex, word.getWord());
                            wordIndex++;
                        }
                    }

                    newPhrase = true;
                    metamapping = false;
                    s = s.replace("Phrase: " ,"");
                    s = s.trim();
                    preTerm = s;

                }
                else if(s.contains("Meta Mapping")){
                    if(metamapping && newPhrase)
                        newPhrase = false;
                    metamapping = true;
                }
                else{
                 //   System.out.println(s);
                    if(newPhrase && metamapping){
                        s = s.trim();
                        int index = 0;
                        if(s.contains("(")){
                            index = s.indexOf("(");
                            int endIndex= s.lastIndexOf(")");
                            s = s.substring(index+1, endIndex);
                        }
                        else{
                            index = s.indexOf("[");
                            s = s.substring(0, index);
                            String[] splitSentence = s.split("\\s+");
                            s = s.replace(splitSentence[0], "").trim();
                        }
                        Word newWord = new Word();
                        newWord.setInUmls(true);
                        newWord.setWord(s);
                        mappedSentence.getWords().add(wordIndex, newWord);
                        mappedSentence.getStringWords().add(wordIndex, newWord.getWord());
                       // System.out.println(s);

                    }

                }

            }

        }
        return mappedSentence;
    }

    public static Sentence getMetamapResult(String sentence) throws IOException {
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
        String results = batch.main(args);

        return fillSentenceWithMetamapResults(results);
    }

    public HashSet<String> filHash(Sentence sentence, HashSet<String> hash){
        for(Word s: sentence.getWords()){
            if(s.isInUmls() && !hash.contains(s.getWord())){
                hash.add(s.getWord());
            }

        }

        return hash;
    }

    public void writeAllUmlsTerms(String fileName, List<Pair> pairs) throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter(new File(fileName)));
        String line;HashSet<String> umlsTerms = new HashSet<String>();
int index = 1;
        for(Pair currentPair: pairs){
           Sentence sentence1 = getMetamapResult(currentPair.getSentence1());
           Sentence sentence2 =  getMetamapResult(currentPair.getSentence2());

            filHash(sentence1, umlsTerms);
            filHash(sentence2, umlsTerms);
            System.out.print(index + ". pair done!");

        }

        for(String s:umlsTerms){
            buffer.write(s);
            buffer.newLine();
        }

        buffer.close();
    }

    public double calculateOnlyWordnetScores(String word1, String word2){
        double similarityScore = 0.0;
        word1 = replacePunctuations(word1);
        word2 = replacePunctuations(word2);
        if(word1.equals(word2))
            return 1.0;

//        WordNetSimilarity wordNet_similarity_measure = new WordNetSimilarity();
//        double wordNet_similarity_score = wordNet_similarity_measure.getSimilarity(word1, word2);
//        if (wordNet_similarity_score != -1)
//            similarityScore = wordNet_similarity_score;

        return similarityScore;
    }

    public static String removeAllNonAsciiLetters(String text){
        text =  text.replaceAll("[^\\x00-\\x7F]", "");
        return text;
    }

    public double calculateOnlyUmlsScores(Word word1, Word word2) throws SLIB_Exception, IOException {
         double similarityScore = 0.0;

        if (word1.getWord().equalsIgnoreCase(word2.getWord())) {
            similarityScore = 1;
        }
         else {
            if (word1.isInUmls() && word2.isInUmls()) {

                UmlsSimilarity umls_similarity_measure = new UmlsSimilarity();
                double umls_similarity_score = umls_similarity_measure.getSimilarity(word1.getWord(), word2.getWord());

                if (umls_similarity_score > 0)
                    similarityScore = umls_similarity_score;

            }
        }
        return similarityScore;
    }

    public double calculateCombinedSimilarityScore(Word word1 , Word word2) throws SLIB_Exception, IOException {

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
        if (word1.getWord().equalsIgnoreCase(word2.getWord())) {
            similarityScore = 1;
        }
//        } else {
//            if (word1.isInUmls() && word2.isInUmls()) {
//
//                UmlsSimilarity umls_similarity_measure = new UmlsSimilarity();
//                double umls_similarity_score = umls_similarity_measure.getSimilarity(word1.getWord(), word2.getWord());
//
//                if (umls_similarity_score > 0)
//                    similarityScore = umls_similarity_score;
//
////            else {
////                WordNetSimilarity wordNet_similarity_measure = new WordNetSimilarity();
////                double wordNet_similarity_score = wordNet_similarity_measure.getSimilarity(word1, word2);
////                if (wordNet_similarity_score > 0)
////                    similarityScore = wordNet_similarity_score;
////            }
//            }
//        }

     //   System.out.println(word1.getWord() + " " + word2.getWord() + " " +similarityScore);

        return similarityScore;
    }

    public Vector<Double> constructVectorForSentence(Sentence sentence, HashSet<Word> dictionary) throws SLIB_Exception, IOException {

        /*LISTEYI DOGRU SORT EDEMEDIK DUZELTME VE KONTROL YAP!!
         *  */

        Vector<Double> vector = new Vector<Double>();
        int vectorIndex = 0;
        for(Word word: dictionary) {
            List<Double> scoresList = new ArrayList<Double>();
            if (sentence.getStringWords().contains(word.getWord())) {
                vector.add(vectorIndex, 1.0);
                vectorIndex++;

            } else {
                for (Word s : sentence.getWords()) {
                    double simScore = calculateOnlyUmlsScores(s, word);
                    scoresList.add(simScore);
                    if (simScore == 1.0)
                        break;
                }
                Collections.sort(scoresList);
                vector.add(vectorIndex, scoresList.get(scoresList.size() - 1));
                vectorIndex++;
            }
        }
        return  vector;
    }


    public static String replacePunctuations(String phrase){

        phrase = phrase.trim();
        phrase = phrase.replaceAll("\\.","");
        phrase = phrase.replaceAll(";","");
        phrase = phrase.replaceAll("-","");
        phrase = phrase.replaceAll(":","");
        phrase = phrase.replaceAll(",","");
        phrase = phrase.replaceAll("_","");


        return phrase.toLowerCase();
    }

    public static Sentence returnSentence(String sentence){
        Sentence res = new Sentence();
        res.setStringWords(new LinkedList<String>());
        res.setWords(new LinkedList<Word>());
        String[] split = sentence.split("\\s+");
        int index = 0;
        for(String s: split){
            res.getStringWords().add(index, s.toLowerCase());
            Word newW = new Word();
            newW.setWord(s.toLowerCase());
            res.getWords().add(index, newW);
        }

        return res;
    }

    public double getSimilarity(String sentence1, String sentence2) throws SLIB_Exception, IOException {

        Sentence mappedSentence1 = getMetamapResult(removeAllNonAsciiLetters(sentence1));
        Sentence mappedSentence2 = getMetamapResult(removeAllNonAsciiLetters(sentence2));

        if(mappedSentence1.getStringWords().size() == 0 || mappedSentence2.getStringWords().size() == 0){
            System.out.println("METAMAP RESULT");
            return 0;
        }
//
//        Sentence mappedSentence1 =returnSentence(sentence1);
//        Sentence mappedSentence2 = returnSentence(sentence2);

        HashSet<Word> dictionary  = constructDictionary(mappedSentence1, mappedSentence2);

        Vector<Double> vector1 = constructVectorForSentence(mappedSentence1, dictionary);
        Vector<Double> vector2 = constructVectorForSentence(mappedSentence2, dictionary);

        CosineSimilarity similarityMeasure = new CosineSimilarity(vector1, vector2);
        double similarityScore = similarityMeasure.calculateDistanceAmongVectors();
        return similarityScore;
    }
}