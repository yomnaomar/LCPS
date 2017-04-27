package DoctorFunctionalities;

import Database.Classes.Comment;
import Database.Access.DatabaseAccess;
import Database.Classes.Gene;
import Login.Authentification;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

@ManagedBean
@RequestScoped
public class DoctorPatientView {

    private ArrayList<Gene> tests;
    private String testErrorMsg;
    private ArrayList<Comment> comments;
    private String commentErrorMsg;
    private int patientID;
    private String errorMsg;
    private String comment;
    private boolean commentVisibility;
    private CachedRowSet crs;
    private HashMap<Integer, Boolean> deletedComments;
    private HashMap<Integer, Boolean> deletedTests;
    
    public DoctorPatientView() {
        this.tests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.deletedComments = new HashMap<>();
        this.deletedTests = new HashMap<>();
        this.testErrorMsg = "";
        this.commentErrorMsg = "";
        this.errorMsg = "";
        this.comment = "";
        this.commentVisibility = true;
        this.patientID = DoctorPatients.getPatientID();
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
        } catch (SQLException ex) {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Gene> getTests() {
        this.tests.clear();
        this.testErrorMsg = "";
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
            this.testErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this.tests.isEmpty()) {
            this.testErrorMsg = "No gene tests to retrieve at this time";
        }
        return this.tests;
    }

    public void setTests(ArrayList<Gene> tests) {
        this.tests = tests;
    }

    public HashMap<Integer, Boolean> getDeletedComments() {
        return deletedComments;
    }

    public void setDeletedComments(HashMap<Integer, Boolean> deletedComments) {
        this.deletedComments = deletedComments;
    }

    public HashMap<Integer, Boolean> getDeletedTests() {
        return deletedTests;
    }

    public void setDeletedTests(HashMap<Integer, Boolean> deletedTests) {
        this.deletedTests = deletedTests;
    }

    public ArrayList<Comment> getComments() {
        this.comments.clear();
        this.commentErrorMsg = "";
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
            this.commentErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this.comments.isEmpty()) {
            this.commentErrorMsg = "No gene tests to retrieve at this time";
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

    public String getTestErrorMsg() {
        return testErrorMsg;
    }

    public void setTestErrorMsg(String testErrorMsg) {
        this.testErrorMsg = testErrorMsg;
    }

    public String getCommentErrorMsg() {
        return commentErrorMsg;
    }

    public void setCommentErrorMsg(String commentErrorMsg) {
        this.commentErrorMsg = commentErrorMsg;
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
    
    public void deleteComments() {
        this.commentErrorMsg = "";
        try {
            for(int commentID : deletedComments.keySet()) {
                if (deletedComments.get(commentID)) {
                    crs.setCommand("DELETE FROM Comment WHERE commentID = ?");
                    crs.setInt(1, commentID);
                    crs.execute();
                }
            }
            this.commentErrorMsg = "Comments Deleted Successfully!";
        } catch (SQLException ex) {
            this.commentErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteTests() {
        this.testErrorMsg = "";
        try {
            for(int testID : deletedTests.keySet()) {
                if (deletedTests.get(testID)) {
                    crs.setCommand("DELETE FROM Gene WHERE geneID = ?");
                    crs.setInt(1, testID);
                    crs.execute();
                }
            }
            this.testErrorMsg = "Tests Deleted Successfully!";
        } catch (SQLException ex) {
            this.testErrorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}