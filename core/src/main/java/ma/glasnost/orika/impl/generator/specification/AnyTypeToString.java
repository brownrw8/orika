package ma.glasnost.orika.impl.generator.specification;

import static ma.glasnost.orika.impl.generator.SourceCodeContext.statement;
import ma.glasnost.orika.impl.generator.SourceCodeContext;
import ma.glasnost.orika.impl.generator.VariableRef;
import ma.glasnost.orika.metadata.FieldMap;

public class AnyTypeToString extends AbstractSpecification {

    public boolean appliesTo(FieldMap fieldMap) {
        return String.class.equals(fieldMap.getDestination().getType().getRawType());
    }

    public String generateEqualityTestCode(FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code) {
        return "(\"\" + " + source + ").equals(" + destination +")";
    }

    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code) {
        if (source.isPrimitive()) {
            return statement(destination.assign("\"\"+ %s", source));
        } else {
            if (shouldMapNulls(fieldMap, code)) {
                return statement("if (" + source.notNull() + ") {" + destination.assign("%s.toString()", source) + "} else {" + destination.assign("null") + "}");
            } else {
                return statement(source.ifNotNull() + destination.assign("%s.toString()", source));
            }
        }
    }
}
