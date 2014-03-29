package scheduleGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.text.DateFormatSymbols;

import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author schneimd
 */
public class CalendarGUI extends javax.swing.JFrame {

	private Schedule schedule;
	private GregorianCalendar cal;
	private TreeMap<String, TreeMap<String, Worker>> scheduleMap;
	private int currentMonth;
	private String monthName;
	@SuppressWarnings("unused")
	private int earliestYear, earliestMonth, earliestDay;
	private int monthsAhead = 0;
	private int yearsAhead = 0;

	/**
	 * Creates new form Calendar
	 * 
	 * @param schd
	 */
	public CalendarGUI(Schedule schd) {
		this.schedule = schd;
		this.scheduleMap = this.schedule.getSchedule();
		String[] earliest = this.scheduleMap.firstKey().split("/");
		this.earliestYear = Integer.parseInt(earliest[0]);
		this.earliestMonth = Integer.parseInt(earliest[1]);
		this.earliestDay = Integer.parseInt(earliest[2]);
		this.cal = new GregorianCalendar();
		initComponents();
		this.fillTableForThisMonth();
	}

	private void setTitleMonth(int n, int year) {
		//SWAP 1 TEAM 5
		/*
		 * QUALITY CHANGES
		 * Looked at API to change ints to month strings. In this way, we are able to 
		 * merge or compare a lot easier with other systems, using the universal API.
		 */
		//http://docs.oracle.com/javase/7/docs/api/java/text/DateFormatSymbols.html
		String monthString = new DateFormatSymbols().getMonths()[n-1] + " " + year;
		this.monthTitle.setText(monthString);
		this.monthName = monthString;
	}

	/**
	 * Displays the calendar for the current month based on the computers month.
	 * 
	 */
	public void fillTableForThisMonth() {
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		this.currentMonth = new GregorianCalendar().get(Calendar.MONTH) + 1;
		this.setTitleMonth(this.currentMonth, currentYear);
		this.monthsAhead = 0;
		this.yearsAhead = 0;

		String keyStart = currentYear + "/"
				+ String.format("%02d", this.currentMonth);
		String currentKey = "";

		// Generates calendar for current month if none exists
		while (currentKey.equals("")) {
			Set<String> keys = this.scheduleMap.keySet();
			for (String key : keys) {
				if (key.startsWith(keyStart)) {
					currentKey = key;
					break;
				}
			}
			if (currentKey.equals("")) {
				Thread t = new Thread(this.schedule);
				t.start();
				//this.schedule.calculateNextMonth();
			}
		}

		DefaultTableModel table = new DefaultTableModel(new Object[0][0],
				new String[0][0]);

		this.cal = new GregorianCalendar(currentYear, this.currentMonth - 1, 1);

		while (this.currentMonth == this.cal.get(Calendar.MONTH) + 1) {
			String tempKey = this.cal.get(Calendar.YEAR)
					+ "/"
					+ String.format("%02d", (this.cal.get(Calendar.MONTH) + 1))
					+ "/"
					+ String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
			if (this.scheduleMap.containsKey(tempKey)) {

				int numOfJobs = this.scheduleMap.get(tempKey).size();
				String[] colData = new String[numOfJobs];
				int i = 0;

				for (String key : this.scheduleMap.get(tempKey).keySet()) {
					colData[i] = key + ": "
							+ this.scheduleMap.get(tempKey).get(key).getName();
					i++;
				}

				String numDate = (this.cal.get(Calendar.MONTH) + 1)
						+ "/"
						+ String.format("%02d",
								this.cal.get(Calendar.DAY_OF_MONTH)) + "/"
						+ String.format("%02d", this.cal.get(Calendar.YEAR));
				String colTitle = this.getNameforNum(this.cal
						.get(Calendar.DAY_OF_WEEK)) + " (" + numDate + ")";
				table.addColumn(colTitle, colData);

			}
			this.cal.add(Calendar.DATE, 1);
		}

		HTMLGenerator.addMonth(this.monthName, table);
		this.scheduleTable.setModel(table);
	}

	/**
	 * Displays the next month from current month.
	 * 
	 */
	public void fillTableMonthAhead() {
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		this.monthsAhead++;
		int showMonth = new GregorianCalendar().get(Calendar.MONTH)
				+ this.monthsAhead + 1;
		this.yearsAhead = 0;
		while (showMonth > 12) {
			currentYear++;
			showMonth -= 12;
			this.yearsAhead++;
		}
		this.setTitleMonth(showMonth, currentYear);

		String keyStart = currentYear + "/" + String.format("%02d", showMonth);
		String currentKey = "";

		// Generates calendar for current month if none exists
		while (currentKey.equals("")) {
			Set<String> keys = this.scheduleMap.keySet();
			for (String key : keys) {
				if (key.startsWith(keyStart)) {
					currentKey = key;
					break;
				}
			}
			if (currentKey.equals("")) {
				Thread t = new Thread(this.schedule);
				t.start();
				//this.schedule.calculateNextMonth();
			}
		}

		DefaultTableModel table = tableCreate(showMonth, currentYear);
		HTMLGenerator.addMonth(this.monthName, table);
		this.scheduleTable.setModel(table);
	}

	/**
	 * Displays the last months from current month.
	 * 
	 */
	public void fillTableMonthBack() {
		// SWAP 2, TEAM 6
		// Primitive Obsession handled and method shortened.
		// SWAP 1, TEAM 5
		/*
		 * SMELL: Primitive Obsession - There are a lot of instances of primitive types that can be cleaned. 
		 * If this code was cleaned, the code will be easier to understand and a lot flexible for further changes.
		 */
		int tempMonths = this.monthsAhead;
		if ((new GregorianCalendar().get(Calendar.MONTH) + tempMonths) % 12 == 0) {
			this.yearsAhead--;
		}
		int currentYear = new GregorianCalendar().get(Calendar.YEAR)+ this.yearsAhead;
		this.monthsAhead--;
		tempMonths = this.monthsAhead;
		while (tempMonths < -11) {
			tempMonths += 12;
			currentYear--;
			this.yearsAhead--;
		}
		tempMonths = (new GregorianCalendar().get(Calendar.MONTH)+ tempMonths + 1)%12;

		if (currentYear < this.earliestYear|| (currentYear == this.earliestYear && tempMonths < this.earliestMonth)) {
			this.monthsAhead++;
		} else {
			this.setTitleMonth(tempMonths, currentYear);

			String keyStart = currentYear + "/"+ String.format("%02d", tempMonths);
			String currentKey = "";

			// Generates calendar for current month if none exists
			while (currentKey.equals("")) {
				Set<String> keys = this.scheduleMap.keySet();
				for (String key : keys) {
					if (key.startsWith(keyStart)) {
						currentKey = key;
						break;
					}
				}
				if (currentKey.equals("")) {
					Thread t = new Thread(this.schedule);
					t.start();
					//this.schedule.calculateNextMonth();
				}
			}

			DefaultTableModel table = tableCreate(tempMonths, currentYear);

			this.scheduleTable.setModel(table);
			HTMLGenerator.addMonth(this.monthName, table);
		}

	}

	private DefaultTableModel tableCreate(int tempMonths, int currentYear) {
		DefaultTableModel table = new DefaultTableModel(new Object[0][0],new String[0][0]);
		this.cal = new GregorianCalendar(currentYear, tempMonths - 1, 1);

		while (tempMonths == this.cal.get(Calendar.MONTH) + 1) {
			String tempKey = this.cal.get(Calendar.YEAR)
					+ "/"+ String.format("%02d",(this.cal.get(Calendar.MONTH) + 1))
					+ "/"+ String.format("%02d",this.cal.get(Calendar.DAY_OF_MONTH));
			if (this.scheduleMap.containsKey(tempKey)) {

				int numOfJobs = this.scheduleMap.get(tempKey).size();
				String[] colData = new String[numOfJobs];
				int i = 0;

				for (String key : this.scheduleMap.get(tempKey).keySet()) {
					colData[i] = key + ": "+ this.scheduleMap.get(tempKey).get(key).getName();
					i++;
				}

				String numDate = String.format("%02d",(this.cal.get(Calendar.MONTH) + 1))
						+ "/"+ String.format("%02d",this.cal.get(Calendar.DAY_OF_MONTH))
						+ "/"+ this.cal.get(Calendar.YEAR);
				String colTitle = this.getNameforNum(this.cal.get(Calendar.DAY_OF_WEEK)) + " (" + numDate + ")";
				table.addColumn(colTitle, colData);
			}
			this.cal.add(Calendar.DATE, 1);
		}
		return table;
	}

	private String getNameforNum(int n) {
		//SWAP 1 TEAM 5. 
		/*
		 * QUALITY CHANGES
		 * Looked things up and found an easier way to do days of the week; a lot easier to understand
		 * while compressing 20 lines of useless duplicate code.
		 */
		if(n == 1) {
			return "Sunday";
		} else if (n > 1 && n <= 7){
			return new DateFormatSymbols().getWeekdays()[n-1];
		}
		//1 is monday, 2 is tuesday
		return null;
	}

	private void initComponents() {
		// SWAP 1, TEAM 5
		/*
		 *  SMELL: Parallel Inheritance Hierarchies - Every time a subclass of one class is made,
		 *  multiple other instances are made. 
		 */ 
		
		this.monthTitle = new javax.swing.JLabel();
		this.previousMonthButton = new javax.swing.JButton();
		this.nextMonthButton = new javax.swing.JButton();
		this.jScrollPane1 = new javax.swing.JScrollPane();
		this.scheduleTable = new javax.swing.JTable();
		this.popup = new javax.swing.JPopupMenu();
		this.menuBar = new javax.swing.JMenuBar();
		this.fileMenu = new javax.swing.JMenu();
		this.saveChanges = new javax.swing.JMenuItem();
		this.undoChanges = new javax.swing.JMenuItem();
		this.editMenu = new javax.swing.JMenu();
		this.editWorkers = new javax.swing.JMenuItem();
		this.editDays = new javax.swing.JMenuItem();
		this.generateMenu = new javax.swing.JMenu();
		this.genHtml = new javax.swing.JMenuItem();
		this.generateText = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Calendar");

		this.monthTitle.setFont(new java.awt.Font("Tahoma", 1, 24));
		this.monthTitle.setText("Month Name Here");

		this.previousMonthButton.setText("<");
		this.previousMonthButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						previousMonthActionPerformed(evt);
					}
				});

		this.nextMonthButton.setText(">");
		this.nextMonthButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						nextMonthActionPerformed(evt);
					}
				});
		// SWAP 2, TEAM 6
		// Many of the items were pulled out into other methods and modified.
		// Much of the code had to remain because they are establishing GUI controls.
		// SWAP 1, TEAM 5
		/*
		 * SMELL: Data Clumps - too many clumps of data that could be simplified. If this is 
		 * simplified, the code will be easier to understand.
		 */
		scheduleTableCreate();

		saveChangesCreate();

		undoChangesCreate();

		editMenuCreate();

		generationMenuCreate();

		setJMenuBar(this.menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(this.jScrollPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 1002,
						Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(this.previousMonthButton)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.monthTitle)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(this.nextMonthButton)
						.addGap(0, 0, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap(18, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING,false)
									.addComponent(this.monthTitle,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE)
									.addComponent(this.previousMonthButton,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE)
									.addComponent(this.nextMonthButton,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											29,
											javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(this.jScrollPane1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								265,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		pack();
	}

	private void undoChangesCreate() {
		this.undoChanges.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
		this.undoChanges.setText("Undo Changes");
		this.undoChanges.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				undoChangesActionPerformed(evt);
			}
		});
		//this.fileMenu.add(this.undoChanges);
	}

	private void generationMenuCreate() {
		this.generateMenu.setText("Generate");

		this.genHtml.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
		this.genHtml.setText("Generate Web Page");
		this.genHtml.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					genHtmlActionPerformed(evt);
				}
			});
		this.generateMenu.add(this.genHtml);

		this.generateText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
		this.generateText.setText("Generate Text");
		this.generateText.addActionListener(new java.awt.event.ActionListener() 
			{
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					generateTextActionPerformed(evt);
				}
			});
		this.generateMenu.add(this.generateText);

		this.menuBar.add(this.generateMenu);
	}

	private void editMenuCreate() {
		this.menuBar.add(this.fileMenu);

		this.editMenu.setText("Edit");

		this.editWorkers.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
		this.editWorkers.setText("Edit Workers");
		this.editWorkers.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editWorkersActionPerformed(evt);
			}
		});
		this.editMenu.add(this.editWorkers);

		this.editDays.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.editDays.setText("Edit Days");
		this.editDays.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editDaysActionPerformed(evt);
			}
		});
		this.editMenu.add(this.editDays);

		this.menuBar.add(this.editMenu);
	}

	private void saveChangesCreate() {
		this.fileMenu.setText("File");

		this.saveChanges.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		this.saveChanges.setText("Save Changes");
		this.saveChanges.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveChangesActionPerformed(evt);
			}
		});
		this.fileMenu.add(this.saveChanges);
	}

	private void scheduleTableCreate() {
		this.scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { 	{ null, null, null }, { null, null, null },
									{ null, null, null }, { null, null, null } },
				new String[] { 	"Monday (10/22/2012)", "Wednesday (10/24/12)",
								"Thursday (10/26/12)" }));
		this.scheduleTable.setColumnSelectionAllowed(true);
		this.scheduleTable.getTableHeader().setReorderingAllowed(false);
		
		for(Worker i:this.schedule.getWorkers())
		{	
			final Worker input = i;
			this.popup.add(new JMenuItem(input.getName())).addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					editCell(input);
				}
			});
		}
		this.scheduleTable.setComponentPopupMenu(this.popup);
		
		this.jScrollPane1.setViewportView(this.scheduleTable);
	}

	/**
	 * @param evt
	 */
	private void editWorkersActionPerformed(java.awt.event.ActionEvent evt) {
		Main.wSet = new WorkerSetup(this.schedule.getWorkers());
		Main.toggleWorkerSetup();
		Main.toggleCalendar();
	}

	/**
	 * @param evt
	 */
	private void editDaysActionPerformed(java.awt.event.ActionEvent evt) {
		Main.config = new Config(Main.getDays());
		Main.toggleConfig();
		Main.toggleCalendar();
	}

	/**
	 * @param evt
	 */
	private void previousMonthActionPerformed(java.awt.event.ActionEvent evt) {
		this.fillTableMonthBack();
	}

	/**
	 * @param evt
	 */
	private void nextMonthActionPerformed(java.awt.event.ActionEvent evt) {
		this.fillTableMonthAhead();
	}

	/**
	 * @param evt
	 */
	private void genHtmlActionPerformed(java.awt.event.ActionEvent evt) {
		HTMLGenerator.writeHtml();
	}

	/**
	 * @param evt
	 */
	private void generateTextActionPerformed(java.awt.event.ActionEvent evt) {
		NavigableSet<String> keySet = this.scheduleMap.navigableKeySet();
		String textOutput = new String();
		File readout = new File("Calendar.txt");
		ArrayList<String> dutyRows = new ArrayList<String>();

		int column = 1;
		for (String i : keySet) {
			textOutput += String.format("%-30s", "|" + i);
			NavigableSet<String> valueSet = this.scheduleMap.get(i)
					.navigableKeySet();
			int row = 0;
			for (String j : valueSet) {
				if (dutyRows.size() <= row)
					dutyRows.add("");
				String newCol = dutyRows.get(row) + "|" + j + ": "
						+ this.scheduleMap.get(i).get(j).getName();

				dutyRows.set(row,
						String.format("%-" + 30 * column + "s", newCol));
				row += 1;
			}
			column += 1;
		}

		for (String i : dutyRows) {
			textOutput += "\n" + i;
		}
		
		char[] letterOutput = textOutput.toCharArray();

		try {
			readout.createNewFile();

			FileWriter outFile = new FileWriter(readout);
			for(char i:letterOutput)
				outFile.write(i);
			outFile.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * @param evt
	 */
	private void saveChangesActionPerformed(java.awt.event.ActionEvent evt) {
		Main.dumpConfigFile();
	}

	/**
	 * @param evt
	 */
	private void undoChangesActionPerformed(java.awt.event.ActionEvent evt) {
		// removed
	}
	
	private void editCell(Worker input)
	{
		int i = this.scheduleTable.getSelectedRow();
		int j = this.scheduleTable.getSelectedColumn();
		if(this.scheduleTable.getValueAt(i,j) != null)
		{
			System.out.println(this.scheduleTable.getColumnName(j));
			String job = this.scheduleTable.getValueAt(i,j).toString().split(":")[0];
			String date = this.scheduleTable.getColumnName(j).split(" ")[1];
			date = date.substring(1,date.length()-1);
			String[] dateNums = date.split("/");
			date = dateNums[2] + "/" + dateNums[0] + "/" + dateNums[1];
			System.out.println(date);
			this.scheduleMap.get(date).put(job,input);
			this.scheduleTable.setValueAt(job + ": " + input.getName(),i,j);
		}
	}

	private javax.swing.JMenuItem editDays;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem editWorkers;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenuItem genHtml;
	private javax.swing.JMenu generateMenu;
	private javax.swing.JMenuItem generateText;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JLabel monthTitle;
	private javax.swing.JButton nextMonthButton;
	private javax.swing.JPopupMenu popup;
	private javax.swing.JButton previousMonthButton;
	private javax.swing.JMenuItem saveChanges;
	private javax.swing.JTable scheduleTable;
	private javax.swing.JMenuItem undoChanges;
}
