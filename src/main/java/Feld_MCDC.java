import java.util.Arrays;

public class Feld_MCDC extends Feld {
  private String[] signs;
  private String MCDC;

  public Feld_MCDC(boolean[] columns, boolean cond) {
    super(columns, cond);
    this.signs = new String[columns.length];
    Arrays.fill(this.signs, "");
    this.MCDC = "";
  }

  public String[] getSigns() {
    return signs;
  }

  @Override
  public String toMDHeaders() {
    StringBuilder sb = new StringBuilder(super.toMDHeaders());
    for (int i = 0; i < signs.length; i++) {
      sb.append(" S").append((char) ('A' + i)).append(" |");
    }
    sb.append(" MCDC |");
    return sb.toString();
  }

  @Override
  public String toCSVHeaders() {
    StringBuilder sb = new StringBuilder(super.toCSVHeaders());
    for (int i = 0; i < signs.length; i++) {
      sb.append(";SIGN").append((char) ('A' + i));
    }
    sb.append(";MCDC");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    for (String sign : signs) {
      sb.append(sign).append(" | ");
    }
    sb.append(MCDC).append(" |");
    return sb.toString();
  }

  @Override
  public String toCSVString() {
    StringBuilder sb = new StringBuilder(super.toCSVString());
    for (String sign : signs) {
      sb.append(";").append(sign);
    }
    sb.append(";").append(MCDC);
    return sb.toString();
  }

  public String getSign(int index) {
    if (index >= 0 && index < signs.length) {
      return signs[index];
    }
    return "";
  }

  public void setSign(int index, String value) {
    if (index >= 0 && index < signs.length) {
      signs[index] = value;
    }
  }

  public String getMCDC() {
    return MCDC;
  }

  public void setMCDC(String MCDC) {
    this.MCDC = MCDC;
  }
}
