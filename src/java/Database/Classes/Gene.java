package Database.Classes;

public class Gene {

    private int geneID;
    private int patientID;
    private int doctorID;
    private String name;
    private String result;
    private boolean isFatal;

    public Gene() {
        this.geneID = 0;
        this.patientID = 0;
        this.doctorID = 0;
        this.name = "no name";
        this.result = "no result";
        this.isFatal = false;
    }

    public Gene(int geneID, int patientID, int doctorID, String name, String result, boolean isFatal) {
        this.geneID = geneID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.name = name;
        this.result = result;
        this.isFatal = isFatal;
    }

    public int getGeneID() {
        return geneID;
    }

    public void setGeneID(int geneID) {
        this.geneID = geneID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isIsFatal() {
        return isFatal;
    }

    public void setIsFatal(boolean isFatal) {
        this.isFatal = isFatal;
    }
}
