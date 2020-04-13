/*
   Local Search Engine
   Java II Project 5 Submission
   Professor Wayne Pollock
   Team: Thi, Hamzah, Jordan, Diane
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
	
	private static int LOAD_WIDTH  = 800;
	private static int LOAD_HEIGHT = 400;
	private static int MIN_WIDTH   = 670;
	private static int MIN_HEIGHT  = 400;

    private 				ArrayList<String>	   keys;
    private HashMap<String, ArrayList<Integer[]>> index;
	
	private 		   JFrame frmSearchEngine;
	private DefaultTableModel tblModel2;
	private DefaultTableModel tblModel1;
    private			   JTable tblSearch;
    private			   JTable tblMainte;
    private			   JLabel lblSearch3;
    private			   JLabel lblMainte4;
    
    private String idxLbl;
    private int mode;
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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
		initializeDat();
		initializeGui();
    	loadKeys();
    	loadIndex();
    	updateMntGui();
	}
	
	private void initializeDat() {
		frmSearchEngine = new JFrame();
		keys = new ArrayList<String>();
		index = new HashMap<String, ArrayList<Integer[]>>();
		mode = -1;
	}

	private void initializeGui() {
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
			   lblSearch3 = new JLabel(idxLbl);
		JLabel lblMainte1 = new JLabel("Search Engine - Index Maintenance");
		JLabel lblMainte2 = new JLabel("File Name");
		JLabel lblMainte3 = new JLabel("Status");
			   lblMainte4 = new JLabel(idxLbl);
		JLabel lblMainte5 = new JLabel("Search Engine version 5.5");
		
		JButton btnSrcIt = new JButton("Search");
		JButton btnGoSrc = new JButton("Search Engine...");
		JButton btnGoMnt = new JButton("Maintenance...");
		JButton btnGoAbt = new JButton("About...");
		JButton btnAddFi = new JButton("Add File...");
		JButton btnRebil = new JButton("Rebuild Out-of-date");
		JButton btnRmvFi = new JButton("Remove Selected Files");
		
		JRadioButton rdbSearch1 = new JRadioButton("All Search Terms");
		JRadioButton rdbSearch2 = new JRadioButton("Any Search Terms");
		JRadioButton rdbSearch3 = new JRadioButton("Exact Phrase");
		
		JTextField txtSearch1 = new JTextField(50);

		final CardLayout cl = new CardLayout(0, 0);
		final  Container  c = frmSearchEngine.getContentPane();
		
		frmSearchEngine.setTitle("Search Engine");
		frmSearchEngine.setBounds(100, 100, LOAD_WIDTH, LOAD_HEIGHT);
		frmSearchEngine.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		frmSearchEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tblModel1 = new DefaultTableModel(0,2);
		tblModel2 = new DefaultTableModel(0,2);
		tblSearch = new JTable(tblModel1);
		tblMainte = new JTable(tblModel2);
		tblSearch.setTableHeader(null);
		tblMainte.setTableHeader(null);
		pnlSrc2.setViewportView(tblSearch);
		pnlMnt3.setViewportView(tblMainte);
		
		rdbSearch1.setSelected(true);
		
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
		
		pnlMainte2_1.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
		pnlMainte2_2.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
		
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
			@Override
			public void actionPerformed(ActionEvent e) {
				runSearch(txtSearch1.getText());
			}
		});
		btnGoMnt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(c, "Mainte");
			}
		});
		btnGoSrc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(c, "Search");
			}
		});
		btnGoAbt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
			    Desktop.getDesktop().browse(new URL("https://github.com/tnguyen2020/Project3-GroupTHJD-SearchEngine").toURI());
			} catch (Exception ex) {}
			}
		});
		btnAddFi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFiles();
			}
		});
		btnRebil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	loadKeys();
		    	loadIndex();
				saveKeys();
				saveIndex();
		    	updateMntGui();
			}
		});
		btnRmvFi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeFiles();
			}
		});
		rdbSearch1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbSearch1.setSelected(true);
				rdbSearch2.setSelected(false);
				rdbSearch3.setSelected(false);
				mode = -1;
			}
		});
		rdbSearch2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbSearch2.setSelected(true);
				rdbSearch1.setSelected(false);
				rdbSearch3.setSelected(false);
				mode = 0;
			}
		});
		rdbSearch3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbSearch3.setSelected(true);
				rdbSearch1.setSelected(false);
				rdbSearch2.setSelected(false);
				mode = 1;
			}
		});
	}
	
    // Run Search Function
	void runSearch(String in) {
		ArrayList<String> text = makeOver(in);
		switch(mode) {
		case -1:
			updateSrcGui(allSearch(text));
			break;
		case  0:
			updateSrcGui(anySearch(text));
			break;
		case  1:
			updateSrcGui(phrSearch(text));
			break;
		}
	}
	
	// OR Search Logic
	private Set<Integer> anySearch(ArrayList<String> in) {
		Set<Integer> out = new HashSet<Integer>();
		ArrayList<Integer[]> locs;
		for(String s:in)
			if(index.containsKey(s)) {
				locs = index.get(s);
				for(Integer[] k:locs)
					out.add(k[0]);
			}
		return out;
	}
	
	// AND Search Logic
	private Set<Integer> allSearch(ArrayList<String> in) {
		Set<Integer> out = new HashSet<Integer>();
		Set<Integer> docs = new HashSet<Integer>();
		ArrayList<Integer[]> locs;
		for(int b=0; b<keys.size();++b)
			out.add(b);
		for(String s:in) {
			if(index.containsKey(s)) {
				locs = index.get(s);
				for(Integer[] k:locs)
					docs.add(k[0]);
				out.retainAll(docs);
			}
			docs.clear();
		}
		return out;
	}
	
	private Set<Integer> phrSearch(ArrayList<String> in){
		Set<Integer> out = new HashSet<Integer>();
		ArrayList<Integer[]> hocs = new ArrayList<Integer[]>();
		ArrayList<Integer[]> docs = null;
		ArrayList<Integer[]> locs;
		for(String s:in)
			if(index.containsKey(s)){
				locs = index.get(s);
				if(docs==null)
					docs = locs;
				else{
					for(Integer[] i:docs)
						for(Integer[] j:locs)
							if((i[0]==j[0])&&((i[1]+1)==j[1]))
								hocs.add(new Integer[] {j[0],j[1]});
					docs = hocs;
					hocs = new ArrayList<Integer[]>();
				}
			}
		for(Integer[] i:docs)
			out.add(i[0]);
		return out;
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
					indexFile(f,keys.size()-1);
		        	tblModel2.addRow(new Object[]{f.getPath(), "Indexed"});
		            idxLbl = "Number of files indexed: " + keys.size();
		    		lblSearch3.setText(idxLbl);
		    		lblMainte4.setText(idxLbl);
				}
			}
			saveKeys();
			saveIndex();
	}
    
	// Add file to index
	private void indexFile(File f, int i){
		int pos;
		String in;
		ArrayList<String> out;
		ArrayList<Integer[]> locs;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			pos = 0;
			while((in=br.readLine())!=null){
				out = makeOver(in);
				for(String word:out) {
					if(!index.containsKey(word)) {
						locs = new ArrayList<Integer[]>();
					} else {
						locs = index.get(word);
					}
					locs.add(new Integer[]{i,pos});
					index.put(word, locs);
					++pos;
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Unknown Error");
			e.printStackTrace();
		}
	}
	
	// Remove Multiple Files
    private void removeFiles() {
    	int[] rows = tblMainte.getSelectedRows();
        if(rows!=null) {
        	for(int i=rows.length-1; i>=0; --i)
        		keys.remove(rows[i]);
        	index.clear();
        	buildIndex();
        }
		saveKeys();
		saveIndex();
        updateMntGui();
    }
	
    // Write keys to file
    private void saveKeys() {
    	String fn = "keys.txt";
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter(fn));
            for(int i=0; i<keys.size(); i++){
            	pw.println(keys.get(i));
            }
            pw.close();			
		} catch (IOException e) {
			System.out.println("Error Writing to File! ("+fn+")");
		}
    }
    
    // Write index to file
    private void saveIndex() {
    	String fn = "index.txt";
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter(fn));
			for(String s:index.keySet()) {
				pw.print(s);
				for(Integer[] i:index.get(s))
					for(Integer j:i)
						pw.print(" "+j);
				pw.println();
			}
            pw.close();			
		} catch (IOException e) {
			System.out.println("Error Writing to File! ("+fn+")");
		}
    }
    
	// Load keys from file
	private void loadKeys() {
    	String fn = "keys.txt";
		try {
			List<String> lines = Files.readAllLines(Paths.get(fn));
			keys.clear();
			for(String s:lines)
				keys.add(s);
		} catch (IOException e) {
			System.out.println("File Missing! ("+fn+")");
		}
	}
	
	// Load index from file
	private void loadIndex() {
    	String fn = "index.txt";
		ArrayList<Integer[]> locs;
		String line;
		String[] in;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fn));
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
			System.out.println("File Missing! ("+fn+")");
		}
		
	}
    
    // Rebuild Index
    private void buildIndex() {
    	for(int i=0; i<keys.size();++i) {
            indexFile(new File(getPathFromKeys(i)),i);
    	}
    }
    
	// Update Search GUI
    private void updateSrcGui(Set<Integer> in){
    	tblModel1.setRowCount(0);
    	for(Integer i:in) {
    		tblModel1.addRow(new Object[] {getPathFromKeys(i),i});
    	}
    }

	// Update Maintenance GUI
    private void updateMntGui(){
    	tblModel2.setRowCount(0);
        for(int i=0; i<keys.size(); i++){
            String status = getStatus(i);
        	String[]   in = keys.get(i).split("__");
        	tblModel2.addRow(new Object[] {in[1],status});
        }
        idxLbl = "Number of files indexed: " + keys.size();
		lblSearch3.setText(idxLbl);
		lblMainte4.setText(idxLbl);
    }
	
	// Cleans and splits a line of text
	private ArrayList<String> makeOver(String line) {
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
	
	// Is stop word?
    private boolean isStopWord(String in) {
    	String fn = "stopWords.txt";
		String line;
    	ArrayList<String> stops = new ArrayList<String>();
    	try {
			BufferedReader br = new BufferedReader(new FileReader(fn));
			while((line=br.readLine())!=null) {
				stops.add(line);
			}
			br.close();
			if(stops.contains(in))
				return true;
			else
				return false;
		} catch(IOException e){
			System.out.println("File Missing! ("+fn+")");
			return false;
		}
    }
    
    // Get path of file from keys
    private String getPathFromKeys(int in) { return keys.get(in).split("__")[1]; }
    
    // Get Last Modified time-stamp from keys
    private String getLModFromKeys(int in) { return keys.get(in).split("__")[2]; }
    
	// Get status of file(indexed, missing, modified)
    private String getStatus(int i){
        File f = new File(getPathFromKeys(i));
        if (!f.exists()){
            return "File not found";
        } else if (f.lastModified()!=Long.parseLong(getLModFromKeys(i))){
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
	
	// Set Global Font for Application
	public static void setUIFont(FontUIResource f) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource)
	            UIManager.put(key, f);
	    }
	}
    
}
