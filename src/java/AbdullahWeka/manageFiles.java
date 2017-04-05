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
import javax.faces.context.FacesContext;
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
                temp.setID(crs.getInt("fileid"));

                al.add(temp);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(manageFiles.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void chosen(int chosen) throws IOException {
        this.chosen = chosen;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.sendRedirect("doctorDBAnalysis.xhtml");
    }
}
