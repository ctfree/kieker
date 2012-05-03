/***************************************************************************
 * Copyright 2012 by
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

package kieker.test.analysis.junit.plugin.filter.trace;

import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.analysis.plugin.filter.trace.TraceIdFilter;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;

import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Andre van Hoorn
 */
public class TestTraceIdFilter {

	private static final String SESSION_ID = "sv7w1ifhK";
	private static final String HOSTNAME = "srv098";

	public TestTraceIdFilter() {
		// empty default constructor
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link AbstractTraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> is NOT passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertIgnoreTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final long firstTimestamp = 42353; // any number fits
		final long traceIdNotToPass = 11L; // (must NOT be element of idsToPass)

		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(1 + traceIdNotToPass);
		idsToPass.add(2 + traceIdNotToPass);

		final Configuration filterConfig = new Configuration();
		filterConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.FALSE.toString());
		filterConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, Configuration.toProperty(idsToPass.toArray(new Long[idsToPass.size()])));
		final TraceIdFilter filter = new TraceIdFilter(filterConfig);
		final SimpleSinkFilter<AbstractTraceEvent> sinkPlugin = new SimpleSinkFilter<AbstractTraceEvent>(new Configuration());
		final AnalysisController controller = new AnalysisController();

		final TraceEventRecords traceEvents =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdNotToPass, TestTraceIdFilter.SESSION_ID,
						TestTraceIdFilter.HOSTNAME);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		for (final AbstractTraceEvent e : traceEvents.getTraceEvents()) {
			Assert.assertTrue("Testcase invalid", !idsToPass.contains(e.getTraceId()));
			filter.inputTraceEvent(e);
		}

		if (!sinkPlugin.getList().isEmpty()) {
			final long passedId = sinkPlugin.getList().get(0).getTraceId();
			Assert.fail("Filter passed trace with ID " + passedId + " although traceId not element of " + idsToPass);
		}
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link AbstractTraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> IS passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertPassTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final long firstTimestamp = 53222; // any number fits
		final long traceIdToPass = 11L; // (must be element of idsToPass)

		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(0 + traceIdToPass);
		idsToPass.add(1 + traceIdToPass);

		final Configuration filterConfig = new Configuration();
		filterConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.FALSE.toString());
		filterConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, Configuration.toProperty(idsToPass.toArray(new Long[idsToPass.size()])));
		final TraceIdFilter filter = new TraceIdFilter(filterConfig);
		final SimpleSinkFilter<AbstractTraceEvent> sinkPlugin = new SimpleSinkFilter<AbstractTraceEvent>(new Configuration());
		final AnalysisController controller = new AnalysisController();

		final TraceEventRecords trace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdToPass, TestTraceIdFilter.SESSION_ID,
				TestTraceIdFilter.HOSTNAME);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		for (final AbstractTraceEvent e : trace.getTraceEvents()) {
			Assert.assertTrue("Testcase invalid", idsToPass.contains(e.getTraceId()));
			filter.inputTraceEvent(e);
			Assert.assertTrue("Expected event " + e + " to pass the filter", sinkPlugin.getList().contains(e));
		}
		// Somehow redundant but records MIGHT be generated randomly ;-)
		Assert.assertEquals("Unexpected number of output records", sinkPlugin.getList().size(), trace.getTraceEvents().length);
	}

	/**
	 * Given a TraceIdFilter that passes all traceIds, assert that an {@link AbstractTraceEvent} object <i>exec</i> is passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertPassTraceIdWhenPassAll() throws IllegalStateException, AnalysisConfigurationException {
		final long firstTimestamp = 53222; // any number fits
		final long traceIdToPass = 11L; // (must be element of idsToPass)

		final Configuration filterConfig = new Configuration();
		filterConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.TRUE.toString()); // i.e., pass all
		final TraceIdFilter filter = new TraceIdFilter(filterConfig);
		final SimpleSinkFilter<AbstractTraceEvent> sinkPlugin = new SimpleSinkFilter<AbstractTraceEvent>(new Configuration());
		final AnalysisController controller = new AnalysisController();

		final TraceEventRecords trace =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdToPass, TestTraceIdFilter.SESSION_ID, TestTraceIdFilter.HOSTNAME);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		for (final AbstractTraceEvent e : trace.getTraceEvents()) {
			filter.inputTraceEvent(e);
			Assert.assertTrue("Expected event " + e + " to pass the filter", sinkPlugin.getList().contains(e));
		}
		// Somehow redundant but records MIGHT be generated randomly ;-)
		Assert.assertEquals("Unexpected number of output records", sinkPlugin.getList().size(), trace.getTraceEvents().length);
	}
}