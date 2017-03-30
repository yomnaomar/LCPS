package Database.Classes;

public class Patient {
    private int patientID;
    private int doctorID;

    public Patient() {
        patientID = 0;
        doctorID = 0;
    }

    public Patient(int patientID, int doctorID) {
        this.patientID = patientID;
        this.doctorID = doctorID;
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
}