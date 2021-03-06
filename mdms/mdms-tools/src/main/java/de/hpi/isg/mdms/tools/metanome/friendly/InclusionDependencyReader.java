package de.hpi.isg.mdms.tools.metanome.friendly;

import de.hpi.isg.mdms.tools.metanome.DependencyResultReceiver;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.InclusionDependency;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for {@link InclusionDependency} constraints.
 */
public class InclusionDependencyReader extends AbstractFriendlyReader<InclusionDependency> {

    private Pattern rhsPattern = Pattern.compile("\\[([^\\[\\]]*)\\]");

    @Override
    protected void processLine(String line, DependencyResultReceiver<?> resultReceiver) {
        this.toINDs(line).forEach(ind -> {
            try {
                resultReceiver.receiveResult(ind);
            } catch (CouldNotReceiveResultException e) {
                throw new RuntimeException(String.format("Could not process \"{}\".", line), e);
            }
        });
    }

    private Collection<InclusionDependency> toINDs(String line) {
        String[] split = line.split(" c ");
        ColumnPermutation lhs = toColumnPermutation(this.clean(split[0]));
        String rhses = split[1];
        Collection<InclusionDependency> inds = new LinkedList<>();
        final Matcher matcher = this.rhsPattern.matcher(rhses);
        while (matcher.find()) {
            ColumnPermutation rhs = toColumnPermutation(matcher.group(1));
            inds.add(new InclusionDependency(lhs, rhs));
        }
        return inds;
    }

    private String clean(String src) {
        return src.replaceAll(Pattern.quote("[") + "|" + Pattern.quote("]"), "");
    }
}
