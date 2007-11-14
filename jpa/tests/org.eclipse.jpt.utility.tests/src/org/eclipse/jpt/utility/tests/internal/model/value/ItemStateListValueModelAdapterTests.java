/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.model.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jpt.utility.internal.Bag;
import org.eclipse.jpt.utility.internal.HashBag;
import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.value.CollectionValueModel;
import org.eclipse.jpt.utility.internal.model.value.ItemStateListValueModelAdapter;
import org.eclipse.jpt.utility.internal.model.value.ListValueModel;
import org.eclipse.jpt.utility.internal.model.value.SimpleCollectionValueModel;
import org.eclipse.jpt.utility.internal.model.value.SimpleListValueModel;
import org.eclipse.jpt.utility.internal.model.value.SortedListValueModelAdapter;
import org.eclipse.jpt.utility.tests.internal.TestTools;

import junit.framework.TestCase;

public class ItemStateListValueModelAdapterTests extends TestCase {
	private Junk foo;
	private Junk bar;
	private Junk baz;
	private Junk joo;
	private Junk jar;
	private Junk jaz;

	private Junk tom;
	private Junk dick;
	private Junk harry;

	public ItemStateListValueModelAdapterTests(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.foo = new Junk("this.foo");
		this.bar = new Junk("this.bar");
		this.baz = new Junk("this.baz");
		this.joo = new Junk("this.joo");
		this.jar = new Junk("this.jar");
		this.jaz = new Junk("this.jaz");

		this.tom = new Junk("this.tom");
		this.dick = new Junk("this.dick");
		this.harry = new Junk("this.harry");
	}

	@Override
	protected void tearDown() throws Exception {
		TestTools.clear(this);
		super.tearDown();
	}

	public void testCollectionSynchronization() {
		SimpleCollectionValueModel collectionHolder = this.buildCollectionHolder();
		ListValueModel listValueModel = new ItemStateListValueModelAdapter(collectionHolder);
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		assertEquals(6, synchList.size());
		this.compare(listValueModel, synchList);

		collectionHolder.add(this.tom);
		collectionHolder.add(this.dick);
		collectionHolder.add(this.harry);
		assertEquals(9, synchList.size());
		this.compare(listValueModel, synchList);

		collectionHolder.remove(this.foo);
		collectionHolder.remove(this.jar);
		collectionHolder.remove(this.harry);
		assertEquals(6, synchList.size());
		this.compare(listValueModel, synchList);
	}

	public void testListSynchronization() {
		ListValueModel listHolder = this.buildListHolder();
		ListValueModel listValueModel = new ItemStateListValueModelAdapter(listHolder);
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		assertEquals(6, synchList.size());
		this.compare(listValueModel, synchList);

		listHolder.add(6, this.tom);
		listHolder.add(7, this.dick);
		listHolder.add(8, this.harry);
		assertEquals(9, synchList.size());
		this.compare(listValueModel, synchList);

		listHolder.remove(8);
		listHolder.remove(0);
		listHolder.remove(4);
		assertEquals(6, synchList.size());
		this.compare(listValueModel, synchList);
	}

	private void compare(ListValueModel listValueModel, List list) {
		assertEquals(listValueModel.size(), list.size());
		for (int i = 0; i < listValueModel.size(); i++) {
			assertEquals(listValueModel.get(i), list.get(i));
		}
	}

	public void testCollectionSort() {
		this.verifyCollectionSort(null);
	}

	public void testListSort() {
		this.verifyListSort(null);
	}

	public void testCustomCollectionSort() {
		this.verifyCollectionSort(this.buildCustomComparator());
	}

	public void testCustomListSort() {
		this.verifyListSort(this.buildCustomComparator());
	}

	private Comparator buildCustomComparator() {
		// sort with reverse order
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) o2).compareTo(o1);
			}
		};
	}

	private void verifyCollectionSort(Comparator comparator) {
		SimpleCollectionValueModel collectionHolder = this.buildCollectionHolder();
		ListValueModel listValueModel = new ItemStateListValueModelAdapter(new SortedListValueModelAdapter(collectionHolder, comparator));
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		assertEquals(6, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);

		collectionHolder.add(this.tom);
		collectionHolder.add(this.dick);
		collectionHolder.add(this.harry);
		assertEquals(9, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);

		collectionHolder.remove(this.foo);
		collectionHolder.remove(this.jar);
		collectionHolder.remove(this.harry);
		assertEquals(6, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);
	}

	private void verifyListSort(Comparator comparator) {
		ListValueModel listHolder = this.buildListHolder();
		ListValueModel listValueModel = new ItemStateListValueModelAdapter(new SortedListValueModelAdapter(listHolder, comparator));
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		assertEquals(6, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);

		listHolder.add(0, this.tom);
		listHolder.add(0, this.dick);
		listHolder.add(0, this.harry);
		assertEquals(9, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);

		listHolder.remove(8);
		listHolder.remove(4);
		listHolder.remove(0);
		listHolder.remove(5);
		assertEquals(5, synchList.size());
		this.compareSort(listValueModel, synchList, comparator);
	}

	private void compareSort(ListValueModel listValueModel, List list, Comparator comparator) {
		SortedSet ss = new TreeSet(comparator);
		for (int i = 0; i < listValueModel.size(); i++) {
			ss.add(listValueModel.get(i));
		}
		assertEquals(ss.size(), list.size());
		for (Iterator stream1 = ss.iterator(), stream2 = list.iterator(); stream1.hasNext(); ) {
			assertEquals(stream1.next(), stream2.next());
		}
	}

	public void testHasListeners() throws Exception {
		SimpleListValueModel listHolder = this.buildListHolder();
		assertFalse(listHolder.hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());

		ListValueModel listValueModel = new ItemStateListValueModelAdapter(new SortedListValueModelAdapter(listHolder));
		assertFalse(listHolder.hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());
		this.verifyHasNoListeners(listValueModel);

		SynchronizedList synchList = new SynchronizedList(listValueModel);
		assertTrue(listHolder.hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
		assertTrue(this.foo.hasAnyStateChangeListeners());
		assertTrue(this.foo.hasAnyStateChangeListeners());
		assertTrue(this.jaz.hasAnyStateChangeListeners());
		assertTrue(this.jaz.hasAnyStateChangeListeners());
		this.verifyHasListeners(listValueModel);

		listValueModel.removeListChangeListener(ListValueModel.LIST_VALUES, synchList);
		assertFalse(listHolder.hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.foo.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());
		assertFalse(this.jaz.hasAnyStateChangeListeners());
		this.verifyHasNoListeners(listValueModel);
	}

	public void testGetSize() throws Exception {
		SimpleListValueModel listHolder = this.buildListHolder();
		ListValueModel listValueModel = new ItemStateListValueModelAdapter(new SortedListValueModelAdapter(listHolder));
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		this.verifyHasListeners(listValueModel);
		assertEquals(6, listValueModel.size());
		assertEquals(6, synchList.size());
	}

	public void testGet() throws Exception {
		SimpleListValueModel listHolder = this.buildListHolder();
		ListValueModel listValueModel = new SortedListValueModelAdapter(new ItemStateListValueModelAdapter(listHolder));
		SynchronizedList synchList = new SynchronizedList(listValueModel);
		this.verifyHasListeners(listValueModel);
		assertEquals(this.bar, listValueModel.get(0));
		assertEquals(this.bar, synchList.get(0));
		this.bar.setName("zzz");
		assertEquals(this.bar, listValueModel.get(5));
		assertEquals(this.bar, synchList.get(5));
		this.bar.setName("this.bar");
	}

	private void verifyHasNoListeners(ListValueModel listValueModel) throws Exception {
		assertTrue(((AbstractModel) listValueModel).hasNoListChangeListeners(ListValueModel.LIST_VALUES));
	}

	private void verifyHasListeners(ListValueModel listValueModel) throws Exception {
		assertTrue(((AbstractModel) listValueModel).hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
	}

	private SimpleCollectionValueModel buildCollectionHolder() {
		return new SimpleCollectionValueModel(this.buildCollection());
	}

	private Collection buildCollection() {
		Bag bag = new HashBag();
		this.populateCollection(bag);
		return bag;
	}

	private SimpleListValueModel buildListHolder() {
		return new SimpleListValueModel(this.buildList());
	}

	private List buildList() {
		List list = new ArrayList();
		this.populateCollection(list);
		return list;
	}

	private void populateCollection(Collection c) {
		c.add(this.foo);
		c.add(this.bar);
		c.add(this.baz);
		c.add(this.joo);
		c.add(this.jar);
		c.add(this.jaz);
	}

private class Junk extends AbstractModel implements Comparable<Junk> {
	private String name;
	public Junk(String name) {
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
		this.fireStateChanged();
	}
	public int compareTo(Junk j) {
		return this.name.compareTo(j.name);
	}
	@Override
	public String toString() {
		return "Junk(" + this.name + ")";
	}
}

}
