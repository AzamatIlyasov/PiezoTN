package tn.piezo.model;

import javafx.beans.property.*;

/**
 * Created by djaza on 16.02.2017.
 * класс обертка
 */
public class HydraC {

    // свойства - исходные данные для расчета
    private StringProperty NamePartTN;
    private StringProperty NamePartTNpred;
    private DoubleProperty D;
    private DoubleProperty L;
    private DoubleProperty G;
    private DoubleProperty Kekv;
    private DoubleProperty Geo;
    private DoubleProperty ZdanieEtaj;
    private DoubleProperty Hrasp_ist;
    private IntegerProperty num; // для нумерации участков

    // свойства - результаты расчета
    private DoubleProperty W;
    private DoubleProperty Rud;
    private DoubleProperty b;
    private DoubleProperty Rrash;
    private DoubleProperty Hl;
    private DoubleProperty Hm;
    private DoubleProperty H1x;
    private DoubleProperty H2x;
    private DoubleProperty dH_fist; //падение напора от источника
    private DoubleProperty Hrasp_endP; // падение напора в конце участка

    //дополнительные сведения
    private StringProperty BoilerName;
    private StringProperty MainName;
    private StringProperty BranchName;

    /**
     * Конструктор по умолчанию.
     */
    public HydraC() {
        this(null, null, 0, 0, 0,0,0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,0, 0,
                null, null, null);
    }

    /**
     * Конструктор с некоторыми начальными данными.
     * - параметры участков
     * @param NamePartTN - название текущего (расчетного) участка
     * @param NamePartTNpred - название предыдущего участка
     * @param D - диаметр, мм
     * @param L - длина, м
     * @param G - расход, т/ч
     * @param Kekv - кэкв, мм
     * @param Geo - геодезическая отметка, м
     * @param Hrasp_ist - распологаемы напор у источника, м
     * @param W - скорость, м/с
     * @param Rud - Rуд
     * @param b - коэф-т б
     * @param Rrash - Rрасчетный
     * @param Hl - линейное падение напора, м
     * @param Hm - местное падение напора, м
     * @param H1x - падение напора в одной трубе, м
     * @param H2x - в двух трубах, м
     * @param dH_fist - падение напора от источника, м
     * @param Hrasp_endP - распологаемы напор в конце учатска, м
     * @param num - номер
     * @param BoilerName - название котельной (источника)
     * @param MainName - название магистрали
     * @param BranchName - название ответвления
     */
    public HydraC(String NamePartTN, String NamePartTNpred, double D, double L, double G, double Kekv,
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
                  double Hrasp_endP, // падение напора в конце участка
                  int num,
                  String BoilerName,
                  String MainName,
                  String BranchName
                    ) {
        //исходные данные
        this.NamePartTN = new SimpleStringProperty(NamePartTN);
        this.NamePartTNpred = new SimpleStringProperty(NamePartTNpred);
        this.D = new SimpleDoubleProperty(D);
        this.L = new SimpleDoubleProperty(L);
        this.G = new SimpleDoubleProperty(G);
        this.Kekv = new SimpleDoubleProperty(Kekv);
        this.Geo = new SimpleDoubleProperty(Geo);
        this.ZdanieEtaj = new SimpleDoubleProperty(ZdanieEtaj);
        this.Hrasp_ist = new SimpleDoubleProperty(Hrasp_ist);
        // расчетные данные
        this.W = new SimpleDoubleProperty(W);
        this.Rud = new SimpleDoubleProperty(Rud);
        this.b = new SimpleDoubleProperty(b);
        this.Rrash = new SimpleDoubleProperty(Rrash);
        this.Hl = new SimpleDoubleProperty(Hl);
        this.Hm = new SimpleDoubleProperty(Hm);
        this.H1x = new SimpleDoubleProperty(H1x);
        this.H2x = new SimpleDoubleProperty(H2x);
        this.dH_fist = new SimpleDoubleProperty(dH_fist);
        this.Hrasp_endP = new SimpleDoubleProperty(Hrasp_endP);
        this.num = new SimpleIntegerProperty(num);
        //дополнительные сведения
        this.BoilerName  = new SimpleStringProperty(BoilerName);
        this.MainName  = new SimpleStringProperty(MainName);
        this.BranchName  = new SimpleStringProperty(BranchName);
    }

    // конструктор не полноценный
    public HydraC(String NamePartTN,
                  String NamePartTNpred,
                  double D,
                  double L,
                  double G,
                  double Kekv,
                  double Geo,
                  double ZdanieEtaj,
                  int num,
                  String BoilerName,
                  String MainName,
                  String BranchName
                    ) {
        //исходные данные
        this.NamePartTN = new SimpleStringProperty(NamePartTN);
        this.NamePartTNpred = new SimpleStringProperty(NamePartTNpred);
        this.D = new SimpleDoubleProperty(D);
        this.L = new SimpleDoubleProperty(L);
        this.G = new SimpleDoubleProperty(G);
        this.Kekv = new SimpleDoubleProperty(Kekv);
        this.Geo = new SimpleDoubleProperty(Geo);
        this.ZdanieEtaj = new SimpleDoubleProperty(ZdanieEtaj);
        this.num = new SimpleIntegerProperty(num);
        //дополнительные сведения
        this.BoilerName  = new SimpleStringProperty(BoilerName);
        this.MainName  = new SimpleStringProperty(MainName);
        this.BranchName  = new SimpleStringProperty(BranchName);
    }

    //NamePartTN гетер, сетер, пропер
    public String getNamePartTN() {
        return NamePartTN.get();
    }

    public void setNamePartTN(String NamePartTN) {
        this.NamePartTN.set(NamePartTN);
    }

    public StringProperty NamePartTNProperty() {
        return NamePartTN;
    }

    //NamePartTNpred гетер, сетер, пропер
    public String getNamePartTNpred() {
        return NamePartTNpred.get();
    }

    public void setNamePartTNpred(String NamePartTNpred) {
        this.NamePartTNpred.set(NamePartTNpred);
    }

    public StringProperty NamePartTNpredProperty() {
        return NamePartTNpred;
    }

    //D гетер, сетер, пропер
    public Double getD() {
        return D.get();
    }

    public void setD(Double D) {
        this.D.set(D);
    }

    public DoubleProperty DProperty() {
        return D;
    }

    //L гетер, сетер, пропер
    public Double getL() {
        return L.get();
    }

    public void setL(Double L) {
        this.L.set(L);
    }

    public DoubleProperty LProperty() {
        return L;
    }

    //G гетер, сетер, пропер
    public Double getG() {
        return G.get();
    }

    public void setG(Double G) {
        this.G.set(G);
    }

    public DoubleProperty GProperty() {
        return G;
    }

    //Kekv гетер, сетер, пропер
    public Double getKekv() {
        return Kekv.get();
    }

    public void setKekv(Double Kekv) {
        this.Kekv.set(Kekv);
    }

    public DoubleProperty KekvProperty() {
        return Kekv;
    }

    //Geo гетер, сетер, пропер
    public Double getGeo() {
        return Geo.get();
    }

    public void setGeo(Double Geo) {
        this.Geo.set(Geo);
    }

    public DoubleProperty GeoProperty() {
        return Geo;
    }

    //ZdanieEtaj гетер, сетер, пропер
    public Double getZdanieEtaj() {
        return ZdanieEtaj.get();
    }

    public void setZdanieEtaj(Double ZdanieEtaj) {
        this.ZdanieEtaj.set(ZdanieEtaj);
    }

    public DoubleProperty ZdanieEtajProperty() {
        return ZdanieEtaj;
    }

    //Hrasp_ist гетер, сетер, пропер
    public Double getHrasp_ist() {
        return Hrasp_ist.get();
    }

    public void setHrasp_ist(Double Hrasp_ist) {
        this.Hrasp_ist.set(Hrasp_ist);
    }

    public DoubleProperty Hrasp_istProperty() {
        return Hrasp_ist;
    }

    //W гетер, сетер, пропер
    public Double getW() {
        return W.get();
    }

    public void setW(Double W) {
        this.W.set(W);
    }

    public DoubleProperty WProperty() {
        return W;
    }

    //Rud гетер, сетер, пропер
    public Double getRud() {
        return Rud.get();
    }

    public void setRud(Double Rud) {
        this.Rud.set(Rud);
    }

    public DoubleProperty RudProperty() {
        return Rud;
    }

    //b гетер, сетер, пропер
    public Double getb() {
        return b.get();
    }

    public void setb(Double b) {
        this.b.set(b);
    }

    public DoubleProperty bProperty() {
        return b;
    }

    //Rrash гетер, сетер, пропер
    public Double getRrash() {
        return Rrash.get();
    }

    public void setRrash(Double Rrash) {
        this.Rrash.set(Rrash);
    }

    public DoubleProperty RrashProperty() {
        return Rrash;
    }

    //Hl гетер, сетер, пропер
    public Double getHl() {
        return Hl.get();
    }

    public void setHl(Double Hl) {
        this.Hl.set(Hl);
    }

    public DoubleProperty HlProperty() {
        return Hl;
    }

    //Hm гетер, сетер, пропер
    public Double getHm() {
        return Hm.get();
    }

    public void setHm(Double Hm) {
        this.Hm.set(Hm);
    }

    public DoubleProperty HmProperty() {
        return Hm;
    }

    //H1x гетер, сетер, пропер
    public Double getH1x() {
        return H1x.get();
    }

    public void setH1x(Double H1x) {
        this.H1x.set(H1x);
    }

    public DoubleProperty H1xProperty() {
        return H1x;
    }

    //H2x гетер, сетер, пропер
    public Double getH2x() {
        return H2x.get();
    }

    public void setH2x(Double H2x) {
        this.H2x.set(H2x);
    }

    public DoubleProperty H2xProperty() {
        return H2x;
    }

    //dH_fist гетер, сетер, пропер
    public Double getdH_fist() {
        return dH_fist.get();
    }

    public void setdH_fist(Double dH_fist) {
        this.dH_fist.set(dH_fist);
    }

    public DoubleProperty dH_fistProperty() {
        return dH_fist;
    }

    //Hrasp_endP гетер, сетер, пропер
    public Double getHrasp_endP() {
        return Hrasp_endP.get();
    }

    public void setHrasp_endP(Double Hrasp_endP) {
        this.Hrasp_endP.set(Hrasp_endP);
    }

    public DoubleProperty Hrasp_endPProperty() {
        return Hrasp_endP;
    }

    //num гетер, сетер, пропер
    public Integer getNum() {
        return num.get();
    }

    public void setNum(Integer Num) {
        this.num.set(Num);
    }

    public IntegerProperty NumProperty() {
        return num;
    }

    //BoilerName гетер, сетер
    public StringProperty getBoilerName() {
        return BoilerName;
    }

    public void setBoilerName(StringProperty boilerName) {
        BoilerName = boilerName;
    }

    //MainName гетер, сетер
    public StringProperty getMainName() {
        return MainName;
    }

    public void setMainName(StringProperty mainName) {
        MainName = mainName;
    }

    //BranchName гетер, сетер
    public StringProperty getBranchName() {
        return BranchName;
    }

    public void setBranchName(StringProperty branchName) {
        BranchName = branchName;
    }

}
