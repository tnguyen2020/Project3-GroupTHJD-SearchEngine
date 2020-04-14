/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thjd.search_engine;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser; // For adding and saving files
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Team: THJD
 * Team Members: Hamzah Qasim, Jordan Adania, Thi Nguyen, and Diane Vargas
 */
public class Main_GUI extends javax.swing.JFrame {

    /**
     * Creates new form Main_GUI
     */
    
    // Creates table model object
    DefaultTableModel model;
    // Creates Index array list object. index_list holds the lines of index.txt
    ArrayList<String> index_list = new ArrayList<>();
    // Creates Index array list object. index_list holds the words of Words.txt
    ArrayList<String> words_list = new ArrayList<>();
    // Creates Search array list object. search_list holds the search words
    ArrayList<String> search_list = new ArrayList<>();
    
    public Main_GUI() {
        initComponents();
        // Table model object constructor
        model = (DefaultTableModel) tbl_index.getModel();
        
    }

    // Reads index file and display on table
    private void Read_Index(){
       // Clears the index_list of any previous contents
       index_list.clear();
       
       try {
            // Reads all lines from index.txt
            FileReader fr = new FileReader ("index.txt");
            BufferedReader br = new BufferedReader(fr);
            
            // Adds read lines into index_list
            String str;
            while ((str = br.readLine()) != null){
                index_list.add(str);
            }
            // Closes the file
            br.close();
            //System.out.println("Index Read");
            
        } catch(IOException e){
            System.out.println("index.txt File not found");
        }
    }
    
    // Writs index_list items to index.txt file and table model on GUI
    private void Update(){
        // Clear table model rows
        model.setRowCount(0);        
        
        // Place index_list items into table model to display on GUI
        for (int i = 0; i < index_list.size(); i++) {
            // Index Validation implemented
            String tbl_status = Index_Validation(index_list,i);
            
            // Spliting string on "__" character
            String[] str_split = index_list.get(i).split("__"); 
            model.insertRow(tbl_index.getRowCount(), new Object[]{
            str_split[0],
            str_split[1],
            tbl_status
        });
        }
       
        // Write index_list to index.txt, deleting all old content in index file
        try {
            FileWriter fw = new FileWriter ("index.txt");
            PrintWriter pw = new PrintWriter(fw);
            
            // Prints index_list items per line on index.txt file
            for (int i = 0; i < index_list.size(); i++){
                pw.println(index_list.get(i));
            }
            
            pw.close();
            // Call Words_Builder function
            Words_Builder(index_list);
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }
    
// Checks file index status
    private String Index_Validation (ArrayList<String> index_list, int i){
        String[] str_split = index_list.get(i).split("__"); 
        String absolute_path = str_split[1];
        File f = new File(absolute_path); // Gets the file with abs path
        if (!f.exists()){ // File not found handling
            return "File not found";
        } else if (f.lastModified() != 
                Long.parseLong(str_split[2]) ){ // File modified
            return "File Modified";
        } else { // File unchanged
            return "Indexed";
        }
        
    }
    
    // Adds file and calls Update() method
    private void Add_File() {
        try {
            // Opens add file dialogue at default user directory
            JFileChooser j = new JFileChooser();
            j.setDialogTitle("Choose file to index");
            j.showOpenDialog(null);
        
            // Validates a .txt file is chosen
            // Holds the filename
            String file_name = j.getSelectedFile().getName();
            // Holds file type
            String file_type = file_name.substring(
                    file_name.lastIndexOf("."),file_name.length());
            
            if (file_type.equals(".txt")) {
                // Build string format: fileName__absolutePath__lastModified
                String append_string = j.getSelectedFile().getName() + "__" + 
                    j.getSelectedFile().getAbsolutePath() + "__" + 
                        (j.getSelectedFile().lastModified());
        
                // Append added file name and absolute path to index_list
                index_list.add(append_string);
        
                // Call update function
                Update();
                
            } else {
                JOptionPane.showMessageDialog(null, "Only text files with .txt "
                        + " extension can be indexed");
            }
        } catch (NullPointerException e){
            System.out.println("Please select a file.");
        }
    }
    
    // Removes selected file. Calls Update() method
    private void Remove_File() {
        // If a row is selected, removed that row
        if (tbl_index.getSelectedRow() != -1) {
            index_list.remove(tbl_index.getSelectedRow());
        }
        Update();
    }
    
    // Build words from inverted index files into words.txt
    private void Words_Builder (ArrayList<String> index_list){
        // Reads words from each indexed file and adds to words_list
        try {
            // Clears the words list
            words_list.clear();
            // iterate index_list
            for (int indexID = 0; indexID < index_list.size(); indexID++){
                // Gets absolute path for each file
                String[] iteration_split = index_list.get(indexID).split("__");
                String absolute_path = iteration_split[1];
                // Reads each file
                Scanner sc = new Scanner(new File(absolute_path));
                // Appends list in format word__indexID__positionID
                int word_position = 0; // word position of file
                while (sc.hasNext()){
                    // Reads next word and removes non-letters and non-digits
                    String word = (sc.next()).replaceAll("[^a-zA-Z0-9]", "")
                            .toLowerCase();
                    // Adds words to list
                    if (!word.equals("")){
                        words_list.add(word + "__" + indexID + "__" + 
                                word_position);
                        word_position ++;
                    }
                    
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); 
        }
        
        // Write words_list to words.txt, deleting all old content in word file
        try {
            FileWriter fw = new FileWriter ("words.txt");
            PrintWriter pw = new PrintWriter(fw);
            
            // Prints words_list items per line on words.txt file
            for (int i = 0; i < words_list.size(); i++){
                pw.println(words_list.get(i));
            }
            
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Or Search
    private void Or_Search (ArrayList<String> search_list){
        search_list.clear(); // Clears list
        MatchTextArea.setText("Matched Files: \n"); // Field Header
        HashSet<String> match_set = new HashSet<String>(); //matched files set
        
        // Build search words from the text input, conditions the, builds list
        String[] search_words = SearchTextField.getText().
                replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().split(" ");
        for (int indexID = 0; indexID < search_words.length; indexID++){
            if (!search_words[indexID].equals("")){
                search_list.add(search_words[indexID]);
            }
        }
        
        // Match search_list with words_list
        for (int indexID = 0; indexID < words_list.size(); indexID++){
            // Gets the word from words.txt, removing index id and position id
            String index_word = words_list.get(indexID);
            String[] string_array = index_word.split("__");
            index_word = string_array[0];
            for (String search_word : search_list){
                // Condition check for word match
                if (search_word.equals(index_word)){
                    // Finds the absolute path of matched word
                    String[] index_entry = index_list.
                            get(Integer.parseInt(string_array[1])).split("__");
                    // Adds path to HashSet
                    match_set.add(index_entry[1]);
                }
            }
        }
        // Outputs absolute path HashSet in text field using iterator
        Iterator<String> i = match_set.iterator();
        while(i.hasNext())  {
            MatchTextArea.append(i.next() +"\n");
        } 
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        SearchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MatchTextArea = new javax.swing.JTextArea();
        Search = new javax.swing.JButton();
        AndSearchRadio = new javax.swing.JRadioButton();
        OrSearchRadio = new javax.swing.JRadioButton();
        PhraseSearchRadio = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_index = new javax.swing.JTable();
        Add_File_Button = new javax.swing.JButton();
        Rebuild_Button = new javax.swing.JButton();
        Remove_File_Button = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });

        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel1.setText("Search Engine");

        MatchTextArea.setColumns(20);
        MatchTextArea.setRows(5);
        jScrollPane1.setViewportView(MatchTextArea);

        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        buttonGroup.add(AndSearchRadio);
        AndSearchRadio.setText("All of the Search Terms (And)");

        buttonGroup.add(OrSearchRadio);
        OrSearchRadio.setText("Any of the Search Terms (OR)");

        buttonGroup.add(PhraseSearchRadio);
        PhraseSearchRadio.setText("Exact Phrase");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(AndSearchRadio)
                                        .addGap(18, 18, 18)
                                        .addComponent(OrSearchRadio))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(191, 191, 191)
                                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PhraseSearchRadio))
                            .addComponent(jScrollPane1)
                            .addComponent(SearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AndSearchRadio)
                    .addComponent(OrSearchRadio)
                    .addComponent(PhraseSearchRadio))
                .addGap(12, 12, 12)
                .addComponent(Search)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search", jPanel1);

        jPanel2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel2FocusGained(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel5.setText("Search Engine - Index Maintenance");

        tbl_index.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Path", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_index.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_index.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_index.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbl_index);

        Add_File_Button.setText("Add File");
        Add_File_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add_File_ButtonActionPerformed(evt);
            }
        });

        Rebuild_Button.setText("Rebuild Out-of-date");
        Rebuild_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Rebuild_ButtonActionPerformed(evt);
            }
        });

        Remove_File_Button.setText("Remove Selected Files");
        Remove_File_Button.setPreferredSize(new java.awt.Dimension(160, 32));
        Remove_File_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Remove_File_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(123, 123, 123)
                                .addComponent(Add_File_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Rebuild_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Remove_File_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add_File_Button)
                    .addComponent(Rebuild_Button)
                    .addComponent(Remove_File_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        jTabbedPane1.addTab("Maintenance", jPanel2);

        jLabel4.setText("Search Engine 1.1");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel3.setText("Built by THJD");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addContainerGap(457, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(398, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("About", jPanel3);

        jLabel2.setText("Number of files indexed: 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(233, 233, 233))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Add_File_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_File_ButtonActionPerformed
        // TODO add your handling code here:
        // Add_File method call
        Add_File();
    }//GEN-LAST:event_Add_File_ButtonActionPerformed

    private void Remove_File_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Remove_File_ButtonActionPerformed
        // TODO add your handling code here:
        // Remove_File method call
        Remove_File();
    }//GEN-LAST:event_Remove_File_ButtonActionPerformed

    private void Rebuild_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Rebuild_ButtonActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_Rebuild_ButtonActionPerformed

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:  
        
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel1FocusGained

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formFocusGained

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel2FocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel2FocusGained

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel2MouseEntered

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        // Code gets run when ever window is selected and reselected
        Read_Index();
        Update();
        
    }//GEN-LAST:event_formWindowActivated

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
        // Condition to check which search to perform
        if (OrSearchRadio.isSelected()){
            Or_Search(search_list);
        }
        
    }//GEN-LAST:event_SearchActionPerformed
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_GUI().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add_File_Button;
    private javax.swing.JRadioButton AndSearchRadio;
    private javax.swing.JTextArea MatchTextArea;
    private javax.swing.JRadioButton OrSearchRadio;
    private javax.swing.JRadioButton PhraseSearchRadio;
    private javax.swing.JButton Rebuild_Button;
    private javax.swing.JButton Remove_File_Button;
    private javax.swing.JButton Search;
    private javax.swing.JTextField SearchTextField;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_index;
    // End of variables declaration//GEN-END:variables
}
