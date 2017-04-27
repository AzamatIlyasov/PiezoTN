package tn.piezo.model;

/**
 * Created by djaza on 18.02.2017.
 * класс - структура данных
 */
public class HydraDataClassStruct {

    // структура данных Гидравлического расчета
    public String NamePartTN;
    public String NamePartTNpred;
    public double D;
    public double L;
    public double G;
    public double Kekv;
    public double Geo;
    public double ZdanieEtaj;
    public double Hrasp_ist;
    // *расчетные переменые
    public double W;
    public double Rud;
    public double b;
    public double Rrash;
    public double Hl;
    public double Hm;
    public double H1x;
    public double H2x;
    public double dH_fist; //падение напора от источника
    public double Hrasp_endP; // падение напора в конце участка

    public HydraDataClassStruct( String NamePartTN, String NamePartTNpred, double D, double L, double G, double Kekv,
                                 double Geo,
                                 double ZdanieEtaj,
                                 double Hrasp_ist,
                                 double W,
                                 double Rud,
                                 double b,
                                 double Rrash,
                                 double Hl,
                                 double Hm,
                                 double H1x,
                                 double H2x,
                                 double dH_fist, //падение напора от источника
                                 double Hrasp_endP // падение напора в конце участка
                                ) {
        this.NamePartTN = NamePartTN;
        this.NamePartTNpred = NamePartTNpred;
        this.D = D;
        this.L = L;
        this.G = G;
        this.Kekv = Kekv;
        this.Geo = Geo;
        this.ZdanieEtaj = ZdanieEtaj;
        this.Hrasp_ist = Hrasp_ist;
        this.W = W;
        this.Rud = Rud;
        this.b = b;
        this.Rrash = Rrash;
        this.Hl = Hl;
        this.Hm = Hm;
        this.H1x = H1x;
        this.H2x = H2x;
        this.dH_fist = dH_fist;
        this.Hrasp_endP = Hrasp_endP;

    }

}
