/*******************************************************************************
 * Copyright 2012 assoba.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package fr.assoba.open.perf;

/**
 * Save {@link MeasureVector} to persistant storage, or send it to
 * a central server, or give it to your dog
 */
public interface MeasureSaver {

	/**
	 * Save the {@link MeasureVector} provided to persistant storage, or send it
	 * to a central server, or give it to your dog
	 * 
	 * Whatever you do, do it fast or do it in another thread, this method
	 * blocks aggregation !
	 * 
	 * @param measureVector
	 */
	public abstract void save(MeasureVector measureVector);
}
