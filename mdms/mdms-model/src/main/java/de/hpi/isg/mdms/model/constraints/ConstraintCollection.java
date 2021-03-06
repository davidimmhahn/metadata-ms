package de.hpi.isg.mdms.model.constraints;

import java.util.Collection;

import de.hpi.isg.mdms.model.common.Described;
import de.hpi.isg.mdms.model.common.Identifiable;
import de.hpi.isg.mdms.model.experiment.Experiment;
import de.hpi.isg.mdms.model.MetadataStore;
import de.hpi.isg.mdms.model.targets.Target;

/**
 * A {@link ConstraintCollection} groups several {@link Constraint}s that were collected by an experiment on a given
 * scope.
 * 
 * @author fabian
 *
 */

public interface ConstraintCollection<T> extends Identifiable, Described {
    /**
     * This functions returns the corresponding {@link de.hpi.isg.mdms.model.experiment.Experiment}.
     * 
     * @return the {@link de.hpi.isg.mdms.model.experiment.Experiment}
     */
    Experiment getExperiment();

    /**
     * This function returns all {@link Constraint}s of this collection.
     * 
     * @return {@link Collection} of all containing {@link Constraint}s.
     */
    Collection<T> getConstraints();

    /**
     * This functions returns the scope of this {@link ConstraintCollection}. The scope is a {@link Collection} of
     * {@link de.hpi.isg.mdms.model.targets.Target}. If the parent {@link de.hpi.isg.mdms.model.targets.Schema} of on
     * e of this scope targets (or if a scope element is a
     * {@link de.hpi.isg.mdms.model.targets.Schema} itself) is deleted, this {@link ConstraintCollection} will be deleted too.
     * 
     * @return
     */
    Collection<Target> getScope();

    /**
     * Adds a new {@link Constraint} to this collection.
     * 
     * @param constraint
     *        The {@link Constraint} to add.
     */
    void add(T constraint);

    /**
     * This function returns the {@link de.hpi.isg.mdms.model.MetadataStore} this collection belongs to.
     * 
     * @return the {@link de.hpi.isg.mdms.model.MetadataStore}.
     */
    MetadataStore getMetadataStore();

    Class<T> getConstraintClass();
}
