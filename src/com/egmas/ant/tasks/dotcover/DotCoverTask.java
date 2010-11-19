package com.egmas.ant.tasks.dotcover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;

import com.egmas.ant.tasks.dotcover.configuration.coverage.CoverageParams;
import com.egmas.ant.tasks.dotcover.configuration.coverage.ExcludeFilters;
import com.egmas.ant.tasks.dotcover.configuration.coverage.Filters;
import com.egmas.ant.tasks.dotcover.configuration.coverage.IncludeFilters;
import com.egmas.ant.tasks.dotcover.configuration.merge.MergeParams;
import com.egmas.ant.tasks.dotcover.configuration.merge.Source;
import com.egmas.ant.tasks.dotcover.configuration.report.ReportParams;
import com.egmas.ant.tasks.dotcover.utils.FileUtils;
import com.egmas.ant.tasks.dotcover.xml.XStream;

/**
 * Executes the dotcover tool given the assemblies
 * 
 * @author emuenz
 * 
 */
public class DotCoverTask extends Task {

	public DotCoverTask() {
		xstream.alias("string", String.class);
	}

	/**
	 * The <IncludeFilters>
	 * <p>
	 * Optional
	 */
	IncludeFilters includeFilters;

	public void addIncludeFilters(IncludeFilters includeFilters) {
		this.includeFilters = includeFilters;
	}

	/**
	 * The path where we can find the dotCover.exe file
	 * <p>
	 * Optional. If not specified, will try to find at the PATH
	 */
	private String execPath;

	public void setExecPath(String var) {
		this.execPath = var;
	}

	/**
	 * Adds a test assembly by name.
	 */
	public void addTestAssembly(NamedElement a) {
		testAssemblies.add(a);
	}

	/**
	 * The test assemblies. They are specified like:
	 * 
	 * <pre>
	 * 		<testassembly name="" />
	 * </pre>
	 * 
	 * <p>
	 * Required at least one element
	 */
	private List<NamedElement> testAssemblies = new ArrayList<DotCoverTask.NamedElement>();

	/**
	 * The WorkingDir at the coverage step
	 */
	private String workingDir;

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	/**
	 * The ExcludeFilters part at the coverage step
	 * <p>
	 * Optional
	 */
	private ExcludeFilters excludeFilters;

	public void addExcludeFilters(ExcludeFilters excludeFilters) {
		this.excludeFilters = excludeFilters;
	}

	@Override
	public void execute() throws BuildException {
		if (testAssemblies.isEmpty())
			throw new BuildException(
					"You must specify at least one test assembly.");

		if (xmlout == null)
			throw new BuildException(
					"You must specify the path to the output xml file with 'xmlout'");

		if (nunitPath == null)
			throw new BuildException(
					"You must specify the path to the test framework executable with 'testingFrameworkPath'");

		fixArguments();

		try {
			coverageStep();
			mergeStep();
			reportStep();

			storeSnapshot();
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	private void storeSnapshot() throws IOException {
		if (snapshotPath == null)
			return;

		File file = new File(tempDir + "/output_merge.xml");
		String contents = FileUtils.read(file);

		if (contents == null) {
			log("Can't find the file (" + file.getPath()
					+ "), won't rename the snapshot", Project.MSG_WARN);
			return;
		}

		Pattern p = Pattern.compile("<string>(.+)</string>");
		Matcher matcher = p.matcher(contents);
		while (matcher.find()) {
			String filename = matcher.group(1);
			filename.trim();

			log(String.format("Renaming %s to %s", filename, snapshotPath),
					Project.MSG_VERBOSE);
			new File(filename).renameTo(new File(snapshotPath));
		}
	}

	private void reportStep() throws IOException {
		log("Report step!");
		writeReportFile();
		executeDotCover(DotcoverStep.Report, getReportXmlPath());
	}

	private String getReportXmlPath() {
		return tempDir + "/dotcover_report.xml";
	}

	private void writeReportFile() throws IOException {
		ReportParams params = new ReportParams();
		params.setSource(tempDir + "/output_merge.xml");
		params.setOutput(xmlout);

		params.alias(xstream);

		String xmlContents = xstream.toXML(params);
		FileUtils.write(new File(tempDir, "dotcover_report.xml"), xmlContents);
	}

	private void mergeStep() throws IOException {
		System.out.println("Merge step!");
		writeMergeFile();
		executeDotCover(DotcoverStep.Merge, getMergeXmlPath());
	}

	private String getMergeXmlPath() {
		return tempDir + "/dotcover_merge.xml";
	}

	private void coverageStep() throws IOException {
		System.out.println("Coverage step!");
		writeCoverageFiles();
		executeDotCover(DotcoverStep.Cover, getCoverageXmlPath());
	}

	private enum DotcoverStep {
		Cover, Merge, Report
	}

	private void executeDotCover(DotcoverStep step, String xmlFile) {
		ExecTask exec = new ExecTask(this);
		exec.setProject(getProject());
		exec.setExecutable(execPath);
		exec.setFailonerror(true);
		exec.setFailIfExecutionFails(true);
		exec.createArg().setValue(step.name().toLowerCase());
		exec.createArg().setValue(xmlFile);
		exec.execute();
	}

	private void writeMergeFile() throws IOException {

		MergeParams params = new MergeParams();
		params.setOutput(tempDir + "/output_merge.xml");
		// TODO: set the tempdir to be the dir where the snapshots will be saved
		params.setTempDir(tempDir);
		params.setSource(new Source(tempDir + "/output_coverage.xml"));

		params.alias(xstream);

		String xmlContents = xstream.toXML(params);
		FileUtils.write(new File(tempDir, "dotcover_merge.xml"), xmlContents);
	}

	private String getCoverageXmlPath() {
		return tempDir + "/dotcover_coverage.xml";
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	/**
	 * The file that will have the snapshot that is generated by dotCover. If
	 * specified, will rename the dotCover file to this path
	 * <p>
	 * Optional
	 */
	private String snapshotPath;

	public void setSnapshotPath(String snapshotPath) {
		this.snapshotPath = snapshotPath;
	}

	private void fixArguments() {
		if (tempDir == null)
			tempDir = System.getProperty("java.io.tmpdir");
		tempDir = fixpath(tempDir);

		if (workingDir == null)
			workingDir = ".";

		if (execPath == null)
			execPath = "";
	}

	private String fixpath(String path) {
		if (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		return path;
	}

	private XStream xstream = new XStream();

	/**
	 * Write the XML for the coverage step
	 * 
	 * @throws IOException
	 */
	private void writeCoverageFiles() throws IOException {
		CoverageParams params = new CoverageParams();
		params.setExecutable(nunitPath);
		params.setArguments(assembliesAsString());
		params.setWorkingDir(workingDir);
		params.setOutput(tempDir + "/output_coverage.xml");
		params.setTempDir(tempDir);

		params.setFilters(getFilters());

		params.alias(xstream);

		String xmlContents = xstream.toXML(params);
		FileUtils
				.write(new File(tempDir, "dotcover_coverage.xml"), xmlContents);
	}

	private String assembliesAsString() {
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < testAssemblies.size(); i++) {
			if (i != 0)
				ss.append(" ");
			ss.append(testAssemblies.get(i).toString());
		}
		return ss.toString();
	}

	private Filters getFilters() {
		Filters ret = new Filters();
		ret.setIncludeFilters(includeFilters);
		ret.setExcludeFilters(excludeFilters);
		return ret;
	}

	/**
	 * Where the final XML file will be stored (generated by dotCover)
	 * <p>
	 * Required
	 */
	private String xmlout;

	public void setXmlout(String var) {
		this.xmlout = var;
	}

	/**
	 * Where the created XML files will be placed
	 */
	private String tempDir;

	/**
	 * The path to the nunit-console.exe file
	 * <p>
	 * Required
	 */
	private String nunitPath;

	public void setNunitPath(String var) {
		this.nunitPath = var;
	}

	public static class NamedElement {
		private String name;

		public NamedElement() {
		}

		public NamedElement(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String s) {
			name = s;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}
