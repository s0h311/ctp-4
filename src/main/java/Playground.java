import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Playground {

    public static void main(String[] args) {
        Playground p1 = new Playground();
        List<Feld> felder = new ArrayList<>();
        // ToDO: nicht hardcoded
        String inputFilePath = "src/main/resources/aufgaben1.csv";
        String outputFilePath = "src/main/resources/Output/new_data.csv";

        // Datei einlesen
        felder = p1.readInput(inputFilePath);

        List<Feld_MMBUE> mmbue_felder = new ArrayList<>();
        for (Feld feld : felder) {
            mmbue_felder.add(new Feld_MMBUE(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
        }

        p1.doMMBUE(mmbue_felder);

        p1.writeOutput(inputFilePath, outputFilePath, mmbue_felder);
    }

    public void doMMBUE(List<Feld_MMBUE> felder) {
        for (Feld_MMBUE feld : felder) {
            if (feld.getMMBUE().isEmpty()) {
                Feld_MMBUE condToggleA = findMatchingField(felder, !feld.isA(), feld.isB(), feld.isC());
                Feld_MMBUE condToggleB = findMatchingField(felder, feld.isA(), !feld.isB(), feld.isC());
                Feld_MMBUE condToggleC = findMatchingField(felder, feld.isA(), feld.isB(), !feld.isC());

                if (condToggleA != null && condToggleB != null && condToggleC != null &&
                        feld.isCond() == condToggleA.isCond() &&
                        feld.isCond() == condToggleB.isCond() &&
                        feld.isCond() == condToggleC.isCond()) {
                    feld.setMMBUE("-");
                }else{
                    feld.setMMBUE("X");
                }
            }
        }
    }

    private Feld_MMBUE findMatchingField(List<Feld_MMBUE> felder, boolean a, boolean b, boolean c) {
        for (Feld_MMBUE feld : felder) {
            if (feld.isA() == a && feld.isB() == b && feld.isC() == c) {
                return feld;
            }
        }
        return null;
    }

    public List<Feld> readInput(String inputFilePath) {
        List<Feld> felder = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean firstLine = true;
            boolean isMarkdown = inputFilePath.endsWith(".md");
            while ((line = br.readLine()) != null) {
                // Skip Header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (isMarkdown && (line.trim().isEmpty() || line.startsWith("| ---"))) {
                    continue;
                }
                String[] values = isMarkdown ? line.split("\\|") : line.split(";");
                if (values.length == (isMarkdown ? 5 : 4)) {
                    boolean a = values[isMarkdown ? 1 : 0].trim().equals("1");
                    boolean b = values[isMarkdown ? 2 : 1].trim().equals("1");
                    boolean c = values[isMarkdown ? 3 : 2].trim().equals("1");
                    boolean cond = values[isMarkdown ? 4 : 3].trim().equals("1");
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

    public void writeOutput(String inputFilePath,String outputFilePath, List<Feld_MMBUE> felder) {
        boolean isMarkdown = inputFilePath.endsWith(".md");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            if (isMarkdown) {
                bw.write("| A0 | A1 | A2 | B | MMBUE |\n");
                bw.write("| --- | --- | --- | --- | --- |\n");
            } else {
                bw.write("A0;A1;A2;B;MMBUE\n");
            }
            for (Feld_MMBUE feld : felder) {
                if (isMarkdown) {
                    bw.write("| " + feld.toString() + " |\n");
                } else {
                    bw.write(feld.toCSVString() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}