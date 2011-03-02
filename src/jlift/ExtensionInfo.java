/*
 * File          : ExtensionInfo.java
 * Project       : jlift
 * Description   : jlift entry point
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:27:38 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jif.FieldLabelInferenceGoal;
import jif.JifScheduler;
import jif.OutputExtensionInfo;
import jif.ast.JifNodeFactory;
import jif.types.JifTypeSystem;
import jif.visit.NotNullChecker;
import jif.visit.PreciseClassChecker;
import jifclipse.InternalCompilerInfo;
import jifclipse.JifclipseExtensionInfo;
import jlift.ast.JLiftNodeFactory_c;
import jlift.frontend.GenerateCallGraphGoal;
import jlift.frontend.JLiftScheduler;
import jlift.frontend.SummaryGeneratedGoal;
import jlift.frontend.SummaryLabelCheckGoal;
import jlift.types.JLiftTypeSystem_c;
import jlift.util.CallGraph;
import jlift.visit.CountAnnotationsVisitor;
import jlift.visit.DeclassifierPlacer;
import jlift.visit.DisplayRemainingAnnotationsVisitor;
import jlift.visit.LabelEraser;
import jlift.visit.ProfilerVisitor;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Compiler;
import polyglot.frontend.CupParser;
import polyglot.frontend.CyclicDependencyException;
import polyglot.frontend.FileSource;
import polyglot.frontend.Job;
import polyglot.frontend.Parser;
import polyglot.frontend.Scheduler;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.main.Options;
import polyglot.main.Report;
import polyglot.types.LoadedClassResolver;
import polyglot.types.Named;
import polyglot.types.SemanticException;
import polyglot.types.SourceClassResolver;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.ClassFileLoader;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;

public class ExtensionInfo extends jif.ExtensionInfo implements JifclipseExtensionInfo {

	protected InternalCompilerInfo internalCompilerInfo;

	protected final class JLiftOutputExtensionInfo extends OutputExtensionInfo {
		private final class RecordPrincipalsResolver extends
				SourceClassResolver {
			private RecordPrincipalsResolver(Compiler compiler,
					polyglot.frontend.ExtensionInfo ext, String classpath,
					ClassFileLoader loader, boolean allowRawClasses,
					boolean compileCommandLineOnly, boolean ignoreModTimes) {
				super(compiler, ext, classpath, loader, allowRawClasses,
						compileCommandLineOnly, ignoreModTimes);
			}

			@Override
			public Named find(String name)
					throws SemanticException {
				Named returnNamed = super.find(name);
				if (name.startsWith("jif.principals.") && returnNamed != null) {
					int startIndex = new String("jif.principals.").length();
					ExtensionInfo.this.addPrincipal(name.substring(startIndex));
				}
				return returnNamed;
			}
		}

		protected JLiftOutputExtensionInfo(ExtensionInfo jifExtInfo) {
			super(jifExtInfo);
		}

		@Override
		protected void initTypeSystem() {
			try {
				LoadedClassResolver lr;
				lr = new RecordPrincipalsResolver(compiler, this, ExtensionInfo.this.getJifOptions().constructOutputExtClasspath(),
						compiler.loader(), true, getOptions().compile_command_line_only, getOptions().ignore_mod_times);
				ts.initialize(lr, this);
			}
			catch (SemanticException e) {
				throw new InternalCompilerError(
						"Unable to initialize type system.", e);
			}
		}
	}

	public ExtensionInfo() {
		super();
		this.jlext = new JLiftOutputExtensionInfo(this);
		this.internalCompilerInfo = InternalCompilerInfo.getInstance();

		theSummaryInfo = new SummaryConstraintInfo(typeSystem());
		theCallGraph = new CallGraph();

		callGraphGoal = scheduler().internGoal(new GenerateCallGraphGoal(scheduler(), theCallGraph, typeSystem(), nodeFactory()));
		summaryConstraintGoal = scheduler().internGoal(new SummaryGeneratedGoal());
		labelCheckAllFilesGoal = scheduler().internGoal(new SummaryLabelCheckGoal(null));
		//preconditionGoal = scheduler().internGoal(new PreconditionGoal(null, theCallGraph, typeSystem(), nodeFactory()));
		setupOutputGoal = false;
		fileForJobMap = new HashMap<String, Job>();
	}

	protected void addPrincipal(String principalName) {
		JLiftOptions.getInstance().principalNames.add(principalName);
	}

//	protected SummaryConstraintInfo theSummaryInfo = new SummaryConstraintInfo(typeSystem());
//	protected CallGraph theCallGraph = new CallGraph();

//	protected Goal callGraphGoal = scheduler().internGoal(new GenerateCallGraphGoal(scheduler(), theCallGraph, typeSystem(), nodeFactory()));
//	protected Goal summaryConstraintGoal = scheduler().internGoal(new SummaryGeneratedGoal());
//	protected Goal labelCheckAllFilesGoal = scheduler().internGoal(new SummaryLabelCheckGoal(null));
//	//protected Goal outputMethodSignaturesGoal = scheduler().internGoal(new OutputMethodSignaturesGoal(theCallGraph));
//	private final boolean setupOutputGoal = false;
//	private final Map<String,Job> fileForJobMap = new HashMap<String, Job>();

	protected SummaryConstraintInfo theSummaryInfo;// = new SummaryConstraintInfo(typeSystem());
	protected CallGraph theCallGraph;// = new CallGraph();

	protected Goal callGraphGoal;// = scheduler().internGoal(new GenerateCallGraphGoal(scheduler(), theCallGraph, typeSystem(), nodeFactory()));
	protected Goal summaryConstraintGoal; //= scheduler().internGoal(new SummaryGeneratedGoal());
	protected Goal labelCheckAllFilesGoal; // = scheduler().internGoal(new SummaryLabelCheckGoal(null));
	protected Goal dynamicLabelAnalysisGoal;
	protected Goal preconditionGoal;

	//protected Goal outputMethodSignaturesGoal = scheduler().internGoal(new OutputMethodSignaturesGoal(theCallGraph));
	private final boolean setupOutputGoal; // = false;
	private final Map<String,Job> fileForJobMap; // = new HashMap<String, Job>();

	@Override
	protected NodeFactory createNodeFactory() {
		return new JLiftNodeFactory_c();
	}

	@Override
	protected TypeSystem createTypeSystem()
	{
		return new JLiftTypeSystem_c(jlTypeSystem(), internalCompilerInfo);
	}

	@Override
	protected Options createOptions() {
		JLiftOptions theOptions = new JLiftOptions(this);


		theOptions.post_compiler = null;
		theOptions.output_ext = "jif";
		theOptions.serialize_type_info = false;
		theOptions.fully_qualified_names = false;
		//theOptions.runtimeExceptions = false;

		return theOptions;
	}

	@Override
	public String defaultFileExtension() {
		return "java";
	}

	@Override
	public String[] defaultFileExtensions() {
		return new String[] { "java", "jl", "jif" };
	}

	@Override
	protected Scheduler createScheduler() {
		return new JLiftScheduler(this, jlext);
	}

	@Override
	public Goal getCompileGoal(Job job) {
		JifScheduler jifScheduler = (JifScheduler)scheduler();

		if (JLiftOptions.getInstance().addDeclassifierSuggestions) {
			Goal declSuggester = jifScheduler.internGoal(new VisitorGoal(job,
					new DeclassifierPlacer(job, typeSystem(), nodeFactory(),
							JLiftOptions.getInstance().declSuggestionFile,
							JLiftOptions.getInstance().suggestionNumber)));

			jifScheduler.addGoal(declSuggester);
			try {
				jifScheduler.addPrerequisiteDependency(declSuggester, jifScheduler.Parsed(job));
				jifScheduler.addPrerequisiteDependency(jifScheduler.Disambiguated(job), declSuggester);
			} catch (CyclicDependencyException e1) {
				throw new InternalCompilerError(e1);
			}
		}
		if (JLiftOptions.getInstance().eraseLabels) {
			Goal labelEraser = jifScheduler.internGoal(new VisitorGoal(job, new LabelEraser(job, typeSystem(), nodeFactory())));
			Goal annotationsCounter = jifScheduler.internGoal(new VisitorGoal(job, new CountAnnotationsVisitor()));
			jifScheduler.addGoal(labelEraser);
			//jifScheduler.addGoal(annotationsCounter);
			try {
				jifScheduler.addPrerequisiteDependency(labelEraser, jifScheduler.Parsed(job));
				jifScheduler.addPrerequisiteDependency(jifScheduler.TypesInitialized(job), labelEraser);
				jifScheduler.addPrerequisiteDependency(annotationsCounter, jifScheduler.Parsed(job));
				jifScheduler.addPrerequisiteDependency(jifScheduler.TypesInitialized(job), annotationsCounter);
				jifScheduler.addPrerequisiteDependency(labelEraser, annotationsCounter);

				if (Report.should_report("labelerase", 2)) {
					Goal displayRemainingAnnotations = jifScheduler.internGoal(new VisitorGoal(job, new DisplayRemainingAnnotationsVisitor(job, ts, nf)));
					jifScheduler.addGoal(displayRemainingAnnotations);
					jifScheduler.addPrerequisiteDependency(displayRemainingAnnotations, labelEraser);
					jifScheduler.addPrerequisiteDependency(jifScheduler.TypesInitialized(job), displayRemainingAnnotations);
				}
			}
			catch (CyclicDependencyException e1) {
				throw new InternalCompilerError(e1);
			}
		}

		fileForJobMap.put(job.source().name(), job);

		List<Goal> l = new ArrayList<Goal>();

		// add not null check and precise classes check before exception checking
		l.add(jifScheduler.ReachabilityChecked(job));
		//l.add(jifScheduler.ConstantsChecked(job));

		l.add(jifScheduler.internGoal(new VisitorGoal(job, new NotNullChecker(job, ts, nf))));
		l.add(jifScheduler.internGoal(new VisitorGoal(job, new PreciseClassChecker(job, ts, nf))));
		if (JLiftOptions.getInstance().shouldUseProfiling())
			l.add(jifScheduler.internGoal(new VisitorGoal(job, new ProfilerVisitor(job, ts, nf))));
		l.add(jifScheduler.internGoal(this.callGraphGoal));
		l.add(((JLiftScheduler) jifScheduler).RuntimeExceptionsAdded());
		l.add(jifScheduler.ExceptionsChecked(job));
		l.add(jifScheduler.ExitPathsChecked(job));   // had to do add this manually here -- why?
		//l.add(jifScheduler.internGoal(new VisitorGoal(job, new NullPointerPreconditionDataFlow(job, ts, nf))));
		//l.add(jifScheduler.internGoal(new VisitorGoal(job, new NullPointerAnalysisVisitor(job, ts, nf, this.theCallGraph))));

		// add field label inference after exception checking.
		// TODO: do we want this?
		// ANSWER: maybe
		FieldLabelInferenceGoal fliGoal = jifScheduler.FieldLabelInference(job);
		l.add(fliGoal);

		if (JLiftOptions.getInstance().oldDynamicLabelAnalysis) {
			l.add(jifScheduler.internGoal(this.dynamicLabelAnalysisGoal));
		}

		// add summary constraint generation after FLI
		l.add(jifScheduler.internGoal(this.summaryConstraintGoal));

		// and then check the summaries
		// TODO: only one label checker should exist
		Goal g = this.labelCheckAllFilesGoal;

		if (JLiftOptions.getInstance().shouldGenerateJifCode()) {
			l.add(g);
			g = jifScheduler.internGoal(jifScheduler.CodeGenerated(job));
		}

		l.add(g);

		if (false) {
			g = jifScheduler.internGoal(this.preconditionGoal);
			l.add(g);
		}

		try {
			jifScheduler.addPrerequisiteDependencyChain(l);
		}
		catch (CyclicDependencyException e) {
			throw new InternalCompilerError(e);
		}

		return g;
	}

	public CallGraph getCallGraph() {
		return theCallGraph;
	}

	public SummaryConstraintInfo getSummaryInfo() {
		return theSummaryInfo;
	}

	public Job getJobForFile(String fileName) {
		return fileForJobMap.get(fileName);
	}

	public InternalCompilerInfo getInternalCompilerInfo() {
		return internalCompilerInfo;
	}

	@Override
	public Parser parser(Reader reader, FileSource source, ErrorQueue eq) {
		polyglot.lex.Lexer lexer = new jif.parse.Lexer_c(reader, source, eq);
		// TODO: needed to override array in original jif-ccs codebase --
		// do we need to do that here?
		polyglot.parse.BaseParser grm = new jlift.parse.Grm(lexer, (JifTypeSystem) ts, (JifNodeFactory) nf, eq);

		return new CupParser(grm, source, eq);

	}
}
