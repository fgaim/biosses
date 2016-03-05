package semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod;

/**
 * Created by orhan on 31.01.2016.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.openrdf.model.URI;
import semanticSimilaritySystems.core.SimilarityMeasure;
import slib.graph.io.conf.GDataConf;
import slib.graph.io.loader.GraphLoaderGeneric;
import slib.graph.io.loader.bio.snomedct.GraphLoaderSnomedCT_RF2;
import slib.graph.io.util.GFormat;
import slib.graph.model.graph.G;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.sml.sm.core.engine.SM_Engine;
import slib.sml.sm.core.metrics.ic.utils.IC_Conf_Topo;
import slib.sml.sm.core.metrics.ic.utils.ICconf;
import slib.sml.sm.core.utils.SMConstants;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.Timer;
public class UmlsSimilarity implements SimilarityMeasure {
    @Override
    public double getSimilarity(String word1, String word2) throws SLIB_Exception {

        return calculateUmlsPairScore(word1, word2);
    }

    public static void main(String[] args) throws Exception {


        //calculateUmlsPairScore("", "");
        method();

    }
    public static void method() throws Exception {

        // Some variables defining the locations of the files from which the Snomed-CT will be loaded
        // The loader we use requires Snomed-CT expressed using RF2 format.

        String DATA_DIR = "C:\\Users\\orhan\\Documents\\SemanticSimilarityEstimationSystem\\resources\\umlsData\\"; // this is the directory in which the downloaded snomed version has been extracted
        String SNOMEDCT_VERSION = "20160131";
        String SNOMEDCT_DIR = DATA_DIR + "/SnomedCT_Release_INT_" + SNOMEDCT_VERSION + "/RF2Release/Full/Terminology";
        String SNOMEDCT_CONCEPT = SNOMEDCT_DIR + "/sct2_Concept_Full_INT_" + SNOMEDCT_VERSION + ".txt";
        String SNOMEDCT_RELATIONSHIPS = SNOMEDCT_DIR + "/sct2_Relationship_Full_INT_" + SNOMEDCT_VERSION + ".txt";
        String SNOMEDCT_DESCRIPTION = SNOMEDCT_DIR + "/sct2_Description_Full-en_INT_" + SNOMEDCT_VERSION + ".txt";



        // We configure a timer
        Timer t = new Timer();
        t.start();

        // We create an in-memory graph in which we will load Snomed-CT.
        // Notice that Snomed-CT is quite large (e.g. version 20120731 contains 296433 concepts and872318 relationships ).
        // You will need to allocate extra memory to the JVM e.g add -Xmx3000m parameter to allocate 3Go.
        URIFactory factory = URIFactoryMemory.getSingleton();

        String uri_prefix = "http://snomedct/";

        Map<String, Set<URI>> index = loadMapIndex(SNOMEDCT_DESCRIPTION, factory, uri_prefix);

        Set<URI> heartURIs = index.get("heart");
        System.out.println("URIs associated to the label heart: " + heartURIs);

        Set<URI> myocardiumURI = index.get("bcl2");
        System.out.println("URIs associated to the label myocardium: " + myocardiumURI);


//        //UtilDebug.exit();
//
//        URI snomedctURI = factory.getURI(uri_prefix);
//        G g = new GraphMemory(snomedctURI);
//
//        GDataConf conf = new GDataConf(GFormat.SNOMED_CT_RF2);
//        conf.addParameter(GraphLoaderSnomedCT_RF2.ARG_CONCEPT_FILE, SNOMEDCT_CONCEPT);
//        conf.addParameter(GraphLoaderSnomedCT_RF2.ARG_RELATIONSHIP_FILE, SNOMEDCT_RELATIONSHIPS);
//
//        GraphLoaderGeneric.populate(conf, g);
//
//        System.out.println(g.toString());
//
//        // We compute the similarity between the concepts
//        // associated to Heart	and Myocardium, i.e. 80891009 and 74281007 respectively
//        // We first build URIs correspondind to those concepts
//        //URI heartURI = factory.getURI(snomedctURI.stringValue() + "80891009"); // i.e http://snomedct/230690007
//        URI myocardiumURI = factory.getURI(snomedctURI.stringValue() + "74281007");
//
//        // First we configure an intrincic IC
//        ICconf icConf = new IC_Conf_Topo(SMConstants.FLAG_ICI_SECO_2004);
//        // Then we configure the pairwise measure to use, we here choose to use Lin formula
//        SMconf smConf = new SMconf(SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998, icConf);
//
////        UtilDebug.exit();
//        // We define the engine used to compute the similarity
//        SM_Engine engine = new SM_Engine(g);
//
//        double sim;
//        for (URI heartURI : heartURIs) {
//
//            // We retrieve the vertices corresponding to the two concepts
//            sim = engine.compare(smConf, heartURI, myocardiumURI);
//            System.out.println("uri heart: "+heartURI);
//            System.out.println("Similarity Heart/Myocardium: " + sim);
//
//        }
//        /*
//         * Notice that the first computation is expensive as the engine compute the IC and extra information
//         * which are cached by the engine
//         * Let's perform 100000 random computations (we only print some results).
//         * We retrieve the set of vertices as a list
//         */
//        int totalComparison = 100000;
//
//        List<URI> listVertices = new ArrayList<URI>(g.getV());
//        int nbConcepts = listVertices.size();
//        int id1, id2;
//        URI c1, c2;
//        String idC1, idC2;
//        Random r = new Random();
//
//        for (int i = 0; i < totalComparison; i++) {
//            id1 = r.nextInt(nbConcepts);
//            id2 = r.nextInt(nbConcepts);
//
//            c1 = listVertices.get(id1);
//            c2 = listVertices.get(id2);
//
//            sim = engine.compare(smConf, c1, c2);
//
//            if ((i + 1) % 1000 == 0) {
//                idC1 = c1.getLocalName();
//                idC2 = c2.getLocalName();
//
//                System.out.println("Sim " + (i + 1) + "/" + totalComparison + "\t" + idC1 + "/" + idC2 + ": " + sim);
//            }
//        }
        t.stop();
        t.elapsedTime();
    }
    public static Map<String, Set<URI>> loadMapIndex(String SNOMEDCT_DESCRIPTION, URIFactory factory, String uri_prefix) throws Exception {

        System.out.println("Loading index from " + SNOMEDCT_DESCRIPTION);
        Map<String, Set<URI>> index = new HashMap();

        try (BufferedReader br = new BufferedReader(new FileReader(SNOMEDCT_DESCRIPTION))) {

            String line = br.readLine();

            while (line != null) {
                String[] data = line.split("\t");
                String id = data[4];
                String label = data[7].toLowerCase();

                if (!index.containsKey(label)) {
                    index.put(label, new HashSet());
                }
                URI u = factory.getURI(uri_prefix + id);
                index.get(label).add(u);

//                System.out.println(id + "\t" + label + "\t" + u);
                line = br.readLine();
            }

        }
        System.out.println("Index loaded");

        return index;

    }
    public static double calculateUmlsPairScore(String word1, String word2) throws SLIB_Exception {

        // Some variables defining the locations of the files from which the Snomed-CT will be loaded
        // The loader we use requires Snomed-CT expressed using RF2 format.
        String DATA_DIR = "C:\\Users\\orhan\\Documents\\SemanticSimilarityEstimationSystem\\resources\\umlsData\\"; // this is the directory in which the downloaded snomed version has been extracted
        String SNOMEDCT_VERSION = "20160131";
        String SNOMEDCT_DIR = DATA_DIR + "/SnomedCT_Release_INT_" + SNOMEDCT_VERSION + "/RF2Release/Full/Terminology";
        String SNOMEDCT_CONCEPT = SNOMEDCT_DIR + "/sct2_Concept_Full_INT_" + SNOMEDCT_VERSION + ".txt";
        String SNOMEDCT_RELATIONSHIPS = SNOMEDCT_DIR + "/sct2_Relationship_Full_INT_" + SNOMEDCT_VERSION + ".txt";

        // We configure a timer
        Timer t = new Timer();
        t.start();

        // We create an in-memory graph in which we will load Snomed-CT.
        // Notice that Snomed-CT is quite large (e.g. version 20120731 contains 296433 concepts and872318 relationships ).
        // You will need to allocate extra memory to the JVM e.g add -Xmx3000m parameter to allocate 3Go.
        URIFactory factory = URIFactoryMemory.getSingleton();
        URI snomedctURI = factory.getURI("http://snomedct/");
        G g = new GraphMemory(snomedctURI);

        GDataConf conf = new GDataConf(GFormat.SNOMED_CT_RF2);
        conf.addParameter(GraphLoaderSnomedCT_RF2.ARG_CONCEPT_FILE, SNOMEDCT_CONCEPT);
        conf.addParameter(GraphLoaderSnomedCT_RF2.ARG_RELATIONSHIP_FILE, SNOMEDCT_RELATIONSHIPS);

        GraphLoaderGeneric.populate(conf, g);

        System.out.println(g.toString());

        // We compute the similarity between the concepts
        // associated to Heart  and Myocardium, i.e. 80891009 and 74281007 respectively
        // We first build URIs correspondind to those concepts
        URI heartURI = factory.getURI(snomedctURI.stringValue() + "80891009"); // i.e http://snomedct/230690007
       // System.out.println(factory.getURI("heart"));
        URI myocardiumURI = factory.getURI(snomedctURI.stringValue() + "74281007");

        // First we configure an intrincic IC
        ICconf icConf = new IC_Conf_Topo(SMConstants.FLAG_ICI_SECO_2004);
        // Then we configure the pairwise measure to use, we here choose to use Lin formula
        SMconf smConf = new SMconf(SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998, icConf);

//        UtilDebug.exit();

        // We define the engine used to compute the similarity
        SM_Engine engine = new SM_Engine(g);

        // We retrieve the vertices corresponding to the two concepts


        double sim = engine.compare(smConf, heartURI, myocardiumURI);


        System.out.println("Similarity Heart/Myocardium: " + sim+ " " + heartURI.getLocalName() +" " + heartURI.toString());

        /*
         * Notice that the first computation is expensive as the engine compute the IC and extra information
         * which are cached by the engine
         * Let's perform 100000 random computations (we only print some results).
         * We retrieve the set of vertices as a list
         */

        return 0;

    }
}
