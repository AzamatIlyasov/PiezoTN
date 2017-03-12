package tn.piezo.model;

import javafx.beans.property.*;

/**
 * Created by djaza on 16.02.2017.
 */
public class PiezoC {
    // переменные - исходные данные для расчета
    private final DoubleProperty HSN; // напор в сетевом насосе
    private final DoubleProperty HPN; // напор в подпиточном насосе
    private final DoubleProperty Hist; // падение напора в источнике (на выходе)
    // переменные - исходные данные для расчета
    private final StringProperty NamePartTN;
    private final DoubleProperty L;
    private final DoubleProperty Geo;
    private final DoubleProperty ZdanieEtaj;
    private final DoubleProperty Hrasp_ist;
    private final DoubleProperty H1x;
    // переменные для расчета
    private final DoubleProperty Hpoln;
    private final DoubleProperty HraspPod;
    private final DoubleProperty HraspObrat;

    /**
     * Конструктор по умолчанию.
     */
    public PiezoC() {
        this(0, 0, 0, null, 0, 0, 0, 0,0);
    }
    /**
     * Конструктор с некоторыми начальными данными.
     *
     * @param HSN
     * @param HPN
     * @param Hist
     * @param NamePartTN
     * @param L
     * @param Geo
     * @param ZdanieEtaj
     * @param Hrasp_ist
     * @param H1x
     */
    public PiezoC(double HSN, double HPN, double Hist, String NamePartTN, double L, double Geo, double ZdanieEtaj,
                  double Hrasp_ist, double H1x )
    {
        this.HSN = new SimpleDoubleProperty(HSN);
        this.HPN = new SimpleDoubleProperty(HPN);
        this.Hist = new SimpleDoubleProperty(Hist);
        this.NamePartTN = new SimpleStringProperty(NamePartTN);
        this.L = new SimpleDoubleProperty(L);
        this.Geo = new SimpleDoubleProperty(Geo);
        this.ZdanieEtaj = new SimpleDoubleProperty(ZdanieEtaj);
        this.Hrasp_ist = new SimpleDoubleProperty(Hrasp_ist);
        this.H1x = new SimpleDoubleProperty(H1x);
        this.Hpoln = new SimpleDoubleProperty(0);
        this.HraspPod = new SimpleDoubleProperty(0);
        this.HraspObrat = new SimpleDoubleProperty(0);
    }

    public PiezoC(int numTN, String NamePartTN,
                  double L, double Geo, double ZdanieEtaj, double HraspPod, double HraspObrat )
    {
        this.HSN = new SimpleDoubleProperty(0);
        this.HPN = new SimpleDoubleProperty(0);
        this.Hist = new SimpleDoubleProperty(0);
        this.NamePartTN = new SimpleStringProperty(NamePartTN);
        this.L = new SimpleDoubleProperty(L);
        this.Geo = new SimpleDoubleProperty(Geo);
        this.ZdanieEtaj = new SimpleDoubleProperty(ZdanieEtaj);
        this.Hrasp_ist = new SimpleDoubleProperty(0);
        this.H1x = new SimpleDoubleProperty(0);
        this.Hpoln = new SimpleDoubleProperty(0);
        this.HraspPod = new SimpleDoubleProperty(HraspPod);
        this.HraspObrat = new SimpleDoubleProperty(HraspObrat);
    }

    //HSN гетер, сетер, пропер
    public Double getHSN() {
        return HSN.get();
    }

    public void setHSN(Double HSN) {
        this.HSN.set(HSN);
    }

    public DoubleProperty HSNProperty() {
        return HSN;
    }

    //HPN гетер, сетер, пропер
    public Double getHPN() {
        return HPN.get();
    }

    public void setHPN(Double HPN) {
        this.HPN.set(HPN);
    }

    public DoubleProperty HPNProperty() {
        return HPN;
    }

    //Hist гетер, сетер, пропер
    public Double getHist() {
        return Hist.get();
    }

    public void setHist(Double Hist) {
        this.Hist.set(Hist);
    }

    public DoubleProperty HistProperty() {
        return Hist;
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

    //HraspPod гетер, сетер, пропер
    public Double getHraspPod() {
        return HraspPod.get();
    }

    public void setHraspPod(Double HraspPod) {
        this.HraspPod.set(HraspPod);
    }

    public DoubleProperty HraspPodProperty() {
        return HraspPod;
    }

    //HraspObrat гетер, сетер, пропер
    public Double getHraspObrat() {
        return HraspObrat.get();
    }

    public void setHraspObrat(Double HraspObrat) {
        this.HraspObrat.set(HraspObrat);
    }

    public DoubleProperty HraspObratProperty() {
        return HraspObrat;
    }

    //Hpoln гетер, сетер, пропер
    public Double getHpoln() {
        return Hpoln.get();
    }

    public void setHpoln(Double Hpoln) {
        this.Hpoln.set(Hpoln);
    }

    public DoubleProperty HpolnProperty() {
        return Hpoln;
    }

}
