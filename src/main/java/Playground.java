import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Playground {

    public static void main(String[] args) {
        // TODO: Possibilty to address inputFilePath,baseOutputFilePath and useMMBUE/useMCDC via Terminal???
        Playground p1 = new Playground();
        List<Feld> felder = new ArrayList<>();
        //String format = ".md";
        String format = ".csv";

        // Configurable file paths and methods
        String inputFilePath = "src/main/resources/aufgaben1.csv";
        String baseOutputFilePath = "src/main/resources/Output/aufgaben1";
        boolean useMMBUE = true;
        boolean useMCDC = true;

        // Datei einlesen
        felder = p1.readInput(inputFilePath);

        // Process fields based on chosen methods
        if (useMMBUE) {
            List<Feld_MMBUE> mmbueFelder = new ArrayList<>();
            for (Feld feld : felder) {
                mmbueFelder.add(new Feld_MMBUE(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
            }
            p1.doMMBUE(mmbueFelder);
            String outputFilePath = baseOutputFilePath + "_MMBUE" + format;
            p1.writeOutput(inputFilePath, outputFilePath, mmbueFelder);
        }

        if (useMCDC) {
            List<Feld_MCDC> mcdcFelder = new ArrayList<>();
            for (Feld feld : felder) {
                mcdcFelder.add(new Feld_MCDC(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
            }
            p1.doMCDC(mcdcFelder);
            String outputFilePath = baseOutputFilePath + "_MCDC" + format;
            p1.writeOutput(inputFilePath, outputFilePath, mcdcFelder);
        }
    }

    public List<Feld> readInput(String inputFilePath) {
        List<Feld> felder = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            boolean firstLine = true;
            boolean isMarkdown = inputFilePath.endsWith(".md");
            while ((line = br.readLine()) != null) {
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
                    //TODO: improve checks if input file is correct
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

    //TODO: Decide if isMarkdown depends on inputFilePath or outputFilePath
    public void writeOutput(String inputFilePath, String outputFilePath, List<? extends Feld> felder) {
        if (felder.size() > 0) {
            boolean isMarkdown = inputFilePath.endsWith(".md");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
                //TODO: better readability
                if (isMarkdown) {
                    bw.write(felder.get(0).toMDHeaders() + "\n");
                    long count = felder.get(0).toMDHeaders().chars().filter(ch -> ch == '|').count() - 1;
                    String newLine = "";
                    for (int i = 0; i < count; i++) {
                        newLine += "| --- ";
                        if (i == count - 1) {
                            newLine += "|\n";
                        }
                    }
                    bw.write(newLine);
                } else {
                    bw.write(felder.get(0).toCSVHeaders() + "\n");
                }
                for (Feld feld : felder) {
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


    public void doMCDC(List<Feld_MCDC> felder) {
        //TODO: better readability
        int a = 1;
        int b = 1;
        int c = 1;

        // Sign A, Sign B, Sign C
        for (Feld_MCDC feld : felder) {
            if (feld.getSignA().isEmpty()) {
                Feld_MCDC condToggleA = findMatchingField(felder, !feld.isA(), feld.isB(), feld.isC());
                if (feld.isCond() != condToggleA.isCond()) {
                    feld.setSignA("A" + a);
                    condToggleA.setSignA("A" + a);
                    a++;
                } else {
                    feld.setSignA("-");
                    condToggleA.setSignA("-");
                }
            }

            if (feld.getSignB().isEmpty()) {
                Feld_MCDC condToggleB = findMatchingField(felder, feld.isA(), !feld.isB(), feld.isC());
                if (feld.isCond() != condToggleB.isCond()) {
                    feld.setSignB("B" + b);
                    condToggleB.setSignB("B" + b);
                    b++;
                } else {
                    feld.setSignB("-");
                    condToggleB.setSignB("-");
                }
            }

            if (feld.getSignC().isEmpty()) {
                Feld_MCDC condToggleC = findMatchingField(felder, feld.isA(), feld.isB(), !feld.isC());
                if (feld.isCond() != condToggleC.isCond()) {
                    feld.setSignC("C" + c);
                    condToggleC.setSignC("C" + c);
                    c++;
                } else {
                    feld.setSignC("-");
                    condToggleC.setSignC("-");
                }
            }
        }

        // MCDC
        Optional<Feld_MCDC> mostSign = felder.stream()
                .max(Comparator.comparingInt(feld -> {
                    int count = 0;
                    count += feld.getSignA().equals("-") ? 0 : 1;
                    count += feld.getSignB().equals("-") ? 0 : 1;
                    count += feld.getSignC().equals("-") ? 0 : 1;
                    return count;
                }));
        mostSign.get().setMCDC("X");
        findMatchingField(felder, mostSign.get(), mostSign.get().getSignA()).setMCDC("X");
        findMatchingField(felder, mostSign.get(), mostSign.get().getSignB()).setMCDC("X");
        findMatchingField(felder, mostSign.get(), mostSign.get().getSignC()).setMCDC("X");

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
                } else {
                    feld.setMMBUE("X");
                }
            }
        }
    }

    private <T extends Feld> T findMatchingField(List<T> felder, boolean a, boolean b, boolean c) {
        for (T feld : felder) {
            if (feld.isA() == a && feld.isB() == b && feld.isC() == c) {
                return feld;
            }
        }
        return null;
    }

    private Feld_MCDC findMatchingField(List<Feld_MCDC> felder, Feld_MCDC origin, String sign) {
        for (Feld_MCDC feld : felder) {
            if (!feld.equals(origin) && (feld.getSignA().equals(sign) || feld.getSignB().equals(sign) || feld.getSignC().equals(sign))) {
                return feld;
            }
        }
        return null;
    }
}