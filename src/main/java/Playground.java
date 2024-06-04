import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playground {

  public static void main(String[] args) {
    List<Feld> felder = new ArrayList<>();
    String inputFilePath = "src/main/resources/exercise1.md";
    String outputFilePath = "src/main/resources/new_data.md";

    // Datei einlesen
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
          boolean a0 = values[1].trim().equals("1");
          boolean a1 = values[2].trim().equals("1");
          boolean a2 = values[3].trim().equals("1");
          boolean b = values[4].trim().equals("1");
          Feld feld = new Feld(a0, a1, a2, b);
          felder.add(feld);
        }else {
          throw new Exception("Input-File not in correct format");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    for (Feld feld : felder) {
      // TODO : Füge hier die Logik für die neue Spalte hinzu
      feld.setMCDC("A1");
    }

    // Neue Datei schreiben
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
      bw.write("| A0 | A1 | A2 | B |\n");
      bw.write("| --- | --- | --- | --- |\n");
      for (Feld feld : felder) {
        bw.write("| " + feld.toStringMCDC() + " |\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
