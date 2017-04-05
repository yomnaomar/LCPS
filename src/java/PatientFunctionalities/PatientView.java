package PatientFunctionalities;

import Database.Access.DatabaseAccess;
import Database.Classes.Comment;
import Database.Classes.Gene;
import DoctorFunctionalities.DoctorPatientView;
import Login.Authentification;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

@ManagedBean
@SessionScoped
public class PatientView {

    private ArrayList<Gene> tests;
    private String gErrorMsg;
    private ArrayList<Comment> comments;
    private String cErrorMsg;
    private String errorMsg;
    private CachedRowSet crs;

    public PatientView() {
        this.tests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.gErrorMsg = "";
        this.cErrorMsg = "";
        this.errorMsg = "";
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
        } catch (SQLException ex) {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(PatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Gene> getTests() {
        this.tests.clear();
        this.gErrorMsg = "";
        try {
            crs.setCommand("SELECT * FROM Gene WHERE patientID = ?");
            crs.setInt(1, Authentification.getUserID());
            crs.execute();
            crs.beforeFirst();
            while (crs.next()) {
                Gene tempGene = new Gene(crs.getInt("geneID"),
                        crs.getInt("patientID"),
                        crs.getInt("doctorID"),
                        crs.getString("name"),
                        crs.getString("result"),
                        crs.getBoolean("isFatal"));
                tests.add(tempGene);
            }
        } catch (SQLException ex) {
            this.gErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.tests.isEmpty()) {
            this.gErrorMsg = "No gene tests to retrieve at this time";
        }
        return this.tests;
    }

    public void setTests(ArrayList<Gene> tests) {
        this.tests = tests;
    }

    public String getgErrorMsg() {
        return gErrorMsg;
    }

    public void setgErrorMsg(String gErrorMsg) {
        this.gErrorMsg = gErrorMsg;
    }

    public ArrayList<Comment> getComments() {
        this.comments.clear();
        this.cErrorMsg = "";
        try {
            crs.setCommand("SELECT * FROM Comment WHERE patientID = ?");
            crs.setInt(1, Authentification.getUserID());
            crs.execute();
            crs.beforeFirst();
            while (crs.next()) {
                if (crs.getBoolean("isVisible")) {
                    Comment tempComment = new Comment(crs.getInt("commentID"),
                            crs.getInt("patientID"),
                            crs.getInt("doctorID"),
                            crs.getString("comment"),
                            crs.getDate("dateAdded"),
                            crs.getBoolean("isVisible"));
                    comments.add(tempComment);
                }
            }
        } catch (SQLException ex) {
            this.cErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.comments.isEmpty()) {
            this.cErrorMsg = "No gene tests to retrieve at this time";
        }
        return this.comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getcErrorMsg() {
        return cErrorMsg;
    }

    public void setcErrorMsg(String cErrorMsg) {
        this.cErrorMsg = cErrorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}