import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Playground {

    public static void main(String[] args) {
        Playground p1 = new Playground();
        List<Feld> felder = new ArrayList<>();
        // ToDO: nicht hardcoded
        String inputFilePath = "src/main/resources/exercise1.csv";
        String outputFilePath = "src/main/resources/new_data.csv";

        // Datei einlesen
        felder = p1.readInputFromCSV(inputFilePath);

        List<Feld_MMBUE> mmbue_felder = new ArrayList<>();
        for (Feld feld : felder) {
            mmbue_felder.add(new Feld_MMBUE(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
        }
        for (Feld_MMBUE feld : mmbue_felder) {
            // TODO : Füge hier die Logik für die neue Spalte hinzu
            if (feld.isA()) {
                feld.setMMBUE("X");
            } else {
                feld.setMMBUE("-");
            }
        }

        p1.writeOutputToCSV(outputFilePath, mmbue_felder);
    }

    public List<Feld> readInputFromMD(String inputFilePath) {
        List<Feld> felder = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip Header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.trim().isEmpty() || line.startsWith("| ---")) {
                    continue;
                }
                String[] values = line.split("\\|");
                if (values.length == 5) {
                    boolean a = values[1].trim().equals("1");
                    boolean b = values[2].trim().equals("1");
                    boolean c = values[3].trim().equals("1");
                    boolean cond = values[4].trim().equals("1");
                    Feld feld = new Feld(a, b, c, cond);
                    felder.add(feld);
                } else {
                    throw new Exception("Input-File not in correct format");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return felder;
    }

    public void writeOutputToMD(String outputFilePath, List<Feld_MMBUE> felder) {
        // Neue Datei schreiben
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write("| A0 | A1 | A2 | B |\n");
            bw.write("| --- | --- | --- | --- |\n");
            for (Feld_MMBUE feld : felder) {
                bw.write("| " + feld.toString() + " |\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Feld> readInputFromCSV(String inputFilePath) {
        List<Feld> felder = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip Header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(";");
                if (values.length == 4) {
                    boolean a = values[0].trim().equals("1");
                    boolean b = values[1].trim().equals("1");
                    boolean c = values[2].trim().equals("1");
                    boolean cond = values[3].trim().equals("1");
                    Feld feld = new Feld(a, b, c, cond);
                    felder.add(feld);
                } else {
                    throw new Exception("Input-File not in correct format");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return felder;
    }

    public void writeOutputToCSV(String outputFilePath, List<Feld_MMBUE> felder) {
        // Neue Datei schreiben
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write("A0;A1;A2;B\n");
            for (Feld_MMBUE feld : felder) {
                bw.write(feld.toCSVString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}