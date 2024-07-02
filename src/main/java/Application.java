import java.io.*;
import java.util.ArrayList;
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
    List<Field> fields;
    String format = formatInput.equals("csv") ? ".csv" : ".md";

    Application p1 = new Application();
    fields = p1.readInput(inputFilePath);

    if (useMMBUE) {
      List<FieldMMBUE> mmbueFields = new ArrayList<>();
      for (Field field : fields) {
        mmbueFields.add(new FieldMMBUE(field.isA(), field.isB(), field.isC(), field.isCond()));
      }
      p1.doMMBUE(mmbueFields);
      String outputFilePath = baseOutputFilePath + "_MMBUE" + format;
      p1.writeOutput(format, outputFilePath, mmbueFields);
    }

    if (useMCDC) {
      List<FieldMCDC> mcdcFields = new ArrayList<>();
      for (Field field : fields) {
        mcdcFields.add(new FieldMCDC(field.isA(), field.isB(), field.isC(), field.isCond()));
      }
      p1.doMCDC(mcdcFields);
      String outputFilePath = baseOutputFilePath + "_MCDC" + format;
      p1.writeOutput(format, outputFilePath, mcdcFields);
    }
  }

  public List<Field> readInput(String inputFilePath) {
    List<Field> fields = new ArrayList<>();
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
          Field field = new Field(boolValues[0], boolValues[1], boolValues[2], boolValues[3]);
          fields.add(field);
        }
        else {
          throw new IllegalArgumentException("Input-File not in correct format");
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    return fields;
  }

  public void writeOutput(String inputFilePath, String outputFilePath, List<? extends Field> fields) {

    boolean isMarkdown = inputFilePath.equals(".md");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
      if (isMarkdown) {
        String mdHeaders = fields.get(0).toMDHeaders();
        bw.write(mdHeaders + "\n");

        int count = (int) mdHeaders.chars().filter(ch -> ch == '|').count() - 1;
        String separatorLine = "| --- ".repeat(Math.max(0, count)) + "|\n";
        bw.write(separatorLine);
      }
      else {
        bw.write(fields.get(0).toCSVHeaders() + "\n");
      }
      for (Field field : fields) {
        if (isMarkdown) {
          bw.write(field.toString() + "\n");
        }
        else {
          bw.write(field.toCSVString() + "\n");
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void doMMBUE(List<FieldMMBUE> fields) {
    for (FieldMMBUE field : fields) {
      if (field.getMMBUE().isEmpty()) {
        FieldMMBUE condToggleA = findMatchingField(fields, !field.isA(), field.isB(), field.isC());
        FieldMMBUE condToggleB = findMatchingField(fields, field.isA(), !field.isB(), field.isC());
        FieldMMBUE condToggleC = findMatchingField(fields, field.isA(), field.isB(), !field.isC());

        if (condToggleA != null && condToggleB != null && condToggleC != null && field.isCond() == condToggleA.isCond() && field.isCond() == condToggleB.isCond() && field.isCond() == condToggleC.isCond()) {
          field.setMMBUE("-");
        }
        else {
          field.setMMBUE("X");
        }
      }
    }
  }

  private <T extends Field> T findMatchingField(List<T> fields, boolean a, boolean b, boolean c) {
    for (T field : fields) {
      if (field.isA() == a && field.isB() == b && field.isC() == c) {
        return field;
      }
    }

    return null;
  }

  public void doMCDC(List<FieldMCDC> fields) {
    int[] counters = new int[]{1, 1, 1};

    // Sign A, Sign B, Sign C
    for (FieldMCDC field : fields) {
      counters[0] = updateSignIfNeeded(fields, field, 'A', counters[0]);
      counters[1] = updateSignIfNeeded(fields, field, 'B', counters[1]);
      counters[2] = updateSignIfNeeded(fields, field, 'C', counters[2]);
    }

    // MCDC
    List<FieldMCDC> filteredFields = fields.stream()
            .filter(field -> getCount(field) > 0)
            .collect(Collectors.toList());

    List<List<FieldMCDC>> allCombinations = getAllCombinations(filteredFields);
    List<List<FieldMCDC>> validCombinations = allCombinations.stream()
            .filter(combination -> {
              boolean pairA = counters[0] == 1;
              boolean pairB = counters[1] == 1;
              boolean pairC = counters[2] == 1;

              for (FieldMCDC field : combination) {
                pairA |= findMatchingSignField(combination, field, field.getSignA()) != null;
                pairB |= findMatchingSignField(combination, field, field.getSignB()) != null;
                pairC |= findMatchingSignField(combination, field, field.getSignC()) != null;
              }

              return pairA && pairB && pairC;
            })
            .toList();

    List<FieldMCDC> smallestCombination = validCombinations.stream()
            .min(Comparator.comparingInt(List::size))
            .orElse(new ArrayList<>());

    fields.forEach(field -> {
      boolean matchFound = findMatchingField(smallestCombination, field.isA(), field.isB(), field.isC()) != null;
      field.setMCDC(matchFound ? "X" : "-");
    });

  }

  private int updateSignIfNeeded(List<FieldMCDC> fields, FieldMCDC field, char sign, int counter) {
    String signValue = field.getSign(sign);
    if (signValue.isEmpty()) {
      FieldMCDC condToggle = findMatchingField(fields, (sign == 'A') != field.isA(), (sign == 'B') != field.isB(), (sign == 'C') != field.isC());
      if (field.isCond() != condToggle.isCond()) {
        field.setSign(sign, String.valueOf(sign) + counter);
        condToggle.setSign(sign, String.valueOf(sign) + counter);
        counter++;
      }
      else {
        field.setSign(sign, "-");
        condToggle.setSign(sign, "-");
      }
    }
    return counter;
  }

  private FieldMCDC findMatchingSignField(List<FieldMCDC> fields, FieldMCDC origin, String sign) {
    for (FieldMCDC field : fields) {
      if (!field.equals(origin) && !sign.equals("-") && (field.getSignA().equals(sign) || field.getSignB().equals(sign) || field.getSignC().equals(sign))) {
        return field;
      }
    }

    return null;
  }

  private static int getCount(FieldMCDC field) {
    int count = 0;
    count += field.getSignA().equals("-") ? 0 : 1;
    count += field.getSignB().equals("-") ? 0 : 1;
    count += field.getSignC().equals("-") ? 0 : 1;
    return count;
  }

  private static List<List<FieldMCDC>> getAllCombinations(List<FieldMCDC> list) {
    List<List<FieldMCDC>> result = new ArrayList<>();
    generateCombinations(list, 0, new ArrayList<>(), result);
    return result;
  }

  private static void generateCombinations(List<FieldMCDC> list, int index, List<FieldMCDC> current, List<List<FieldMCDC>> result) {
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