package tn.reporter;

public class Item {

    private String name;
    private String BoilerName;
    private String MainName;
    private String BranchName;
    private Double length;
    private Double flow;
    private Double diametr;
    private Double hrasp;
    private Double dhpoteri;


    public Double getFlow() {
        return flow;
    }

    public void setFlow(Double flow) {
        this.flow = flow;
    }

    public Double getDiametr() {
        return diametr;
    }

    public void setDiametr(Double diametr) {
        this.diametr = diametr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHrasp() {
        return hrasp;
    }

    public void setHrasp(Double hrasp) {
        this.hrasp = hrasp;
    }

    public Double getdhpoteri() {
        return dhpoteri;
    }

    public void setdhpoteri(Double dhpoteri) {
        this.dhpoteri = dhpoteri;
    }

    public String getBoilerName() {
        return BoilerName;
    }

    public void setBoilerName(String boilerName) {
        BoilerName = boilerName;
    }

    public String getMainName() {
        return MainName;
    }

    public void setMainName(String mainName) {
        MainName = mainName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}
