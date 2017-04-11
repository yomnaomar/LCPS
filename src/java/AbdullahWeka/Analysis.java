/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;

import Database.Access.DatabaseAccess;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author Abdullah
 */
@ManagedBean
@SessionScoped
public class Analysis {

    private int id;

    private CachedRowSet crs = null;
    private String prediction;
    private double CorrectPercentage;
    private double correctInstances;
    private boolean valid;
    private filesBean fb;
    private Evaluation eval;
    private String extraTestInfo;
    private String matrix;
    private Instances train;
    private J48 j48;
    private String attributeSelectedName;
    private int maxInstances;
    private double maxAttValue;
    private double minAttValue;

    public String getExtraTestInfo() {

        try {
            extraTestInfo = ("Correct % = " + eval.pctCorrect());
            extraTestInfo += ("Incorrect % = " + eval.pctIncorrect());
            extraTestInfo += ("AUC = " + eval.areaUnderROC(1));
            extraTestInfo += ("kappa = " + eval.kappa());
            extraTestInfo += ("MAE = " + eval.meanAbsoluteError());
            extraTestInfo += ("RMSE = " + eval.rootMeanSquaredError());
            extraTestInfo += ("RAE = " + eval.relativeAbsoluteError());
            extraTestInfo += ("RRSE = " + eval.rootRelativeSquaredError());
            extraTestInfo += ("Precision = " + eval.precision(1));
            extraTestInfo += ("Recall = " + eval.recall(1));
            extraTestInfo += ("fMeasure = " + eval.fMeasure(1));
            extraTestInfo += ("Error Rate = " + eval.errorRate());
            return extraTestInfo;
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getMatrix() {

        try {
            return (eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public Analysis() {
        fb = new filesBean();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Evaluation getEval() {
        return eval;
    }

    public filesBean getFb() {
        return fb;
    }

    public String getPrediction() {
        return prediction;
    }

    public boolean isValid() {
        return valid;
    }

    public void setEval(Evaluation eval) {
        this.eval = eval;
    }

    public void setFb(filesBean fb) {
        this.fb = fb;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setCorrectInstances(double correctInstances) {
        this.correctInstances = correctInstances;
    }

    public void setCorrectPercentage(double CorrectPercentage) {
        this.CorrectPercentage = CorrectPercentage;
    }

    public double getCorrectPercentage() {
        return eval.correct() / eval.numInstances();
    }

    public double getCorrectInstances() {
        return correctInstances;
    }

    public void setTrain(Instances train) {
        this.train = train;
    }

    public CachedRowSet getCrs() {
        return crs;
    }

    public Instances getTrain() {
        return train;
    }

    public J48 getJ48() {
        return j48;
    }

    public void setJ48(J48 j48) {
        this.j48 = j48;
    }
    
    
        public String getAttributeSelectedName() {
        return attributeSelectedName;
    }

    public void setAttributeSelectedName(String attributeSelectedName) {
        this.attributeSelectedName = attributeSelectedName;
    }

    public int getMaxInstances() {
        return train.numInstances();
    }

    public void setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
    }

    public double getMaxAttValue() {
        return maxAttValue;
    }

    public double getMinAttValue() {
        return minAttValue;
    }

    public void setMaxAttValue(double maxAttValue) {
        this.maxAttValue = maxAttValue;
    }

    public void setMinAttValue(double minAttValue) {
        this.minAttValue = minAttValue;
    }
    
    
    

    public void analyze(int i) {

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            crs.setCommand("select * from FILES where fileID = ?");
            crs.setInt(1, i);
            crs.execute();
            if (crs.next()) {
                fb.setFilename(crs.getString("fileName"));
                fb.setFiletype(crs.getString("fileType"));
                fb.setPredicts(crs.getString("filePrediction"));
            }

            BufferedReader breader = null;
            breader = new BufferedReader(new FileReader("D:/Documents/University Docs/Spring 2017/CMP 491 - Senior Design II/Testing/AllfilesForProgram/" + fb.getFilename() + "." + fb.getFiletype()));

            train = new Instances(breader);
            train.setClassIndex(train.numAttributes() - 1);
            breader.close();
            j48 = new J48();
            j48.buildClassifier(train);

            eval = new Evaluation(train);
            eval.crossValidateModel(j48, train, 10, new Random(1));
            prediction = eval.toSummaryString("\nResults\n======\n", true);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDonutChartValues() {
        String classes = train.classAttribute().toString();
        String[] tempClasses = classes.split("\\{");
        String[] tempClasses2 = tempClasses[1].split("\\}");
        String Labels = tempClasses2[0];
        String[] att = Labels.split(",");
        String string = "[['Labels','Correctly Classified %'],";
        double[][] temp = eval.confusionMatrix();
        double[] values = new double[train.numClasses()];
        for (int i = 0; i < train.numClasses(); i++) {
            values[i] = temp[i][i] / train.numInstances();
            string += "['" + att[i] + "'," + values[i] + "],";
        }
        string = string.substring(0, string.length() - 1) + "]";
        System.out.println(string);
        return string;
    }
    
         public String graphValues()
    {
            String classes = train.attribute(5).toString();
            //System.out.println(classes);
            String[] tempClasses = classes.split("@attribute");
            //String[] tempClasses2 = tempClasses[1].split("\\}");
            String Labels = tempClasses[1];
            //String[] att = Labels.split(",");
            String string = "[['Number of Instances','"+Labels+"'],";
            //double[][] temp = eval.confusionMatrix();
            double[] values = new double[train.numInstances()];
            maxAttValue = 0;
            minAttValue = 1000000;
            for (int i = 0; i < train.numInstances(); i++) {
                values[i] = train.get(i).value(5);
                //values[i] = (temp[i][i]);
                if(values[i] > maxAttValue)
                    maxAttValue = values[i] ;
                if(values[i] < minAttValue)
                    minAttValue = values[i] ;
                string += "['" + i + "'," + values[i] + "],";
            }
            //string += "['Incorrect'," + eval.incorrect() + "],";
            //string += "['Work',11],['Eat',      2]]";
            string = string.substring(0, string.length() - 1) + "]";
            //System.out.println(string);
            //eval.incorrect();
            return string;
  
    }
         
    public String attributeName(int i)
    {
        
        String attributeSelectedName = train.attribute(i).toString().split("@attribute")[1];

        return attributeSelectedName;
    }

        public String graphValuesCorrect()
    {
            String classes = train.classAttribute().toString();
            String[] tempClasses = classes.split("\\{");
            String[] tempClasses2 = tempClasses[1].split("\\}");
            String Labels = tempClasses2[0];
            String[] att = Labels.split(",");
            String string = "[['Labels','Correctly Classified %'],";
            double[][] temp = eval.confusionMatrix();
            double[] values = new double[train.numClasses()];
            for (int i = 0; i < train.numClasses(); i++) {
                values[i] = (temp[i][i]);
                string += "['" + att[i] + "'," + values[i] + "],";
            }
            string += "['Incorrect'," + eval.incorrect() + "],";
            string = string.substring(0, string.length() - 1) + "]";

            return string;
  
    }
        
             public String graphValuesIncorrect()
    {
            String classes = train.classAttribute().toString();
            String[] tempClasses = classes.split("\\{");
            String[] tempClasses2 = tempClasses[1].split("\\}");
            String Labels = tempClasses2[0];
            String[] att = Labels.split(",");
            String string = "[['Labels','Incorrectly Classified %'],";
            double[][] temp = eval.confusionMatrix();
            double[] values = new double[train.numClasses()];
            for (int i = 0; i < train.numClasses(); i++) {
                
                for(int j =0; j < train.numClasses();j++)
                {
                    if (i != j)
                        values[i] += (temp[i][j]);
                }

                string += "['" + att[i] + "'," + values[i] + "],";
            }

            string = string.substring(0, string.length() - 1) + "]";

            return string;
  
    }
    
    
    
    
    
    
}