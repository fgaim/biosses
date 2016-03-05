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

    public double getSimilarity(String word1, String word2) throws SLIB_Exception {

        return calculateUmlsPairScore(word1, word2);
    }

    public static void main(String[] args) throws Exception {


        //calculateUmlsPairScore("", "");
        method();

    }
    public static void method() throws Exception {


    }
     public static double calculateUmlsPairScore(String word1, String word2) throws SLIB_Exception {


        return 0;

    }
}
