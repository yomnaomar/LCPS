/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;


import Database.Access.DatabaseAccess;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author Abdullah
 */
@ManagedBean
@SessionScoped
public class UpdateBean {

    private Part uploadedFile;
    private String text;
    private String filename;
    private String filenameO;
    private String filetype;
    private String description;
    private String predicts;
    private String fileUpdater;
    private int updateid;
    private int fileid;
    private CachedRowSet crs = null;
    private CachedRowSet crsOriginal = null;

    public UpdateBean() {
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFilenameO(String filenameO) {
        this.filenameO = filenameO;
    }

    public String getFilenameO() {
        return filenameO;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getDescription() {
        return description;
    }

    public String getFileUpdater() {
        return fileUpdater;
    }

    public void setFileUpdater(String fileUpdater) {
        this.fileUpdater = fileUpdater;
    }


    public String getPredicts() {
        return predicts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPredicts(String predicts) {
        this.predicts = predicts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void updateDB(String user) {
        try {
            //ArrayList<filesBean> al = new ArrayList<filesBean>();
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crsOriginal= RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            
            crsOriginal.setUrl(DatabaseAccess.DBurl);
            crsOriginal.setUsername(DatabaseAccess.DBuser);
            crsOriginal.setPassword(DatabaseAccess.DBpass);
            crsOriginal.setCommand("Select * from FILES where filename = ?");
            crsOriginal.setString(1, filenameO);
            crsOriginal.execute();
            filesBean temp = new filesBean();
            crsOriginal.next();
            temp.setFilename(crsOriginal.getString("filename"));
            temp.setFiletype(crsOriginal.getString("filetype"));
            temp.setPredicts(crsOriginal.getString("fileprediction"));
            temp.setDescription(crsOriginal.getString("filedescription"));
            temp.setUploader(crsOriginal.getString("fileUploader"));
            temp.setID(crsOriginal.getInt("fileid"));

            //al.add(temp);
            crs.setCommand("insert into EDITED (fileid, fileName,fileType,filePrediction,fileDescription,fileUpdater) values (?,?,?,?,?,?)");
            crs.setInt(1, temp.getID());
            fileid = temp.getID();
            String temp2 = (user.split("@")[0]);
            fileUpdater = user;
            filename = temp.getFilename()+"_Edited by_"+temp2;
            crs.setString(2, filename);
            crs.setString(3, "arff");
            crs.setString(4, temp.getPredicts());
            crs.setString(5, description);
            crs.setString(6, fileUpdater);
            crs.execute();
            upload();
        } catch (SQLException ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setUpdateid(int updateid) {
        this.updateid = updateid;
    }

    public int getUpdateid() {
        return updateid;
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }
    
       public boolean validme(String user){
        return fileUpdater.equals(user);
    }

    public void upload() {
        FileOutputStream fop = null,fop2 = null;
        File file,fileAttributes;
        try {
            if (null != uploadedFile) {
                try {
                    InputStream is = uploadedFile.getInputStream();
                    text = new Scanner(is).useDelimiter("\\A").next();
                } catch (IOException ex) {
                }
            }
            file = new File("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/updated/" + filename + ".arff");
            if (!file.exists()) {
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            FileInputStream iop = null;
            
            byte[] contentInBytes = text.getBytes();
           
                    
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
   
            ArffLoader loader = new ArffLoader();
            loader.setSource(new File("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/updated/"+ filename + "." + "arff"));
            Instances data = loader.getDataSet();//get instances object
            

            
            // save CSV
            CSVSaver saver = new CSVSaver();
            saver.setInstances(data);//set the dataset we want to convert
            //and save as CSV
            saver.setFile(new File("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/updated/" + filename + ".txt"));
            saver.writeBatch();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(new File("C:/Users/Abdullah/Desktop/SD/AllfilesForProgram/updated/" + filename + ".txt")))));
            String line = null;
            line = reader.readLine();
                    
                            
            
            fileAttributes  = new File("C:/Users/Abdullah/Documents/NetBeansProjects/ELCD_GitHub/web/ToDownloadFiles/" + filename + ".txt");
            fop2 = new FileOutputStream(fileAttributes);
            if (!fileAttributes.exists()) {
                fileAttributes.createNewFile();
            }

            
            
            
            byte[] contentInBytes2 = (line + "\n //ignore the last attribute (it is the class)").getBytes();
            
            fop2.write(contentInBytes2);
            fop2.flush();
            fop2.close();

            text = "Uploaded";
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
            text = "Error in uploading";
        } catch (IOException ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
            text = "Error in uploading";

        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                response.sendRedirect("doctorPatientAI.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
