package de.hpi.isg.mdms.domain.experiment;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.hpi.isg.mdms.exceptions.MetadataStoreException;
import de.hpi.isg.mdms.model.constraints.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.isg.mdms.model.MetadataStore;
import de.hpi.isg.mdms.model.common.AbstractIdentifiable;
import de.hpi.isg.mdms.model.common.ExcludeHashCodeEquals;
import de.hpi.isg.mdms.model.constraints.ConstraintCollection;
import de.hpi.isg.mdms.model.experiment.Algorithm;
import de.hpi.isg.mdms.model.experiment.Annotation;
import de.hpi.isg.mdms.model.experiment.Experiment;
import de.hpi.isg.mdms.rdbms.SQLInterface;

public class RDBMSExperiment extends AbstractIdentifiable implements Experiment{
	
	private static final long serialVersionUID = -7269775415872860403L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RDBMSExperiment.class);

	private  Set<ConstraintCollection<?>> constraintCollections = new HashSet<ConstraintCollection<?>>();
    private Algorithm algorithm;
    private Map<String, String> parameters = new HashMap<String, String>();
	private Set<Annotation> annotations = new HashSet<Annotation>();
    
    private String description;
    private long executionTime;
	private String timestamp;
    
    @ExcludeHashCodeEquals
    private final SQLInterface sqlInterface;

	public RDBMSExperiment(int id, String description, Long executionTime, Algorithm algorithm, Map<String, String> parameters, Set<Annotation> annotations, String timestamp, SQLInterface sqlInterface) {
		super(id);
		this.description = description;
		this.executionTime = executionTime;
		this.algorithm = algorithm;
		this.parameters = parameters;
		this.annotations = annotations;
		this.sqlInterface = sqlInterface;
		this.timestamp = timestamp;
	}
    
	public RDBMSExperiment(int id, String description, Long executionTime, Algorithm algorithm, Map<String, String> parameters, Set<Annotation> annotations, SQLInterface sqlInterface) {
		super(id);
		this.description = description;
		this.executionTime = executionTime;
		this.algorithm = algorithm;
		this.parameters = parameters;
		this.annotations = annotations;
		this.sqlInterface = sqlInterface;
		this.timestamp = new Timestamp(new java.util.Date().getTime()).toString();
	}

	public RDBMSExperiment(int id, String description, Long executionTime, Algorithm algorithm, SQLInterface sqlInterface) {
		super(id);
		this.description = description;
		this.executionTime = executionTime;
		this.algorithm = algorithm;
		this.sqlInterface = sqlInterface;
		this.timestamp = new Timestamp(new java.util.Date().getTime()).toString();
	}	
	
	public RDBMSExperiment(int id, String description, Algorithm algorithm, SQLInterface sqlInterface) {
		super(id);
		this.description = description;
		this.algorithm = algorithm;
		this.sqlInterface = sqlInterface;
		this.timestamp = new Timestamp(new java.util.Date().getTime()).toString();
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		return Collections.unmodifiableCollection(this.annotations);
	}

	@Override
	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	@Override
	public void addParameter(String key, String value) {
		this.parameters.put(key, value);
		try {
			this.sqlInterface.addParameterToExperiment(this, key, value);
		} catch (SQLException e) {
			throw new MetadataStoreException(e);
		}
	}

	@Override
	public Long getExecutionTime() {
		return this.executionTime;
	}

	@Override
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
		try {
			this.sqlInterface.setExecutionTimeToExperiment(this, executionTime);
		} catch (SQLException e) {
			throw new MetadataStoreException(e);
		}

	}

	@Override
	public Collection<ConstraintCollection<?>> getConstraintCollections() {
		ensureConstraintCollectionsLoaded();
		return this.constraintCollections;
	}

    private void ensureConstraintCollectionsLoaded() {
        if (this.constraintCollections == null) {
			try {
				this.constraintCollections = this.sqlInterface.getAllConstraintCollectionsForExperiment(this);
			} catch (SQLException e) {
				throw new MetadataStoreException(e);
			}
		}
    }

	@Override
	public void add(ConstraintCollection<?> constraintCollection) {
		this.constraintCollections = null;
		try {
			this.sqlInterface.addConstraintCollection(constraintCollection);
		} catch (SQLException e) {
			throw new MetadataStoreException(e);
		}

	}

	@Override
	public MetadataStore getMetadataStore() {
		return this.sqlInterface.getMetadataStore();
	}

	@Override
	public void addAnnotation(String tag, String text) {
		try {
			this.sqlInterface.addAnnotation(this, tag, text);
		} catch (SQLException e) {
			throw new MetadataStoreException(e);
		}

	}

	@Override
	public String getTimestamp() {
		return this.timestamp;
	}

}
