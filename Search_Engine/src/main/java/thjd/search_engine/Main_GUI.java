package thjd.search_engine;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class Main_GUI{
/*
 * 
 * YOU WERE WORKING ON ALL SEARCH
 * 
 */
	private static int LOAD_WIDTH  = 800;
	private static int LOAD_HEIGHT = 400;
	private static int MIN_WIDTH   = 670;
	private static int MIN_HEIGHT  = 400;

    private 				ArrayList<String>	   keys;
    private HashMap<String, ArrayList<Integer[]>> index;
	private 				ArrayList<Integer[]>   locs;
	
	// Can be used to increase the accuracy of word position in document
	//private HashMap<ArrayList<Integer[]>,ArrayList<Integer[]>> locsMap;
	
	private 		   JFrame frmSearchEngine;
	private DefaultTableModel tblModel1;
	private DefaultTableModel tblModel2;
    private			   JTable tblSearch;
    private			   JTable tblMainte;
    private			   JLabel lblSearch3;
    private			   JLabel lblMainte4;
    
    private byte b;
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setUIFont(new javax.swing.plaf.FontUIResource("Courier New", Font.BOLD, 14));
					Main_GUI window = new Main_GUI();
					window.frmSearchEngine.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main_GUI() {
		keys = new ArrayList<String>();
		index = new HashMap<String, ArrayList<Integer[]>>();
		// Can be used to increase the accuracy of word position in document
		//locsMap = new HashMap<ArrayList<Integer[]>,ArrayList<Integer[]>>();
		frmSearchEngine = new JFrame();
		tblModel1 = new DefaultTableModel(0,2);
		tblModel2 = new DefaultTableModel(0,2);
		tblSearch = new JTable(tblModel2);
		tblMainte = new JTable(tblModel1);
		initialize();
		loadData();
	}

	private void initialize() {		
		JPanel    pnlSearch = new JPanel();
		JPanel    pnlMainte = new JPanel();
		JPanel   pnlSearch1 = new JPanel();
		JScrollPane pnlSrc2 = new JScrollPane();
		JPanel   pnlSearch3 = new JPanel();
		JPanel pnlSearch1_1 = new JPanel();
		JPanel	 pnlMainte1 = new JPanel();
		JPanel	 pnlMainte2 = new JPanel();
		JPanel pnlMainte2_1 = new JPanel();
		JPanel pnlMainte2_2 = new JPanel();
		JScrollPane pnlMnt3 = new JScrollPane();
		JPanel   pnlMainte4 = new JPanel();
		
		JLabel lblSearch1 = new JLabel("Search Engine");
		JLabel lblSearch2 = new JLabel("Search Terms:");
		lblSearch3 = new JLabel("Number of files indexed: " + keys.size());
		JLabel lblMainte1 = new JLabel("Search Engine - Index Maintenance");
		JLabel lblMainte2 = new JLabel("File Name");
		JLabel lblMainte3 = new JLabel("Status");
		lblMainte4 = new JLabel("Number of files indexed: " + keys.size());
		JLabel lblMainte5 = new JLabel("Search Engine version 5.2");
		
		JButton  btnSrcIt = new JButton("Search");
		JButton  btnGoSrc = new JButton("Search Engine...");
		JButton  btnGoMnt = new JButton("Maintenance...");
		JButton  btnGoAbt = new JButton("About...");
		JButton  btnAddFi = new JButton("Add File...");
		JButton  btnRebil = new JButton("Rebuild Out-of-date");
		JButton  btnRmvFi = new JButton("Remove Selected Files");
		
		JRadioButton rdbSearch1 = new JRadioButton("All Search Terms");
		JRadioButton rdbSearch2 = new JRadioButton("Any Search Terms");
		JRadioButton rdbSearch3 = new JRadioButton("Exact Phrase");
		
		JTextField	 txtSearch1 = new JTextField(50);

		final CardLayout cl = new CardLayout(0, 0);
		final  Container  c = frmSearchEngine.getContentPane();
		
		frmSearchEngine.setTitle("Search Engine");
		frmSearchEngine.setBounds(100, 100, LOAD_WIDTH, LOAD_HEIGHT);
		frmSearchEngine.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		frmSearchEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tblSearch.setTableHeader(null);
		tblMainte.setTableHeader(null);
		
		pnlSrc2.setViewportView(tblSearch);
		pnlMnt3.setViewportView(tblMainte);
		
		c.setLayout(cl);
		pnlSearch.setLayout(new MigLayout("", "[784px,grow]", "[120px][120px,grow][0]"));
		pnlSearch1.setLayout(new MigLayout("", "[grow][grow][grow]", "[][][grow]"));
		pnlSearch3.setLayout(new MigLayout("", "[32:n,grow,left][32:n,grow,shrinkprio 50,center][32:n,grow,shrinkprio 0,right]", "[]"));
		pnlMainte.setLayout(new MigLayout("", "[grow]", "[::200,grow][24:n:24][0:600,grow][64:96:128,grow]"));
		pnlMainte1.setLayout(new MigLayout("", "[grow,center]", "[grow]"));
		pnlMainte2.setLayout(new GridLayout(0, 2, 0, 0));
		pnlMainte4.setLayout(new MigLayout("", "[grow][grow][grow]", "[32:64:96,grow,top][32:64:96,grow]"));

		lblSearch1.setFont(new Font("Courier New", Font.BOLD, 32));
		lblMainte1.setFont(new Font("Courier New", Font.BOLD, 32));
		
		pnlMainte2_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		pnlMainte2_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		
		c.add(pnlSearch, "Search");
		c.add(pnlMainte, "Mainte");
		
		pnlSearch.add(pnlSearch1, "cell 0 0,growx");
		pnlSearch.add(pnlSrc2, "cell 0 1,grow");
		pnlSearch.add(pnlSearch3, "cell 0 2,growx,aligny center");
		pnlSearch1.add(lblSearch1, "cell 0 0 3 1,alignx center");
		pnlSearch1.add(lblSearch2, "cell 0 1,alignx right");
		pnlSearch1.add(txtSearch1, "cell 1 1,growx");
		pnlSearch1.add(btnSrcIt, "cell 2 1");
		pnlSearch1.add(pnlSearch1_1, "cell 0 2 3 1,grow");
		pnlSearch1_1.add(rdbSearch1);
		pnlSearch1_1.add(rdbSearch2);
		pnlSearch1_1.add(rdbSearch3);
		pnlSearch3.add(btnGoMnt, "cell 0 0,alignx left");
		pnlSearch3.add(lblSearch3, "cell 1 0");
		pnlSearch3.add(btnGoAbt, "cell 2 0");
		
		pnlMainte.add(pnlMainte1, "cell 0 0,grow");
		pnlMainte.add(pnlMainte2, "cell 0 1,grow");
		pnlMainte.add(pnlMnt3, "cell 0 2,grow");
		pnlMainte.add(pnlMainte4, "cell 0 3,grow");
		pnlMainte1.add(lblMainte1, "cell 0 0");
		pnlMainte2.add(pnlMainte2_1);
		pnlMainte2.add(pnlMainte2_2);
		pnlMainte2_1.add(lblMainte2);
		pnlMainte2_2.add(lblMainte3);
		pnlMainte4.add(btnAddFi, "cell 0 0,alignx center,aligny top");
		pnlMainte4.add(btnRebil, "cell 1 0,alignx center,aligny top");
		pnlMainte4.add(btnRmvFi, "cell 2 0,alignx center,aligny top");
		pnlMainte4.add(btnGoSrc, "cell 0 1,alignx left,aligny top");
		pnlMainte4.add(lblMainte4, "cell 1 1,alignx center,aligny top");
		pnlMainte4.add(lblMainte5, "cell 2 1,alignx right,aligny top");
		

		btnSrcIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSearch(txtSearch1.getText());
			}
		});
		btnGoMnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(c, "Mainte");
			}
		});
		btnGoSrc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(c, "Search");
			}
		});
		btnGoAbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
			    Desktop.getDesktop().browse(new URL("https://github.com/tnguyen2020/Project3-GroupTHJD-SearchEngine").toURI());
			} catch (Exception ex) {}
			}
		});
		btnAddFi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFiles();
			}
		});
		btnRebil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadData();
			}
		});
		btnRmvFi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFiles();
			}
		});
		rdbSearch1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbSearch1.setSelected(true);
				rdbSearch2.setSelected(false);
				rdbSearch3.setSelected(false);
				b = -1;
			}
		});
		rdbSearch2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbSearch2.setSelected(true);
				rdbSearch1.setSelected(false);
				rdbSearch3.setSelected(false);
				b = 0;
			}
		});
		rdbSearch3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbSearch3.setSelected(true);
				rdbSearch1.setSelected(false);
				rdbSearch2.setSelected(false);
				b = 1;
			}
		});
	}
	
	public static void setUIFont(FontUIResource f) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource)
	            UIManager.put(key, f);
	    }
	}

	// Update Maintenance GUI
    private void updateMntGui(){
    	// Clear GUI
    	tblModel1.setRowCount(0);
    	// Update GUI
        for(int i=0; i<keys.size(); i++){
            String status = getStatus(i);
        	String[]   in = keys.get(i).split("__");
        	tblModel1.addRow(new Object[] {in[1],status});
        }
		lblSearch3.setText(new String("Number of files indexed: " + keys.size()));
		lblMainte4.setText(new String("Number of files indexed: " + keys.size()));
    }
	// Update Search GUI
    private void updateSrcGui(){
    	// Clear GUI
    	tblModel2.setRowCount(0);
    	// Update GUI
        for(int i=0; i<keys.size(); i++){
            String status = getStatus(i);
        	String[]   in = keys.get(i).split("__");
        	tblModel1.addRow(new Object[] {in[1],status});
        }
		lblSearch3.setText(new String("Number of files indexed: " + keys.size()));
		lblMainte4.setText(new String("Number of files indexed: " + keys.size()));
    }

	// Add file to index
	private void indexFile(File f){
		//int col, row;
		int pos;
		String in;
		ArrayList<String> out;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			//row = 0;
			pos = 0;
			while((in=br.readLine())!=null){
				//col = 0;
				out = makeOver(in);
				for(String word:out) {
					if(!index.containsKey(word)) {
						locs = new ArrayList<Integer[]>();
					} else {
						locs = index.get(word);
					}
					locs.add(new Integer[]{keys.size()-1,pos});
					// locsMap.put(locs, new ArrayList<Integer[]>());
					index.put(word, locs);
					++pos;
					//++col;
				}
				//++row;
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Unknown Error");
			e.printStackTrace();
		}
	}
	// .split("\\s+") & .trim() & .toLowerCase()
	private ArrayList<String>makeOver(String line) {
		int	i = 0;
		String[] in;
		ArrayList<String> out = new ArrayList<String>();
		in = line.trim().split("\\s+");
		for(String word:in) {
			word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
			if(!word.equals("")) {
				if(!isStopWord(word)) {
					out.add(word);
				}
			}
		}
		return out;
	}
	// ALL Search Logic full array then remove 1 by 1
	private Set<Integer>allSearch(ArrayList<String> in, Set<Integer> out) {
		// Walk Search Terms
		for(String s:in) {
			// If any term is not in the index
			if(!index.containsKey(s)) {
				// get that word's loc map
				locs = index.get(s);
				// Walk the locs
				for(Integer[] k:locs) {
					// If docID is NOT in Set is
					if(!out.contains(k[0])) {
						// Add it to the Set {docID,pos}
						out.add(k[0]);
					}// !is.contains(k[0])
				}// Integer[] k:locs
			}// index.containsKey(s)
		}// Strings:out
		return out;
	}
	// OR Search Logic
	private Set<Integer> anySearch(ArrayList<String> in, Set<Integer> out) {
		// Walk Search Terms
		for(String s:in) {
			// If term is in index
			if(index.containsKey(s)) {
				// get that word's loc map
				locs = index.get(s);
				// Walk the locs
				for(Integer[] k:locs) {
					// If docID is NOT in Set is
					if(!out.contains(k[0])) {
						// Add it to the Set {docID,pos}
						out.add(k[0]);
					}// !is.contains(k[0])
				}// Integer[] k:locs
			}// index.containsKey(s)
		}// Strings:out
		return out;
	}
    // Run Search Function
	private Set<Integer> runSearch(String in) {
		Set<Integer> out = new HashSet<Integer>();
		ArrayList<String> text = makeOver(in);
		// "All" RdBtn Selected
		if(b==-1)
			return allSearch(text, out);
		// "Any" RdBtn Selected
		if(b==0)
			return anySearch(text, out);
		// "Phrase" RdBtn Selected
		if(b==1) {
			//return phrSearch(text, out);
			
		
		updateSrcGui();
		return null;
	}// void runSearch(String)
    
    private void saveData() {
    	saveKeys();
        saveIndex();
    }
    // Write keys to file
    private void saveKeys() {
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter("keys.txt"));
            for(int i=0; i<keys.size(); i++){
            	pw.println(keys.get(i));
            }
            pw.close();			
		} catch (IOException e) {
			System.out.println("Error Writing to File!");
		}
    }
    // Write index to file
    private void saveIndex() {
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter("index.txt"));
			for(String s:index.keySet()) {
				pw.print(s);
				for(Integer[] i:index.get(s))
					for(Integer j:i)
						pw.print(" "+j);
				pw.println();
			}
            pw.close();			
		} catch (IOException e) {
			System.out.println("Error Writing to File!");
		}
    	
    }
    // Startup/Rebuild Button Pressed
    private void loadData() {
    	loadKeys();
    	loadIndex();
    	saveData();
    	updateMntGui();
    }
	// Load keys from file
	private void loadKeys() {
		try {
			List<String> lines = Files.readAllLines(Paths.get("keys.txt"));
			keys.clear();
			for(String s:lines)
				keys.add(s);
		} catch (IOException e) {
			System.out.println("Keys File Missing!");
		}
		
	}
	// Load index from file
	private void loadIndex() {
		String line;
		String[] in;
		try {
			BufferedReader br = new BufferedReader(new FileReader("index.txt"));
			index.clear();
			while((line=br.readLine())!=null){
				locs = new ArrayList<Integer[]>();
				in = line.split("\\s+");
				index.put(in[0],locs);
				for(int i=1; i<in.length;i+=2) {
					locs.add(new Integer[] {(Integer.parseInt(in[i])),(Integer.parseInt(in[i+1]))});
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Index File Missing!");
		}
		
	}
	// Add Multiple Files
	private void addFiles(){
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Choose file");
			fc.setFileFilter(new FileTypeFilter(".txt", "Plain Text File"));
			fc.setMultiSelectionEnabled(true);
			fc.showOpenDialog(null);
			File[] fs = fc.getSelectedFiles();
			for(File f:fs) {
				String out = formatKeys(f);
				if(!keys.contains(out)){
					keys.add(out);
					indexFile(f);
		        	tblModel1.addRow(new Object[] {f.getPath(),"Indexed"});
		    		lblSearch3.setText(new String("Number of files indexed: " + keys.size()));
		    		lblMainte4.setText(new String("Number of files indexed: " + keys.size()));
				}
			}
			saveData();
	}
	// Remove Multiple Files
    private void removeFiles() {
    	int[] rows = tblMainte.getSelectedRows();
        if(rows!=null)
        	for(int i=rows.length-1; i>=0; --i)
        		keys.remove(rows[i]);
        saveData();
        updateMntGui();
    }
	// Get status of file(indexed, missing, modified)
    private String getStatus (int i){
        String[] key = keys.get(i).split("__"); 
        String  path = key[1];
        File f = new File(path);
        if (!f.exists()){
            return "File not found";
        } else if (f.lastModified() != 
                Long.parseLong(key[2]) ){
            return "File Modified";
        } else {
            return "Indexed";
        }
    }
   
    // File name format for files ArrayList
	private String formatKeys(File f) {
		try{
			String fName = f.getName();
			String fType = fName.substring(fName.lastIndexOf("."),fName.length());
			if(fType.equals(".txt")){
				String fPath = f.getAbsolutePath();
				long   fLast = f.lastModified();
				return new String(fName+"__"+fPath+"__"+fLast);
			} else{
        	JOptionPane.showMessageDialog(null, ".txt ONLY");
        }
		} catch(NullPointerException e){
			System.out.println("Please select a file.");
		}
		return null;
	}
	// Is stop word?
    private boolean isStopWord(String in) {
		String line;
    	ArrayList<String> stops = new ArrayList<String>();
    	try {
			BufferedReader br = new BufferedReader(new FileReader("stopWords.txt"));
			while((line=br.readLine())!=null) {
				stops.add(line);
			}
			br.close();
			if(stops.contains(in))
				return true;
			else
				return false;
		} catch (IOException e) {
			System.out.println("Stop Words File Missing");
			return false;
		}
    }
}
