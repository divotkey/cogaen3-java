/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2011 Roman Divotkey

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */

package org.cogaen.util;

import java.util.Arrays;

/**
 * <p>A multiset container (also known as <em>bag</em>) that allows to be iterated and modified at the same time.
 * It is save to add and remove elements while iterating this bag. However this container does not follow the 
 * Java Colletiosn Framework API.</p>
 * 
 * <p>Another limitation is that the sequence of inserted elements might not be preserved during different 
 * iterations.</p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * Bag<Integer> myBag = new Bag<Integer>();
 * myBag.add(1);
 * myBag.add(2);
 * myBag.add(3);
 * 
 * for (myBag.reset(); myBag.hasNext(); ) {
 *     System.out.println("element: " + myBag.next());
 * }
 * </pre>
 * </p>
 * 
 *
 * @param <E> the type of elements maintained by this bag
 * @author Roman Divotkey
 */
public class Bag<E> {

	private static final int DEFAULT_CAPACITY = 10;
	private Object[] elements;
	private int size;
	private int remaining;
	private int pos;
	private boolean elementRemoved = false;
	
	public Bag(int initialCapacity) {
		this.elements = new Object[initialCapacity];
	}
	
	public Bag() {
		this(DEFAULT_CAPACITY);
	}

	public int size() {
		return this.size;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public boolean add(E e) {
		int length = this.elements.length;
		for (int i = this.pos; i < length; ++i) {
			if (this.elements[i] == null) {
				this.elements[i] = e;
				++this.size;
				++this.remaining;
				return true;
			}
		}

		ensureCapacity(length + 1);
		this.elements[length] = e;
		++this.size;
		++this.remaining;
		return true;
	}
	
	public boolean contains(E e) {
		return find(e) != -1;
	}
		
	public boolean remove(E e) {
		int idx = find(e);
		if (idx != -1) {
			this.elements[idx] = null;
			--this.size;
			this.elementRemoved = true;
			if (idx >= this.pos) {
				--this.remaining;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void reset() {
		this.pos = 0;
		this.remaining = this.size;

		if (this.elementRemoved) {
			optimize();
		}
	}
	
	private void optimize() {
		int length = this.elements.length;
		int cnt = 0;
		int idx = length - 1;
		
		for (int i = 0; i < length && cnt < this.size; ++i) {
			if (this.elements[i] == null) {
				while (this.elements[idx] == null) {
					--idx;
				}
				this.elements[i] = this.elements[idx];
				this.elements[idx--] = null;
			}
			++cnt;
		}
	}
	
	public boolean hasNext() {
		return this.remaining > 0;
	}
	
	public E next() {
		if (this.remaining <= 0) {
			return null;
		}
		
		while (this.elements[pos] == null) {
			++pos;
		}
		
		--this.remaining;
		
		@SuppressWarnings("unchecked")
		E result = (E) this.elements[this.pos++];
		return result;
	}
	
	private int find(E e) {
		int size = this.elements.length;
		for (int i = 0; i < size; ++i) {
			if (e.equals(this.elements[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	private void ensureCapacity(int minCapacity) {
		int oldCapacity = this.elements.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			
			this.elements = Arrays.copyOf(this.elements, newCapacity);
		}
	}
}
