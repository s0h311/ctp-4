public class Feld_MCDC extends Feld {
  private String signA;
  private String signB;
  private String signC;
  private String MCDC;

  public Feld_MCDC(boolean a, boolean b, boolean c, boolean cond) {
    super(a, b, c, cond);
    this.signA = "";
    this.signB = "";
    this.signC = "";
    this.MCDC = "";
  }

  @Override
  public String toMDHeaders() {
    return super.toMDHeaders() + " SA | SB | SC | MCDC |";
  }

  @Override
  public String toCSVHeaders() {
    return super.toCSVHeaders() + ";SIGNA;SIGNB;SIGNC;MCDC";
  }

  @Override
  public String toString() {
    return super.toString() + signA + " | " + signB + " | " + signC + " | " + MCDC + " |";
  }

  @Override
  public String toCSVString() {
    return super.toCSVString() + ";" + signA + ";" + signB + ";" + signC + ";" + MCDC;
  }

  public String getSign(char sign) {
    return switch (sign) {
      case 'A' -> signA;
      case 'B' -> signB;
      case 'C' -> signC;
      default -> "";
    };
  }

  public void setSign(char sign, String value) {
    switch (sign) {
      case 'A':
        setSignA(value);
        break;
      case 'B':
        setSignB(value);
        break;
      case 'C':
        setSignC(value);
        break;
      default:
        break;
    }
  }

  public String getSignA() {
    return signA;
  }

  public void setSignA(String signA) {
    this.signA = signA;
  }

  public String getSignB() {
    return signB;
  }

  public void setSignB(String signB) {
    this.signB = signB;
  }

  public String getSignC() {
    return signC;
  }

  public void setSignC(String signC) {
    this.signC = signC;
  }

  public String getMCDC() {
    return MCDC;
  }

  public void setMCDC(String MCDC) {
    this.MCDC = MCDC;
  }
}
