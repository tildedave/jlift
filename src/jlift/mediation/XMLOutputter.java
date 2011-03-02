package jlift.mediation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import jif.types.Equation;
import jif.types.JifTypeSystem;
import jif.types.LabelConstraint;
import jif.types.label.DynamicLabel;
import jif.types.label.JoinLabel;
import jif.types.label.Label;
import jif.types.label.PairLabel;
import jif.types.label.VarLabel;
import jlift.JLiftOptions;
import jlift.types.JLiftLabelConstraint;
import jlift.types.label.JLiftVarLabel;
import jlift.util.CallGraph;
import jlift.visit.ProfilerVisitor;

import org.apache.xmlbeans.XmlObject;
import org.openuri.constraint.ConstraintSetDocument;
import org.openuri.constraint.XmlConstraint;
import org.openuri.constraint.XmlJoin;
import org.openuri.constraint.XmlLabel;
import org.openuri.constraint.XmlLatticeLabel;
import org.openuri.constraint.XmlLatticeLeq;
import org.openuri.constraint.XmlProcedure;
import org.openuri.constraint.ConstraintSetDocument.ConstraintSet;

import polyglot.ast.Expr;
import polyglot.ast.Special;
import polyglot.types.ConstructorInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;

public class XMLOutputter {

	private static int currentUniqIdIndex = 1;
	protected Collection<JLiftLabelConstraint> constraints;
	protected Collection<Label> baseLabels;
	private final ProcedureInstance pi;
	protected CallGraph callGraph;
	private final Map<ProcedureInstance, BigInteger> procToIdMap;
	protected static Map<Label, BigInteger> labelToIntMap = new HashMap<Label, BigInteger>();
	protected static Map<VarLabel, ProcedureInstance> varLabelToProcedureMap = new HashMap<VarLabel, ProcedureInstance>();

	public XMLOutputter(ProcedureInstance pi, CallGraph cg, Collection<JLiftLabelConstraint> constraints) {
		this.constraints = constraints;
		this.pi = pi;
		this.callGraph = cg;
		this.baseLabels = new HashSet<Label>();
		this.procToIdMap = new HashMap<ProcedureInstance, BigInteger>();

		initializeProcedureMap();
	}



	private void initializeProcedureMap() {
		for(JLiftLabelConstraint baseConstraint: constraints) {
			if (baseConstraint.lhs() instanceof VarLabel) {
				VarLabel jvl = (VarLabel) baseConstraint.lhs();
				if (baseConstraint.kind() == JLiftLabelConstraint.DEFINE &&
						(jvl.name().startsWith("NV") || jvl.name().startsWith("Exception") || jvl.name().startsWith("pc") || jvl.name().startsWith("local"))) {
					ProcedureInstance pi = callGraph.getProcedureInstanceForNode(baseConstraint.getNode());
					//System.out.println("associate: " + jvl + " with " + pi);
					varLabelToProcedureMap.put(jvl, pi);
				}
			}
			Collection<VarLabel> vars = new HashSet<VarLabel>();
			vars.addAll(baseConstraint.lhs().variableComponents());
			vars.addAll(baseConstraint.rhs().variableComponents());

			for(VarLabel v : vars) {
				if (v.name().startsWith("NV") || v.name().startsWith("Exception") ||
						v.name().startsWith("pc") || v.name().startsWith("local")) {
					if (!varLabelToProcedureMap.containsKey(v)) {
						ProcedureInstance pi = callGraph.getProcedureInstanceForNode(baseConstraint.getNode());
						varLabelToProcedureMap.put(v, pi);
					}
				}
			}
		}
	}

	public void convertConstraints(String name) {
		labelToIntMap.clear();
		try {
			FileOutputStream fos = new FileOutputStream(name + ".xml");
			PrintStream ps = new PrintStream(fos);
			ps.println("<constraint-set xmlns=\"http://openuri.org/constraint\">");
			ConstraintSetDocument csd = convertConstraintSetToXMLDocument(ps);

			ps.println(scrub(csd.toString()));
			ps.println("</constraint-set>");

			System.out.println("constraints written to " + name + ".xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected ConstraintSetDocument convertConstraintSetToXMLDocument(PrintStream ps) {
		ConstraintSetDocument theDoc = ConstraintSetDocument.Factory.newInstance();
		ConstraintSet constraintSet = theDoc.addNewConstraintSet();

		if (constraints.isEmpty())
			return theDoc;

		JifTypeSystem jts = (JifTypeSystem) constraints.iterator().next().lhs().typeSystem();
		labelToIntMap.put(jts.bottomLabel(), BigInteger.valueOf(0));

		int procNumber = 0;
		for(ProcedureInstance pi : this.callGraph.getAllProcedures()) {
			procToIdMap.put(pi, new BigInteger(new Integer(procNumber).toString()));
			++procNumber;
		}

		for(JLiftLabelConstraint c : constraints) {
			Collection<XmlConstraint> xml_cs = constraintToXMLObject(c);
			for(XmlConstraint xml_c : xml_cs) {
				//XmlConstraint temp_con = constraintSet.addNewCon();
				//temp_con.set(xml_c);

				ConstraintSetDocument tempDoc = ConstraintSetDocument.Factory.newInstance();
				ConstraintSet tempSet = tempDoc.addNewConstraintSet();

				tempSet.addNewCon().set(xml_c);

				String tempStr = tempDoc.toString();
				tempStr = scrub(tempStr);
				ps.print(tempStr);
			}

//			System.out.println("converted: " + c + " into " + xml_cs);
		}

		ConstraintSet.Lattice latt = constraintSet.addNewLattice();
		for(Label l : baseLabels) {
			XmlLatticeLabel xll = latt.addNewLabel();
			xll.setName(l.toString());
			xll.setId(idForLabel(l, labelToIntMap));
		}

		for(Label l1 : baseLabels) {
			for (Label l2 : baseLabels) {
				if (!l1.equals(l2)) {
					// output dependency between l1 + l2
					JifTypeSystem ts = (JifTypeSystem) l1.typeSystem();

					if (ts.leq(l1, l2)) {
						XmlLatticeLeq xlleq = latt.addNewLt();
						xlleq.setLhs(idForLabel(l1, labelToIntMap));
						xlleq.setRhs(idForLabel(l2, labelToIntMap));
					}
				}
			}
		}

		ConstraintSet.ProcedureList pl = constraintSet.addNewProcedureList();

		for(ProcedureInstance pi : this.callGraph.getAllProcedures()) {
			XmlProcedure currentProcedure = pl.addNewProcedure();
			currentProcedure.setName(pi.toString());
			currentProcedure.setId(this.procToIdMap.get(pi));
			BigInteger procedureWeight = ProfilerVisitor.getProcedureToWeightMap().get(ProfilerVisitor.getFullNameForProcedureInstance(pi));
			if (procedureWeight != null)
				currentProcedure.setWeight(procedureWeight);
			else
				currentProcedure.setWeight(BigInteger.valueOf(-1));
		}

		return theDoc;
	}


	private String scrub(String tempStr) {
		return tempStr.replaceAll("<constraint-set xmlns=\"http://openuri.org/constraint\">\n", "").replaceAll("</constraint-set>","");
	}

	private BigInteger idForLabel(Label l, Map<Label, BigInteger> labelToIntMap) {
		if (!labelToIntMap.containsKey(l)) {
			labelToIntMap.put(l, BigInteger.valueOf(XMLOutputter.currentUniqIdIndex));
			XMLOutputter.currentUniqIdIndex++;
		}

		return labelToIntMap.get(l);
	}

	protected Collection<XmlConstraint> constraintToXMLObject(JLiftLabelConstraint jlc) {
		Collection<Equation> eqs = jlc.getEquations();
		Collection<XmlConstraint> cons = new LinkedList<XmlConstraint>();
		for(Equation eq : eqs) {
			XmlConstraint c = equationToXMLObject(eq, jlc);
			cons.add(c);
		}

		return cons;
	}

	private XmlConstraint equationToXMLObject(Equation eq, JLiftLabelConstraint jlc) {
		XmlJoin lhs = joinLabelToXMLObject(eq.lhs(), jlc);
		XmlLabel rhs = labelToXMLObject(eq.rhs(), jlc);

		XmlConstraint theConstraint = XmlConstraint.Factory.newInstance();
		theConstraint.setLhs(lhs);
		theConstraint.setRhs(rhs);
		theConstraint.setPos(jlc.position().toString());

		if (jlc.node() != null) {
			theConstraint.setBecause(filterBadCharacters(jlc.getNode().toString()));
			theConstraint.setBecauseType(jlc.getNode().getClass().getName());
		}
		else {
			theConstraint.setBecause("#UNKNOWN");
			theConstraint.setBecauseType("#UNKNOWN");
		}

		theConstraint.setAsString(jlc.toString());

		return theConstraint;
	}

	private String filterBadCharacters(String string) {
		// DHK: these particular characters confuse the OCAML XML parser --
		// XML-light doesn't seem to understand "CDATA".  A smarter
		// input library wouldn't require this kind of output filtering.
		return string.replace(">", "#gt").replace("&","#amp").replace("<","#lt");
	}

	private XmlLabel labelToXMLObject(Label label, JLiftLabelConstraint baseConstraint) {

		if (label.toString().equals("{}")) {
			JifTypeSystem jts = (JifTypeSystem) label.typeSystem();
			return labelToXMLObject(jts.bottomLabel(), baseConstraint);
		}

		XmlLabel xl = XmlLabel.Factory.newInstance();

		if (label instanceof PairLabel) {
			PairLabel pl = (PairLabel) label;
			//String nameToSet;

			baseLabels.add(pl);

			xl.setName("LATTICE#" + idForLabel(label, labelToIntMap).toString());
			xl.setIsvar(false);
		}
		else if (label instanceof DynamicLabel) {
			DynamicLabel dl = (DynamicLabel) label;


			xl.setName("LATTICE#" + idForLabel(label, labelToIntMap).toString());
			xl.setIsvar(false);

			baseLabels.add(dl);
		}
		else if (label instanceof JLiftVarLabel) {
			JLiftVarLabel jvl = (JLiftVarLabel) label;
			xl.setName(jvl.name());
			xl.setIsvar(true);
			String key = baseConstraint.getNode().toString() + baseConstraint.getNode().position().toString();
			if (JLiftOptions.getInstance().shouldUseProfiling() &&
					isVariableWithWeight(jvl) &&
					ProfilerVisitor.getStringNodeToWeightMap().containsKey(key)) {
				xl.setWeight(ProfilerVisitor.getStringNodeToWeightMap().get(key));
			}
			else {
				xl.setWeight(new BigInteger("1"));
			}

			boolean canDeclExpression = label.equals(baseConstraint.lhs()) &&
					baseConstraint.kind() == JLiftLabelConstraint.DEFINE &&
					jvl.name().matches("NV[0-9]+");

			boolean canDeclParam = label.equals(baseConstraint.lhs()) &&
					baseConstraint.kind() == LabelConstraint.EQUAL &&
					jvl.name().matches("PARAM[0-9]+");

//			if (baseConstraint.kind() == JLiftLabelConstraint.DEFINE &&
//				label.equals(baseConstraint.lhs()) &&
//				(jvl.name().startsWith("NV") || jvl.name().startsWith("Exception") || jvl.name().startsWith("pc"))) {
//				BigInteger id = this.procToIdMap.get(callGraph.getProcedureInstanceForNode(baseConstraint.getNode()));
//				xl.setProcedure(id);
//			}
			if (isVariableWithWeight(jvl)) {
				BigInteger id = this.procToIdMap.get(this.varLabelToProcedureMap.get(jvl));
				xl.setProcedure(id);
			}
			else {
				xl.setProcedure(new BigInteger(new Integer(-1).toString()));
			}

			if (canDeclExpression || canDeclParam) {
				xl.setCanDecl(true);
				if (baseConstraint.node() instanceof Special)
					xl.setCanDecl(false);
				else if (JLiftOptions.getInstance().noDeclassifySources &&
						baseConstraint.node() instanceof Expr) {
					Type t = ((Expr) baseConstraint.node()).type();
					if (t != null) {
						JifTypeSystem ts = (JifTypeSystem) t.typeSystem();

						if (ts.isLabeled(t)) {
							Label l = ts.labelOfType(t);
							if (l instanceof PairLabel) {
								xl.setCanDecl(false);
							}
						}
					}
				}


				// This is where we would place hard constraints: for each constraints, indiciate if it
				// can be a target declassification site or not.  (Should only need to do this for _def
				// constraints).

				// We need an interface for specifying what parts of the code could serve as declassification
				// sites and what cannot.  Possibly an extension of the compiler.

				if (true) {
					ProcedureInstance pi = callGraph.getProcedureInstanceForNode(baseConstraint.getNode());
					ReferenceType container = null;
					if (pi instanceof MethodInstance)
						container = ((MethodInstance) pi).container();
					if (pi instanceof ConstructorInstance)
						container = ((ConstructorInstance) pi).container();

					if (container != null) {
						if (container.toString().contains("FieldTokenizer")) {
							System.out.println("not declassifying " + baseConstraint + " -- in a bad class" + pi);
							xl.setCanDecl(false);
						}
						if (container.toString().contains("LetsBreak")) {
							System.out.println("not declassifying " + baseConstraint + " -- in a bad class" + pi);
							xl.setCanDecl(false);
						}
					}
				}
			}
		}

		return xl;
	}

	private boolean isVariableWithWeight(JLiftVarLabel jvl) {
		return jvl.name().startsWith("NV") || jvl.name().startsWith("Exception") || jvl.name().startsWith("pc") || jvl.name().startsWith("local:");
	}



	private XmlJoin joinLabelToXMLObject(Label lhs, JLiftLabelConstraint jlc) {
		XmlJoin xjoin = XmlJoin.Factory.newInstance();
		if (lhs instanceof JoinLabel) {
			Collection<Label> joinComponents = ((JoinLabel) lhs).joinComponents();
			for(Label l : joinComponents) {
				XmlObject xml_l = labelToXMLObject(l, jlc);
				XmlLabel newLabel = xjoin.addNewLabel();
				newLabel.set(xml_l);
			}
		}
		else {
			XmlObject xml_l = labelToXMLObject(lhs, jlc);
			XmlLabel singleJoinLabel = xjoin.addNewLabel();
			singleJoinLabel.set(xml_l);
		}
		return xjoin;
	}


}
