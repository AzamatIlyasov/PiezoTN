package tn.piezo.model;

import javafx.beans.property.*;

/**
 * Created by djaza on 16.02.2017.
 * класс обертка
 */
public class HydraC {

    // свойства - исходные данные для расчета
    private final StringProperty NamePartTN;
    private final StringProperty NamePartTNpred;
    private final DoubleProperty D;
    private final DoubleProperty L;
    private final DoubleProperty G;
    private final DoubleProperty Kekv;
    private final DoubleProperty Geo;
    private final DoubleProperty ZdanieEtaj;
    private final DoubleProperty Hrasp_ist;
    private final IntegerProperty num; // для нумерации участков

    // свойства - результаты расчета
    private final DoubleProperty W;
    private final DoubleProperty Rud;
    private final DoubleProperty b;
    private final DoubleProperty Rrash;
    private final DoubleProperty Hl;
    private final DoubleProperty Hm;
    private final DoubleProperty H1x;
    private final DoubleProperty H2x;
    private final DoubleProperty dH_fist; //падение напора от источника
    private final DoubleProperty Hrasp_endP; // падение напора в конце участка

    /**
     * Конструктор по умолчанию.
     */
    public HydraC() {
        this(null, null, 0, 0, 0,0,0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,0, 0);
    }

    /**
     * Конструктор с некоторыми начальными данными.
     * - параметры участков
     * @param NamePartTN - название участка
     * @param D - диаметр
     * @param L - длина
     * @param G - расход
     * @param Kekv - кэкв
     * @param Hrasp_ist - распологаемы напор у источника
     * @param W - скорость
     * @param Rud - Rуд
     * @param b - коэф-т б
     * @param Rrash - Rрасчетный
     * @param Hl - линейное падение напора
     * @param Hm - местное падение напора
     * @param H1x - падение напора в одной трубе
     * @param H2x - в двух трубах
     * @param dH_fist - падение напора от источника
     * @param Hrasp_endP - распологаемы напор в конце учатска
     * @param num - номер
     */
    public HydraC(String NamePartTN, String NamePartTNpred, double D, double L, double G, double Kekv, double Geo,
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
                  int num
                    ) {
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

}
