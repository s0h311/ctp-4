import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MarkdownService {

  public List<String[]> getRows(String inputPath, boolean skipHead) {
    List<String[]> rows = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {

      br.lines()
          .skip(skipHead ? 2 : 0)
          .forEach((rawLine) -> {
            String line = rawLine.replaceAll(" ", "");
            String[] values = line.split("\\|");
            rows.add(values);
          });
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return rows;
  }

  public void write(String outputPath, List<? extends Field> fields) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
      String mdHeaders = fields.get(0).toMDHeaders();

      bw.write(mdHeaders + "\n");

      int count = (int) mdHeaders.chars().filter(ch -> ch == '|').count() - 1;
      String separatorLine = "| --- ".repeat(Math.max(0, count)) + "|\n";
      bw.write(separatorLine);

      for (Field field : fields) {
        bw.write(field.toString() + "\n");
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
