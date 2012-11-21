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

import java.util.ArrayList;

public class Measure {

	Histogram histogram = new Histogram();
	long totalMicros = 0l;
	long events = 0l;
	ArrayList<Event> outliers = new ArrayList<Event>(10);
	private MeasureVector previousMeasure = new MeasureVector();
	private Measures parent;

	public Measure(Measures parent) {
		this.parent = parent;
	}

	void clear() {
		histogram.clear();
		totalMicros = 0l;
		events = 0l;
		outliers.clear();
	}

	public void add(Event e) {
		e.setMeasure(this);
		parent.add(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Measure [histogram=" + histogram + ", totalMicros="
				+ totalMicros + ", events=" + events + "]";
	}

	/**
	 * @return the previousMeasure
	 */
	public final MeasureVector getPreviousMeasure() {
		return previousMeasure;
	}

	/**
	 * @param previousMeasure
	 *            the previousMeasure to set
	 */
	public final void setPreviousMeasure(MeasureVector previousMeasure) {
		this.previousMeasure = previousMeasure;
	}

}
