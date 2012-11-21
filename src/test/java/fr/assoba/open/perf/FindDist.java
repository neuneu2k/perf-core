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

import org.apache.commons.math3.distribution.LogNormalDistribution;

public class FindDist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (double s = 5; s < 9; s = s + 0.01) {
			for (double c = 0.5; c < 5; c = c + 0.01) {
				LogNormalDistribution distribution = new LogNormalDistribution(
						s, c);
				if ((distribution.cumulativeProbability(200) > .248)
						&& (distribution.cumulativeProbability(200) < .252)) {
					System.out.println(s + ":" + c);
					System.out.println("\t"
							+ distribution.cumulativeProbability(200));
					System.out.println("\t"
							+ distribution.cumulativeProbability(600));
					System.out.println("\t"
							+ distribution.cumulativeProbability(1000 * 1000));
				}
			}
		}
	}

}
