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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public final class MeasureVector implements Externalizable {
	String name;
	long start;
	long end;
	long events;
	long totalMicros;
	Histogram histogram = new Histogram();

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the start
	 */
	public final long getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public final void setStart(long start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public final long getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public final void setEnd(long end) {
		this.end = end;
	}

	/**
	 * @return the events
	 */
	public final long getEvents() {
		return events;
	}

	/**
	 * @param events
	 *            the events to set
	 */
	public final void setEvents(long events) {
		this.events = events;
	}

	/**
	 * @return the totalMicros
	 */
	public final long getTotalMicros() {
		return totalMicros;
	}

	/**
	 * @param totalMicros
	 *            the totalMicros to set
	 */
	public final void setTotalMicros(long totalMicros) {
		this.totalMicros = totalMicros;
	}

	/**
	 * @return the histogram
	 */
	public final Histogram getHistogram() {
		return histogram;
	}

	/**
	 * @param histogram
	 *            the histogram to set
	 */
	public final void setHistogram(Histogram histogram) {
		this.histogram = histogram;
	}

	public MeasureVector merge(MeasureVector o) {
		if (end == o.start) {
			if (o.name.equals(name)) {
				throw new IllegalArgumentException(
						"Merged vectors have same name !");
			}
			MeasureVector result = new MeasureVector();
			result.name = name;
			result.start = start;
			result.end = o.end;
			result.totalMicros = totalMicros + o.totalMicros;
			result.events = events + o.events;
			return result;
		} else {
			throw new IllegalArgumentException(
					"Merged vectors should be contiguous in time !");
		}
	}

	public byte[] store() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(baos);
			writeExternal(objectOutputStream);
			objectOutputStream.close();
		} catch (IOException e) {
		}
		return baos.toByteArray();
	}

	public void read(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(bais);
			readExternal(objectInputStream);
			objectInputStream.close();
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		name = in.readUTF();
		start = in.readLong();
		end = in.readLong();
		events = in.readLong();
		totalMicros = in.readLong();
		int index = 0;
		while (index < 1024) {
			long l = readVarint(in);
			if (l == 0) {
				long size = readVarint(in);
				for (int i = 0; i < size; i++) {
					histogram.data[index + i] = 0;
				}
				index += size;
			} else {
				histogram.data[index] = l;
				index++;
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
		out.writeLong(start);
		out.writeLong(end);
		out.writeLong(events);
		out.writeLong(totalMicros);
		int zeros = 0;
		for (long l : histogram.data) {
			if (l == 0) {
				zeros++;
			} else {
				if (zeros > 0) {
					// Write RLE 0z, doubles 00 inside data, RLE outside
					writeVarint(0, out);
					writeVarint(zeros, out);
					zeros = 0;
				}
				writeVarint(l, out);
			}
		}
		if (zeros > 0) {
			// Write RLE 0z, doubles 00 inside data, RLE outside
			writeVarint(0, out);
			writeVarint(zeros, out);
			zeros = 0;
		}
	}

	public void writeVarint(long value, ObjectOutput out) throws IOException {
		while (true) {
			if ((value & ~0x7FL) == 0) {
				out.writeByte((int) value);
				return;
			} else {
				out.writeByte((int) (value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	public long readVarint(ObjectInput in) throws IOException {
		int shift = 0;
		long result = 0;
		while (shift < 64) {
			final byte b = in.readByte();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw new IOException("Illegal Varint Encoding");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (end ^ (end >>> 32));
		result = prime * result + (int) (events ^ (events >>> 32));
		result = prime * result + Arrays.hashCode(histogram.data);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (start ^ (start >>> 32));
		result = prime * result + (int) (totalMicros ^ (totalMicros >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasureVector other = (MeasureVector) obj;
		if (end != other.end)
			return false;
		if (events != other.events)
			return false;
		if (!Arrays.equals(histogram.data, other.histogram.data))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (start != other.start)
			return false;
		if (totalMicros != other.totalMicros)
			return false;
		return true;
	}

}
