package Database.Classes;

public class ELCDuser {
    private int userID;
    private String firstname;
    private String lastname;
    private String name;
    private String sex;
    private java.sql.Date dob;
    private int age;
    private String  email;
    private String  password;
    private String  hospitalName;

    public ELCDuser() {
        userID = 0;
        firstname = "no firstname";
        lastname = "no lastname";
        name = "no name";
        sex = "N";
        dob = new java.sql.Date(new java.util.Date().getTime());
        age = 0;
        email = "no email";
        password = "no password";
        hospitalName = "no hospital name";
    }

    public ELCDuser(int userID, String firstname, String lastname, String sex, java.sql.Date dob, int age, String email, String password, String hospitalName) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.name = firstname + " " + lastname;
        this.sex = sex;
        this.dob = dob;
        this.age = age;
        this.email = email;
        this.password = password;
        this.hospitalName = hospitalName;
    }

    public int getUserID() {
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public java.sql.Date getDob() {
        return dob;
    }

    public void setDob(java.sql.Date dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}