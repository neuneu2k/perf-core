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

import org.junit.Test;

public class MeasuresTest {

	@Test
	public void test() {
		Measures measures=new Measures();
		Measure m = measures.getMeasure("toto");
		measures.setMeasureSaver(new MeasureSaver() {
			
			@Override
			public void save(MeasureVector measureVector) {
				byte[] data=measureVector.store();
				System.out.println("Compressed data size :"+data.length);
			}
		});
		for(int j=0;j<50000;j++) {
			Event event = new Event();
			double rand = 0;
			int iters = (int) (100000.0 * Math.random());
			for (int i = 0; i < iters; i++) {
				rand += Math.random();
			}
			event.stop();
			m.add(event);
		}
	}

}
