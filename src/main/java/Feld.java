public class Feld {
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean cond;

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

    public static String getHeader() {
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
}