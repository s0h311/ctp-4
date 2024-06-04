public class Feld_MMBUE extends Feld {
    private String mmbue;

    public Feld_MMBUE(boolean a, boolean b, boolean c, boolean cond) {
        super(a, b, c, cond);
    }

    public void setMMBUE(String mmbue) {
        this.mmbue = mmbue;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + mmbue;
    }

    public String toCSVString() {
        return (isA() ? "1" : "0") + ";" + (isB() ? "1" : "0") + ";" + (isC() ? "1" : "0") + ";" + (isCond() ? "1" : "0") + ";" + mmbue;
    }
}