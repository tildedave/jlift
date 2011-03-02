package jlift.visit;

import polyglot.ast.Block;
import polyglot.ast.CodeNode;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.NodeVisitor;
import polyglot.visit.ReachChecker;

public class JLiftReachChecker extends ReachChecker {

	public JLiftReachChecker(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

    @Override
	public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        // check for reachability.
        if (n instanceof Term) {
           n = checkReachability((Term)n);
           if (!((Term)n).reachable()) {
               // Do we throw an exception or not?

               // Compound statements are allowed to be unreachable
               // (e.g., "{ return; }" or "while (true) S").  If a compound
               // statement is truly unreachable, one of its sub-statements
               // will be also and we will report an error there.

               if ((n instanceof Block && ((Block) n).statements().isEmpty()) ||
                   (n instanceof Stmt && ! (n instanceof CompoundStmt))) {
//                   throw new SemanticException("Unreachable statement.",
//                                               n.position());

            	   System.out.println("Unreachable statement: " + n + "@" + n.position());
               }
           }

        }

        if (n instanceof CodeNode) {
            if (!dataflowOnEntry) {
                dataflow((CodeNode)n);
            }
            else if (dataflowOnEntry && !flowgraphStack.isEmpty()) {
                FlowGraphSource fgs = (FlowGraphSource)flowgraphStack.getFirst();
                if (fgs.source().equals(old)) {
                    // we are leaving the code decl that pushed this flowgraph
                    // on the stack. pop tbe stack.
                    flowgraphStack.removeFirst();
                }
            }
        }
        return n;
    }


}
