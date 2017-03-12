package tn.piezo.model;

import java.util.ArrayList;

/**
 * Created by djaza on 09.02.2017.
 */
public class PiezoPlotC {
    // переменные - исходные данные для расчета
    private double HSN; // напор в сетевом насосе
    private double HPN; // напор в подпиточном насосе
    private double Hist; // падение напора в источнике (на выходе)
    // переменные - исходные данные для расчета
    public String[] NamePartTN;
    public double[] L;
    public double[] Geo;
    public double[] ZdanieEtaj;
    public double Hrasp_ist;
    public double[] H1x;
    // переменные для расчета
    double Hpoln;
    double[] HraspPod;
    double[] HraspObrat;

    PiezoPlotC(double HSN, double HPN, double Hist, String[] NamePartTN, double[] L, double[] Geo, double[] ZdanieEtaj,
            double Hrasp_ist, double[] H1x )
    {
        this.HSN = HSN;
        this.HPN = HPN;
        this.Hist = Hist;
        this.NamePartTN = NamePartTN;
        this.L = L;
        this.Geo = Geo;
        this.ZdanieEtaj = ZdanieEtaj;
        this.Hrasp_ist = Hrasp_ist;
        this.H1x = H1x;
        this.Hpoln = HSN + HPN + 20; // + 20 запас для последнего участка
        HraspPod = new double[L.length+1];
        HraspObrat = new double[L.length+1];

    }
    public ArrayList PiezoSolver(PiezoPlotC piezoPart)
    {
        // выходная переменная
        ArrayList<PiezoDataClassStructure> piezoData = new ArrayList<>();
        // для расчета подачи и обратки
        HraspObrat[0] = this.HPN;
        HraspPod[0] = this.Hpoln;
        int j = 0;
        for (int i = 1; i < HraspPod.length; i++)
        {
            HraspPod[i] = HraspPod[i-1] - piezoPart.H1x[j];
            HraspObrat[i] = HraspObrat[i-1] + piezoPart.H1x[j];
            j++;
        }
        j=0;
        for (int i = 0; i < HraspPod.length; i++)
        {
            if (i==0) piezoData.add(new PiezoDataClassStructure(i, "Источник", 0, Geo[i], 0, this.Hpoln, this.HPN));
            else {
                piezoData.add(new PiezoDataClassStructure(i, NamePartTN[j], L[j], Geo[j], ZdanieEtaj[j], HraspPod[i], HraspObrat[i]));
                j++;
            }

        }
        return piezoData;
    }
}
