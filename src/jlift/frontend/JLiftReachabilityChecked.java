package jlift.frontend;

import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import jlift.visit.JLiftReachChecker;
import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.Scheduler;
import polyglot.frontend.VisitorPass;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.ReachabilityChecked;
import polyglot.types.TypeSystem;

public class JLiftReachabilityChecked extends ReachabilityChecked {

	protected JLiftReachabilityChecked(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		ExtensionInfo jifext = extInfo;
		final JifTypeSystem ts = (JifTypeSystem) jifext.typeSystem();
		final JifNodeFactory nf = (JifNodeFactory) jifext.nodeFactory();
		return new VisitorPass(this, new JLiftReachChecker(this.job(), ts, nf));
	}

    public static Goal create(Scheduler scheduler, Job job, TypeSystem ts, NodeFactory nf) {
        return scheduler.internGoal(new JLiftReachabilityChecked(job, ts, nf));
    }

}
