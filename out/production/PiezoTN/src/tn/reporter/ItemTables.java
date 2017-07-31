package tn.reporter;

public class ItemTables {

    private String NamePartTN;
    private Double L;
    private Double G;
    private Double D;

    public ItemTables() {
        NamePartTN = "";
        L = 0.0;
        G = 0.0;
        D = 0.0;
    }

    public String getNamePartTN() {
        return NamePartTN;
    }

    public void setNamePartTN(String namePartTN) {
        NamePartTN = namePartTN;
    }

    public Double getL() {
        return L;
    }

    public void setL(Double l) {
        L = l;
    }

    public Double getG() {
        return G;
    }

    public void setG(Double g) {
        G = g;
    }

    public Double getD() {
        return D;
    }

    public void setD(Double d) {
        D = d;
    }

}
