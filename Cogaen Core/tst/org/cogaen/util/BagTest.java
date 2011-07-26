package org.cogaen.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BagTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSize() {
		Bag<Integer> myList = new Bag<Integer>();
		
		assertTrue(myList.size() == 0);
		for (int i = 0; i < 5; ++i) {
			myList.add(i);
			assertTrue(myList.size() == i + 1);
		}
		
		for (int i = 4; i >= 0; --i) {
			myList.remove(i);
			assertTrue(myList.size() == i);
		}
		assertTrue(myList.size() == 0);
	}

	@Test
	public void testContains() {
		Bag<Integer> myList = new Bag<Integer>();
		
		assertFalse(myList.contains(0));
		for (int i = 0; i < 5; ++i) {
			myList.add(i);
			assertTrue(myList.contains(i));
			for (int j = i + 1; j < 5; ++j) {
				assertFalse(myList.contains(j));
			}
		}
		
		for (int i = 0; i < 5; ++i) {
			myList.remove(i);
			assertFalse(myList.contains(i));
			for (int j = i + 1; j < 5; ++j) {
				assertTrue(myList.contains(j));
			}		
		}
	}
	
	@Test
	public void testRemove() {
		Bag<Integer> myList = new Bag<Integer>(2);		
		
		myList.add(1);
		myList.add(2);
		
		myList.reset();
		myList.next();
		assertTrue(myList.remove(2));
		assertFalse(myList.remove(2));
		assertTrue(myList.remove(1));
		assertFalse(myList.hasNext());
		
		myList.reset();
		assertFalse(myList.hasNext());
	}
	
	@Test
	public void testAddWhileIterate() {
		Bag<Integer> myList = new Bag<Integer>(2);		
		
		myList.add(1);
		myList.add(2);
		
		myList.reset();
		assertTrue(myList.hasNext());
		myList.next();
		int x = myList.next();
		myList.remove(x);
		myList.add(3);
		assertTrue(myList.hasNext());
		assertTrue(myList.next() == 3);
		assertFalse(myList.hasNext());
		
		myList.reset();
		ArrayList<Integer> testList = new ArrayList<Integer>();
		testList.add(x==1 ? 2 : 1);
		testList.add(3);
		verifyContents(testList, myList);
	}
	
	@Test
	public void testIterate() {
		Bag<Integer> myList = new Bag<Integer>();		
		Set<Integer> mySet = new HashSet<Integer>();
		
		for (int i = 0; i < 5; ++i) {
			myList.add(i);
			mySet.add(i);
		}

		myList.reset();
		for (int i = 0; i < 5; ++i) {
			assertTrue(myList.hasNext());
			Integer x = myList.next();
			assertTrue(mySet.contains(x));
			mySet.remove(x);
		}
		assertTrue(mySet.isEmpty());
		
		for (int i = 0; i < 100; ++i) {
			assertFalse(myList.hasNext());			
		}
		
		for (int i = 0; i < 5; ++i) {
			mySet.add(i);
		}
		
		myList.reset();
		for (int i = 0; i < 5; ++i) {
			assertTrue(myList.hasNext());
			Integer x = myList.next();
			assertTrue(mySet.contains(x));
			mySet.remove(x);
		}
		assertTrue(mySet.isEmpty());
		
		for (int i = 0; i < 100; ++i) {
			assertFalse(myList.hasNext());			
		}		
	}
	
	@Test
	public void testRemoveAfterQuery() {
		Bag<Integer> myList = new Bag<Integer>();		

		myList.add(1);
		myList.add(2);
		
		myList.reset();
		assertTrue(myList.hasNext());
		int x = myList.next();
		assertTrue(x == 1 || x == 2);
		assertTrue(myList.hasNext());
		myList.remove(x);
		
		assertTrue(myList.hasNext());
		int y = myList.next();
		assertTrue(y != x && (y == 1 || y == 2));
		assertFalse(myList.hasNext());
		
		myList.reset();
		assertTrue(myList.hasNext());
		int z = myList.next();
		assertTrue(z != x && (z == 1 || z == 2));
		assertFalse(myList.hasNext());
	}

	@Test
	public void testRemoveBeforeQuery() {
		Bag<Integer> myList = new Bag<Integer>();		

		myList.add(1);
		myList.add(2);
		
		myList.reset();
		assertTrue(myList.hasNext());
		int x = myList.next();
		if (x == 1) {
			myList.remove(2);
		} else if (x == 2){
			myList.remove(1);
		} else {
			fail();
		}
		assertFalse(myList.hasNext());
		
		myList.reset();
		assertTrue(myList.hasNext());
		int z = myList.next();
		assertTrue(z == x);
		assertFalse(myList.hasNext());
	}
	
	@Test
	public void testAddWhileQuery1() {
		Bag<Integer> myList = new Bag<Integer>();		

		myList.add(1);
		myList.add(2);
		
		myList.reset();
		assertTrue(myList.hasNext());
		int x = myList.next();
		assertTrue(x == 1 || x == 2);
		myList.add(3);
		assertTrue(myList.hasNext());
		int y = myList.next();
		assertTrue(x != y && (y == 1 || y == 2 || y == 3));
		
		assertTrue(myList.hasNext());
		int z = myList.next();
		assertTrue(z != y && z != x && (z == 1 || z == 2 || z == 3));
		assertFalse(myList.hasNext());
	}

	@Test
	public void testAddWhileQuery2() {
		Bag<Integer> myList = new Bag<Integer>();		

		myList.add(1);
		
		myList.reset();
		assertTrue(myList.hasNext());
		assertTrue(myList.next() == 1);
		
		myList.add(2);
		assertTrue(myList.hasNext());
		assertTrue(myList.next() == 2);
		assertFalse(myList.hasNext());
	}
	
	@Test
	public void testRandom() {
		final int n = 1000;
		
		Bag<Double> myList = new Bag<Double>();		
		ArrayList<Double> bag = new ArrayList<Double>();
		Random rnd = new Random();

		for (int i = 0; i < n; ++i) {
			double x = rnd.nextDouble();
			myList.add(x);
			bag.add(x);
		}

		int size = n;
		for (myList.reset(); myList.hasNext();) {
			myList.next();
			if (rnd.nextDouble() <= 0.33) {
				if (rnd.nextBoolean()) {
					double toAdd = rnd.nextDouble();
					bag.add(toAdd);
					myList.add(toAdd);
				} else {
					double toRemove = bag.remove(rnd.nextInt(size--));
					myList.remove(toRemove);			
				}
			}
		}
		verifyContents(bag, myList);
	}
	
	private void verifyContents(Collection<? extends Object> contents, Bag<? extends Object> cl) {
		Set<Object> set = new HashSet<Object>(contents); 
		
		cl.reset();
		for (cl.reset(); cl.hasNext(); ) {
			Object obj = cl.next();
			assertTrue(set.contains(obj));
			set.remove(obj);
		}
		assertTrue(set.isEmpty());
	}
	
}
