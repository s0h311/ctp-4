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

    @Override
    public String toString() {
        return (a ? "1" : "0") + " | " + (b ? "1" : "0") + " | " + (c ? "1" : "0") + " | " + (cond ? "1" : "0");
    }
}