/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.traceAnalysis.filter.traceWriter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractInvalidExecutionTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class InvalidExecutionTraceWriterFilter extends AbstractInvalidExecutionTraceProcessingFilter {

	public static final String CONFIG_OUTPUT_FN = "outputFn";
	public static final String INVALID_EXECUTION_TRACES_INPUT_PORT_NAME = "newEvent";
	private static final Log LOG = LogFactory.getLog(InvalidExecutionTraceWriterFilter.class);

	private static final String ENCODING = "UTF-8";

	private final String outputFn;
	private final BufferedWriter ps;

	public InvalidExecutionTraceWriterFilter(final Configuration configuration)
			throws IOException {
		super(configuration);
		this.outputFn = configuration.getStringProperty(InvalidExecutionTraceWriterFilter.CONFIG_OUTPUT_FN);
		this.ps = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFn), InvalidExecutionTraceWriterFilter.ENCODING));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		this.stdOutPrintln("Wrote " + numTraces + " execution trace artifact" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				InvalidExecutionTraceWriterFilter.LOG.error("IOException while terminating", ex);
			}
		}
	}

	@Override
	public String getInvalidExecutionTraceInputPortName() {
		return InvalidExecutionTraceWriterFilter.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME;
	}

	@InputPort(
			name = InvalidExecutionTraceWriterFilter.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME,
			description = "Invalid Execution traces", eventTypes = { InvalidExecutionTrace.class })
	public void newEvent(final Object obj) {
		final InvalidExecutionTrace et = (InvalidExecutionTrace) obj;
		try {
			InvalidExecutionTraceWriterFilter.this.ps.append(et.getInvalidExecutionTraceArtifacts().toString());
			InvalidExecutionTraceWriterFilter.this.reportSuccess(et.getInvalidExecutionTraceArtifacts().getTraceId());
		} catch (final IOException ex) {
			InvalidExecutionTraceWriterFilter.this.reportError(et.getInvalidExecutionTraceArtifacts().getTraceId());
			InvalidExecutionTraceWriterFilter.LOG.error("IOException", ex);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(InvalidExecutionTraceWriterFilter.CONFIG_OUTPUT_FN, "");

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(InvalidExecutionTraceWriterFilter.CONFIG_OUTPUT_FN, this.outputFn);

		return configuration;
	}

}