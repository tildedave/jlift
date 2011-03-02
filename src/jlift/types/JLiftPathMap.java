/*
 * File          : JLiftPathMap.java
 * Project       : jlift
 * Description   : Extension of jif.types.PathMap that can create definitional
 *				   constraints for paths.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:39:08 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jif.types.JifTypeSystem;
import jif.types.LabelConstraintMessage;
import jif.types.LabeledType;
import jif.types.NamedLabel;
import jif.types.Path;
import jif.types.PathMap;
import jif.types.VarMap;
import jif.types.label.JoinLabel;
import jif.types.label.Label;
import jif.types.label.NotTaken;
import jif.types.label.VarLabel;
import jif.visit.LabelChecker;
import jlift.JLiftOptions;
import jlift.util.NumberProvider;
import jlift.visit.SummaryConstraintGenerator;
import polyglot.ast.Node;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftPathMap extends PathMap {

	protected Map<Path, Label> pathVarMap;

	@Override
	public Label get(Path p) {
//		if (pathVarMap.containsKey(p))
//			return pathVarMap.get(p);

		Label l = (Label) map.get(p);
        if (l == null)
        	return ts.notTaken();
        return l;
	}

	public JLiftPathMap(JifTypeSystem ts) {
		super(ts);
//		this.pathVarMap = new HashMap<Path, Label>();
	}

	@Override
	public PathMap join(PathMap m) {
		JLiftPathMap n = new JLiftPathMap(ts);
		n.map.putAll(map);

		// Iterate over the elements of X, joining those labels with the ones
		// in this and adding the ones that aren't there.
		for (Iterator i = ((JLiftPathMap) m).map.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry) i.next();
			Path p = (Path) e.getKey();
			Label l1 = (Label) e.getValue();
			Label l2 = n.get(p);
			Label joinLabel = ts.join(l1, l2);
			joinLabel = getVariableAndConstrainEquality(p, joinLabel, n);
			n.map.put(p, joinLabel);
			//set(p, ts.join(l1,l2)); -- why can't we do this?
		}

		return n;
	}

	@Override
	public PathMap subst(VarMap bounds) {
		JLiftPathMap n = new JLiftPathMap(ts);

		for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry) i.next();
			Path p = (Path) e.getKey();
			Label L = (Label) e.getValue();
			n.map.put(p, bounds.applyTo(L));
		}

		return n;
	}


	@Override
	public PathMap set(Path p, Label L) {
		JLiftPathMap n = new JLiftPathMap(ts);
		n.map.putAll(map);

		if (p == Path.N && JLiftOptions.getInstance().shouldIgnoreImplicitFlows()) {
			// do nothing
			if (L instanceof NotTaken) {
				n.map.remove(p);
			}
			else {
				n.map.put(p, ts.bottomLabel());
			}
		}
		else {
			if (L instanceof NotTaken) {
				n.map.remove(p);
			}
			else {
				// time to go nuclear
				L = getVariableAndConstrainEquality(p, L, n);
				n.map.put(p, L);
			}
		}

		return n;
	}

	public static String getExceptionVariableName(Type type, boolean fromCall) {
    	String name = type.toString();
    	if (type instanceof ParsedClassType) {
    		ParsedClassType pct = (ParsedClassType) type;
    		name = pct.name();
    	}
    	if (type instanceof LabeledType) {
    		LabeledType pct = (LabeledType) type;
    		return getExceptionVariableName(pct.typePart(), fromCall);
    	}

    	if (fromCall)
    		name = "FromCall:" + name;

    	int number = NumberProvider.getNextNumber();
    	return "Exception:" + name + number;

//		if (exceptionVariableNameMap.containsKey(name)) {
//			int number = exceptionVariableNameMap.get(name) + 1;
//			exceptionVariableNameMap.put(name, number);
//			return name + number;
//		}
//
//		exceptionVariableNameMap.put(name, 1);
//		return name + "1";
	}

	private Label getVariableAndConstrainEquality(Path p, Label L, JLiftPathMap n) {
		if (JLiftOptions.getInstance().shouldUsePathVariables() && (!L.equals(n.map.get(p))) && shouldGenerateDefinitionalConstraintForPath(p)) {
			int number = NumberProvider.getNextNumber();
			LabelChecker lc = JLiftLabelCheckingUtil.global;

			Position pos = L.position();
			Node node = null;
			Node currentNode = ((SummaryConstraintGenerator) lc).getCurrentNode();
			if (lc instanceof SummaryConstraintGenerator) {
				node = currentNode;
				pos = node.position();
			}

			String varDesc;
			if (node != null)
				varDesc = "path variable for value of " + node;
			else
				varDesc = "path variable for " + p;

			VarLabel pathVar = ts.freshLabelVariable(L.position(), p.toString() + number, varDesc);
			JLiftTypeSystem jts = (JLiftTypeSystem) ts;


			// && !currentNode.getClass().toString().contains("JifNew_c")
			if (p == Path.NV && JLiftOptions.getInstance().mutateConstraintSet) {
				// strip out any PC labels from this...
				Label oldL = L;
				L = stripPCLabels(L);
				//System.out.println("stripped " + oldL + " to " + L + " for setting NV " + pathVar + " (" + currentNode + ":" + currentNode.getClass() + ":" + currentNode.ext().getClass() + ")");
			}


//			LabelConstraint pathConstraint = jts.definitionalLabelConstraint(pathVar,
//					L,
//					jts.createLabelEnv(),
//					pos);

			try {
				lc.constrain(new NamedLabel("", pathVar), JLiftLabelConstraint.DEFINE, new NamedLabel("", L), jts.createLabelEnv(), pos);
			} catch (SemanticException e) {
				throw new InternalCompilerError(e);
			}
			L = pathVar;
		}
		return L;
	}

	private Label stripPCLabels(Label l) {
		Label returnLabel = l;
		if (l instanceof VarLabel) {
			VarLabel vl = (VarLabel) l;
			if (vl.name().startsWith("pc"))
				returnLabel = ts.bottomLabel();
			else
				returnLabel = l;
		}
		else if (l instanceof JoinLabel) {
			JoinLabel jl = (JoinLabel) l;
			Collection<Label> joins = jl.joinComponents();
			Set returns = new HashSet<Label>();
			returnLabel = ts.bottomLabel();
			for(Label join : joins) {
				returns.add(stripPCLabels(join));
			}

			returnLabel = ts.joinLabel(l.position(), returns);
		}

		return returnLabel;
	}

	private boolean shouldGenerateDefinitionalConstraintForPath(Path p) {
		return p == Path.NV || p == Path.R;
	}

	@Override
    public PathMap exc(Label label, Type type) {
    	if (ts instanceof JLiftTypeSystem) {
    		if (!((JLiftTypeSystem) ts).exceptionTypeShouldAffectSecurity(type)) {
    			return this;
    		}
    	}

		LabelChecker lc = JLiftLabelCheckingUtil.global;

		if (JLiftOptions.getInstance().mutateConstraintSet && lc != null) {
			label = ts.join(label,lc.jifContext().pc());
		}

		Label labelToJoin = label;

    	if (JLiftOptions.getInstance().noVariablesForExceptions == false) {
    		// generate a variable
    		JifTypeSystem ts = lc.typeSystem();

    		Position pos = label.position();
    		if (lc instanceof SummaryConstraintGenerator) {
				SummaryConstraintGenerator scg = (SummaryConstraintGenerator) lc;
				pos = (scg).getCurrentNode().position();
			}

    		String name = getExceptionVariableName(type, false);
    		VarLabel excVariable = ts.freshLabelVariable(pos, name, "exception variable for " + type);
//  		JLiftLabelConstraint c = new JLiftLabelConstraint(new NamedLabel("", excVariable), JLiftLabelConstraint.DEFINE, new NamedLabel("", label), lc.context().labelEnv(), pos, new LabelConstraintMessage(), false);
    		try {
    			lc.constrain(new NamedLabel("", excVariable), JLiftLabelConstraint.DEFINE, new NamedLabel("", label), lc.context().labelEnv(), pos, false, new LabelConstraintMessage());
    		} catch (SemanticException e) { throw new InternalCompilerError("got semantic exception when constraining an equality constraint: " + e); }
    		labelToJoin = excVariable;
    	}

    	return super.exc(labelToJoin, type);
	}

	@Override
	public PathMap exception(Type type, Label label) {
		if (!((JLiftTypeSystem) ts).exceptionTypeShouldAffectSecurity(type))
			return this;

		return super.exception(type, label);
	}
}
