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

import java.text.NumberFormat;

/**
 * Flyweight time elapsed event
 * 
 * @author jpujo@assoba.fr
 *
 */
public class Event {

	private long usStart;
	private long usEnd;

	private Measure measure;
	
	public Event(long startMs, long endMs) {
		usStart = startMs * 1000;
		usEnd= endMs * 1000;
	}

	public Event() {
		usStart = System.nanoTime() / 1000;
	}

	public void stop() {
		usEnd = System.nanoTime() / 1000;
	}

	static NumberFormat format = NumberFormat.getInstance();
	static {
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(1);
	}

	/**
	 * Utility function to format a number of microSeconds to String
	 * @param micros
	 * @return
	 */
	public static String toString(long micros) {
		if (micros < 1000000) {
			return format.format(micros / 1000.0f) + "ms";
		} else if (micros == Long.MAX_VALUE) {
			return "\u221E"; // Infinity
		} else {
			return format.format(micros / 1000000f) + "s";
		}
	}

	public String toString() {
		return toString(usEnd - usStart);
	}

	/**
	 * 
	 * @return elapsed time in seconds
	 */
	public float getTime() {
		return (usEnd - usStart) / 1000000f;
	}

	/**
	 * 
	 * @return elapsed time in microSeconds
	 */
	public long getMicros() {
		return usEnd - usStart;
	}

	/**
	 * @return the measure
	 */
	public final Measure getMeasure() {
		return measure;
	}

	/**
	 * @param measure the measure to set
	 */
	public final void setMeasure(Measure measure) {
		this.measure = measure;
	}

}
