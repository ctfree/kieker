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

package kieker.test.monitoring.util;

import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.DummyWriter;

/**
 * A writer that simply counts the number of records of type DummyRecord received.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class DummyRecordCountWriter extends DummyWriter {
	private final AtomicInteger numDummyRecords = new AtomicInteger(0);

	public DummyRecordCountWriter(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * @return the number of records
	 */
	public final int getNumDummyRecords() {
		return this.numDummyRecords.get();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof EmptyRecord) {
			this.numDummyRecords.incrementAndGet();
		}
		return super.newMonitoringRecord(record);
	}
}