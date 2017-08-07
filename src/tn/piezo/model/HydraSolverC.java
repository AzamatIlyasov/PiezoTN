package tn.piezo.model;

import java.util.ArrayList;

/**
 * Created by Azamat Ilyasov on 05.02.2017.
 * класс - решатель для гидравлической таблицы
 */
public class HydraSolverC {

    // переменные - исходные данные для расчета
    public String[] NamePartTN;
    public String[] NamePartTNpred;
    public double[] D;
    public double[] L;
    public double[] G;
    public double[] Kekv;
    public double[] Geo;
    public double[] ZdanieEtaj;

    private double Hrasp_ist;
    private int n; // нумерация участков

    // переменные - результаты расчета
    private double[] W;
    private double[] Rud;
    private double[] b;
    private double[] Rrash;
    private double[] Hl;
    private double[] Hm;
    private double[] H1x;
    private double[] H2x;
    private double[] dH_fist; //падение напора от источника
    private double[] Hrasp_endP; // падение напора в конце участка
    private double dHsum; // суммарные потери напора
    //дополнительные сведения
    public String BoilerName;
    public String MainName;
    public String BranchName;

    // постоянные величины
    private final double g = 9.81;
    private final double pi = 3.1416;
    private double ro = 97.6554;
    private double gamma = 958; //gamma = ro*g

    //1 конструктор
    public HydraSolverC(String[] NamePartTN, String[] NamePartTNpred, double[] D, double[] L, double[] G, double[] Kekv,
                 double[] Geo, double[] ZdanieEtaj, String BoilerName, String MainName, String BranchName) {
        this.NamePartTN = NamePartTN;
        this.NamePartTNpred = NamePartTNpred;
        this.D = D;
        this.L = L;
        this.G = G;
        this.Kekv = Kekv;
        this.Hrasp_ist = Hrasp_ist;
        this.Geo = Geo;
        this.ZdanieEtaj = ZdanieEtaj;

        this.BoilerName = BoilerName;
        this.MainName = MainName;
        this.BranchName = BranchName;
    }
    //2 конструктор полный с учетом Hrasp_ist
    public HydraSolverC(String[] NamePartTN, String[] NamePartTNpred, double[] D, double[] L, double[] G, double[] Kekv,
                        double[] Geo, double[] ZdanieEtaj,
                        double Hrasp_ist, String BoilerName, String MainName, String BranchName) {
        this.NamePartTN = NamePartTN;
        this.NamePartTNpred = NamePartTNpred;
        this.D = D;
        this.L = L;
        this.G = G;
        this.Kekv = Kekv;
        this.Hrasp_ist = Hrasp_ist;
        this.Geo = Geo;
        this.ZdanieEtaj = ZdanieEtaj;
        this.n = D.length;

        this.W = new double[n];
        this.Rud = new double[n];
        this.b = new double[n];
        this.Rrash = new double[n];
        this.Hl = new double[n];
        this.Hm = new double[n];
        this.H1x = new double[n];
        this.H2x = new double[n];
        this.dH_fist = new double[n];
        this.Hrasp_endP = new double[n];
        this.dHsum = 0;
        this.BoilerName = BoilerName;
        this.MainName = MainName;
        this.BranchName = BranchName;
    }

    public ArrayList HydraPartTN (HydraSolverC partTN) {
        ArrayList<HydraDataClassStruct> hydraD = new ArrayList<>();

        //гидравлический расчет участка
        for (int i = 0; i < partTN.n; i++) {
            this.W[i] = Math.sqrt((0.00638 * Math.pow(partTN.G[i], 2) * 2 * g) / (Math.pow(partTN.D[i] / 1000, 4) * Math.pow(gamma, 2)));
            this.Rud[i] = (0.00638 * Math.pow(partTN.G[i], 2)) / (Math.pow(1.14 + 2 * Math.log10(partTN.D[i] / 0.5), 2) * Math.pow(partTN.D[i] / 1000, 5) * gamma);
            this.Rrash[i] = (0.00638 * Math.pow(partTN.G[i], 2)) / (Math.pow(1.14 + 2 * Math.log10(partTN.D[i] / partTN.Kekv[i]), 2) * Math.pow(partTN.D[i] / 1000, 5) * gamma);
            this.b[i] = this.Rrash[i] / this.Rud[i];
            this.Hl[i] = this.Rrash[i] * L[i];
            this.Hm[i] = this.Hl[i] * 0.2;
            this.H1x[i] = this.Hl[i] + this.Hm[i];
            this.H2x[i] = this.H1x[i] * 2 / 1000;
            dHsum += this.H2x[i];
            this.dH_fist[i] = dHsum;
            this.Hrasp_endP[i] = partTN.Hrasp_ist - this.dH_fist[i];
            hydraD.add( new HydraDataClassStruct(NamePartTN[i],
                                                 NamePartTNpred[i],
                                                 D[i],
                                                 L[i],
                                                 G[i],
                                                 Kekv[i],
                                                 Geo[i],
                                                 ZdanieEtaj[i],
                                                 Hrasp_ist,
                                                 W[i],
                                                 Rud[i],
                                                 b[i],
                                                 Rrash[i],
                                                 Hl[i],
                                                 Hm[i],
                                                 H1x[i],
                                                 H2x[i],
                                                 dH_fist[i], //падение напора от источника
                                                 Hrasp_endP[i],
                                                 BoilerName,
                                                 MainName,
                                                 BranchName
                                                ) );

        }

        return hydraD;
        //конец гидравлического расчета данного участка

    }

}
