package model;

import java.util.List;

public class ImmobileEImmagini {
    private Immobile immobile;
    private List<String> immagini;
    private List<String> fileNames;

    public ImmobileEImmagini(Immobile i, List<String> immagini){
        this.immobile = i;
        this.immagini = immagini;
    }

    public ImmobileEImmagini(){}

    public Immobile getImmobile() {
        return immobile;
    }

    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }

    public List<String> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<String> immagini) {
        this.immagini = immagini;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}