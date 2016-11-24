/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.forward.CountingFilter;

import teetime.framework.test.StageTester;

/**
 * This test is for the class {@link CountingDisplayFilter}.
 *
 * @author Nils Christian Ehmke, Jan Waller, Lars Erik Bluemke
 *
 * @since 1.6
 */
public class TestCountingFilter {

	private CountingFilter countingFilter = null;
	private List<Object> testElements = null;

	@Before
	public void initializeNewFilter() {
		this.countingFilter = new CountingFilter();
		this.testElements = new ArrayList<Object>();
	}

	/**
	 * A simple test for the counting filter.
	 */
	@Test
	public void testNormal() {
		this.testElements.add(new Object());
		this.testElements.add(new Object());
		this.testElements.add(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		StageTester.test(this.countingFilter).and().send(this.testElements).to(this.countingFilter.getInputPort()).start();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	/**
	 * A simple test for the counting filter using objects of different classes.
	 */
	@Test
	public void testDifferentClasses() {
		this.testElements.add(Long.valueOf(10));
		this.testElements.add(new Object());
		this.testElements.add("");
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		StageTester.test(this.countingFilter).and().send(this.testElements).to(this.countingFilter.getInputPort()).start();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}
}