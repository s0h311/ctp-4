public class Field {
  private boolean a;
  private boolean b;
  private boolean c;
  private boolean cond;

  public Field(boolean a, boolean b, boolean c, boolean cond) {
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

  public String toMDHeaders() {
    return "| A0 | A1 | A2 | B |";
  }

  public String toCSVHeaders() {
    return "A0;A1;A2;B";
  }

  @Override
  public String toString() {
    return "| " + (a ? "1" : "0") + " | " + (b ? "1" : "0") + " | " + (c ? "1" : "0") + " | " + (cond ? "1" : "0") + " | ";
  }

  public String toCSVString() {
    return (isA() ? "1" : "0") + ";" + (isB() ? "1" : "0") + ";" + (isC() ? "1" : "0") + ";" + (isCond() ? "1" : "0");
  }
}