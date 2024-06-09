import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvService {

  public List<String[]> getRows(String inputPath, boolean skipHead) {
    List<String[]> rows = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {

      br.lines()
          .skip(skipHead ? 1 : 0)
          .forEach((line) -> {
            String[] values = line.split(";");
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
      bw.write(fields.get(0).toCSVHeaders() + "\n");

      for (Field field : fields) {
        bw.write(field.toCSVString() + "\n");
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
