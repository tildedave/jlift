/*
 * Project       : jlift
 * Description   : Extension of JifTypeSystem_c.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:40:15 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jif.types.JifClassType;
import jif.types.JifConstructorInstance;
import jif.types.JifMethodInstance;
import jif.types.JifProcedureInstance;
import jif.types.JifSubstClassType_c;
import jif.types.JifSubstType;
import jif.types.LabelConstraint;
import jif.types.LabelConstraintMessage;
import jif.types.NamedLabel;
import jif.types.Param;
import jif.types.Path;
import jif.types.PathMap;
import jif.types.Solver;
import jif.types.LabelConstraint.Kind;
import jif.types.hierarchy.LabelEnv;
import jif.types.hierarchy.LabelEnv_c;
import jif.types.label.AccessPath;
import jif.types.label.AccessPathConstant;
import jif.types.label.ArgLabel;
import jif.types.label.ArgLabel_c;
import jif.types.label.ConfPolicy;
import jif.types.label.DynamicLabel;
import jif.types.label.IntegPolicy;
import jif.types.label.Label;
import jif.types.label.PairLabel;
import jif.types.label.VarLabel;
import jif.types.principal.DynamicPrincipal;
import jif.types.principal.ExternalPrincipal;
import jif.types.principal.Principal;
import jifclipse.InternalCompilerInfo;
import jifclipse.types.JifclipseTypeSystem_c;
import jlift.JLiftOptions;
import jlift.types.label.JLiftDynamicLabel_c;
import jlift.types.label.JLiftPairLabel_c;
import jlift.types.label.JLiftVarLabel;
import jlift.types.label.JLiftVarLabel_c;
import jlift.types.label.SummaryVarLabel;
import jlift.types.principal.JLiftDynamicPrincipal_c;
import jlift.util.MultiMap;
import polyglot.ast.Node;
import polyglot.frontend.Source;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.NoMemberException;
import polyglot.types.ParsedClassType;
import polyglot.types.PrimitiveType;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.TopLevelResolver;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class JLiftTypeSystem_c extends JifclipseTypeSystem_c implements JLiftTypeSystem {

	private final JLiftDefaultSignature ds;
	private JLiftFixedSignature jds;

	public static LabelEnv_c emptyEnvironment;

	public MultiMap<ParsedClassType, ParsedClassType> signatureImplementers;

	@Override
	public ArrayType arrayOf(Type type, int dims) {
		return arrayOf(type.position(), type, dims);
	}

	@Override
	protected ArrayType arrayType(Position pos, Type type) {
		if (!isLabeled(type)) {
			// type's position is null for some reason?
			type = labeledType(pos, type, defaultSignature().defaultArrayBaseLabel(pos));
		}
		return super.arrayType(pos, type);
	}

	public JLiftTypeSystem_c(TypeSystem jlts, InternalCompilerInfo ici) {
		super(jlts, ici);
		ds = new SummarySignature(this);
		signatureImplementers = new MultiMap<ParsedClassType, ParsedClassType>();
		JLiftTypeSystem_c.emptyEnvironment = new LabelEnv_c(this, true) {
			@Override
			public void setSolver(Solver s) {
				this.solver = s;
			}
		};
		JLiftLabelConstraint.label = new StrippedNamedLabel(this.bottomLabel());
	}

	@Override
	public Context createContext() {
		return new JLiftContext_c(this, jlts, null);
	}

	@Override
	public Solver createSolver(String solverName) {
		return new JLiftSolver(this, extInfo.compiler(), solverName);
	}

	public JLiftSolver createRehofSolver(String name) {
		return (JLiftSolver) createSolver(name);
	}

	@Override
	public Collection uncheckedExceptions() {
		if (JLiftOptions.getInstance().runtimeExceptions) {
			return super.uncheckedExceptions();
		}
		if (JLiftOptions.getInstance().noExceptions) {
			List<ClassType> l  = new LinkedList<ClassType>();
			l.add(Error());
			l.add(RuntimeException());
			l.add(Exception());

			return l;
		}
		else {
			List<ClassType> l = new LinkedList<ClassType>();
			l.add(Error());
			l.add(ArithmeticException());
			l.add(ArrayStoreException());
			l.add(ClassCastException());
			l.add(NegativeArraySizeException());
			l.add(NullPointerException());
			l.add(OutOfBoundsException());
			return l;
		}
	}

	private ClassType NegativeArraySizeException() {
		return load("java.lang.NegativeArraySizeException");
	}

	@Override
	public JLiftDefaultSignature defaultSignature() {
		return ds;
	}

	public SummaryVarLabel freshSummaryVariable(Position pos, String s, String description) {
		SummaryVarLabel t = new SummaryVarLabel(s, description, this, pos);
		return t;
	}

	@Override
	public Label callSitePCLabel(JifProcedureInstance pi) {
		String name;
		if (pi instanceof JifMethodInstance)
			name = ((MethodInstance) pi).name();
		else
			name = ((ConstructorInstance) pi).container().toString();

		ArgLabel pcLabel = new ArgLabel_c(this, pi, "caller_pc@" + name, pi.position());
		pcLabel.setUpperBound(pi.pcBound());
		pcLabel.setDescription("The pc at the call site of this " +
				pi.designator() + " (bounded above by " +
				pi.pcBound() + ")");
		return pcLabel;
	}

	@Override
	public LabelEnv createLabelEnv() {
		return new LabelEnv_c(this, false) {
			@Override
			public void setSolver(Solver s) {
				this.solver = s;
			}
		};
	}

	public List<ReferenceType> getSubtypes(ReferenceType refType) {
		// recursively get the subtypes for refType
		if (refType instanceof JLiftParsedPolyType) {
			LinkedList<ReferenceType> returnList = new LinkedList<ReferenceType>(((JLiftParsedPolyType) refType).getSubtypes());
			for(ReferenceType rt : ((JLiftParsedPolyType) refType).getSubtypes())
			{
				List<ReferenceType> rtSubtypes = getSubtypes(rt);
				returnList.addAll(rtSubtypes);
			}
			returnList.add(refType);
			return returnList;
		}
		if (refType instanceof JifSubstClassType_c) {
			return getSubtypes((ReferenceType) ((JifSubstClassType_c) refType).base());
		}
		else
		{
			return new LinkedList<ReferenceType>();
		}
	}

	public void getSubtypesAndImplementersForClass(ReferenceType rt, Collection<ReferenceType> checkClasses) {
		if (checkClasses.contains(rt))
			return;

		checkClasses.add(rt);

		List<ReferenceType> subtypes = getSubtypes(rt);
		List<ReferenceType> implementers = getImplementers(rt);

		for(ReferenceType st : subtypes) {
			getSubtypesAndImplementersForClass(st, checkClasses);
		}
		for(ReferenceType imp : implementers) {
			getSubtypesAndImplementersForClass(imp, checkClasses);
		}

	}


	public List<ReferenceType> getSubtypesAndImplementers(ProcedureInstance pi) {
		if (pi instanceof MemberInstance) {
			ReferenceType container = ((MemberInstance) pi).container();

			List<ReferenceType> checkClasses = new LinkedList<ReferenceType>();

			if (false) {
				List<ReferenceType> subtypes = getSubtypes(((MemberInstance) pi).container());
				for (ReferenceType rt : subtypes)
				{
					checkClasses.add(rt);
					checkClasses.addAll(getImplementers(rt));
				}
			}
			else {
				getSubtypesAndImplementersForClass(container, checkClasses);
			}

			if (container instanceof JifSubstClassType_c) {
				Type base = ((JifSubstClassType_c) container).base();
				if (base instanceof ParsedClassType) {
					Collection<ParsedClassType> sigImplementers = signatureImplementers.get(base);
					if (sigImplementers != null)
						checkClasses.addAll(sigImplementers);
				}
				//System.err.println("let's try: " + signatureImplementers.get((ParsedClassType) base));
			}
			//System.err.println(signatureImplementers.get(((MemberInstance) pi).container()));

			return checkClasses;
		}
		return null;
	}

	public List<ReferenceType> getImplementers(ReferenceType refType) {
		//System.out.println("getImplementers: " + refType + " (" + refType.getClass() + ")");
		if (refType instanceof JLiftParsedPolyType) {
			JLiftParsedPolyType parsedPolyType = (JLiftParsedPolyType) refType;
			LinkedList<ReferenceType> returnList = new LinkedList<ReferenceType>(parsedPolyType.getImplementers());
			return returnList;
		}
		if (refType instanceof JifSubstType) {
			JifSubstType jst = (JifSubstType) refType;
			if (jst.base() instanceof ReferenceType)
				return getImplementers((ReferenceType) jst.base());
		}
		return new LinkedList<ReferenceType>();
	}


	public List<ProcedureInstance> getProceduresInSubtypes(ProcedureInstance pi) {
		List<ProcedureInstance> procedureInstancesToCheck = new LinkedList<ProcedureInstance>();
		List<ReferenceType> checkClasses = getSubtypesAndImplementers(pi);

		if (pi instanceof MethodInstance)
		{
			for(ReferenceType rt : checkClasses) {
				if (rt.hasMethod((MethodInstance) pi)) {
					List<MethodInstance> methods = rt.methods();
					for(MethodInstance mi : methods) {
						if (isSameMethod(mi, (MethodInstance) pi)) {
							procedureInstancesToCheck.add(mi);
						}
					}
				}
			}
		}
		else if (pi instanceof ConstructorInstance)
		{
			// need to add constraints during a super() -- this can't be done here, really
			// change how we label check the super call -- maybe this is already done?
			procedureInstancesToCheck.add(pi);
		}

		return procedureInstancesToCheck;
	}

	@Override
	public FieldInstance findField(ReferenceType container, String name, ClassType currClass) throws SemanticException {
		try
		{
			return super.findField(container, name, currClass);
		}
		catch (NoMemberException nme) {
			throw nme;
		}
	}

	public boolean allowSmallLeaks() {
		return true;
	}

	public boolean exceptionTypeShouldAffectSecurity(Type type) {
		if (isUncheckedException(type)) {
//			System.err.println(type + " does not affect security");
			return false;
		}

//		System.err.println(type + " does affect security");
		return true;
	}

	@Override
	protected synchronized ClassType load(String name) {
		// need to override superclass to make it synchronized
		ClassType ct = super.load(name);
		return ct;
	}

	@Override
	public ExternalPrincipal externalPrincipal(Position pos, String name) {
		return super.externalPrincipal(pos, name);
	}

	@Override
	public ParsedClassType createClassType(LazyClassInitializer init,
			Source fromSource) {
		if (!init.fromClassFile()) {
			return new JLiftParsedPolyType_c(this, init, fromSource);
		} else {
			return super.createClassType(init, fromSource);
		}
	}

	@Override
	public PathMap pathMap() {
		return new JLiftPathMap(this);
	}

	@Override
	public PathMap pathMap(Path path, Label L) {
		return super.pathMap(path, L);
	}

	public JLiftLabelConstraint definitionalLabelConstraint(VarLabel pathVar,
			Label l, LabelEnv labelEnv, Position pos) {
		return new JLiftLabelConstraint(new NamedLabel("",pathVar),
				JLiftLabelConstraint.DEFINE,
				new NamedLabel("",l),
				labelEnv,
				pos);
	}

	public JLiftLabelConstraint labelConstraint(Label l1, LabelConstraint.Kind kind, Label l2,
			LabelEnv labelEnv, Position pos, LabelConstraintMessage msg, boolean report) {
		return new JLiftLabelConstraint(new NamedLabel("",l1), kind, new NamedLabel("", l2), labelEnv, pos, msg, report);
	}

	@Override
	public JLiftVarLabel freshLabelVariable(Position pos, String s, String description) {
		if (description.contains("label of the local variable")) {
			s = "local:" + s;
		}
		return new JLiftVarLabel_c(s, description, this, pos);
	}

	public JLiftLabelConstraint labelConstraint(Label newLhs, Kind kind,
			Label newRhs, LabelEnv env, Position position, Node node,
			LabelConstraintMessage msg,
			boolean report) {
		JLiftLabelConstraint c = labelConstraint(newLhs, kind, newRhs, env, position, msg, report);
		c.setNode(node);

		return c;
	}

	@Override
	public PairLabel pairLabel(Position pos,
			ConfPolicy confPol,
			IntegPolicy integPol) {
		return new JLiftPairLabel_c(this, confPol, integPol, pos, pairLabelTranslator());
	}

	@Override
	public boolean isParamsRuntimeRep(Type t) {
		// DHK: we always say yes -- this is an issue for normal Jif -> Java.
		// TODO: might be an issue for us too, when outputting to Jif code
		return true;
	}

	@Override
	public boolean canCoerceToString(Type t, Context c) {
		if (!allowSmallLeaks())
			return super.canCoerceToString(t, c);
		else
			return ! t.isVoid();
	}

	@Override
	public Resolver classContextResolver(ClassType type) {
		// do what the polyglot type system does, as we might
		// have an inner class
		return classContextResolver(type, null);
	}

	@Override
	public JifMethodInstance jifMethodInstance(Position pos,
			ReferenceType container, Flags flags, Type returnType, String name,
			Label startLabel, boolean isDefaultStartLabel, List formalTypes,
			List formalArgLabels, Label endLabel, boolean isDefaultEndLabel,
			List excTypes, List constraints) {
		JifMethodInstance mi =
			new JLiftMethodInstance_c(this, pos, container, flags, returnType, name,
					startLabel, isDefaultStartLabel, formalTypes, formalArgLabels,
					endLabel, isDefaultEndLabel, excTypes, constraints);

		return mi;
	}

	@Override
	public JifConstructorInstance jifConstructorInstance(Position pos,
			ClassType container, Flags flags, Label startLabel,
			boolean isDefaultStartLabel, Label returnLabel,
			boolean isDefaultReturnLabel, List formalTypes,
			List formalArgLabels, List excTypes, List constraints) {
		return new JLiftConstructorInstance_c(this, pos, container, flags, startLabel,
				isDefaultStartLabel, returnLabel, isDefaultReturnLabel, formalTypes,
				formalArgLabels, excTypes, constraints);
	}

	public void registerInterfaceImplemented(ParsedClassType interfaze, ParsedClassType implementer) {
		signatureImplementers.put(interfaze, implementer);
	}

	@Override
	public boolean isSubtype(Type child, Type ancestor) {
		//System.err.println("checking if " + child + " is a subtype of " + ancestor + ": " + super.isSubtype(child, ancestor));
		return super.isSubtype(child, ancestor);
	}

	private boolean allowImplicitArrayCasts = true;

	@Override
	public boolean callValid(ProcedureInstance prototype, List argTypes) {
		// TODO Auto-generated method stub

		//System.out.println("is call " + argTypes + " valid for " + prototype);
		if (prototype instanceof MemberInstance) {
			MemberInstance mi = (MemberInstance) prototype;
			Type t = strip(mi.container());
			if (t instanceof ParsedClassType) {
				ParsedClassType pct = (ParsedClassType) t;
				if (pct.fromSource() == null) {
					this.allowImplicitArrayCasts = true;
				}
			}
		}
		boolean answer = super.callValid(prototype, argTypes);
		this.allowImplicitArrayCasts = true;
		//System.out.println("answer: " + answer);
		return answer;
	}

	@Override
	public boolean isImplicitCastValid(Type fromType, Type toType) {
		// TODO Auto-generated method stub
        Type strpFromType = strip(fromType);
        Type strpToType = strip(toType);

        //System.out.println("checking if " + fromType + " can be cast into " + toType);

        if (allowImplicitArrayCasts && strpFromType.isArray() && strpToType.isArray()) {
        	ArrayType fromArray = strpFromType.toArray();
        	ArrayType toArray = strpToType.toArray();

        	if (isImplicitCastValid(fromArray.base(), toArray.base()))
        		return true;
        }

        if (isCastValid(fromType, toType))
        	return true;

        return super.isImplicitCastValid(fromType, toType);
	}

	@Override
	public Type strip(Type type) {
		return super.strip(type);
	}

	@Override
	protected Type leastCommonAncestorSubtype(Type subtype, Type supertype) {
        while (subtype != null && !equals(subtype, supertype)) {
            subtype = subtype.toClass().superType();
        }
        // subtype is now the same type as supertype, when stripped of their
        // parameters. Now check their parameters.
        Iterator iter1 = ((JifClassType)subtype).actuals().iterator();
        Iterator iter2 = ((JifClassType)supertype).actuals().iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            Param p1 = (Param)iter1.next();
            Param p2 = (Param)iter2.next();
            if (p1 instanceof Principal && p2 instanceof Principal) {
                if (!((Principal)p1).equals(p2)) {
                    return null;
                }

            }
            // We will make sure that the L_vars in an array initialization expression match up
            // during label checking
            if (p1 instanceof Label && p2 instanceof Label && !p1.toString().contains("L_var") && !p2.toString().contains("L_var")) {
                if (!(leq((Label)p1, (Label)p2) && leq((Label)p2, (Label)p1))) {
                    // the labels are not equivalent
                    return null;
                }
            }
            else if (!p1.equals(p2)) {
                // there are two non equal parameters, so we don't have an
                // appropriate least common ancestor
                return null;
            }
        }
        if (iter1.hasNext() || iter2.hasNext()) {
            // different number of parameters!
            return null;
        }
        // all the parameters agreed! we've found the least common ancestor!
        return supertype;
    }

	@Override
	public TopLevelResolver loadedResolver() {
		// TODO Auto-generated method stub
		return super.loadedResolver();
	}

	@Override
	public DynamicLabel dynamicLabel(Position pos, AccessPath path) {
		if (JLiftOptions.getInstance().dynamicLabelAnalysis) {
	        DynamicLabel t = new JLiftDynamicLabel_c(path, this, pos, dynamicLabelTranslator());
	        return t;
		}
		else
			return super.dynamicLabel(pos, path);
	}

	@Override
	public DynamicPrincipal dynamicPrincipal(Position pos, AccessPath path) {
        DynamicPrincipal t = new JLiftDynamicPrincipal_c(path, this, pos, dynamicPrincipalTranslator());
        return t;
	}

	@Override
	public PrimitiveType Label() {
		if (JLiftOptions.getInstance().dynamicLabelAnalysis)
			return new LabelPrimitiveType_c(this);
		else
			return super.Label();
	}

	@Override
	public PrimitiveType Principal() {
		if (JLiftOptions.getInstance().dynamicLabelAnalysis)
			return new PrincipalPrimitiveType_c(this);
		else
			return super.Principal();
	}

	@Override
	public boolean isLabel(Type type) {
		if (JLiftOptions.getInstance().dynamicLabelAnalysis)
			return unlabel(type) instanceof LabelPrimitiveType_c;
		else
			return super.isLabel(type);
	}

	@Override
	public boolean isPrincipal(Type type) {
		if (JLiftOptions.getInstance().dynamicLabelAnalysis)
			return unlabel(type) instanceof PrincipalPrimitiveType_c;
		else
			return super.isPrincipal(type);
	}

    @Override
	public Principal pathToPrincipal(Position pos, AccessPath path) {
        if (path instanceof AccessPathConstant) {
            AccessPathConstant apc = (AccessPathConstant)path;
            if (!apc.isPrincipalConstant()) {
                throw new InternalCompilerError("Dynamic principal with a constant access path: " + apc);
            }
            return (Principal)apc.constantValue();
        }
        DynamicPrincipal t = new JLiftDynamicPrincipal_c(path, this, pos, dynamicPrincipalTranslator());
        return t;
    }

    @Override
    public boolean isCastValid(Type fromType, Type toType) {
        Type strpFromType = strip(fromType);
        Type strpToType = strip(toType);

        // can cast from "principal" to any subclass of "jif.lang.Principal"
        if (Principal().equals(strpFromType) && isCastValid(PrincipalClass(), toType)) {
            return true;
        }

        // can cast from any subtype of "jif.lang.Principal" to "principal"
        if (Principal().equals(strpToType) && isSubtype(strpFromType, PrincipalClass())) {
            return true;
        }

        //System.out.println("IS CAST VALID " + fromType + " : " + toType);

        return super.isCastValid(strpFromType, strpToType);
    }

}