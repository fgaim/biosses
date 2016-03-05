package semanticSimilaritySystems.supervisedMethod.regressors;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;

import java.io.FileReader;
import java.util.Random;

/**
 * Created by orhan on 31.01.2016.
 */
public class MultiLayerPerceptron {
    public static void main(String[] args) throws Exception {
        MultiLayerPerceptron m  = new MultiLayerPerceptron();
        m.classify();

    }
    public  void classify(){

        try{
            FileReader trainreader = new FileReader("D:\\IntelligentChatBot\\ipec\\scripts\\trainingData.arff");


            Instances train = new Instances(trainreader);

            train.setClassIndex(train.numAttributes() - 1);

            MultilayerPerceptron mlp = new MultilayerPerceptron();
            mlp.setOptions(Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 4"));


            mlp.buildClassifier(train);

            Evaluation eval = new Evaluation(train);
            //evaluation.crossValidateModel(rf, trainData, numFolds, new Random(1));
            eval.crossValidateModel(mlp, train, 3, new Random(1));
            // eval.evaluateModel(mlp, train);
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            trainreader.close();


        } catch(Exception ex){

            ex.printStackTrace();

        }


    }
}
