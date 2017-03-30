package Database.Classes;

public class Doctor {
    private int doctorID;

    public Doctor() {
        doctorID = 0;
    }

    public Doctor(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
}