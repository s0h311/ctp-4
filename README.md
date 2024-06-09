# Certified Tester Praktikum 4

## Usage

To use this code, follow these steps:

## 1. **Compile the Java Classes**:

Open a terminal and navigate to the root directory of your project. Use the `javac` command to compile all Java files in
the `src/main/java/` directory:

```bash
javac -d out src/main/java/*.java
```

## 2. **Run the Main Class**:

Use the java command to run your Application class and pass the necessary arguments.

### 2.1. **Example with Single File Input**:

When uploading a **single file**, the output path is directly utilized.
The resulting file **inherits** the name of the output path, supplemented with either a .csv or .md extension.

```bash
java -cp out Application src/main/resources/aufgaben1.csv src/main/resources/Output/aufgaben1 csv true true
```

### 2.2. **Example with Directory Input**:

You also have the option to upload multiple files at once by specifying a **directory**.
The generated filenames are **based on the input names**, so please ensure that you provide a directory
(Note: The directory path must end with `"/"`).

```bash
java -cp out Application src/main/resources/exercises src/main/resources/Output/ csv true true
```

Here’s a breakdown of the command-line arguments:

- `src/main/resources/aufgaben1.csv`: Path to the input **CSV** or **Markdown** file. Feel free to specify a different
  directory if needed.
- `src/main/resources/Output/aufgaben1`: Base path for the output files. You can specify any other directory as well.
- `csv` or `md`: Output file format (**CSV** or **Markdown**).
- `true`: Boolean flag indicating the use of **MMBUE** (Minimal Determining Multiple Condition Cover/ Minimal
  bestimmende Mehrfachbedingungsüberdeckung).
- `true`: Boolean flag indicating the use of **MCDC** (Modified Condition / Decision Coverage).


3. Output Files:
   The output files will be exported to the specified directory. There you can see the results.