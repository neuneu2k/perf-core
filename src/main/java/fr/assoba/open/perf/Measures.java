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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;

@SuppressWarnings("unchecked")
public class Measures {

	private ConcurrentHashMap<String, Measure> map = new ConcurrentHashMap<String, Measure>();

	private Executor executor = Executors.newSingleThreadExecutor();

	private Disruptor<EventHolder> disruptor;
	private RingBuffer<EventHolder> ringBuffer;

	private long lastUpdate = 0l;
	private MeasureSaver measureSaver;

	public Measures() {
		disruptor = new Disruptor<EventHolder>(EventHolder.factory, executor,
				new MultiThreadedClaimStrategy(8192),
				new SleepingWaitStrategy());
		disruptor.handleEventsWith(new EventHandler<EventHolder>() {
			@Override
			public void onEvent(final EventHolder event, final long sequence,
					final boolean endOfBatch) throws Exception {
				final Measure m = event.event.getMeasure();
				m.histogram.add(event.event);
				m.events++;
				m.totalMicros += event.event.getMicros();

				if (endOfBatch) {
					final long now = System.currentTimeMillis();
					if (now > (lastUpdate + 5000)) {
						// Spin the buffers !
						save(now);
						m.clear();
						lastUpdate = now;
					}
				}
			}
		});
		ringBuffer = disruptor.start();
	}

	public Measure getMeasure(String name) {
		if (map.containsKey(name)) {
			return map.get(name);
		} else {
			Measure m = new Measure(this);
			map.put(name, m);
			return m;
		}
	}

	private static class EventHolder {
		Event event;

		public static Factory factory = new Factory();

		public static class Factory implements EventFactory<EventHolder> {
			@Override
			public EventHolder newInstance() {
				return new EventHolder();
			}

		}
	}

	void add(Event e) {
		long seq = ringBuffer.next();
		EventHolder h = ringBuffer.get(seq);
		h.event = e;
		ringBuffer.publish(seq);
	}

	public void save(long now) {
		for (String name : map.keySet()) {
			Measure m = map.get(name);
			MeasureVector vector = new MeasureVector();
			vector.setName(name);
			vector.setEvents(m.events);
			vector.setTotalMicros(m.totalMicros);
			vector.setStart(lastUpdate);
			vector.setEnd(now);
			vector.setHistogram(m.histogram);
			m.setPreviousMeasure(vector);
			if (measureSaver != null) {
				measureSaver.save(vector);
			}
		}
	}

	/**
	 * @return the measureSaver
	 */
	public final MeasureSaver getMeasureSaver() {
		return measureSaver;
	}

	/**
	 * @param measureSaver
	 *            the measureSaver to set
	 */
	public final void setMeasureSaver(MeasureSaver measureSaver) {
		this.measureSaver = measureSaver;
	}

}