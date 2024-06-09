import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Engine {

  private final MMBUEService mmbueService;
  private final MCDCService mcdcService;
  private final CsvService csvService;
  private final MarkdownService markdownService;

  public Engine() {
    this.mmbueService = new MMBUEService();
    this.mcdcService = new MCDCService();
    this.csvService = new CsvService();
    this.markdownService = new MarkdownService();
  }

  public void process(String inputPath, String outputPath, String outputFormat, boolean withMmbue, boolean withMcdc) {
    File inputDirectory = new File(inputPath);

    if (inputDirectory.isDirectory()) {
      processDirectory(inputPath, outputPath, outputFormat, withMmbue, withMcdc);
      return;
    }

    processSingleFile(inputPath, outputPath, outputFormat, withMmbue, withMcdc);
  }

  public void processDirectory(String inputDirectoryPath, String baseOutputPath, String format, boolean useMMBUE, boolean useMCDC) {
    File inputDirectory = new File(inputDirectoryPath);
    File[] files = inputDirectory.listFiles();

    if (files == null) {
      System.err.println("Error: Failed to list files in the input directory.");
      return;
    }

    for (File file : files) {
      if (file.isFile()) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        if (extension.equals(".csv") || extension.equals(".md")) {
          String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
          processSingleFile(file.getPath(), baseOutputPath + fileNameWithoutExtension, format, useMMBUE, useMCDC);
        }
        else {
          System.out.println("Unsupported file format: " + fileName);
        }
      }
      else {
        //TODO: Diskutiere, ob verschachteltes Verzeichnis
        System.out.println("Skipping directory: " + file.getName());
      }
    }
  }


  public void processSingleFile(String inputPath, String outputPath, String outputFormat, boolean withMmbue, boolean withMcdc) {
    List<Field> fields = getFields(inputPath);

    if (withMmbue) {
      List<Field_MMBUE> mmbueFields = new ArrayList<>();

      for (Field field : fields) {
        mmbueFields.add(new Field_MMBUE(field.isA(), field.isB(), field.isC(), field.isCond()));
      }

      this.mmbueService.process(mmbueFields);
      String outputFilePath = outputPath + "_MMBUE" + outputFormat;
      write(mmbueFields, outputFilePath, outputFormat);
    }

    if (withMcdc) {
      List<Field_MCDC> mcdcFields = new ArrayList<>();

      for (Field field : fields) {
        mcdcFields.add(new Field_MCDC(field.isA(), field.isB(), field.isC(), field.isCond()));
      }

      this.mcdcService.process(mcdcFields);
      String outputFilePath = outputPath + "_MCDC" + outputFormat;
      write(mcdcFields, outputFilePath, outputFormat);
    }
  }

  public List<Field> getFields(String inputPath) {
    FileFormat fileFormat = getFileFormat(inputPath);

    List<Field> fields = new ArrayList<>();

    List<String[]> rows = List.of();

    if (fileFormat.equals(FileFormat.MARKDOWN)) {
      rows = this.markdownService.getRows(inputPath, true);
    }

    if (fileFormat.equals(FileFormat.CSV)) {
      rows = this.csvService.getRows(inputPath, true);
    }

    rows.forEach((row) -> {
      boolean a = row[0].equals("1");
      boolean b = row[1].equals("1");
      boolean c = row[0].equals("1");
      boolean cond = row[0].equals("1");

      fields.add(new Field(a, b, c, cond));
    });

    return fields;
  }

  private FileFormat getFileFormat(String inputPath) {
    if (inputPath.endsWith(".md")) {
      return FileFormat.MARKDOWN;
    }

    if (inputPath.endsWith(".csv")) {
      return FileFormat.CSV;
    }

    throw new IllegalArgumentException("Input file format must be either csv or md");
  }

  private void write(List<? extends Field> fields, String outputPath, String outputFormat) {
    FileFormat fileFormat = FileFormat.getFromString(outputFormat);

    if (fileFormat.equals(FileFormat.MARKDOWN)) {
      this.markdownService.write(outputPath, fields);
      return;
    }

    this.csvService.write(outputPath, fields);
  }
}
