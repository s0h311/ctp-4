import java.util.List;

public class ConditionCoverageService {

  protected int updateSignIfNeeded(List<Field_MCDC> fields, Field_MCDC field, char sign, int counter) {
    String signValue = field.getSign(sign);
    if (signValue.isEmpty()) {
      Field_MCDC condToggle = findMatchingField(fields, sign == 'A' ? !field.isA() : field.isA(), sign == 'B' ? !field.isB() : field.isB(), sign == 'C' ? !field.isC() : field.isC());

      if (field.isCond() != condToggle.isCond()) {
        field.setSign(sign, String.valueOf(sign) + counter);
        condToggle.setSign(sign, String.valueOf(sign) + counter);
        counter++;
      }
      else {
        field.setSign(sign, "-");
        condToggle.setSign(sign, "-");
      }
    }
    return counter;
  }

  protected <T extends Field> T findMatchingField(List<T> fields, boolean a, boolean b, boolean c) {
    for (T field : fields) {
      if (field.isA() == a && field.isB() == b && field.isC() == c) {
        return field;
      }
    }

    return null;
  }

  protected Field_MCDC findMatchingField(List<Field_MCDC> fields, Field_MCDC origin, String sign) {
    for (Field_MCDC field : fields) {
      if (!field.equals(origin) && (field.getSignA().equals(sign) || field.getSignB().equals(sign) || field.getSignC().equals(sign))) {
        return field;
      }
    }
    
    return null;
  }
}
