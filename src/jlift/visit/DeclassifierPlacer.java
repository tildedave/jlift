package jlift.visit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import jlift.util.MultiMap;

import org.apache.xmlbeans.XmlException;
import org.openuri.declassifiers.DeclElement;
import org.openuri.declassifiers.DeclSuggestion;
import org.openuri.declassifiers.DeclassifierSuggestionsDocument;

import polyglot.ast.AmbReceiver;
import polyglot.ast.Expr;
import polyglot.ast.Id;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.ErrorHandlingVisitor;

public class DeclassifierPlacer extends ErrorHandlingVisitor {

	protected static MultiMap<Integer,String> posStringsToDeclassify = null;

	protected static Map<String,MultiMap<Integer,String>> posStringDeclassifier = null;

	protected static File file;
	protected static int suggestionNumber;

	public DeclassifierPlacer(Job job, TypeSystem ts, NodeFactory nf, File suggestionFile, int suggestionNumber) {
		super(job, ts, nf);
		DeclassifierPlacer.suggestionNumber = suggestionNumber;
		DeclassifierPlacer.file = suggestionFile;
	}

	@Override
	protected Node leaveCall(Node n) throws SemanticException {
		if (posStringDeclassifier == null) {
			// need to wait until after the options are parsed to do this.
			// if there's an error I would like to throw a SemanticException
			// as opposed to an internal compiler error.
			initializePositions();
		}

		if (posStringsToDeclassify.containsKey(suggestionNumber)) {
			boolean posInDatabase = posStringsToDeclassify.get(suggestionNumber).contains(n.position().toString());
			if (posInDatabase) {
				JifNodeFactory jnf = (JifNodeFactory) nf;
				JifTypeSystem jts = (JifTypeSystem) ts;
				Expr e = null;
				if (n instanceof Expr) {
					e = (Expr) n;
				}
				else if (n instanceof AmbReceiver) {
					//System.out.println(n.position() + " (" + n + ") in database but not expr -- " + n.toString() + " (" + n.getClass().toString() + ")");
					AmbReceiver a = (AmbReceiver) n;
					e = jnf.AmbExpr(a.position(), a.nameNode());
				}

				if (e == null) {
					if (!(n instanceof Id)) {
						System.out.println("WARNING: position is in database, but not sure what to do about node -- " + n + " (" +  n.getClass() + ")");
					}
				}
				else {
					Node returnNode = jnf.DeclassifyExpr(n.position(), e, jnf.CanonicalLabelNode(n.position(),jts.bottomLabel()));

					//System.out.println(n + " (" + n.getClass() + ") matches!");
					System.out.println("--> replacing " + n + " with " + returnNode);
					return returnNode;
				}
			}
			else {
				//System.out.println(n.position() + " (" + n + ") not in database");
			}
		}
		return super.leaveCall(n);

	}

	private static void initializePositions() throws SemanticException {
		try {
			posStringDeclassifier = new HashMap<String, MultiMap<Integer,String>>();
			posStringsToDeclassify = new MultiMap<Integer,String>();

			DeclassifierSuggestionsDocument fileDoc = DeclassifierSuggestionsDocument.Factory.parse(file);
			DeclassifierSuggestionsDocument.DeclassifierSuggestions suggestions = fileDoc.getDeclassifierSuggestions();
			DeclassifierSuggestionsDocument.DeclassifierSuggestions.Flow[] flowArray = suggestions.getFlowArray();

			int numberOfSuggestions = 1;

			for (DeclassifierSuggestionsDocument.DeclassifierSuggestions.Flow f: flowArray) {
				//System.out.println("flow: " + f.getFrom() + "->" + f.getTo());
				DeclSuggestion[] suggestionArray = f.getSuggestionArray();
				int currentNumber = 0;

				MultiMap<Integer,String> currentFlowMap = new MultiMap<Integer, String>();

				for (DeclSuggestion ds : suggestionArray) {
					DeclElement[] declForSuggestionArray = ds.getDeclArray();
					for (DeclElement de : declForSuggestionArray) {
						int ls = Integer.parseInt(de.getLineStart());
						int le = Integer.parseInt(de.getLineEnd());
						int cs = Integer.parseInt(de.getColumnStart());
						int ce = Integer.parseInt(de.getColumnEnd());
						Position convertedPosition =
							new Position(de.getPath(),
									de.getFile(),
									ls, cs, le, ce);

						currentFlowMap.put(currentNumber,convertedPosition.toString());

//						if (currentNumber == suggestionNumber) {
//						System.out.println("declassify: " + de.getExp() + " @ " + convertedPosition);
//						}
					}
					++currentNumber;
				}

				numberOfSuggestions *= currentNumber;
//				System.out.println("numberOfSuggestions: " + numberOfSuggestions);
//				System.out.println("currentNumber: " + currentNumber);
				posStringDeclassifier.put(f.getFrom() + "->" + f.getTo(), currentFlowMap);
				//System.out.println(f.getFrom() + "->" + f.getTo() + ":" + currentFlowMap);
			}

			for(int i = 0; i < numberOfSuggestions; ++i) {
				for(String pos : getSuggestionsForInteger(i)) {
					posStringsToDeclassify.put(i, pos);
				}
				//System.out.println("suggestion #" + i + ": " + posStringsToDeclassify.get(i));
			}
		} catch (XmlException e) {
			throw new SemanticException(e);
		} catch (IOException e) {
			throw new SemanticException(e);
		}
	}

	/**
	 * Treat the suggestion index as an index into the suggestions.  Can view it as integer
	 * written in binary-ish notation.  If size of buckets is k_1, ..., k_n, then view i as
	 * encoding where suggestion in bucket 1 is i % k_1, suggestion in bucket 2 is
	 * ((i - i % k_1) / k_1) % k_2, etc
	 * @param i
	 * @return
	 */
	private static Collection<String> getSuggestionsForInteger(int i) {
		LinkedList<String> returnList = new LinkedList<String>();

		//System.out.println("getting suggestion for integer: " + i);
		Iterator<Map.Entry<String, MultiMap<Integer, String>>> keyValueIterator = posStringDeclassifier.entrySet().iterator();
		while (keyValueIterator.hasNext()) {
			Map.Entry<String, MultiMap<Integer, String>> nextEntry = keyValueIterator.next();
			int index = i % nextEntry.getValue().size();
			//System.out.println("index: " + index);
			i = (i - index) / nextEntry.getValue().size();

			//System.out.println("adding: " + nextEntry.getValue().get(index));
			returnList.addAll(nextEntry.getValue().get(index));
		}

		return returnList;
	}

}
