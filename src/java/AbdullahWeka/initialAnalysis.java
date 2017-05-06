/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sql.rowset.CachedRowSet;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.core.converters.ArffSaver;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.attribute.Discretize;
/**
 *
 * @author Abdullah
 */
@ManagedBean
@RequestScoped
public class initialAnalysis {

    private CachedRowSet crs = null;
    private filesBean fb;
    private Evaluation eval;
    private String prediction;
    private String correctPercentage;

    public initialAnalysis() {
    
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getCorrectPercentage() {
        return correctPercentage;
    }

    public void setCorrectPercentage(String correctPercentage) {
        this.correctPercentage = correctPercentage;
    }
    
    
    
    
    
    public void analyze(String filename){
    
         try {
            BufferedReader breader = null;
            breader = new BufferedReader(new FileReader("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/"+filename+".arff"));
            Instances toTrain = new Instances (breader);
            breader.close();
            
            Instances train = applyFilters(toTrain);
            
            
            train.setClassIndex(train.numAttributes() -1);
            J48 j48 = new J48();
            j48.buildClassifier(train);
            eval = new Evaluation(train);
            eval.crossValidateModel(j48, train, 10, new Random(1));
            //prediction = eval.toSummaryString("\nResults\n======\n",true);
            
            
            
            ArffSaver saver = new ArffSaver();
            saver.setInstances(train);
            saver.setFile(new File("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/"+filename+"SUCCESSDED.arff"));
            saver.writeBatch();
            
    }catch (FileNotFoundException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
    
    public Instances applyFilters(Instances train)
    {
        try {
            //Sparse
            NonSparseToSparse sp = new NonSparseToSparse();
            sp.setInputFormat(train);
            
            /*
            //Attribute selection
            AttributeSelection filter = new AttributeSelection();
            CfsSubsetEval eval = new CfsSubsetEval();
            GreedyStepwise search = new GreedyStepwise();
            search.setSearchBackwards(true);
            filter.setEvaluator(eval);
            filter.setSearch(search);
            filter.setInputFormat(train);
            */
            /*
            String [] options = new String[4];
            options[0] = "-B"; 
            options[1] = "2";
            options[2] = "-R";
            options[3] = "" + train.numAttributes();
            Discretize discretize = new Discretize();
            discretize.setOptions(options);
            discretize.setInputFormat(train);
            */
            
            
            
            Instances newData = Filter.useFilter(train, sp);
            //newData = Filter.useFilter(newData, filter);
            //newData = Filter.useFilter(newData, discretize);

            return newData;
        } catch (Exception ex) {
            Logger.getLogger(initialAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
}
