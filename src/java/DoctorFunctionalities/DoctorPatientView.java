package DoctorFunctionalities;

import Database.Classes.Comment;
import Database.Access.DatabaseAccess;
import Database.Classes.Gene;
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
public class DoctorPatientView {

    private ArrayList<Gene> tests;
    private String gErrorMsg;
    private ArrayList<Comment> comments;
    private String cErrorMsg;
    private int patientID;
    private String errorMsg;
    private String comment;
    private boolean commentVisibility;
    private CachedRowSet crs;
    
    public DoctorPatientView() {
        this.tests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.gErrorMsg = "";
        this.cErrorMsg = "";
        this.errorMsg = "";
        this.comment = "";
        this.commentVisibility = true;
        this.patientID = DoctorPatients.getPatientID();
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
        } catch (SQLException ex) {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Gene> getTests() {
        this.tests.clear();
        this.gErrorMsg = "";
        try {
            crs.setCommand("SELECT * FROM Gene WHERE patientID = ?");
            crs.setInt(1, this.patientID);
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
        if(this.tests.isEmpty()) {
            this.gErrorMsg = "No gene tests to retrieve at this time";
        }
        return this.tests;
    }

    public void setTests(ArrayList<Gene> tests) {
        this.tests = tests;
    }

    public ArrayList<Comment> getComments() {
        this.comments.clear();
        this.cErrorMsg = "";
        try {
            crs.setCommand("SELECT * FROM Comment WHERE patientID = ?");
            crs.setInt(1, this.patientID);
            crs.execute();
            crs.beforeFirst();
            while (crs.next()) {
                Comment tempComment = new Comment(crs.getInt("commentID"),
                            crs.getInt("patientID"),
                            crs.getInt("doctorID"),
                            crs.getString("comment"),
                            crs.getDate("dateAdded"),
                            crs.getBoolean("isVisible"));
                comments.add(tempComment);
            }
        } catch (SQLException ex) {
            this.cErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this.comments.isEmpty()) {
            this.cErrorMsg = "No gene tests to retrieve at this time";
        }
        return this.comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getgErrorMsg() {
        return gErrorMsg;
    }

    public void setgErrorMsg(String gErrorMsg) {
        this.gErrorMsg = gErrorMsg;
    }

    public String getcErrorMsg() {
        return cErrorMsg;
    }

    public void setcErrorMsg(String cErrorMsg) {
        this.cErrorMsg = cErrorMsg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCommentVisibility() {
        return commentVisibility;
    }

    public void setCommentVisibility(boolean commentVisibility) {
        this.commentVisibility = commentVisibility;
    }
    
    public void addComment() {
        try {
            crs.setCommand("INSERT INTO Comment(patientID, doctorID, comment, dateAdded, isVisible) VALUES(?, ?, ?, ?, ?)");
            crs.setInt(1, patientID);
            crs.setInt(2, Authentification.getUserID());
            crs.setString(3, comment);
            crs.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            crs.setBoolean(5, commentVisibility);
            crs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            this.comment = "";
        }
    }
}