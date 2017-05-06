/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;

import Database.Access.DatabaseAccess;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author Abdullah
 */
@ManagedBean
@SessionScoped
public class manageFiles {

    private CachedRowSet crs = null;
    private int chosen;
    private int uchosen;

    public int getUchosen() {
        return uchosen;
    }

    public void setUchosen(int uchosen) throws IOException {
        this.uchosen = uchosen;
                FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.sendRedirect("doctorDBAnalysis.xhtml");
    }
    
    public int getChosen() {
        return chosen;
    }

    public void setChosen(int chosen) {
        this.chosen = chosen;

    }

    public manageFiles() {
    }

    public ArrayList<filesBean> getFiles() {
        ArrayList<filesBean> al = new ArrayList<filesBean>();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            crs.setCommand("Select * from FILES");
            crs.execute();
            while (crs.next()) {
                filesBean temp = new filesBean();
                temp.setFilename(crs.getString("filename"));
                temp.setFiletype(crs.getString("filetype"));
                temp.setPredicts(crs.getString("fileprediction"));
                temp.setDescription(crs.getString("filedescription"));
                temp.setUploader(crs.getString("fileUploader"));
                temp.setID(crs.getInt("fileid"));

                al.add(temp);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
        public ArrayList<UpdateBean> getUpdatedFiles() {
        ArrayList<UpdateBean> al = new ArrayList<UpdateBean>();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
            crs.setCommand("Select * from EDITED where fileID = ?");
            crs.setInt(1, chosen);
            crs.execute();
            while (crs.next()) {
                UpdateBean temp = new UpdateBean();
                temp.setDescription(crs.getString("fileDescription"));
                temp.setFileUpdater(crs.getString("fileUpdater"));
                temp.setFilename(crs.getString("fileName"));
                temp.setUpdateid(crs.getInt("editID"));
                temp.setFileid(crs.getInt("fileID"));

                al.add(temp);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void deletesU(int num)
    {
        try {
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl("jdbc:derby://localhost:1527/ELCD");
            crs.setUsername("seniordesign");
            crs.setPassword("CmpCoe491");
            crs.setCommand("DELETE FROM EDITED WHERE editID = ?");
            crs.setInt(1, num);
            crs.execute();
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (SQLException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    public void deletes(int num)
    {
        try {
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl("jdbc:derby://localhost:1527/ELCD");
            crs.setUsername("seniordesign");
            crs.setPassword("CmpCoe491");
            crs.setCommand("DELETE FROM FILES WHERE fileID = ?");
            crs.setInt(1, num);
            crs.execute();
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (SQLException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }

    public void chosen(int chosen) throws IOException {
        this.chosen = chosen;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.sendRedirect("doctorDBAnalysis.xhtml");
    }
    
        public void editions(int chosen) throws IOException {
        this.chosen = chosen;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.sendRedirect("doctorUpdatedDBs.xhtml");
    }
    
}
