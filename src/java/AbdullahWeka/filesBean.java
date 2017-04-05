/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbdullahWeka;

import java.io.Serializable;


/**
 *
 * @author Abdullah
 */

public class filesBean implements Serializable{
    private String filename;
    private String filetype;
    private String description;
    private String predicts;
    private int ID;
    /**
     * Creates a new instance of filesBean
     */
    public filesBean() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPredicts() {
        return predicts;
    }

    public void setPredicts(String predicts) {
        this.predicts = predicts;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
    
    
}
