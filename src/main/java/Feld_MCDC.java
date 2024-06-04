public class Feld_MCDC extends Feld {
    private String signA;
    private String signB;
    private String signC;
    private String MCDC;

    public Feld_MCDC(boolean a, boolean b, boolean c, boolean cond) {
        super(a, b, c, cond);
        signA = "";
        signB = "";
        signC = "";
        MCDC = "";

    }

    @Override
    public String toString() {
        return super.isA() + " | " + super.isB() + " | " + super.isB() + " | " + super.isCond() + " | " + signA + " | " + signB + " | " + signC + " | " + MCDC;
    }

    public String getSignA() {
        return signA;
    }

    public String getSignB() {
        return signB;
    }

    public String getSignC() {
        return signC;
    }

    public String getMCDC() {
        return MCDC;
    }

    public void setSignA(String signA) {
        this.signA = signA;
    }

    public void setSignB(String signB) {
        this.signB = signB;
    }

    public void setSignC(String signC) {
        this.signC = signC;
    }

    public void setMCDC(String MCDC) {
        this.MCDC = MCDC;
    }
}
