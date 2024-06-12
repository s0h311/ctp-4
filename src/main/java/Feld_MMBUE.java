public class Feld_MMBUE extends Feld {
    private String mmbue;

    public Feld_MMBUE(boolean[] columns, boolean cond) {
        super(columns, cond);
        this.mmbue = "";
    }

    @Override
    public String toMDHeaders() {
        return super.toMDHeaders()+ " MMBUE |";
    }

    @Override
    public String toCSVHeaders() {
        return super.toCSVHeaders() + ";MMBUE";
    }

    @Override
    public String toString() {
        return super.toString() + mmbue +" |";
    }

    @Override
    public String toCSVString() {
        return super.toCSVString() + ";" + mmbue;
    }

    public String getMMBUE() {
        return mmbue;
    }

    public void setMMBUE(String mmbue) {
        this.mmbue = mmbue;
    }
}