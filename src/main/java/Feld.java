public class Feld {
    private boolean[] columns;
    private boolean cond;

    public Feld(boolean[] columns, boolean cond) {
        this.columns = columns;
        this.cond = cond;
    }

    public boolean[] getColumns() {
        return columns;
    }

    public boolean isCond() {
        return cond;
    }

    public String toMDHeaders() {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < columns.length; i++) {
            sb.append(" A").append(i).append(" |");
        }
        sb.append(" B |");
        return sb.toString();
    }

    public String toCSVHeaders() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            sb.append("A").append(i).append(";");
        }
        sb.append("B");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("| ");
        for (int i = 0; i < columns.length; i++) {
            sb.append(columns[i] ? "1" : "0").append(" | ");
        }
        sb.append(cond ? "1" : "0").append(" | ");
        return sb.toString();
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            sb.append(columns[i] ? "1" : "0").append(";");
        }
        sb.append(cond ? "1" : "0");
        return sb.toString();
    }
}
