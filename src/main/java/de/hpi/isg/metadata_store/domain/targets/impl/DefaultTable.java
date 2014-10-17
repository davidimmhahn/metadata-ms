package de.hpi.isg.metadata_store.domain.targets.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;

import de.hpi.isg.metadata_store.domain.Location;
import de.hpi.isg.metadata_store.domain.MetadataStore;
import de.hpi.isg.metadata_store.domain.common.Observer;
import de.hpi.isg.metadata_store.domain.common.impl.ExcludeHashCodeEquals;
import de.hpi.isg.metadata_store.domain.impl.AbstractTarget;
import de.hpi.isg.metadata_store.domain.location.impl.IndexedLocation;
import de.hpi.isg.metadata_store.domain.targets.Column;
import de.hpi.isg.metadata_store.domain.targets.Schema;
import de.hpi.isg.metadata_store.domain.targets.Table;
import de.hpi.isg.metadata_store.domain.util.IdUtils;

/**
 * The default implementation of the {@link Table}.
 *
 */
public class DefaultTable extends AbstractTarget implements Table {

    public static Table buildAndRegister(final Observer observer, final Schema schema, final int id, final String name,
            final Location location) {
        final DefaultTable newTable = new DefaultTable(observer, schema, id, name, location);
        newTable.notifyObserver();
        return newTable;
    }

    public static Table buildAndRegister(final Observer observer, final Schema schema, final String name,
            final Location location) {
        final DefaultTable newTable = new DefaultTable(observer, schema, -1, name, location);
        newTable.notifyObserver();
        return newTable;
    }

    private static final long serialVersionUID = 1695408629652071459L;

    @ExcludeHashCodeEquals
    private final Collection<Column> columns;

    @ExcludeHashCodeEquals
    private final Schema schema;

    private DefaultTable(final Observer observer, final Schema schema, final int id, final String name,
            final Location location) {
        super(observer, id, name, location);
        this.columns = Collections.synchronizedSet(new HashSet<Column>());
        this.schema = schema;
    }

    @Override
    public Table addColumn(final Column column) {
        this.columns.add(column);
        return this;
    }

    @Override
    public Column addColumn(final MetadataStore metadataStore, final String name, final int index) {
        Validate.isTrue(metadataStore.getSchemas().contains(getSchema()));
        final int localSchemaId = IdUtils.getLocalSchemaId(getId());
        final int localTableId = IdUtils.getLocalTableId(getId());
        final int columnId = IdUtils.createGlobalId(localSchemaId, localTableId, IdUtils.MIN_COLUMN_NUMBER + index);
        final Location location = new IndexedLocation(index, getLocation());
        final Column column = DefaultColumn.buildAndRegister(metadataStore, this, columnId, name, location);
        addColumn(column);
        return column;
    }

    @Override
    public Collection<Column> getColumns() {
        return this.columns;
    }

    /**
     * @return the parent schema
     */
    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public String toString() {
        return String.format("Table[%s, %d columns, %08x]", getName(), getColumns().size(), getId());
    }
}
