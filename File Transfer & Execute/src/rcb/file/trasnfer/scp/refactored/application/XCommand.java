package rcb.file.trasnfer.scp.refactored.application;

import java.io.File;

import rcb.file.trasnfer.scp.refactored.commandline.CommandLine;

/*
Copyright 2017 Remko Popma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import rcb.file.trasnfer.scp.refactored.commandline.CommandLine.Command;
import rcb.file.trasnfer.scp.refactored.commandline.CommandLine.Option;
import rcb.file.trasnfer.scp.refactored.commandline.CommandLine.Parameters;

/**
 * Demonstrates picocli subcommands. // picocli.Demo
 */
@Command(name = "XRemote", sortOptions = false, header = {
		"@|green xxxxx xxxxx  xxxxxxxx                                              xx                     |@",
		"@|green xxxxx xxxxx  xxxxxxxxx                                             xx                     |@",
		"@|green   xx   xx     xx    xxx                                            xx                     |@",
		"@|green    xx xx      xx     xx      xxxxx    xxx xxx xxxx     xxxxx     xxxxxxxxxx      xxxxx    |@",
		"@|green    xx xx      xx    xxx    xxxxxxxxx  xxxxxxxxxxxxx  xxxxxxxxx   xxxxxxxxxx    xxxxxxxxx  |@",
		"@|green     xxx       xxxxxxxx     xx     xx   xxx  xxx  xx  xx     xx     xx          xx     xx  |@",
		"@|green     xxx       xxxxxxx     xxxxxxxxxxx  xx   xx   xx xx       xx    xx         xxxxxxxxxxx |@",
		"@|green    xx xx      xx   xxx    xxxxxxxxxxx  xx   xx   xx xx       xx    xx         xxxxxxxxxxx |@",
		"@|green    xx xx      xx    xx    xx           xx   xx   xx xx       xx    xx         xx          |@",
		"@|green   xx   xx     xx    xxx    xx      xx  xx   xx   xx  xx     xx     xx     xx   xx      xx |@",
		"@|green xxxxx xxxxx  xxxxx   xxxx  xxxxxxxxxx xxxx  xxx  xx  xxxxxxxxx     xxxxxxxxx   xxxxxxxxxx |@",
		"@|green xxxxx xxxxx  xxxxx    xxx    xxxxxx   xxxx  xxx  xx    xxxxx        xxxxxx       xxxxxx   |@",
		"" }, description = { "", "XRemote usage Help.", }, optionListHeading = "@|bold %nOptions|@:%n", footer = { "",
				"Run without any options to see this help", "" })

public class XCommand implements Runnable {
	public static void main(String[] args) {
		CommandLine.run(new XCommand(), System.err, args);
	}

	@Option(names = { "-c", "--config" }, description = "Config File for Launching XRemote")
	private boolean showUsageForSubcommandGitCommit;

	@Option(names = { "-2",
			"--showUsageForMainCommand" }, description = "Shows usage help for a command with subcommands")
	private boolean showUsageForMainCommand;

	@Option(names = { "-3",
			"--showUsageForSubcommandGitStatus" }, description = "Shows usage help for the git-status subcommand")
	private boolean showUsageForSubcommandGitStatus;

	@Option(names = "--simple", description = "Show help for the first simple Example in the manual")
	private boolean showSimpleExample;

	@Option(names = "--mixed", hidden = true, description = "Show help with mixed Ansi colors and styles in description")
	private boolean showAnsiInDescription;

	@Option(names = { "-t", "--tests" }, description = "Runs all tests in this class")
	private boolean runTests;

	public void run() {

			if (showAnsiInDescription)           { showAnsiInDescription(); }
			if (showSimpleExample)               { showSimpleExampleUsage(); }

	}

	 private void showAnsiInDescription() {
	     @Command(description = "Custom @|bold,underline styles|@ and @|fg(red) colors|@.")
	     class AnsiDescription { }
	     CommandLine.usage(new AnsiDescription(), System.out);
	 }
	 
	 private void showSimpleExampleUsage() {
	     class Example {
	         @Option(names = { "-v", "--verbose" }, description = "Be verbose.")
	         private boolean verbose = false;

	         @Option(names = { "-h", "--help" }, help = true,
	                 description = "Displays this help message and quits.")
	         private boolean helpRequested = false;

	         @Parameters(arity = "1..*", paramLabel = "FILE", description = "File(s) to process.")
	         private File[] inputFiles;
	     }
	     CommandLine.usage(new Example(), System.out);
	 }
}