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
import java.util.ArrayList;
import java.util.Enumeration;

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
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class Main_GUI{

	private JFrame frmSearchEngine;
	private DefaultTableModel dtm = new DefaultTableModel(0,2);
    private JTable table = new JTable(dtm);
    private ArrayList<String> list = new ArrayList<String>();
	
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
		initialize();
	}

	private void initialize() {		
		JPanel    pnlSearch = new JPanel();
		JPanel    pnlMainte = new JPanel();
		JPanel   pnlSearch1 = new JPanel();
		JPanel   pnlSearch2 = new JPanel();
		JPanel   pnlSearch3 = new JPanel();
		JPanel pnlSearch1_1 = new JPanel();
		JPanel	 pnlMainte1 = new JPanel();
		JPanel	 pnlMainte2 = new JPanel();
		JPanel pnlMainte2_1 = new JPanel();
		JPanel pnlMainte2_2 = new JPanel();
		JPanel   pnlMainte4 = new JPanel();
		
		JScrollPane pnlMainte3 = new JScrollPane();
		
		JLabel lblSearch1 = new JLabel("Search Engine");
		JLabel lblSearch2 = new JLabel("Search Terms:");
		JLabel lblSearch3 = new JLabel("Number of files indexed: 0");
		JLabel lblMainte1 = new JLabel("Search Engine - Index Maintenance");
		JLabel lblMainte2 = new JLabel("File Name");
		JLabel lblMainte3 = new JLabel("Status");
		JLabel lblMainte4 = new JLabel(lblSearch3.getText());
		JLabel lblMainte5 = new JLabel("Search Engine version 1.1");
		
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
		
		frmSearchEngine = new JFrame();
		frmSearchEngine.setTitle("Search Engine");
		frmSearchEngine.setBounds(100, 100, 800, 400);
		frmSearchEngine.setMinimumSize(new Dimension(670, 400));
		frmSearchEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final CardLayout cl = new CardLayout(0, 0);
		final  Container c  = frmSearchEngine.getContentPane();

		table.setTableHeader(null);
		pnlMainte3.setViewportView(table);
		
		c.setLayout(cl);
		pnlSearch.setLayout(new MigLayout("", "[784px,grow]", "[120px][120px,grow][0]"));
		pnlSearch1.setLayout(new MigLayout("", "[grow][grow][grow]", "[][][grow]"));
		pnlSearch3.setLayout(new MigLayout("", "[32:n,grow,left][32:n,grow,shrinkprio 50,center][32:n,grow,shrinkprio 0,right]", "[]"));
		pnlMainte.setLayout(new MigLayout("", "[grow]", "[::200,grow][24:n:24][0:600,grow][64:96:128,grow]"));
		pnlMainte1.setLayout(new MigLayout("", "[grow,center]", "[grow]"));
		pnlMainte2.setLayout(new GridLayout(0, 2, 0, 0));
		pnlMainte4.setLayout(new MigLayout("", "[grow][grow][grow]", "[32:64:96,grow,top][32:64:96,grow]"));
		
		pnlSearch2.setBackground(Color.BLACK);

		lblSearch1.setFont(new Font("Courier New", Font.BOLD, 32));
		lblMainte1.setFont(new Font("Courier New", Font.BOLD, 32));
		
		pnlMainte2_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		pnlMainte2_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		
		c.add(pnlSearch, "Search");
		c.add(pnlMainte, "Mainte");
		
		pnlSearch.add(pnlSearch1, "growx");
		pnlSearch.add(pnlSearch2, "cell 0 1,grow");
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
		pnlMainte.add(pnlMainte3, "cell 0 2,grow");
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
				addFile();
			}
		});
		btnRmvFi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFile();
			}
		});
	}
	
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	            UIManager.put(key, f);
	    }
	}
	
	// Read File
	private void readIt() {
		String file;
		list.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader("index.txt"));
			while ((file = br.readLine())!=null) { 
				list.add(file);
			}
			br.close();
			System.out.println("Index Read");
		}
		catch(IOException e){
			System.out.println("index.txt File not found");
		}
	}
	
	// Update GUI and Index
    private void Update(){
    	// Clear GUI
    	dtm.setRowCount(0);
    	// Update GUI
        for(int i=0; i<list.size(); i++){
            String  idx = verifyIndex(i);
        	String[] in = list.get(i).split("__");
        	dtm.addRow(new Object[] {in[1],idx});
        }
        // Try updating the index
        try{
            PrintWriter pw = new PrintWriter(new FileWriter("index.txt"));
            for(int i=0; i<list.size(); i++){
            	pw.println(list.get(i));
            }
            pw.close();
        } catch(IOException e){
        	System.out.println("Error!");
        }
    }
	
	// Add File
	private void addFile(){
		try{
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Choose file to index");
			fc.showOpenDialog(null);
			String fName = fc.getSelectedFile().getName();
			String fType = fName.substring(fName.lastIndexOf("."),fName.length());
			if(fType.equals(".txt")){
				String out = fc.getSelectedFile().getName() + "__" +
						 fc.getSelectedFile().getAbsolutePath() + "__" +
						(fc.getSelectedFile().lastModified());
				// Checks for duplicate entries
				if(!list.contains(out)) {
					list.add(out);
					Update();
				}
            } else{ // fType.equals(".txt")
            	JOptionPane.showMessageDialog(null, ".txt ONLY");
            }
		} catch(NullPointerException e){
			System.out.println("Please select a file.");
		}
	}
	
	// Remove Selected File
    private void removeFile() {
    	byte b = (byte) table.getSelectedRow();
        if(b!=-1) list.remove(b);
        Update();
    }
	
    // Verify Index
    private String verifyIndex (int i){
        String[] str_split = list.get(i).split("__"); 
        String absolute_path = str_split[1];
        File f = new File(absolute_path);
        if (!f.exists()){
            return "File not found";
        } else if (f.lastModified() != 
                Long.parseLong(str_split[2]) ){
            return "File Modified";
        } else {
            return "Indexed";
        }
        
    }
}
