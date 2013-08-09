/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.flow.trace.concurrency;

import kieker.common.record.flow.trace.AbstractTraceEvent;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class SplitEvent extends AbstractTraceEvent {
	private static final long serialVersionUID = -4454625562107999414L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
	};

	/**
	 * This constructor uses the given parameters to initialize the fields of this record.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 */
	public SplitEvent(final long timestamp, final long traceId, final int orderIndex) {
		super(timestamp, traceId, orderIndex);
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public SplitEvent(final Object[] values) {
		super(values, TYPES); // values[0..2]
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public final Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
}
