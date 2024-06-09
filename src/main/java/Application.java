public class Application {

  public static void main(String[] args) {
    if (args.length < 5) {
      System.err.println("Usage: java Playground <inputPath> <baseOutputPath> <format> <useMMBUE> <useMCDC>");
      System.exit(1);
    }

    String inputPath = args[0];
    String baseOutputPath = args[1];
    String outputFormat = args[2];
    boolean useMMBUE = Boolean.parseBoolean(args[3]);
    boolean useMCDC = Boolean.parseBoolean(args[4]);

    Engine engine = new Engine();
    engine.process(inputPath, baseOutputPath, outputFormat, useMMBUE, useMCDC);
  }
}