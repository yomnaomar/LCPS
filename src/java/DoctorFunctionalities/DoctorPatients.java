package DoctorFunctionalities;

import Database.Access.DatabaseAccess;
import Database.Classes.ELCDuser;
import Login.Authentification;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

@ManagedBean
@SessionScoped
public class DoctorPatients {

    private ArrayList<ELCDuser> patients;
    private static int patientID;
    private String patientName;
    private String errorMsg;
    private CachedRowSet crs;

    public DoctorPatients() {
        this.patients = new ArrayList<>();
        this.errorMsg = "";
        this.patientName = "";
        DoctorPatients.patientID = 0;
        try {
            // To get Patient
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
        } catch (SQLException ex) {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<ELCDuser> getPatients() {
        this.patients.clear();
        this.errorMsg = "";
        try {
            // To get ELCDuser
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            CachedRowSet crs2 = RowSetProvider.newFactory().createCachedRowSet();
            crs2.setUrl(DatabaseAccess.DBurl);
            crs2.setUsername(DatabaseAccess.DBuser);
            crs2.setPassword(DatabaseAccess.DBpass);
            // Get the Doctor's ID
            int doctorID = Authentification.getUserID();
            crs.setCommand("SELECT * FROM Patient WHERE doctorID = ?");
            crs.setInt(1, doctorID);
            crs.execute();
            crs.beforeFirst();
            while (crs.next()) {
                crs2.setCommand("SELECT * FROM ELCDuser WHERE userID = ?");
                crs2.setInt(1, crs.getInt("patientID"));
                crs2.execute();
                crs2.beforeFirst();
                while (crs2.next()) {
                    ELCDuser tempUser = new ELCDuser(crs2.getInt("userID"),
                            crs2.getString("firstname"),
                            crs2.getString("lastname"),
                            crs2.getString("sex"),
                            crs2.getDate("dob"),
                            crs2.getInt("age"),
                            crs2.getString("email"),
                            crs2.getString("password"),
                            crs2.getString("hospitalName"));
                    patients.add(tempUser);
                }
            }
        } catch (SQLException ex) {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(DoctorPatients.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.patients.isEmpty()) {
            this.errorMsg = "No patients to retrieve at this time";
        }
        return this.patients;
    }

    public void setPatients(ArrayList<ELCDuser> patients) {
        this.patients = patients;
    }

    public static int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        DoctorPatients.patientID = patientID;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object goToDoctorPatientView() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            DoctorPatients.patientID = Integer.parseInt(params.get("selectedPatientID"));
            crs.setCommand("SELECT * FROM ELCDuser WHERE userID = ?");
            crs.setInt(1, DoctorPatients.patientID);
            crs.execute();
            crs.beforeFirst();
            while (crs.next()) {
                this.patientName = crs.getString("firstname") + " " + crs.getString("lastname");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoctorPatients.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "patientView";
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
