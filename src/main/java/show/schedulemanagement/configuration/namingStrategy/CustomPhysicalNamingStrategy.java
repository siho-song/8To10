package show.schedulemanagement.configuration.namingStrategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(logicalName);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return null;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(logicalName);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return null;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(logicalName);
    }

    private Identifier convertToSnakeCase(final Identifier identifier){
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText()
                .replaceAll(regex,replacement)
                .toLowerCase();
        return Identifier.toIdentifier(newName);
    }
}
