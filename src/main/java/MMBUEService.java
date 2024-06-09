import java.util.List;

public class MMBUEService extends ConditionCoverageService {
  public MMBUEService() {
    super();
  }

  public void process(List<Field_MMBUE> fields) {
    for (Field_MMBUE field : fields) {
      if (field.getMMBUE().isEmpty()) {
        Field_MMBUE condToggleA = findMatchingField(fields, !field.isA(), field.isB(), field.isC());
        Field_MMBUE condToggleB = findMatchingField(fields, field.isA(), !field.isB(), field.isC());
        Field_MMBUE condToggleC = findMatchingField(fields, field.isA(), field.isB(), !field.isC());

        if (condToggleA != null && condToggleB != null && condToggleC != null && field.isCond() == condToggleA.isCond() && field.isCond() == condToggleB.isCond() && field.isCond() == condToggleC.isCond()) {
          field.setMMBUE("-");
        }
        else {
          field.setMMBUE("X");
        }
      }
    }
  }
}
