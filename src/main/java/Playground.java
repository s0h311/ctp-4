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
        String format = ".md";
        //String format = ".csv";

        // Configurable file paths and methods
        String inputFilePath = "src/main/resources/exercise1.md";
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
                if (isMarkdown) {
                    String mdHeaders = felder.get(0).toMDHeaders();
                    bw.write(mdHeaders + "\n");

                    int count = (int) mdHeaders.chars().filter(ch -> ch == '|').count() - 1;
                    String separatorLine = "| --- ".repeat(Math.max(0, count)) + "|\n";
                    bw.write(separatorLine);
                } else {
                    bw.write(felder.get(0).toCSVHeaders() + "\n");
                }
                for (Feld feld : felder) {
                    if (isMarkdown) {
                        bw.write(feld.toString() + "\n");
                    } else {
                        bw.write(feld.toCSVString() + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doMMBUE(List<Feld_MMBUE> felder) {
        for (Feld_MMBUE feld : felder) {
            if (feld.getMMBUE().isEmpty()) {
                Feld_MMBUE condToggleA = findMatchingField(felder, !feld.isA(), feld.isB(), feld.isC());
                Feld_MMBUE condToggleB = findMatchingField(felder, feld.isA(), !feld.isB(), feld.isC());
                Feld_MMBUE condToggleC = findMatchingField(felder, feld.isA(), feld.isB(), !feld.isC());

                if (condToggleA != null && condToggleB != null && condToggleC != null && feld.isCond() == condToggleA.isCond() && feld.isCond() == condToggleB.isCond() && feld.isCond() == condToggleC.isCond()) {
                    feld.setMMBUE("-");
                } else {
                    feld.setMMBUE("X");
                }
            }
        }
    }

    public void doMCDC(List<Feld_MCDC> felder) {
        int[] counters = new int[]{1, 1, 1};

        // Sign A, Sign B, Sign C
        for (Feld_MCDC feld : felder) {
            counters[0] = updateSignIfNeeded(felder, feld, 'A', counters[0]);
            counters[1] = updateSignIfNeeded(felder, feld, 'B', counters[1]);
            counters[2] = updateSignIfNeeded(felder, feld, 'C', counters[2]);
        }

        // MCDC
        Optional<Feld_MCDC> mostSign = felder.stream().max(Comparator.comparingInt(feld -> {
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
        felder.stream().filter(feld -> feld.getMCDC().isEmpty()).forEach(feld -> feld.setMCDC("-"));
    }

    private int updateSignIfNeeded(List<Feld_MCDC> felder, Feld_MCDC feld, char sign, int counter) {
        String signValue = feld.getSign(sign);
        if (signValue.isEmpty()) {
            Feld_MCDC condToggle = findMatchingField(felder, sign == 'A' ? !feld.isA() : feld.isA(), sign == 'B' ? !feld.isB() : feld.isB(), sign == 'C' ? !feld.isC() : feld.isC());
            if (feld.isCond() != condToggle.isCond()) {
                feld.setSign(sign, String.valueOf(sign) + counter);
                condToggle.setSign(sign, String.valueOf(sign) + counter);
                counter++;
            } else {
                feld.setSign(sign, "-");
                condToggle.setSign(sign, "-");
            }
        }
        return counter;
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