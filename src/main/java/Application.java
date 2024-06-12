import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        mmbueFelder.add(new Feld_MMBUE(feld.getColumns(), feld.isCond()));
      }
      p1.doMMBUE(mmbueFelder);
      String outputFilePath = baseOutputFilePath + "_MMBUE" + format;
      p1.writeOutput(format, outputFilePath, mmbueFelder);
    }

    if (useMCDC) {
      List<Feld_MCDC> mcdcFelder = new ArrayList<>();
      for (Feld feld : felder) {
        mcdcFelder.add(new Feld_MCDC(feld.getColumns(), feld.isCond()));
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
        if (values.length >= (isMarkdown ? 4 : 3)) {
          String[] valuesToCheck = new String[isMarkdown ? values.length - 1 : values.length];
          for (int i = 0; i < valuesToCheck.length; i++) {
            valuesToCheck[i] = values[i + (isMarkdown ? 1 : 0)];
          }
          boolean[] boolValues = new boolean[valuesToCheck.length];

          for (int i = 0; i < boolValues.length; i++) {
            String value = valuesToCheck[i].trim();
            if (value.equals("0") || value.equals("1")) {
              boolValues[i] = value.equals("1");
            } else {
              throw new IllegalArgumentException("Value is not '0' or '1'");
            }
          }
          Feld feld = new Feld(Arrays.copyOfRange(boolValues, 0, boolValues.length - 1), boolValues[boolValues.length - 1]);
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
        boolean[] originalValues = feld.getColumns();
        boolean allMatch = true;

        for (int i = 0; i < originalValues.length; i++) {
          boolean[] toggledValues = Arrays.copyOf(originalValues, originalValues.length);
          toggledValues[i] = !toggledValues[i];

          Feld_MMBUE matchingField = findMatchingField(felder, toggledValues);
          if (matchingField == null || feld.isCond() != matchingField.isCond()) {
            allMatch = false;
            break;
          }
        }

        feld.setMMBUE(allMatch ? "-" : "X");
      }
    }
  }

  private <T extends Feld> T findMatchingField(List<T> felder, boolean[] values) {
    for (T feld : felder) {
      if (Arrays.equals(feld.getColumns(), values)) {
        return feld;
      }
    }
    return null;
  }

  public void doMCDC(List<Feld_MCDC> felder) {
    int[] counters = new int[felder.get(0).getColumns().length];
    Arrays.fill(counters, 1);

    for (Feld_MCDC feld : felder) {
      if (feld != null) {
        for (int i = 0; i < counters.length; i++) {
          counters[i] = updateSignIfNeeded(felder, feld, (char) ('A' + i), counters[i]);
        }
      }
    }

    // MCDC
    List<Feld_MCDC> filteredFelder = felder.stream()
            .filter(f -> getCount(f) > 0)
            .collect(Collectors.toList());

    List<List<Feld_MCDC>> allCombinations = getAllCombinations(filteredFelder);
    List<List<Feld_MCDC>> validCombinations = allCombinations.stream()
            .filter(combination -> {
              Boolean[] pairs = Arrays.stream(counters)
                      .mapToObj(counter -> counter == 1)
                      .toArray(Boolean[]::new);

              for (Feld_MCDC feld : combination) {
                for (int i = 0; i < pairs.length; i++) {
                  pairs[i] |= findMatchingSignField(combination,feld,feld.getSign(i))!=null;
                }
              }

              return Arrays.stream(pairs)
                      .allMatch(element -> element);
            })
            .toList();

    List<Feld_MCDC> smallestCombination = validCombinations.stream()
            .min(Comparator.comparingInt(List::size))
            .orElse(new ArrayList<>());

    felder.forEach(feld -> {
        boolean matchFound = smallestCombination.stream()
                .anyMatch(combinationFeld -> combinationFeld.equals(feld));
        feld.setMCDC(matchFound ? "X" : "-");
    });
  }

  private Feld_MCDC findMatchingSignField(List<Feld_MCDC> felder, Feld_MCDC origin, String sign) {
    for (Feld_MCDC feld : felder) {
      if (!feld.equals(origin) && !sign.equals("-") && Arrays.asList(feld.getSigns()).contains(sign)) {
        return feld;
      }
    }
    return null;
  }

  private int updateSignIfNeeded(List<Feld_MCDC> felder, Feld_MCDC feld, char sign, int counter) {
    int index = sign - 'A';
    if (index>=0 && feld.getSign(index).isEmpty()) {
      boolean[] toggledValues = Arrays.copyOf(feld.getColumns(), feld.getColumns().length);
      toggledValues[index] = !toggledValues[index];
      Feld_MCDC condToggle = findMatchingField(felder, toggledValues);
      if (feld.isCond() != condToggle.isCond()) {
        feld.setSign(index, String.valueOf(sign) + counter);
        condToggle.setSign(index, String.valueOf(sign) + counter);
        counter++;
      }
      else {
        feld.setSign(index, "-");
        condToggle.setSign(index, "-");
      }
    }
    return counter;
  }

  private static int getCount(Feld_MCDC feld) {
    return (int) Arrays.stream(feld.getSigns())
            .filter(col -> !col.equals("-"))
            .count();
  }

  private static List<List<Feld_MCDC>> getAllCombinations(List<Feld_MCDC> list) {
    List<List<Feld_MCDC>> result = new ArrayList<>();
    generateCombinations(list, 0, new ArrayList<>(), result);
    return result;
  }

  private static void generateCombinations(List<Feld_MCDC> list, int index, List<Feld_MCDC> current, List<List<Feld_MCDC>> result) {
    if (index == list.size()) {
      result.add(new ArrayList<>(current));
      return;
    }

    generateCombinations(list, index + 1, current, result);

    current.add(list.get(index));
    generateCombinations(list, index + 1, current, result);

    current.remove(current.size() - 1);
  }
}