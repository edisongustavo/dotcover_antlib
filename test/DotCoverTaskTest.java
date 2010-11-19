import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;
import org.junit.Test;

import com.egmas.ant.tasks.dotcover.DotCoverTask;
import com.egmas.ant.tasks.dotcover.DotCoverTask.NamedElement;
import com.egmas.ant.tasks.dotcover.configuration.ContentElement;
import com.egmas.ant.tasks.dotcover.configuration.coverage.ExcludeFilters;
import com.egmas.ant.tasks.dotcover.configuration.coverage.FilterEntry;
import com.egmas.ant.tasks.dotcover.configuration.coverage.IncludeFilters;

public class DotCoverTaskTest {

	@Test
	public void a() {
		DotCoverTask task = new DotCoverTask();
		task.setProject(createProject());

		task.addTestAssembly(new NamedElement(
				"nunit_temp/Barauna.Math.Tests.dll"));

		task.setExecPath("Z:/projetos/barauna/build/tools/dotCover/Bin/dotCover.exe");
		task.setXmlout("nunit_temp/coverage.xml");
		task.setNunitPath("Z:/projetos/barauna/build/tools/nunit/bin/net-2.0/nunit-console-x86.exe");
		task.setSnapshotPath("nunit_temp/snapshot.tmp");
		task.setTempDir("nunit_temp");

		task.addIncludeFilters(includeFilters());
		task.addExcludeFilters(excludeFilters());

		task.execute();
	}

	private Project createProject() {
		return new Project();
	}

	private ExcludeFilters excludeFilters() {
		ExcludeFilters ret = new ExcludeFilters();
		FilterEntry filterEntry = filterEntry();
		filterEntry.addConfiguredModuleMask(new ContentElement(
				"Barauna.*.Tests"));
		ret.addFilterEntry(filterEntry);

		return ret;
	}

	private IncludeFilters includeFilters() {
		IncludeFilters ret = new IncludeFilters();
		ret.addFilterEntry(filterEntry());
		return ret;
	}

	private FilterEntry filterEntry() {
		FilterEntry ret = new FilterEntry();
		ret.addConfiguredModuleMask(new ContentElement("Barauna*"));
		ret.addConfiguredClassMask(new ContentElement("*"));
		ret.addConfiguredFunctionMask(new ContentElement("*"));
		return ret;
	}
}
