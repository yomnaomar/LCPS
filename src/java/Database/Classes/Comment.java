package Database.Classes;

public class Comment {
    private int commentID;
    private int patientID;
    private int doctorID;
    private String comment;
    private java.sql.Date dateAdded;
    private boolean visible;

    public Comment() {
        this.commentID = 0;
        this.patientID = 0;
        this.doctorID = 0;
        this.comment = "no comment";
        this.dateAdded = new java.sql.Date(new java.util.Date().getTime());
        this.visible = false;
    }

    public Comment(int commentID, int patientID, int doctorID, String comment, java.sql.Date dateAdded, boolean visible) {
        this.commentID = commentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.comment = comment;
        this.dateAdded = dateAdded;
        this.visible = visible;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.sql.Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(java.sql.Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}