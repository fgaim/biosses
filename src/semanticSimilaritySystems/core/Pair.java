package semanticSimilaritySystems.core;

/**
 * Created by orhan on 10.02.2016.
 */
public class Pair {
    private String sentence1;
    private String sentence2;
    private double similarityScore;
    private String pairId;

    public Pair(String sentence1, String sentence2){

        this.sentence1 = sentence1;
        this.sentence2 = sentence2;
    }

    public Pair(String pairId, String sentence1, String sentence2){
        this.pairId = pairId;
        this.sentence1 = sentence1;
        this.sentence2 = sentence2;
    }
    public Pair(){

    }
    public String getSentence1() {
        return sentence1;
    }

    public void setSentence1(String sentence1) {
        this.sentence1 = sentence1;
    }

    public String getSentence2() {
        return sentence2;
    }

    public void setSentence2(String sentence2) {
        this.sentence2 = sentence2;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getPairId() {
        return pairId;
    }

    public void setPairId(String pairId) {
        this.pairId = pairId;
    }
}
