import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ConditionCoverageTest {
  public final String TEMP_DIR = "/Users/soheil.nazari/funprojects/uni/ctp-4/src/test/resources/tmp/";
  public final String EXERCISE_DIR = "/Users/soheil.nazari/funprojects/uni/ctp-4/src/main/resources/exercises/";
  public final String SOLUTION_DIR = "/Users/soheil.nazari/funprojects/uni/ctp-4/src/main/resources/solutions/";

  @AfterEach
  public void cleanUp() throws IOException {
    Files.walk(Paths.get(TEMP_DIR))
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .forEach(File::delete);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ex0", "ex1", "ex3", "ex4", "ex5", "ex6", "ex7"})
  public void testMmbueMarkdownOutput(String exercise) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + ".md";
    String outputPath = TEMP_DIR + exercise;

    Playground.main(new String[]{inputPath, outputPath, "md", "true", "true"});

    String resultPath = TEMP_DIR + exercise + "_MMBUE.md";
    String solutionPath = SOLUTION_DIR + exercise + "_MMBUE.md";

    String resultLines = "1";
    String solutionLines = "2";

    try (BufferedReader br = new BufferedReader(new FileReader(resultPath))) {
      resultLines = br.lines().collect(Collectors.joining());
    }

    try (BufferedReader br = new BufferedReader(new FileReader(solutionPath))) {
      solutionLines = br.lines().collect(Collectors.joining());
    }

    Assertions.assertEquals(solutionLines, resultLines);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ex0", "ex1", "ex3", "ex4", "ex5", "ex6", "ex7"})
  public void testMmbueCsvOutput(String exercise) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + ".md";
    String outputPath = TEMP_DIR + exercise;

    Playground.main(new String[]{inputPath, outputPath, "csv", "true", "true"});

    String resultPath = TEMP_DIR + exercise + "_MMBUE.csv";
    String solutionPath = SOLUTION_DIR + exercise + "_MMBUE.csv";

    String resultLines = "1";
    String solutionLines = "2";

    try (BufferedReader br = new BufferedReader(new FileReader(resultPath))) {
      resultLines = br.lines().collect(Collectors.joining());
    }

    try (BufferedReader br = new BufferedReader(new FileReader(solutionPath))) {
      solutionLines = br.lines().collect(Collectors.joining());
    }

    Assertions.assertEquals(solutionLines, resultLines);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ex0", "ex1", "ex3", "ex4", "ex5", "ex6", "ex7"})
  public void testMcdcMarkdownOutput(String exercise) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + ".md";
    String outputPath = TEMP_DIR + exercise;

    Playground.main(new String[]{inputPath, outputPath, "md", "true", "true"});

    String resultPath = TEMP_DIR + exercise + "_MCDC.md";
    String solutionPath = SOLUTION_DIR + exercise + "_MCDC.md";

    String resultLines = "1";
    String solutionLines = "2";

    try (BufferedReader br = new BufferedReader(new FileReader(resultPath))) {
      resultLines = br.lines().collect(Collectors.joining());
    }

    try (BufferedReader br = new BufferedReader(new FileReader(solutionPath))) {
      solutionLines = br.lines().collect(Collectors.joining());
    }

    Assertions.assertEquals(solutionLines, resultLines);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ex0", "ex1", "ex3", "ex4", "ex5", "ex6", "ex7"})
  public void testMcdcCsvOutput(String exercise) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + ".md";
    String outputPath = TEMP_DIR + exercise;

    Playground.main(new String[]{inputPath, outputPath, "csv", "true", "true"});

    String resultPath = TEMP_DIR + exercise + "_MCDC.csv";
    String solutionPath = SOLUTION_DIR + exercise + "_MCDC.csv";

    String resultLines = "1";
    String solutionLines = "2";

    try (BufferedReader br = new BufferedReader(new FileReader(resultPath))) {
      resultLines = br.lines().collect(Collectors.joining());
    }

    try (BufferedReader br = new BufferedReader(new FileReader(solutionPath))) {
      solutionLines = br.lines().collect(Collectors.joining());
    }

    Assertions.assertEquals(solutionLines, resultLines);
  }

  @Test
  public void testDirectory() throws IOException {
    Playground.main(new String[]{EXERCISE_DIR, TEMP_DIR, "csv", "true", "true"});

    File inputDirectory = new File(TEMP_DIR);
    File[] files = inputDirectory.listFiles();

    for (File file : files) {
      String resultLines = "1";
      String solutionLines = "2";

      try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
        resultLines = br.lines().collect(Collectors.joining());
      }

      try (BufferedReader br = new BufferedReader(new FileReader(SOLUTION_DIR + file.getName()))) {
        solutionLines = br.lines().collect(Collectors.joining());
      }

      Assertions.assertEquals(solutionLines, resultLines);
    }
  }
}