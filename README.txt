dotCover antlib
================
A project to run JetBrains dotCover (http://www.jetbrains.com/dotcover/) from ANT (not NANT)

This ANT task was written with dotCover 1.0 in mind, if something changes, I can't
guarantee it will work properly.


Why?
================
Because dotCover uses xml files to read the configuration, which makes it a little bit
problematic since the configuration for the coverage is not place in one place (at the
build.xml) which makes maintenance a little bit harder.

Please note that I am in no way involved with JetBrains and it's dotCover product except
for being a user of the tool.
JetBrains and me are NOT involved in any way.

This project was created because I had a specific problem and needed to fix it. I'm sharing
the solution with the community so it can solve more problems.


Building
================
Just type ANT (assuming ANT is in your path) at the root dir of the project.

It should build and output the jar at the out dir.
 
How to use?
================
In your build.xml:

<!-- Import the task -->
<taskdef resource="com/egmas/ant/tasks/dotcover/antlib.xml">
	<classpath>
		<pathelement location="build/antlib/dotcover_antlib.jar" />
	</classpath>
</taskdef>

<!-- At your coverage target -->
<dotCover workingDir="path/to/test_assemblies" execPath="path/to/dotCover.exe" xmlout="path/to/coverage-dotcover.xml" nunitPath="path/to/nunit-console-x86.exe">
	<IncludeFilters>
		<FilterEntry>
			<ModuleMask>ProjectNamespace*</ModuleMask>
			<ClassMask>*</ClassMask>
			<FunctionMask>*</FunctionMask>
		</FilterEntry>
	</IncludeFilters>

	<ExcludeFilters>
		<FilterEntry>
			<ModuleMask>ProjectNamespace.*.Tests</ModuleMask>
			<ClassMask>*</ClassMask>
			<FunctionMask>*</FunctionMask>
		</FilterEntry>
	</ExcludeFilters>

	<testassembly name="ProjectNamespace.Math.Tests.dll" />
	<testassembly name="ProjectNamespace.Simulator.Tests.dll" />
</dotCover>

This requires that you provide the correct paths to dotCover.exe and nunit-console-x86.exe

Task documentation
================
<dotCover
	workingDir="" -> Required. What will be written at the WorkingDir section at the Coverage step
	execPath=""   -> Required. The path to dotCover.exe
	xmlout=""	  -> Required. The full path to the xml coverage file which will be written. It's the <Output> at the Report step
	nunitPath=""  -> Required. The path to the nunit runner (usually nunit-console-x86.exe). It's the <Executable> at the Coverage step
	tempDir=""	  -> Optional. The temporary dir. Uses the system temp dir if not specified
	>
	<testassembly name="ProjectTest.dll"> -> At least one element of <testassembly> type is required
	
	<IncludeFilters> and <ExcludeFilters> -> Optional. These are the same as the dotCover xml, see their documentation for more information
</dotCover>

Internals
================
If you run dotCover.exe from the command line, you'll notice that there are three steps:

- Coverage
- Merge
- Report

What this task does is writing a XML file for each step then run dotCover for each step
with that XML file in mind.



What's missing?
================
- Add MSTest support (I don't use MSTest, so I'm probably not adding it in the near future, but it should be very easy)
- Add support to run the test assemblies on their own directory by setting the WorkingDir at the coverage step

License
================
This software is provided AS-IS and I, in no way will answer for any damage caused by it.

I like the Apache License 2.0, so I'm licensing the software with this license.

I'll paste what should go at the top of every source file (even though I won't go into the
trouble of actually pasting this in every source file)

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */