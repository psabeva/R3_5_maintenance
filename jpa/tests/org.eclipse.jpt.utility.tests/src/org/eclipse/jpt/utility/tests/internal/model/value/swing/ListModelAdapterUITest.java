/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.model.value.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.ListValueModel;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.SortedListValueModelAdapter;
import org.eclipse.jpt.utility.internal.model.value.swing.ListModelAdapter;
import org.eclipse.jpt.utility.internal.swing.Displayable;

/**
 * an example UI for testing various permutations of the ListModelAdapter
 */
public class ListModelAdapterUITest {

	private PropertyValueModel taskListHolder;
	private TextField taskTextField;

	public static void main(String[] args) throws Exception {
		new ListModelAdapterUITest().exec(args);
	}

	private ListModelAdapterUITest() {
		super();
	}

	private void exec(String[] args) throws Exception {
		this.taskListHolder = new SimplePropertyValueModel(new TaskList());
		this.openWindow();
	}

	private void openWindow() {
		JFrame window = new JFrame(this.getClass().getName());
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(this.buildWindowListener());
		window.getContentPane().add(this.buildMainPanel(), "Center");
		window.setSize(800, 400);
		window.setVisible(true);
	}

	private WindowListener buildWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				System.exit(0);
			}
		};
	}

	private Component buildMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(this.buildTaskListPanel(), BorderLayout.CENTER);
		mainPanel.add(this.buildControlPanel(), BorderLayout.SOUTH);
		return mainPanel;
	}

	private Component buildTaskListPanel() {
		JPanel taskListPanel = new JPanel(new GridLayout(0, 1));
		taskListPanel.add(this.buildPrimitiveTaskListPanel());
		taskListPanel.add(this.buildDisplayableTaskListPanel());
		return taskListPanel;
	}

	private Component buildPrimitiveTaskListPanel() {
		JPanel taskListPanel = new JPanel(new GridLayout(1, 0));
		taskListPanel.add(this.buildUnsortedPrimitiveListPanel());
		taskListPanel.add(this.buildStandardSortedPrimitiveListPanel());
		taskListPanel.add(this.buildCustomSortedPrimitiveListPanel());
		return taskListPanel;
	}

	private Component buildDisplayableTaskListPanel() {
		JPanel taskListPanel = new JPanel(new GridLayout(1, 0));
		taskListPanel.add(this.buildUnsortedDisplayableListPanel());
		taskListPanel.add(this.buildStandardSortedDisplayableListPanel());
		taskListPanel.add(this.buildCustomSortedDisplayableListPanel());
		return taskListPanel;
	}

	private Component buildUnsortedPrimitiveListPanel() {
		return this.buildListPanel("primitive unsorted", this.buildUnsortedPrimitiveListModel());
	}

	private Component buildStandardSortedPrimitiveListPanel() {
		return this.buildListPanel("primitive sorted", this.buildStandardSortedPrimitiveListModel());
	}

	private Component buildCustomSortedPrimitiveListPanel() {
		return this.buildListPanel("primitive reverse sorted", this.buildCustomSortedPrimitiveListModel());
	}

	private Component buildUnsortedDisplayableListPanel() {
		return this.buildListPanel("displayable unsorted", this.buildUnsortedDisplayableListModel());
	}

	private Component buildStandardSortedDisplayableListPanel() {
		return this.buildListPanel("displayable sorted", this.buildStandardSortedDisplayableListModel());
	}

	private Component buildCustomSortedDisplayableListPanel() {
		return this.buildListPanel("displayable reverse sorted", this.buildCustomSortedDisplayableListModel());
	}

	private ListModel buildUnsortedPrimitiveListModel() {
		return new ListModelAdapter(this.buildPrimitiveTaskListAdapter());
	}

	private ListModel buildStandardSortedPrimitiveListModel() {
		return new ListModelAdapter(new SortedListValueModelAdapter(this.buildPrimitiveTaskListAdapter()));
	}

	private ListModel buildCustomSortedPrimitiveListModel() {
		return new ListModelAdapter(new SortedListValueModelAdapter(this.buildPrimitiveTaskListAdapter(), this.buildCustomStringComparator()));
	}

	private ListModel buildUnsortedDisplayableListModel() {
		return new ListModelAdapter(this.buildDisplayableTaskListAdapter());
	}

	private ListModel buildStandardSortedDisplayableListModel() {
		return new ListModelAdapter(new SortedListValueModelAdapter(this.buildDisplayableTaskListAdapter()));
	}

	private ListModel buildCustomSortedDisplayableListModel() {
		return new ListModelAdapter(new SortedListValueModelAdapter(this.buildDisplayableTaskListAdapter(), this.buildCustomTaskObjectComparator()));
	}

	private Component buildListPanel(String label, ListModel listModel) {
		JPanel listPanel = new JPanel(new BorderLayout());
		JLabel listLabel = new JLabel("  " + label);
		listPanel.add(listLabel, BorderLayout.NORTH);

		JList listBox = new JList();
		listBox.setModel(listModel);
		listBox.setDoubleBuffered(true);
		listLabel.setLabelFor(listBox);
		listPanel.add(new JScrollPane(listBox), BorderLayout.CENTER);
		return listPanel;
	}

	private Comparator<String> buildCustomStringComparator() {
		return new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.compareTo(s1);
			}
		};
	}

	private Comparator<TaskObject> buildCustomTaskObjectComparator() {
		return new Comparator<TaskObject>() {
			public int compare(TaskObject to1, TaskObject to2) {
				return to2.compareTo(to1);
			}
		};
	}

	private ListValueModel buildPrimitiveTaskListAdapter() {
		return new ListAspectAdapter(TaskList.TASKS_LIST, this.taskList()) {
			@Override
			protected ListIterator<String> listIterator_() {
				return ((TaskList) this.subject).tasks();
			}
		};
	}

	private ListValueModel buildDisplayableTaskListAdapter() {
		return new ListAspectAdapter(TaskList.TASK_OBJECTS_LIST, this.taskList()) {
			@Override
			protected ListIterator<TaskObject> listIterator_() {
				return ((TaskList) this.subject).taskObjects();
			}
		};
	}

	private Component buildControlPanel() {
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(this.buildAddRemoveTaskPanel(), BorderLayout.CENTER);
		controlPanel.add(this.buildClearButton(), BorderLayout.EAST);
		return controlPanel;
	}

	private Component buildAddRemoveTaskPanel() {
		JPanel addRemoveTaskPanel = new JPanel(new BorderLayout());
		addRemoveTaskPanel.add(this.buildAddButton(), BorderLayout.WEST);
		addRemoveTaskPanel.add(this.buildTaskTextField(), BorderLayout.CENTER);
		addRemoveTaskPanel.add(this.buildRemoveButton(), BorderLayout.EAST);
		return addRemoveTaskPanel;
	}

	private String getTask() {
		return this.taskTextField.getText();
	}

	private TaskList taskList() {
		return (TaskList) this.taskListHolder.value();
	}

	void addTask() {
		String task = this.getTask();
		if (task.length() != 0) {
			this.taskList().addTask(task);
		}
	}

	void removeTask() {
		String task = this.getTask();
		if (task.length() != 0) {
			this.taskList().removeTask(task);
		}
	}

	void clearTasks() {
		this.taskList().clearTasks();
	}

	private TextField buildTaskTextField() {
		this.taskTextField = new TextField();
		return this.taskTextField;
	}

	private JButton buildAddButton() {
		return new JButton(this.buildAddAction());
	}

	private Action buildAddAction() {
		Action action = new AbstractAction("add") {
			public void actionPerformed(ActionEvent event) {
				ListModelAdapterUITest.this.addTask();
			}
		};
		action.setEnabled(true);
		return action;
	}

	private JButton buildRemoveButton() {
		return new JButton(this.buildRemoveAction());
	}

	private Action buildRemoveAction() {
		Action action = new AbstractAction("remove") {
			public void actionPerformed(ActionEvent event) {
				ListModelAdapterUITest.this.removeTask();
			}
		};
		action.setEnabled(true);
		return action;
	}

	private JButton buildClearButton() {
		return new JButton(this.buildClearAction());
	}

	private Action buildClearAction() {
		Action action = new AbstractAction("clear") {
			public void actionPerformed(ActionEvent event) {
				ListModelAdapterUITest.this.clearTasks();
			}
		};
		action.setEnabled(true);
		return action;
	}

	private class TaskList extends AbstractModel {
		private List<String> tasks = new ArrayList<String>();
		private List<TaskObject> taskObjects = new ArrayList<TaskObject>();
		public static final String TASKS_LIST = "tasks";
		public static final String TASK_OBJECTS_LIST = "taskObjects";
		TaskList() {
			super();
		}
		public ListIterator<String> tasks() {
			return this.tasks.listIterator();
		}
		public ListIterator<TaskObject> taskObjects() {
			return this.taskObjects.listIterator();
		}
		public void addTask(String task) {
			int index = this.tasks.size();
			this.tasks.add(index, task);
			this.fireItemAdded(TASKS_LIST, index, task);
	
			TaskObject taskObject = new TaskObject(task);
			this.taskObjects.add(index, taskObject);
			this.fireItemAdded(TASK_OBJECTS_LIST, index, taskObject);
		}		
		public void removeTask(String task) {
			int index = this.tasks.indexOf(task);
			if (index != -1) {
				Object removedTask = this.tasks.remove(index);
				this.fireItemRemoved(TASKS_LIST, index, removedTask);
				// assume the indexes match...
				Object removedTaskObject = this.taskObjects.remove(index);
				this.fireItemRemoved(TASK_OBJECTS_LIST, index, removedTaskObject);
			}
		}
		public void clearTasks() {
			this.tasks.clear();
			this.fireListChanged(TASKS_LIST);
			this.taskObjects.clear();
			this.fireListChanged(TASK_OBJECTS_LIST);
		}
	}

	private class TaskObject extends AbstractModel implements Displayable {
		private String name;
		private Date creationTimeStamp;
		public TaskObject(String name) {
			this.name = name;
			this.creationTimeStamp = new Date();
		}
		public String displayString() {
			return this.name + ": " + this.creationTimeStamp.getTime();
		}
		public Icon icon() {
			return null;
		}
		public int compareTo(Displayable o) {
			return DEFAULT_COMPARATOR.compare(this, o);
		}
		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			Object old = this.name;
			this.name = name;
			this.firePropertyChanged(DISPLAY_STRING_PROPERTY, old, name);
		}
		@Override
		public String toString() {
			return "TaskObject(" + this.displayString() + ")";
		}
	}

}
