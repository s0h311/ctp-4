public enum FileFormat {
  CSV,
  MARKDOWN;

  public static FileFormat getFromString(String fileFormat) {
    if (fileFormat.contains("md") || fileFormat.contains("markdown")) {
      return MARKDOWN;
    }

    return CSV;
  }
}
