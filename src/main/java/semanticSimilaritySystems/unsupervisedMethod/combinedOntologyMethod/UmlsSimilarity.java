package semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod;

/**
 * Created by orhan on 31.01.2016.
 */

import java.io.*;

import semanticSimilaritySystems.core.SimilarityMeasure;

import slib.utils.ex.SLIB_Exception;


public class UmlsSimilarity implements SimilarityMeasure {

    public double getSimilarity(String word1, String word2) throws SLIB_Exception, IOException {

        return calculateUmlsPairScore(word1, word2);
    }

    public static void main(String[] args) throws Exception {

        System.out.println(calculateUmlsPairScore("hand", "skull"));

    }


     public static double calculateUmlsPairScore(String word1, String word2) throws SLIB_Exception, IOException {

         double similarityScore = 0;
         String[] command = {"query-umls-similarity-webinterface.pl", "--measure","cdist","--sab" ,"OMIM,MSH",
                 "--rel" ,"PAR/CHD", word1, word2};
         ProcessBuilder builder = new ProcessBuilder( command );
         File commandDir = new File(com.google.common.io.Resources.getResource("UMLS-Similarity-1.47/utils/").getFile());
         builder.directory(commandDir);
         builder.redirectErrorStream(true);
         Process p = builder.start();

         BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
         String line;
         while (true) {
             line = r.readLine();
             if (line == null) { break; }
            // System.out.println(line);
             if(line.contains(word1) && line.contains(word2)){
                 String[] split = line.split("<>");
                // System.out.println(split[0]);
                 try {
                     similarityScore=Double.parseDouble(split[0]);

                 } catch (NumberFormatException e) {
                 }
             }

         }
         return similarityScore;
    }
}
