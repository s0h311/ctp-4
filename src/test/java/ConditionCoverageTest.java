import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionCoverageTest {
  public final String ROOT_DIR = Paths.get("").toAbsolutePath().toString();
  public final String TEMP_DIR = ROOT_DIR + "/src/test/resources/tmp/";
  public final String EXERCISE_DIR = ROOT_DIR + "/src/main/resources/exercises/";
  public final String SOLUTION_DIR = ROOT_DIR + "/src/main/resources/solutions/";

  @AfterEach
  public void cleanUp() throws IOException {
    Files.walk(Paths.get(TEMP_DIR))
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .forEach(File::delete);
  }

  private static Stream<Arguments> exerciseProvider() {
    return Stream.of(
        Arguments.arguments("ex0", "md"),
        Arguments.arguments("ex0", "csv"),
        Arguments.arguments("ex1", "md"),
        Arguments.arguments("ex1", "csv"),
        Arguments.arguments("ex2", "md"),
        Arguments.arguments("ex2", "csv"),
        Arguments.arguments("ex4", "md"),
        Arguments.arguments("ex4", "csv"),
        Arguments.arguments("ex5", "md"),
        Arguments.arguments("ex5", "csv"),
        Arguments.arguments("ex6", "md"),
        Arguments.arguments("ex6", "csv"),
        Arguments.arguments("ex7", "md"),
        Arguments.arguments("ex7", "csv")
    );
  }

  @ParameterizedTest
  @MethodSource("exerciseProvider")
  public void testMmbue(String exercise, String fileFormat) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + "." + fileFormat;
    String outputPath = TEMP_DIR + exercise;

    Application.main(new String[]{inputPath, outputPath, fileFormat, "mmbue"});

    String resultPath = TEMP_DIR + exercise + "_MMBUE." + fileFormat;
    String solutionPath = SOLUTION_DIR + exercise + "_MMBUE." + fileFormat;

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
  @MethodSource("exerciseProvider")
  public void testMcdc(String exercise, String fileFormat) throws IOException {
    String inputPath = EXERCISE_DIR + exercise + "." + fileFormat;
    String outputPath = TEMP_DIR + exercise;

    Application.main(new String[]{inputPath, outputPath, fileFormat, "mcdc"});

    String resultPath = TEMP_DIR + exercise + "_MCDC." + fileFormat;
    String solutionPath = SOLUTION_DIR + exercise + "_MCDC." + fileFormat;

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
    Application.main(new String[]{EXERCISE_DIR, TEMP_DIR, "csv", "both"});

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

  @Test
  public void testLessThanFourParams() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Application.main(new String[]{EXERCISE_DIR, TEMP_DIR, "csv"})
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
          "src/test/resources/malformed-files/ex3.md",
          "src/test/resources/malformed-files/ex3.csv"
  })
  public void testNotCorrectInputValues(String path){
    Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> Application.main(new String[]{path, TEMP_DIR, "csv", "both"})
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "src/test/resources/malformed-files/malformed0.md",
      "src/test/resources/malformed-files/malformed0.csv"
  })
  public void testMalformedFile(String path) {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Application.main(new String[]{path, TEMP_DIR, "csv", "both"})
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "src/test/resources/not-existing-file.md",
      "src/test/resources/not-existing-file.csv"
  })
  public void testNotExistingFile(String path) {
    Assertions.assertThrows(
        RuntimeException.class,
        () -> Application.main(new String[]{path, TEMP_DIR, "csv", "both"})
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "src/test/resources/",
  })
  public void testNotUnsupportedFileFormat(String path) {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Application.main(new String[]{path, TEMP_DIR, "csv", "both"})
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "src/test/resources/unsupported.mp3/result",
  })
  public void testMalformedPath(String path) {
    Assertions.assertThrows(
        RuntimeException.class,
        () -> Application.main(new String[]{EXERCISE_DIR, path, "csv", "both"})
    );
  }
}