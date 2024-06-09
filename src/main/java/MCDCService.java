import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MCDCService extends ConditionCoverageService {

  public MCDCService() {
    super();
  }

  public void process(List<Field_MCDC> felder) {
    int[] counters = new int[]{1, 1, 1};

    // Sign A, Sign B, Sign C
    for (Field_MCDC feld : felder) {
      counters[0] = updateSignIfNeeded(felder, feld, 'A', counters[0]);
      counters[1] = updateSignIfNeeded(felder, feld, 'B', counters[1]);
      counters[2] = updateSignIfNeeded(felder, feld, 'C', counters[2]);
    }

    // MCDC
    Optional<Field_MCDC> mostSign = felder.stream().max(Comparator.comparingInt(feld -> {
      int count = 0;

      count += feld.getSignA().equals("-") ? 0 : 1;
      count += feld.getSignB().equals("-") ? 0 : 1;
      count += feld.getSignC().equals("-") ? 0 : 1;

      return count;
    }));

    mostSign.get().setMCDC("X");

    findMatchingField(felder, mostSign.get(), mostSign.get().getSignA()).setMCDC("X");
    findMatchingField(felder, mostSign.get(), mostSign.get().getSignB()).setMCDC("X");
    findMatchingField(felder, mostSign.get(), mostSign.get().getSignC()).setMCDC("X");

    felder.stream().filter(feld -> feld.getMCDC().isEmpty()).forEach(feld -> feld.setMCDC("-"));
  }
}
