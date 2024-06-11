import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Application {

  public static void main(String[] args) {
    if (args.length < 4) {
      throw new IllegalArgumentException("Usage: java Playground <inputPath> <baseOutputPath> <format> <method:mmbue,mcdc or both>");
    }

    String inputPath = args[0];
    String baseOutputPath = args[1];
    String format = args[2];
    String method = args[3].toLowerCase();
    boolean useMMBUE = method.equals("both") || method.equals("mmbue");
    boolean useMCDC = method.equals("both") || method.equals("mcdc");

    File inputDirectory = new File(inputPath);
    if (inputDirectory.isDirectory()) {
      processDirectory(inputPath, baseOutputPath, format, useMMBUE, useMCDC);
    }
    else {
      orchestrator(inputPath, baseOutputPath, format, useMMBUE, useMCDC);
    }
  }

  public static void processDirectory(String inputDirectoryPath, String baseOutputPath, String format, boolean useMMBUE, boolean useMCDC) {
    File inputDirectory = new File(inputDirectoryPath);
    File[] files = inputDirectory.listFiles();

    for (File file : files) {
      if (file.isFile()) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        if (extension.equals(".csv") || extension.equals(".md")) {
          String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
          orchestrator(file.getPath(), baseOutputPath + fileNameWithoutExtension, format, useMMBUE, useMCDC);
        }
        else {
          throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
      }
    }
  }

  public static void orchestrator(String inputFilePath, String baseOutputFilePath, String formatInput, boolean useMMBUE, boolean useMCDC) {
    List<Feld> felder;
    String format = formatInput.equals("csv") ? ".csv" : ".md";

    Application p1 = new Application();
    felder = p1.readInput(inputFilePath);

    if (useMMBUE) {
      List<Feld_MMBUE> mmbueFelder = new ArrayList<>();
      for (Feld feld : felder) {
        mmbueFelder.add(new Feld_MMBUE(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
      }
      p1.doMMBUE(mmbueFelder);
      String outputFilePath = baseOutputFilePath + "_MMBUE" + format;
      p1.writeOutput(format, outputFilePath, mmbueFelder);
    }

    if (useMCDC) {
      List<Feld_MCDC> mcdcFelder = new ArrayList<>();
      for (Feld feld : felder) {
        mcdcFelder.add(new Feld_MCDC(feld.isA(), feld.isB(), feld.isC(), feld.isCond()));
      }
      p1.doMCDC(mcdcFelder);
      String outputFilePath = baseOutputFilePath + "_MCDC" + format;
      p1.writeOutput(format, outputFilePath, mcdcFelder);
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
          String[] valuesToCheck = {values[isMarkdown ? 1 : 0], values[isMarkdown ? 2 : 1], values[isMarkdown ? 3 : 2], values[isMarkdown ? 4 : 3]};
          boolean[] boolValues = new boolean[4];

          for (int i = 0; i < 4; i++) {
            String value = valuesToCheck[i].trim();
            if (value.equals("0") || value.equals("1")) {
              boolValues[i] = value.equals("1");
            } else {
              throw new IllegalArgumentException("Value is not '0' or '1'");
            }
          }
          Feld feld = new Feld(boolValues[0], boolValues[1], boolValues[2], boolValues[3]);
          felder.add(feld);
        }
        else {
          throw new IllegalArgumentException("Input-File not in correct format");
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    return felder;
  }

  public void writeOutput(String inputFilePath, String outputFilePath, List<? extends Feld> felder) {

    boolean isMarkdown = inputFilePath.equals(".md");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
      if (isMarkdown) {
        String mdHeaders = felder.get(0).toMDHeaders();
        bw.write(mdHeaders + "\n");

        int count = (int) mdHeaders.chars().filter(ch -> ch == '|').count() - 1;
        String separatorLine = "| --- ".repeat(Math.max(0, count)) + "|\n";
        bw.write(separatorLine);
      }
      else {
        bw.write(felder.get(0).toCSVHeaders() + "\n");
      }
      for (Feld feld : felder) {
        if (isMarkdown) {
          bw.write(feld.toString() + "\n");
        }
        else {
          bw.write(feld.toCSVString() + "\n");
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e.getMessage());
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
        }
        else {
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
      }
      else {
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