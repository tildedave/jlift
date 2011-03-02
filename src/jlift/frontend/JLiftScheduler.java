/*
 * File          : JLiftScheduler.java
 * Project       : jlift
 * Description   : Special scheduler for JLift.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:37:42 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

// TODO: move all of the special goals currently in ExtensionInfo into here

package jlift.frontend;

import jif.ExtensionInfo;
import jif.FieldLabelInferenceGoal;
import jif.OutputExtensionInfo;
import jifclipse.frontend.JifclipseScheduler;
import jlift.visit.ParameterizeVisitor;
import polyglot.ast.NodeFactory;
import polyglot.frontend.CyclicDependencyException;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class JLiftScheduler extends JifclipseScheduler {

	public JLiftScheduler(ExtensionInfo extInfo, OutputExtensionInfo jlext) {
		super(extInfo, jlext);
	}

//	@Override
//	public Goal Disambiguated(Job job) {
//        TypeSystem ts = extInfo.typeSystem();
//        NodeFactory nf = extInfo.nodeFactory();
//
//        Goal g = JLiftDisambiguated.createGoal(this, job, ts, nf);
//        return g;
//	}

	public Goal RuntimeExceptionsAdded() {
		return internGoal(RuntimeExceptionsAdded.createGoal(this, extInfo));
	}

	@Override
	public Goal ExceptionsChecked(Job job) {
		// combine what the superclasses are doing
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        Goal g = JLiftExceptionsChecked.create(this, job, ts, nf);

        try {
            addPrerequisiteDependency(g, this.NotNullChecker(job));
            addPrerequisiteDependency(g, this.PreciseClassChecker(job));
            addPrerequisiteDependency(g, this.IntegerBoundsChecker(job));
        }
        catch (CyclicDependencyException e) {
            throw new InternalCompilerError(e);
        }
        return g;

	}

	@Override
	public FieldLabelInferenceGoal FieldLabelInference(Job job) {
        FieldLabelInferenceGoal g = (FieldLabelInferenceGoal)internGoal(new JLiftFieldLabelInferenceGoal(job));

        try {
            addPrerequisiteDependency(g, this.ExceptionsChecked(job));
        }
        catch (CyclicDependencyException e) {
            throw new InternalCompilerError(e);
        }
        return g;
	}

	@Override
	public Goal TypeChecked(Job job) {
		Goal superTypeChecked = super.TypeChecked(job);
		try {
			addPrerequisiteDependency(superTypeChecked, this.ParameterizeTypeNodes(job));
			addPrerequisiteDependency(this.ParameterizeTypeNodes(job), this.Disambiguated(job));
		} catch (CyclicDependencyException e) {
			throw new InternalCompilerError(e);
		}
		return superTypeChecked;
	}

	private Goal ParameterizeTypeNodes(Job job) {
		return internGoal(new VisitorGoal(job, new ParameterizeVisitor(job, extInfo.typeSystem(), extInfo.nodeFactory())));
	}

	@Override
	public Goal ReachabilityChecked(Job job) {
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        Goal g = JLiftReachabilityChecked.create(this, job, ts, nf);
        return g;
	}
}
