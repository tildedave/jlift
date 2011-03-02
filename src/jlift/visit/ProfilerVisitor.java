package jlift.visit;

import java.math.BigInteger;
import java.util.HashMap;

import jlift.util.ProfileReader;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.ConstructorInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

public class ProfilerVisitor extends ContextVisitor {



	public ProfilerVisitor(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	protected static HashMap<Node, BigInteger> nodeToWeightMap; //To store the map of nodes and their counts
	protected static HashMap<String, BigInteger> stringNodeToWeightMap;
	protected static HashMap<String, BigInteger> ht;//Stores the Hashtable of MethodNames, Counts from the profile.xml  file
	protected static boolean instantiated= false;
	protected static String currentProc = "name ";


	@Override
	public NodeVisitor begin()
	{
		if(!ProfilerVisitor.instantiated) {
			ProfileReader pr = new ProfileReader();
			ht = pr.readProfile(); // Reads the profile.xml file and builds a hashtable
			nodeToWeightMap = new HashMap<Node, BigInteger>();
			stringNodeToWeightMap = new HashMap<String, BigInteger>();
			ProfilerVisitor.instantiated = true;
		}
		return super.begin();
	}

	@Override
	public Node leaveCall(Node parent, Node old, Node n, NodeVisitor v) throws SemanticException
	{
		if(context().currentCode() instanceof ProcedureInstance) {
			String fullname = getFullNameForProcedureInstance((ProcedureInstance) context().currentCode());

			if(ht.containsKey(fullname)) {
				nodeToWeightMap.put(n,ht.get(fullname));
				stringNodeToWeightMap.put(n.toString() + n.position().toString(), ht.get(fullname));
			}
		}

		return n;
	}

	public static String getFullNameForProcedureInstance(ProcedureInstance pi) {
		String fullname = "";

		if (pi instanceof MethodInstance) {
			ReferenceType procContainer = ((MemberInstance) pi).container();

			//If it is a method then fullname = container:MethodName
			fullname = procContainer.toString() + ":" + ((MethodInstance) pi).name();
		}

		if (pi instanceof ConstructorInstance) {
			ReferenceType container = ((ConstructorInstance) pi).container();
			//If it is a constructor then fullname = Container:<init>
			fullname = container.toString() + ":" + "<init>";
		}
		return fullname;
	}

	public static HashMap<Node, BigInteger> getNodeToWeightMap() {
		return ProfilerVisitor.nodeToWeightMap;
	}

	public static HashMap<String, BigInteger> getStringNodeToWeightMap() {
		return ProfilerVisitor.stringNodeToWeightMap;
	}

	public static HashMap<String, BigInteger> getProcedureToWeightMap() {
		return ProfilerVisitor.ht;
	}

}
