public class Feld {
    boolean a;
    boolean b;
    boolean c;
    boolean cond;
    String signA;
    String signB;
    String signC;
    String MCDC;
    String MMBUE;

    public Feld(boolean a, boolean b, boolean c, boolean cond) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.cond = cond;
    }

    @Override
    public String toString() {
        return a + " | " + b + " | " + c + " | " + cond;
    }

    public String toStringMCDC() {
        return a + " | " + b + " | " + c + " | " + cond + " | " + MCDC;
    }

    public String getHeader() {
        StringBuilder header = new StringBuilder("| A0 | A1 | A2 | B |");
        return header.toString() + "\n";
    }

    public boolean isA() {
        return a;
    }

    public boolean isB() {
        return b;
    }

    public boolean isC() {
        return c;
    }

    public boolean isCond() {
        return cond;
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

    public String getMMBUE() {
        return MMBUE;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public void setC(boolean c) {
        this.c = c;
    }

    public void setCond(boolean cond) {
        this.cond = cond;
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

    public void setMMBUE(String MMBUE) {
        this.MMBUE = MMBUE;
    }
}