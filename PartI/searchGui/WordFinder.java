package searchGui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WordFinder extends JFrame {

	private JFileChooser jFileChooser;
	private JPanel topPanel; // the top line of objects is going to go here
	private WordList wordList;
	private JTextArea textArea; 
	private JTextField wordSearch;

	
	public WordFinder() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set the size correctly
		this.setSize(500, 300);
		jFileChooser = new JFileChooser(".");
		wordList = new WordList();
		
		final int AREA_ROWS =180 ;
	    final int AREA_COLUMNS = 10;
		textArea = new JTextArea(AREA_ROWS, AREA_COLUMNS);
		textArea.setEditable(false);
		JPanel panelForTextFields = new JPanel();
		panelForTextFields.setSize(400, 180);
		panelForTextFields.add(textArea);
		JScrollPane scrollPane = new JScrollPane(textArea);
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createMenus());

		
		JLabel f = new JLabel("find:");
		final int FIELD_WIDTH = 10;
	    wordSearch = new JTextField(FIELD_WIDTH);
	    wordSearch.setText("");
	    JButton cleanButton = new JButton("clean");
		//panel at the top
		topPanel = new JPanel();
		topPanel.add(f);
		topPanel.add(wordSearch);
		topPanel.add(cleanButton);
		
		this.add(topPanel,BorderLayout.NORTH);	
		this.add(scrollPane);
		//searchTextChange listener
		wordSearch.addCaretListener(new searchChangeListener());
		//button clean listener
		cleanButton.addActionListener(new buttonClean());
	
	}
	
	//create menus
	private JMenu createMenus() {
		JMenu menu = new JMenu("File");
		menu.add(createFileOpenItem());
	    menu.add(createFileExitItem());
	    return menu;
	}
	private void find(){
		List searchResult = wordList.find(wordSearch.getText()); // figure out from WordList how to get this

		
	    for(Object i : searchResult) {
	    		
	    	textArea.append(i.toString() + "\n");
	    	}
        }
	//create the file-exit item
	public JMenuItem createFileExitItem()
	   {
	      JMenuItem item = new JMenuItem("Exit");      
	      class MenuItemListener implements ActionListener
	      {
	         public void actionPerformed(ActionEvent event)
	         {
	            System.exit(0);
	         }
	      }      
	      ActionListener listener = new MenuItemListener();
	      item.addActionListener(listener);
	      return item;
	   }
	
	//create the file-open item
	public JMenuItem createFileOpenItem() {
		JMenuItem item  = new JMenuItem("Open");
		/* OpenActionListener that will open the file chooser */
		class OpenActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				OpenFileListener myFileListener = new OpenFileListener();
				myFileListener.actionPerformed(e);
			}
		}
		ActionListener listener = new OpenActionListener();
	    item.addActionListener(listener);
	    return item;
	}
	
	class buttonClean implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			wordSearch.setText("");
		}
	}
	
	class searchChangeListener implements CaretListener{

		@Override
		public void caretUpdate(CaretEvent e) {
			textArea.setText("");
			find();
			textArea.setCaretPosition(0);
		}
	}
	
	class OpenFileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = jFileChooser.showOpenDialog(getParent());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					System.out.println("You chose to open this file: " + jFileChooser.getSelectedFile().getAbsolutePath());

					InputStream in = new FileInputStream(jFileChooser.getSelectedFile().getAbsolutePath());
					wordList.load(in);
					for(Object i:wordList.getWords()) {
						textArea.append(i.toString()+"\n");
					}  
				} catch (IOException error){
					error.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {

		WordFinder wordFinder = new WordFinder();
		wordFinder.setVisible(true);
	}
}
