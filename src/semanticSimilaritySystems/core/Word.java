package semanticSimilaritySystems.core;

/**
 * Created by gizem on 06.03.2016.
 */
public class Word {
    private boolean isInUmls;
    private String word;

    public boolean isInUmls() {
        return isInUmls;
    }

    public void setInUmls(boolean inUmls) {
        isInUmls = inUmls;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
