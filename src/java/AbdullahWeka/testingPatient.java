/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Abdullah
 */
@ManagedBean
@SessionScoped
public class testingPatient {
    private Instances trained;
    private String prediction;
    private String patientData;
    
    
    public testingPatient() {
        trained = null;
    }

    public String getPrediction() {
        return prediction;
    }

    public Instances getTrained() {
        return trained;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setTrained(Instances trained) {
        this.trained = trained;
    }

    public String getPatientData() {
        return patientData;
    }

    public void setPatientData(String patientData) {
        this.patientData = patientData;
    }
    
    
    
    public void performPatientAnalysis(Instances trainDataset, J48 j48) {

        try {
            //load training dataset
            //DataSource source = null;
            DataSource source2 = null;
            
            //source = new DataSource("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/LungGenes.arff");
            
            //Instances trainDataset = source.getDataSet();
            //trainDataset.setClassIndex(trainDataset.numAttributes()-1);
            
            String line = null,toAdd = "";
            String completeFile = "";
            int start,end;
            BufferedReader reader =
                    new BufferedReader(new FileReader("D:/Documents/University Docs/Spring 2017/CMP 491 - Senior Design II/Testing/AllfilesForProgram/LungGenes.arff"));

            while((line = reader.readLine()) != null) {
                completeFile += line;
            }
            completeFile = completeFile.toLowerCase();
            String [] lines = completeFile.split("@relation");
            String []lines2;
            if(lines.length != 1)
                lines2 = lines[1].split("@data");
            else
                lines2 = lines[0].split("@data");
           //String []lines3 = lines2[0].split("@");
            
            //lines3.toString();
            lines2[0] = lines2[0].replaceAll("@", "\n@");
            toAdd = "\n@RELATION" + " "+lines2[0]+"\n"+"\n@DATA\n";

            
            reader.close();
            
            FileOutputStream fop = null;
            File file = null;
            file = new File("D:/Documents/University Docs/Spring 2017/CMP 491 - Senior Design II/Testing/AllfilesForProgram/LungGenes.arff");
            fop = new FileOutputStream(file);
            if (!file.exists()) {
            file.createNewFile();
            }
            
            toAdd += patientData+",?";//6.9,3.2,5.7,2.3
            
            byte[] contentInBytes = toAdd.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            
            
            //J48 smo = new J48();
            //smo.buildClassifier(trainDataset);
            
            source2 = new DataSource("D:/Documents/University Docs/Spring 2017/CMP 491 - Senior Design II/Testing/AllfilesForProgram/LungGenes.arff");
            
            Instances testDataset = source2.getDataSet();
            testDataset.setClassIndex(testDataset.numAttributes()-1);
            Instance newInst = testDataset.instance(0);
            int predSMO =(int)j48.classifyInstance(newInst);
            prediction= trainDataset.classAttribute().value( predSMO);
            //return prediction;
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.sendRedirect("doctorPatientAnalysis.xhtml");
            
        } catch (Exception ex) {
            Logger.getLogger(testingPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return null;
    }
    
    
}
