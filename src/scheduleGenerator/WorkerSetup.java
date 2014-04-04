package scheduleGenerator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author schneimd
 */
public class WorkerSetup extends javax.swing.JFrame {

	// Swap 1 - Team 03 - Code Sniffing
	// SMELL: Shotgun Surgery - Days are duplicated and used interchangeably in
	// a handful of places (Worker, Main, WorkerSetup, CalendarGUI) and
	// sometimes they are the same thing, other times they aren't. The duplicate
	// ones will lead to changes in multiple places if they needed to change
	// (say we needed to change Day out for DayWithTime).
	private ArrayList<Day> days;
	
	private ArrayList<JPanel> workerTabs;

	/**
	 * Allows for editing of already made workers.
	 * 
	 * @param workers
	 */
	public WorkerSetup(ArrayList<Worker> workers) {
		this.setPreferredSize(new Dimension(425, 450));
		this.workerTabs = new ArrayList<JPanel>();
		this.days = Main.getDays();
		initComponents();
		for (int c = 0; c < workers.size(); c++) {
			this.addWorker();
		}

		for (int c = 0; c < workers.size(); c++) {
			JTextField nameArea = (JTextField) this.workerTabs.get(c)
					.getComponent(2);
			nameArea.setText(workers.get(c).getName());
			JTabbedPane daysPane = (JTabbedPane) this.workerTabs.get(c)
					.getComponents()[0];
			for (int i = 0; i < daysPane.getTabCount(); i++) {
				for (int n = 0; n < workers.get(c).getDays().size(); n++) {
					if (daysPane.getTitleAt(i).equals(
							workers.get(c).getDays().get(n).getNameOfDay())) {

						JPanel day = (JPanel) daysPane.getComponent(i);
						JScrollPane pane = (JScrollPane) day.getComponent(0);
						JViewport view = (JViewport) pane.getComponent(0);
						JPanel p = (JPanel) view.getComponent(0);

						for (Component job : p.getComponents()) {
							for (String workerJob : workers.get(c).getDays()
									.get(n).getJobs()) {
								if (((JCheckBox) job).getText().equals(
										workerJob)) {
									((JCheckBox) job).setSelected(true);
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Creates new form WorkerSetup
	 */
	public WorkerSetup() {
		this.setPreferredSize(new Dimension(425, 450));
		this.workerTabs = new ArrayList<JPanel>();
		this.days = Main.getDays();
		initComponents();
		addWorker();
	}

	private void addWorker() {
		// Swap 1 - Team 03 - Code Sniffing
		// SMELL: Feature Envy - It has to reach into Main and get days to use.
		
		//SWAP 2, TEAM 4
		//REFACTORING FOR ENHANCEMENT FROM BAD SMELL
		//We thought that for such a small smell moving the method would be too extreme
		//	so instead of doing this we moved the line of code that touched main into the constructor
		//	because we thought it was more applicable there.
		//This puts the line of code in a more relevant part making it more understandable.
		
		javax.swing.JTabbedPane tempWorkerDays = new javax.swing.JTabbedPane();
		javax.swing.JTextField tempWorkerName = new javax.swing.JTextField();
		javax.swing.JPanel tempWorkerTab = new javax.swing.JPanel();

		// Makes a tab for each day and a check box for each job.
		for (Day day : this.days) {
			JCheckBox[] jobs = new JCheckBox[day.getJobs().size()];
			for (int i = 0; i < day.getJobs().size(); i++) {
				jobs[i] = new JCheckBox(day.getJobs().get(i));
			}

			// Put Check Boxes in a scrollPane for dynamics
			JScrollPane tempDayJobPane = new JScrollPane();
			JPanel tempPanel = new JPanel();
			tempPanel.setLayout(new GridLayout(jobs.length, 1));

			for (JCheckBox job : jobs) {
				tempPanel.add(job);
			}
			tempDayJobPane.setViewportView(tempPanel);

			// Label the Pane
			JLabel jobLabel = new JLabel("Preferred Jobs:");

			// Create a tab Panel for the Worker Tab and add the inputs.

			JPanel dayTab = new JPanel();

			// Set veritcal and horizontal layouts.
			javax.swing.GroupLayout sundayTab1Layout = new javax.swing.GroupLayout(
					dayTab);
			dayTab.setLayout(sundayTab1Layout);
			sundayTab1Layout
					.setHorizontalGroup(sundayTab1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									sundayTab1Layout
											.createSequentialGroup()
											.addGap(63, 63, 63)
											.addGroup(
													sundayTab1Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	tempDayJobPane,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	198,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	jobLabel))
											.addContainerGap(73,
													Short.MAX_VALUE)));

			sundayTab1Layout
					.setVerticalGroup(sundayTab1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									sundayTab1Layout
											.createSequentialGroup()
											.addContainerGap()
											.addComponent(jobLabel)
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(
													tempDayJobPane,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													179, Short.MAX_VALUE)
											.addContainerGap()));

			tempWorkerDays.addTab(day.getNameOfDay(), dayTab);

		}

		// Add a section for the worker's name
		JLabel workerNameLabel = new JLabel("Worker's Name:");

		javax.swing.GroupLayout workerTab1Layout = new javax.swing.GroupLayout(
				tempWorkerTab);
		tempWorkerTab.setLayout(workerTab1Layout);
		workerTab1Layout
				.setHorizontalGroup(workerTab1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								workerTab1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												workerTab1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																tempWorkerDays)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																workerTab1Layout
																		.createSequentialGroup()
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				workerNameLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				tempWorkerName,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				150,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(49,
																				49,
																				49)))
										.addContainerGap()));

		// Adds text area and label for name then tab area for days.
		workerTab1Layout
				.setVerticalGroup(workerTab1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								workerTab1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												workerTab1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																workerNameLabel)
														.addComponent(
																tempWorkerName,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												tempWorkerDays,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												249,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		// Prevents a nullPointer
		if (this.workerTabs.size() == 0) {
			this.workerTabs.add(tempWorkerTab);
			this.workerTabPanel.addTab("Worker 1", null, tempWorkerTab, "");
		} else {
			this.workerTabs.add(tempWorkerTab);
			this.workerTabPanel.addTab(
					"Worker " + String.valueOf(this.workerTabs.size()), null,
					tempWorkerTab, "");
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Worker Setup");

		initializeButtons();

		JScrollPane outside = new JScrollPane();
		outside.setViewportView(this.workerTabPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		
		// Swap 1 - Team 03 - Code Sniffing
		// SMELL: Middle Man - layout.setHorizontalGroup has a super long chain
		// but its really just adding some JWrapper to everything as it goes along
		
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(createNavigationLayoutGroup(layout))
				.addGroup(createHorizontalLayoutGroup(outside, layout)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				createAddRemoveVertialGroup(outside, layout)));

		pack();
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Bad Smell - Long Method
	// Breaking up creating the GUI layout

	private SequentialGroup createHorizontalLayoutGroup(JScrollPane outside,
			javax.swing.GroupLayout layout) {
		SequentialGroup group = layout.createSequentialGroup();
		group.addContainerGap();
		group.addGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(createAddRemoveHorizontalGroup(layout))
				.addComponent(outside, javax.swing.GroupLayout.PREFERRED_SIZE,
						0, Short.MAX_VALUE));
		group.addContainerGap();

		return group;
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Bad Smell - Long Method
	// Breaking up creating the GUI layout

	private SequentialGroup createAddRemoveVertialGroup(JScrollPane outside,
			javax.swing.GroupLayout layout) {

		SequentialGroup group = layout.createSequentialGroup();
		group.addComponent(outside, javax.swing.GroupLayout.PREFERRED_SIZE,
				330, javax.swing.GroupLayout.PREFERRED_SIZE);
		group.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		group.addGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(this.addButton).addComponent(this.removeButton));
		group.addGap(18, 18, 18);

		group.addGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(this.nextButton).addComponent(this.backButton)
				.addComponent(this.addCalendarButton));

		group.addGap(0, 8, Short.MAX_VALUE);

		return group;
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Bad Smell - Long Method
	// Breaking up creating the GUI layout

	private SequentialGroup createAddRemoveHorizontalGroup(
			javax.swing.GroupLayout layout) {

		SequentialGroup group = layout.createSequentialGroup();
		group.addComponent(this.addButton,
				javax.swing.GroupLayout.PREFERRED_SIZE, 136,
				javax.swing.GroupLayout.PREFERRED_SIZE);
		group.addPreferredGap(
				javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82,
				Short.MAX_VALUE);
		group.addComponent(this.removeButton,
				javax.swing.GroupLayout.PREFERRED_SIZE, 136,
				javax.swing.GroupLayout.PREFERRED_SIZE);

		return group;
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Bad Smell - Long Method
	// Breaking up creating the GUI layout

	private SequentialGroup createNavigationLayoutGroup(
			javax.swing.GroupLayout layout) {

		SequentialGroup group = layout.createSequentialGroup();
		group.addGap(106, 106, 106);
		group.addComponent(this.backButton,
				javax.swing.GroupLayout.PREFERRED_SIZE, 65,
				javax.swing.GroupLayout.PREFERRED_SIZE);
		group.addGap(18, 18, 18);
		group.addComponent(this.nextButton,
				javax.swing.GroupLayout.PREFERRED_SIZE, 65,
				javax.swing.GroupLayout.PREFERRED_SIZE);
		group.addComponent(this.addCalendarButton,
				javax.swing.GroupLayout.PREFERRED_SIZE, 120,
				javax.swing.GroupLayout.PREFERRED_SIZE);
		group.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE);

		return group;
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Refactoring Long Method
	// The class's init method was far too big to actually understand.

	private void initializeButtons() {
		this.workerTabPanel = new javax.swing.JTabbedPane();
		this.addButton = new javax.swing.JButton("Add Worker");
		this.removeButton = new javax.swing.JButton("Remove Worker");
		this.nextButton = new javax.swing.JButton("Next");
		this.backButton = new javax.swing.JButton("Back");
		this.addCalendarButton = new JButton("Add Calendar");

		this.addButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addButtonActionPerformed(evt);
			}
		});

		this.removeButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						removeButtonActionPerformed(evt);
					}
				});

		this.nextButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});

		this.backButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backButtonActionPerformed(evt);
			}
		});

		this.addCalendarButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				addCalendarActionPerformed(evt);
			}
		});
	}

	/**
	 * @param evt
	 */
	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
		ArrayList<Worker> workers = new ArrayList<Worker>();
		boolean allGood = true;
		for (int tabIndex = 0; tabIndex < this.workerTabs.size(); tabIndex++) {
			JPanel tab = this.workerTabs.get(tabIndex);
			ArrayList<Day> workerDays = new ArrayList<Day>();
			JTextField nameArea = (JTextField) tab.getComponent(2);
			if (nameArea.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"You have not entered a name for every worker.");
				allGood = false;
				break;
			}
			JTabbedPane daysPane = (JTabbedPane) tab.getComponents()[0];
			for (int i = 0; i < daysPane.getTabCount(); i++) {

				JPanel day = (JPanel) daysPane.getComponent(i);

				JScrollPane pane = (JScrollPane) day.getComponent(0);

				JViewport view = (JViewport) pane.getComponent(0);

				JPanel p = (JPanel) view.getComponent(0);

				ArrayList<Object> jobNames = new ArrayList<Object>();

				for (Component job : p.getComponents()) {
					if (((JCheckBox) job).isSelected()) {
						jobNames.add(((JCheckBox) job).getText());
					}
				}
				workerDays.add(new Day(daysPane.getTitleAt(i), jobNames));
			}
			workers.add(new Worker(nameArea.getText(), workerDays,
					new WorkerCalendar(this.calendarFiles.get(tabIndex))));
		}
		if (allGood) {
			HTMLGenerator.reset();
			Main.setWorkers(workers);
			Main.setSchedule(new Schedule(Main.getDays(), Main.getWorkers()));
			Main.dumpConfigFile();
			Main.cal = new CalendarGUI(Main.getSchedule());
			Main.toggleCalendar();
			Main.toggleWorkerSetup();
		}
	}

	/**
	 * @param evt
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Main.toggleConfig();
		Main.toggleWorkerSetup();
	}

	/**
	 * @param evt
	 */
	private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.addWorker();
	}

	// Swap 1 - Team 03 - BONUS FEATURE
	// Adding new code

	private void addCalendarActionPerformed(ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Calendars", "ics");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: "
					+ chooser.getSelectedFile().getName());
			this.calendarFiles.put(this.workerTabPanel.getSelectedIndex(),
					chooser.getSelectedFile());
		}
	}

	/**
	 * @param evt
	 */
	private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.workerTabs.remove(this.workerTabPanel.getSelectedComponent());
		this.workerTabPanel.remove(this.workerTabPanel.getSelectedIndex());
	}

	private javax.swing.JButton addButton;
	private javax.swing.JButton backButton;
	private javax.swing.JButton nextButton;
	private javax.swing.JButton removeButton;
	private javax.swing.JTabbedPane workerTabPanel;

	// Swap 1 - Team 03 - BONUS FEATURE
	// Adding new feature
	private javax.swing.JButton addCalendarButton;
	private Map<Integer, File> calendarFiles = new HashMap<Integer, File>();
}