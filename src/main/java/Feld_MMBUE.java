public class Feld_MMBUE extends Feld{
    String MMBUE;

    public Feld_MMBUE(boolean a, boolean b, boolean c, boolean cond) {
        super(a, b, c, cond);
    }

    @Override
    public String toString() {
        return super.isA() + " | " + super.isB() + " | " + super.isB() + " | " + super.isCond() + " | " + MMBUE;
    }

    public String getMMBUE() {
        return MMBUE;
    }

    public void setMMBUE(String MMBUE) {
        this.MMBUE = MMBUE;
    }
}
