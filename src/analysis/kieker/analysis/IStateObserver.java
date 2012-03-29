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

package kieker.analysis;

import kieker.analysis.AnalysisController.STATE;

/**
 * This interface can be used for observers which want to get notified about state changes of an analysis controller.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
public interface IStateObserver {

	/**
	 * This method will be called for every update of the state.
	 * 
	 * @param controller
	 *            The controller which updated its state.
	 * @param state
	 *            The new state of the given controller.
	 */
	public void update(final AnalysisController controller, final STATE state);

}