package AbdullahWeka;

import Database.Access.DatabaseAccess;
import DoctorFunctionalities.DoctorPatients;
import Login.Authentification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@ManagedBean
@SessionScoped
public class testingPatient {

    private Instances trained;
    private String prediction;
    private String patientData;
    private String testName;
    private Boolean fatal;

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

    public Boolean getFatal() {
        return fatal;
    }

    public void setFatal(Boolean fatal) {
        this.fatal = fatal;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setPatientData(String patientData) {
        this.patientData = patientData;
    }

    public void performPatientAnalysis(Instances trainDataset, J48 j48, NaiveBayes nb, IBk ibk, filesBean fb, String chosen) {

        try {
            //load training dataset
            //DataSource source = null;
            DataSource source2 = null;

            //source = new DataSource("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/LungGenes.arff");
            //Instances trainDataset = source.getDataSet();
            //trainDataset.setClassIndex(trainDataset.numAttributes()-1);
            String line = null, toAdd = "";
            String completeFile = "";
            int start, end;
            BufferedReader reader
                    = new BufferedReader(new FileReader("C:/Users/Yomna/Desktop/SD/SD/AllfilesForProgram/"+ fb.getFilename()+".arff"));

            while ((line = reader.readLine()) != null) {
                completeFile += line;
            }
            completeFile = completeFile.toLowerCase();
            String[] lines = completeFile.split("@relation");
            String[] lines2;
            if (lines.length != 1) {
                lines2 = lines[1].split("@data");
            } else {
                lines2 = lines[0].split("@data");
            }
            //String []lines3 = lines2[0].split("@");

            //lines3.toString();
            lines2[0] = lines2[0].replaceAll("@", "\n@");
            toAdd = "\n@RELATION" + " " + lines2[0] + "\n" + "\n@DATA\n";

            reader.close();

            FileOutputStream fop = null;
            File file = null;
            file = new File("C:/Users/Yomna/Desktop/SD/SD/AllfilesForProgram/Test.arff");
            fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }

            toAdd += patientData + ",?";//6.9,3.2,5.7,2.3

            byte[] contentInBytes = toAdd.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            //J48 smo = new J48();
            //smo.buildClassifier(trainDataset);
            source2 = new DataSource("C:/Users/Yomna/Desktop/SD/SD/AllfilesForProgram/Test.arff");

            Instances testDataset = source2.getDataSet();
            testDataset.setClassIndex(testDataset.numAttributes() - 1);
            Instance newInst = testDataset.instance(0);
            int predSMO;
            if (chosen.equals("J48"))
                predSMO = (int) j48.classifyInstance(newInst);
            else if (chosen.equals("NaiveBayes"))
                predSMO = (int) nb.classifyInstance(newInst); 
            else
                predSMO = (int) ibk.classifyInstance(newInst);
            prediction = trainDataset.classAttribute().value(predSMO);

            //return prediction;
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.sendRedirect("doctorPatientAnalysis.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(testingPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return null;
    }

    public void backToPatient() throws IOException {
        try {
            //Insert into DB
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            testName = params.get("testName");
            
            crs.setCommand("INSERT INTO Gene(patientID, doctorID, name, result, isFatal) VALUES(?, ?, ?, ?, ?)");
            crs.setInt(1, DoctorPatients.getPatientID());
            crs.setInt(2, Authentification.getUserID());
            crs.setString(3, "Lung Cancer");
            crs.setString(4, prediction);
            crs.setBoolean(5, fatal);
            crs.execute();
            
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.sendRedirect("doctorPatientView.xhtml");
            
        } catch (SQLException ex) {
            Logger.getLogger(testingPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
