/*
 * File          : JLiftOptions.java
 * Project       : jlift
 * Description   : Program arguments specific to JLift
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:27:49 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import jif.ExtensionInfo;
import jif.JifOptions;
import polyglot.main.UsageError;

public class JLiftOptions extends JifOptions {

	// DHK: option variables
	// TODO: cut these down.  which ones are used?  which ones are not?
	public boolean noVariablesForPCs = false;
	public boolean noVariablesForCalls = false;
	public boolean noVariablesForExceptions = false;

	public boolean runtimeExceptions = true;
	public boolean noExceptions = false;

	public boolean addMemberForSigs = false;
	public String signature_output_path = ".";

	public boolean keepUnsubbedConstraints = false;
	public boolean imperativeSubstitution = false;

	public boolean looseArraysInSignatures = true;
	public boolean all_errors = false;

	protected boolean multithread = false;
	protected boolean showMethodSignatures = false;

	protected boolean shouldGenerateDeclassificationConstraints = false;
	protected boolean shouldRefactorExceptions = false;
	protected boolean shouldGenerateJifCode = false;
	protected boolean shouldSubstEqualityConstraints = false;

	protected boolean shouldUsePathVariables = false;
	protected boolean shouldReportEqualityErrors = false;
	protected boolean noIntegrity = false;
	protected boolean shouldCompactOutputConstraints = false;
	protected boolean shouldRequireSinkPath = true;
	protected boolean shouldUseOldDeclassification = false;

        public int ctxSize = 0;

	public final Collection<String> principalNames = new LinkedHashSet<String>();
	private int varDebug;
	/**
	 * context sensitive methods -- each method call is treated as a
	 * completely different set of constraints.  this allows us to treat
	 * the same method as returning two different security values, for example.
	 * however, this causes a large blowup in constraint set size.  this is
	 * generally disabled.  however, calls to parameterized classes are treated
	 * as context sensitive.
	 */
	private boolean contextSensitiveMethods = false;

	/**
	 * used by the test harness to verify that it's label checking a method (the
	 * summary variables were all substituted away)
	 */
	private boolean forTestHarness = false;

	/**
	 * should we output multiple RHS constraints as themselves, or should we fragment
	 * them into different constraints -- i.e. A <= {B; C} becomes A <= B, A <= C.  note:
	 * this is not sound.
	 */
	public boolean multiRhs = false;
	/**
	 * should we declassify sources?  this is against one of our hard constraints, but
	 * often constraint systems are unsatisfiable without this.
	 */
	protected boolean declSources = false;

	/**
	 * turn off implicit flows altogether
	 */
	protected boolean noImplicitFlows = false;
	public Collection<String> prioritySigClassPath = new LinkedHashSet<String>();

	// prune dominated?
	public boolean pruneDominated = true;
	public boolean randomWeights = false;
	public boolean allowNondefiniteConstraints = false;
	public boolean mutateConstraintSet = false;
	public File declSuggestionFile;
	public int suggestionNumber;
	public boolean addDeclassifierSuggestions = false;
	public boolean safeMutateConstraintSet = true;

	protected static JLiftOptions instance = null;
	public boolean noDeclassifySources = false;
	public boolean eraseLabels = false;
	public boolean dynamicLabelAnalysis = false;
	public boolean oldDynamicLabelAnalysis = false;
	protected String profileXMLFile = "profile.xml";
	protected boolean shouldUseProfiling = false;

	public JLiftOptions(ExtensionInfo extension) {
		super(extension);
		instance = this;
	}

	public static JLiftOptions getInstance() {
		return instance;
	}

	@Override
	protected int parseCommand(String args[], int index, Set source) throws UsageError {
		if (args[index].equals("-noex")) {
			++index;
			this.runtimeExceptions = false;
			this.noExceptions = true;
		}
		else if (args[index].equals("-nortex")) {
			++index;
			this.runtimeExceptions = false;
		}
		else if (args[index].equals("-allerr")) {
			++index;
			this.all_errors = true;
		}
		else if (args[index].equals("-keepunsubbed")) {
			++index;
			this.keepUnsubbedConstraints = true;
		}
		else if (args[index].equals("-sigout")) {
			index++;
			this.signature_output_path = args[index++];
		}
		else if (args[index].equals("-sig")) {
			index++;
			this.addMemberForSigs = true;
		}
		else if (args[index].equals("-multigen")) {
			index++;
			this.multithread = true;
		}
		else if (args[index].equals("-methodsig")) {
			index++;
			this.showMethodSignatures = true;
		}
//		else if (args[index].equals("-gensig")) {
//			index++;
//			this.additionalSigs.add(args[index++]);
////			System.err.println(additionalSigs);
//		}
		else if (args[index].equals("-nocallvars")) {
			index++;
			this.noVariablesForCalls = true;
		}
		else if (args[index].equals("-nopcvars")) {
			++index;
			this.noVariablesForPCs = true;
		}
		else if (args[index].equals("-noexcvars")) {
			++index;
			this.noVariablesForExceptions = true;
		}
		else if (args[index].equals("-pathvars")) {
			++index;
			this.shouldUsePathVariables = true;
		}
		else if (args[index].equals("-refactorex")) {
			++index;
			shouldRefactorExceptions = true;
		}
		else if (args[index].equals("-genjifcode")) {
			++index;
			shouldGenerateJifCode = true;
		}
		else if (args[index].equals("-nointegrity")) {
			++index;
			disableIntegrity();
		}
		else if (args[index].equals("-declcons")) {
			++index;
			shouldGenerateDeclassificationConstraints  = true;
			shouldUsePathVariables = true;
			this.noVariablesForPCs = false;
			//this.runtimeExceptions = false;
			//this.mutateConstraintSet = true;
		}
		else if (args[index].equals("-olddeclcons")) {
			++index;
			shouldGenerateDeclassificationConstraints  = true;
			shouldUseOldDeclassification = true;
			shouldUsePathVariables = true;
			this.noVariablesForPCs = false;
			this.runtimeExceptions = false;
		}
		else if (args[index].equals("-impsub")) {
			++index;
			imperativeSubstitution = true;
		}
		else if (args[index].equals("-vardebug")) {
			++index;
			varDebug = Integer.parseInt(args[index]);
			++index;
		}
		else if (args[index].equals("-ctxmethods")) {
			++index;
			this.contextSensitiveMethods = true;
		}
		else if (args[index].equals("-multirhs")) {
			++index;
			this.multiRhs = true;
		}
		else if (args[index].equals("-testharness")) {
			++index;
			this.forTestHarness = true;
		}
		else if (args[index].equals("-noimplicit")) {
			++index;
			this.noImplicitFlows = true;
			this.noVariablesForExceptions = true;
			this.runtimeExceptions = false;
			this.noExceptions = true;
		}
		else if (args[index].equals("-ctxsize")) {
		    ++index;
		    ctxSize = Integer.parseInt(args[index]);
		    ++index;
		}
		else if (args[index].equals("-addpsigcp")) {
			++index;
//			this.prioritySigClassPath.add(args[index]);
			this.addSigcp.add(0, args[index]);
			++index;
		}
		else if (args[index].equals("-randomweights")) {
			++index;
			this.randomWeights = true;
		}
		else if (args[index].equals("-nondef")) {
			++index;
			this.allowNondefiniteConstraints = false;
		}
		else if (args[index].equals("-nodeclsources")) {
			++index;
			this.noDeclassifySources = true;
		}
		else if (args[index].equals("-declsuggest")) {
		    ++index;
		    this.addDeclassifierSuggestions = true;
		    this.declSuggestionFile = new File(args[index]);
		    if (!declSuggestionFile.exists())
		    	throw new UsageError("file " + args[index] + " specified by -declsuggestion does not exist");
		    ++index;
		    try {
		    	this.suggestionNumber = Integer.parseInt(args[index]);
		    }
		    catch (NumberFormatException n) {
		    	throw new UsageError(args[index] + " is not a valid number");
		    }
		    ++index;
		}
		else if (args[index].equals("-eraselabels")) {
			++index;
			this.eraseLabels  = true;
		}
		else if (args[index].equals("-dynamic")) {
			++index;
			this.dynamicLabelAnalysis = true;
		}
		else if (args[index].equals("-profiledata")) {
			++index;
			this.profileXMLFile = args[index];
			this.shouldUseProfiling = true;
			++index;

		}
		else {
			int i = super.parseCommand(args, index, source);
			return i;
		}

		return index;
	}

	@Override
	public void usage(PrintStream out) {
		super.usage(out);
		// TODO: trim these as necessary -- I don't think I` use all these any more
		out.println("JLIFT OPTIONS:");
		usageForFlag(out, "-addpsigcp", "add a prioritized signature classpath.  checked first, before sigcp and classpath");
		usageForFlag(out, "-noex", "do not include illegal flows from exceptions");
		usageForFlag(out, "-nortex", "do not include illegal flows from Java-specific runtime exceptions (ex: NPE)");
		usageForFlag(out, "-sigout", "signature output directory");
		//usageForFlag(out, "-multigen", "generate summary constraints using multithreaded procedure");
		usageForFlag(out, "-nocallvars", "do not generate _return and _returnvalue equality constraints");
		usageForFlag(out, "-nopcvars", "do not generate program counter equality constraints");
		usageForFlag(out, "-noexcvars", "do not generate exception equality constraints");
		usageForFlag(out, "-pathvars", "use path variables (adds lots of variables)");
		//usageForFlag(out, "-refactorex", "refactor runtime exceptions using try/catch blocks");
		//usageForFlag(out, "-genjifcode", "generate jif code (use -d to specify the output directory; does not work)");
		usageForFlag(out, "-declcons", "generate declassification constraints");
		//usageForFlag(out, "-nosinkpath", "don't require a sink path for declassifications (adds a lot more constraints to output)");
		//usageForFlag(out, "-impsub", "use imperative label substitutions (broken)");
		//usageForFlag(out, "-multirhs", "output constraints with multiple RHS");
		//usageForFlag(out, "-declsources", "allow sources to be declassified (may result in a satisfiable constraint set)");
		usageForFlag(out, "-ctxmethods", "context sensitive methods (rename summary variables from method calls -- blows up size of constraint set)");
		usageForFlag(out, "-ctxsize <num>", "treat methods with less than <num> summary constraints as context sensitive (default: 0)");
		//usageForFlag(out, "-noimplicit", "disable implicit flows");
		usageForFlag(out, "-nonsat", "use minimum cut to determine declassifier locations");
		usageForFlag(out, "-nondef", "allow nondefinite constraints (generalized constraints of the form L1 <= L2, where L2 may be a join)");
		usageForFlag(out, "-declsuggest <file> <num>", "use suggestion <num> from declassification file <file>");
		usageForFlag(out, "-nodeclsources", "don't declassify sources explicitly marked with a security label");
	}

	/*
	 * Accessor Methods
	 */

	public boolean shouldRefactorExceptions() {
		return shouldRefactorExceptions;
	}

	public boolean shouldGenerateJifCode() {
		return shouldGenerateJifCode;
	}

	public Collection<String> getPrincipalNames() {
		return principalNames ;
	}

	public boolean shouldGenerateDeclassificationConstraints() {
		return shouldGenerateDeclassificationConstraints;
	}

	public boolean shouldUsePathVariables() {
		return shouldUsePathVariables;
	}

	public boolean equalityErrors() {
		return shouldReportEqualityErrors;
	}

	public boolean noIntegrity() {
		return noIntegrity ;
	}

	public void disableIntegrity() {
		noIntegrity = true;
	}

	public void enableIntegrity() {
		noIntegrity = false;
	}

	public boolean shouldRequireSinkPath() {
		return shouldRequireSinkPath;
	}

	public boolean shouldUseOldDeclassification() {
		return shouldUseOldDeclassification;
	}

	public boolean shouldUseImperativeSubstitution() {
		return imperativeSubstitution;
	}

	public boolean shouldIgnoreImplicitFlows() {
		return noImplicitFlows;
	}

	// DHK: debug where variables of the same name are coming from
	public int varDebugLevel() {
		return varDebug;
	}

	public boolean contextSensitiveMethods() {
		return contextSensitiveMethods;
	}

	public boolean forTestHarness() {
		return this.forTestHarness;
	}

	public boolean canDeclassifySources() {
		return declSources;
	}

	public boolean isPrioritySigClassPath(File dir) {
		for(String str : prioritySigClassPath) {
			File dirStr = new File(str);
			if (dirStr.equals(dir))
				return true;
		}
		return false;
	}

	@Override
    public String constructSignatureClasspath() {
        // use the signature classpath if it exists for compiling Jif classes
        String scp = "";
        for (Iterator iter = prioritySigClassPath.iterator(); iter.hasNext(); ) {
        	scp += ((String)iter.next());
        	if (iter.hasNext()) {
        		scp += File.pathSeparator;
        	}
        }
        for (Iterator iter = addSigcp.iterator(); iter.hasNext(); ) {
            scp += ((String)iter.next());
            if (iter.hasNext()) {
                scp += File.pathSeparator;
            }
        }
        if (sigcp != null) {
            scp += File.pathSeparator + sigcp;
        }
        return scp;
    }

	public boolean shouldRefactorNewInstances() {
		return false;
	}

	public String getProfileXMLFile() {
		return profileXMLFile;
	}

	public boolean shouldUseProfiling() {
		return shouldUseProfiling;
	}
}



