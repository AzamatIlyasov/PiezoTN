package tn.piezo.model;

/**
 * Created by djaza on 19.02.2017.
 */
public class PiezoDataClassStructure {
    // структура данных для расчет ПГ
    public int numTN;
    public String NamePartTN;
    public double L;
    public double Geo;
    public double ZdanieEtaj;
    public double HraspPod;
    public double HraspObrat;
    public double HSN;
    public double HPN;
    public double Hist;
    public double Hrasp_ist;
    public double H1x;

    public PiezoDataClassStructure(int numTN, String NamePartTN,
            double L, double Geo, double ZdanieEtaj, double HraspPod, double HraspObrat)
    {
        this.numTN = numTN;
        this.NamePartTN = NamePartTN;
        this.L = L;
        this.Geo = Geo;
        this.ZdanieEtaj = ZdanieEtaj;
        this.HraspPod = HraspPod;
        this.HraspObrat = HraspObrat;

    }

}
