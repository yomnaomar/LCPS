package Login;

import Database.Access.DatabaseAccess;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

@ManagedBean
@SessionScoped
public class Authentification {
    private static int userID;
    private String firstname;
    private String email;
    private String password;
    private String userType;
    private boolean validUser;
    private String errorMsg;
    private CachedRowSet crs;

    public Authentification() {
        validUser = false;
        errorMsg = "";
        try
        {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl(DatabaseAccess.DBurl);
            crs.setUsername(DatabaseAccess.DBuser);
            crs.setPassword(DatabaseAccess.DBpass);
        }
        catch (SQLException ex)
        {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isValidUser() {
        this.validUser = false;
        this.errorMsg = "";
        try
        {
            // Check for the existence of the user
            crs.setCommand("SELECT * FROM ELCDuser WHERE email LIKE ?");
            crs.setString(1, email);
            crs.execute();
            crs.beforeFirst();
            while(crs.next())
            {
                System.out.println(crs.getString("email"));
                if(crs.getString("password").equals(password))
                {
                    this.firstname = crs.getString("firstname");
                    this.userID = crs.getInt("userID");
                    this.validUser = true;
                }
            }
            // Check if the user is a doctor
            crs.setCommand("SELECT * FROM Doctor WHERE doctorID = ?");
            crs.setInt(1, this.userID);
            crs.execute();
            crs.beforeFirst();
            while(crs.next())
            {
                this.userType = "oncologist";
            }
            // Check if the user is a patient
            crs.setCommand("SELECT * FROM Patient WHERE patientID = ?");
            crs.setInt(1, this.userID);
            crs.execute();
            crs.beforeFirst();
            while(crs.next())
            {
                this.userType = "cancerPatient";
            }
        }
        catch (SQLException ex)
        {
            this.errorMsg = "Error in accessing the database: Please try again later";
            Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validUser;
    }

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public Object enterELCD()
    {
        if(this.isValidUser())
        {
            this.errorMsg = "";
            if(userType.equals("oncologist"))
                return "oncologist";
            else
                return "cancerPatient";
        }
        this.errorMsg = "Incorrect Credentials! Please Try Again";
        return "failure";
    }
}