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

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.optimization.MultivariateMultiStartOptimizer;
import org.apache.commons.math3.optimization.fitting.CurveFitter;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;

import com.google.common.base.Joiner;

public class GenDist {

	public static void main(String[] args) {
		// Magic number !
		LogNormalDistribution distribution = new LogNormalDistribution(6.73,
				2.12);
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < 1023; i++) {
			double d = (1.0 * i) / 1023.0;
			list.add((long) (100 * distribution.inverseCumulativeProbability(d)));
		}
		System.out.print(Joiner.on(",").join(list));

	}
}
