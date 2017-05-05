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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
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
    private Evaluation evalNB;
    private Evaluation evalIBK;
    private String extraTestInfo;
    private String matrix;
    private Instances train;
    private J48 j48;
    private NaiveBayes naiveBayes;
    private IBk ibk;
    private String attributeSelectedName;
    private int maxInstances;
    private double maxAttValue;
    private double minAttValue;
    private int attributeID; 
    private String chosenAlgo;
    private String attributes[];

    public String getExtraTestInfo() {

        try {/*
            extraTestInfo = ("Correct % = " + eval.pctCorrect());
            extraTestInfo += ("Incorrect % = " + eval.pctIncorrect());
            extraTestInfo += ("AUC = " + eval.weightedAreaUnderROC());
            extraTestInfo += ("kappa = " + eval.kappa());
            extraTestInfo += ("MAE = " + eval.meanAbsoluteError());
            extraTestInfo += ("RMSE = " + eval.rootMeanSquaredError());
            extraTestInfo += ("RAE = " + eval.relativeAbsoluteError());
            extraTestInfo += ("RRSE = " + eval.rootRelativeSquaredError());
            extraTestInfo += ("Precision = " + eval.weightedPrecision());
            extraTestInfo += ("Recall = " + eval.weightedRecall());
            extraTestInfo += ("fMeasure = " + eval.weightedFMeasure());
            extraTestInfo += ("Error Rate = " + eval.errorRate());
            //extraTestInfo += ("Precision = " + ());
                */
            extraTestInfo = ("Precision = " +Math.round(eval.weightedPrecision()*1000)/(1000.0d));
            
           
            
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
    
    
    public ArrayList<SelectItem> getAttributeList() {
        ArrayList<SelectItem> res = new ArrayList<SelectItem>();

        int i=0;
        String temp = "";
        attributes = new String[train.numAttributes()];
        while (i != train.numAttributes()) {
            temp = train.attribute(i).toString().replaceAll("['']","").replaceAll("\\\\", "");
            res.add(new SelectItem(i,temp));
            attributes[i] = temp + "%5Cn";
            i++;
        }
        
        return res;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

  

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getChosenAlgo() {
        return chosenAlgo;
    }

    public void setChosenAlgo(String chosenAlgo) {
        this.chosenAlgo = chosenAlgo;
    }

    
    public int getAttributeID() {
        return attributeID;
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
        if (chosenAlgo.equals("J48"))
            return Math.round(eval.pctCorrect());
        else if(chosenAlgo.equals("NaiveBayes"))
            return Math.round(evalNB.pctCorrect());
        return Math.round(evalIBK.pctCorrect());

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
        maxInstances = train.numInstances();
        return maxInstances;
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

    public Evaluation getEvalIBK() {
        return evalIBK;
    }

    public Evaluation getEvalNB() {
        return evalNB;
    }

    public IBk getIbk() {
        return ibk;
    }

    public NaiveBayes getNaiveBayes() {
        return naiveBayes;
    }
     
    
    public void j48Algo()
    {
        try {
            j48 = new J48();
            j48.buildClassifier(train);
            eval = new Evaluation(train);
            eval.crossValidateModel(j48, train, 10, new Random(1));
            
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void naiveBayesAlgo()
    {
        try {
            naiveBayes = new NaiveBayes();
            naiveBayes.buildClassifier(train);
            evalNB = new Evaluation(train);
            evalNB.crossValidateModel(naiveBayes, train, 10, new Random(1));
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void iBKAlgo()
    {
        try {
            ibk = new IBk();
            ibk.buildClassifier(train);
            evalIBK = new Evaluation(train);
            evalIBK.crossValidateModel(ibk, train, 10, new Random(1));
        } catch (Exception ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void analyze(int i,int y) {

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            if(y != 0)
            {
                crs.setCommand("select * from EDITED where fileID = ? and editID = ?");
                crs.setInt(1, i);
                crs.setInt(2, y);
            }
            else
            {
                crs.setCommand("select * from FILES where fileID = ?");
                crs.setInt(1, i);
            }
            crs.execute();
            if (crs.next()) {
                fb.setFilename(crs.getString("fileName"));
                fb.setFiletype(crs.getString("fileType"));
                fb.setPredicts(crs.getString("filePrediction"));
            }

            BufferedReader breader = null;
            if(y != 0)
            {
                breader = new BufferedReader(new FileReader("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/updated/" + fb.getFilename() + ".arff"));
            }
            else
            {
                breader = new BufferedReader(new FileReader("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/" + fb.getFilename() + ".arff"));
            }
            train = new Instances(breader);
            train.setClassIndex(train.numAttributes() - 1);
            breader.close();
            //prediction = eval.toSummaryString("\nResults\n======\n", true);
            j48Algo();
            naiveBayesAlgo();
            iBKAlgo();
            
            if(eval.correct() > evalIBK.correct())
                if(eval.correct() > evalNB.correct())
                    chosenAlgo = "J48";
                else
                    chosenAlgo = "NaiveBayes";
            else if(evalIBK.correct() > evalNB.correct())
                    chosenAlgo = "IBK";
            else 
                chosenAlgo = "NaiveBayes";

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
            string += "['" + att[i].replaceAll("['']","")+ "'," + values[i] + "],";
        }
        string = string.substring(0, string.length() - 1) + "]";
        string = string.replaceAll("\\\\", "");
//System.out.println(string);
        return string;
    }
    
         public String graphValues()
    {

        String classes = train.attribute(attributeID).toString().replaceAll("['']","").replaceAll("\\\\", "");
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
                values[i] = train.get(i).value(attributeID);
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
        
        String attributeSelectedName = train.attribute(i).toString().split("@attribute")[1].replaceAll("['']","").replaceAll("\\\\", "");

        return attributeSelectedName;
    }

        public String graphValuesCorrect(int num)
    {
                Evaluation eva;
        if(num == 1)
            eva = eval;
        else if(num == 2)
            eva = evalNB;
        else 
            eva = evalIBK;
            String classes = train.classAttribute().toString();
            String[] tempClasses = classes.split("\\{");
            String[] tempClasses2 = tempClasses[1].split("\\}");
            String Labels = tempClasses2[0];
            String[] att = Labels.split(",");
            String string = "[['Labels','Correctly Classified %'],";
            double[][] temp = eva.confusionMatrix();
            double[] values = new double[train.numClasses()];
            for (int i = 0; i < train.numClasses(); i++) {
                values[i] = (temp[i][i]);
                string += "['" + att[i].replaceAll("['']","") + "'," + values[i] + "],";
            }
            string += "['Incorrect'," + eva.incorrect() + "],";
            string = string.substring(0, string.length() - 1) + "]";

            string = string.replaceAll("\\\\", "");
            return string;
  
    }
        
             public String graphValuesIncorrect(int num)
    {
        Evaluation eva;
        if(num == 1)
            eva = eval;
        else if(num == 2)
            eva = evalNB;
        else 
            eva = evalIBK;
            String classes = train.classAttribute().toString();
            String[] tempClasses = classes.split("\\{");
            String[] tempClasses2 = tempClasses[1].split("\\}");
            String Labels = tempClasses2[0];
            String[] att = Labels.split(",");
            String string = "[['Labels','Incorrectly Classified %'],";
            double[][] temp = eva.confusionMatrix();
            double[] values = new double[train.numClasses()];
            for (int i = 0; i < train.numClasses(); i++) {
                
                for(int j =0; j < train.numClasses();j++)
                {
                    if (i != j)
                        values[i] += (temp[i][j]);
                }

                string += "['" + att[i].replaceAll("['']","") + "'," + values[i] + "],";
            }
            
            string = string.substring(0, string.length() - 1) + "]";
            string = string.replaceAll("\\\\", "");
            return string;
  
    }
    
    public String graphValuesBar1()
    {
        double j48C = eval.correct();
        double nbc = evalNB.correct();
        double ibkc = evalIBK.correct();
        
        double j48nC = eval.incorrect();
        double nbnc = evalNB.incorrect();
        double ibknc = evalIBK.incorrect();
        
        String temp = "[['','J48', 'Naive Bayes','iBK'],['Total Correct',"+j48C+","+nbc+","+ibkc+"],['Total Incorrect',"+j48nC+","+nbnc+","+ibknc+"]]";
            return temp;
  
    }
     public String graphValuesBar2()
    {
        double j48C = eval.correct();
        double nbc = evalNB.correct();
        double ibkc = evalIBK.correct();
        
        double j48nC = eval.incorrect();
        double nbnc = evalNB.incorrect();
        double ibknc = evalIBK.incorrect();
        
        String temp = "[['','J48', 'Naive Bayes','iBK'],['True Positives Rate',"+eval.weightedTruePositiveRate()+","+evalNB.weightedTruePositiveRate()+","+evalIBK.weightedTruePositiveRate()+"],"
                + "['False Positve Rate',"+eval.weightedFalsePositiveRate()+","+evalNB.weightedFalsePositiveRate()+","+evalIBK.weightedFalsePositiveRate()+"]]";

      
            return temp;
  
    }
     
     
     public String graphValuesLine(int num){
         String type = "";
        Evaluation eva;
        if(num == 1){
            eva = eval;
        type="J48";}
        else if(num == 2){
            eva = evalNB;type="NB";}
        
        else {type="iBK";
            eva = evalIBK;
        }
         String temp = "[['year','"+type+"'],";
        for (int i = 0; i < train.numClasses(); i++) {
                temp += "["+eva.falsePositiveRate(i)+","+eva.truePositiveRate(i)+"]";
                if (i != train.numClasses()-1)
                    temp+=",";
            }
         temp+="]";
         
         return temp;
         
         
         /*[
          ['Year', 'Sales'],
          ['0.5523432',  0.5123424],
          [0.9945,  0.777],
          [0.345345, 0.753232],
          [0.3451435,  0.123134]
        ]*/
         
         
         
         
         
         
         //[
        //          [0, 0, 0,6],    [1, 10, 5,11],   [2, 23, 15,12],  [3, 17, 9,20],   [4, 18, 10,12],  [5, 9, 5,2],
         //         [6, 11, 3,5],   [7, 27, 19,25],  [8, 33, 25,6],  [9, 40, 32,9],  [10, 32, 24,10], [11, 35, 27,11]
          //      ]
         
         
         
         
         
         
         
         
         
         
         
         
         
         /*function drawAxisTickColors() {
                var data = new google.visualization.DataTable();
                data.addColumn('number', 'X');
                data.addColumn('number', 'J48');

                data.addRows(#{analysis.graphValuesLine()});

                var options = {
                    'width': 1500,
                    'height': 600,
                  hAxis: {
                    title: 'False Postive Rate (FP)',
                    textStyle: {
                      color: '#01579b',
                      fontSize: 20,
                      fontName: 'Arial',
                      bold: true,
                      italic: true
                    },
                    titleTextStyle: {
                      color: '#01579b',
                      fontSize: 16,
                      fontName: 'Arial',
                      bold: false,
                      italic: true
                    }
                  },
                  vAxis: {
                    title: 'True Postive Rate (TP)',
                    textStyle: {
                      color: '#1a237e',
                      fontSize: 24,
                      bold: true
                    },
                    titleTextStyle: {
                      color: '#1a237e',
                      fontSize: 24,
                      bold: true
                    }
                  },
                  colors: ['#77e5a9', '#02c95c']
                };
                var chart = new google.visualization.LineChart(document.getElementById('tpvsfp'));
                chart.draw(data, options);
    }*/
     }
    
    
    
    
}