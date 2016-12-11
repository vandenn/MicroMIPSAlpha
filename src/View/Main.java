
package View;

import Model.ErrorLogData;
import Model.InstructionPipeline;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import lib.TextLineNumber;
import Model.Parser;
import Model.Processor;
import Model.Step;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

public class Main extends javax.swing.JFrame {

    private final JFileChooser fc = new JFileChooser();
    private Processor processor;
    private Boolean finished;
    
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        
        processor = null;
        finished = false;
        
        TextLineNumber codeAreaTLN = new TextLineNumber(codeArea);
        codeAreaScrollPane.setRowHeaderView(codeAreaTLN);
        TextLineNumber opcodeAreaTLN = new TextLineNumber(opcodeArea);
        opcodeAreaScrollPane.setRowHeaderView(opcodeAreaTLN);
        
        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        runSingleMenuItem.setAccelerator(f7);
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        runFullMenuItem.setAccelerator(f4);
        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
        resetMenuItem.setAccelerator(ctrlR);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        loadCodeMenuItem.setAccelerator(ctrlO);
        
        DefaultCaret internalRegCaret = (DefaultCaret)internalRegTextArea.getCaret();
        internalRegCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        DefaultCaret opcodeCaret = (DefaultCaret)opcodeArea.getCaret();
        opcodeCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        pipelineTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MIPS64 Files", "txt", "asm");
        fc.setFileFilter(filter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codeAreaScrollPane = new javax.swing.JScrollPane();
        codeArea = new javax.swing.JTextArea();
        opcodeAreaScrollPane = new javax.swing.JScrollPane();
        opcodeArea = new javax.swing.JTextArea();
        codeLabel = new javax.swing.JLabel();
        opcodeLabel = new javax.swing.JLabel();
        internalRegLabel = new javax.swing.JLabel();
        internalRegScrollPane = new javax.swing.JScrollPane();
        internalRegTextArea = new javax.swing.JTextArea();
        pipelineLabel = new javax.swing.JLabel();
        pipelineScrollPane = new javax.swing.JScrollPane();
        pipelineTable = new javax.swing.JTable();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadCodeMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editRegistersMenuItem = new javax.swing.JMenuItem();
        editMemoryMenuItem = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        runSingleMenuItem = new javax.swing.JMenuItem();
        runFullMenuItem = new javax.swing.JMenuItem();
        resetMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        aboutThisProjectMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MicroMIPS");

        codeArea.setColumns(20);
        codeArea.setRows(5);
        codeAreaScrollPane.setViewportView(codeArea);

        opcodeAreaScrollPane.setHorizontalScrollBar(null);

        opcodeArea.setEditable(false);
        opcodeArea.setColumns(20);
        opcodeArea.setRows(5);
        opcodeAreaScrollPane.setViewportView(opcodeArea);

        codeLabel.setText("Code");

        opcodeLabel.setText("Opcode");

        internalRegLabel.setText("Internal MIPS64 Registers");

        internalRegTextArea.setEditable(false);
        internalRegTextArea.setColumns(20);
        internalRegTextArea.setRows(5);
        internalRegScrollPane.setViewportView(internalRegTextArea);

        pipelineLabel.setText("Pipeline");

        pipelineTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pipelineScrollPane.setViewportView(pipelineTable);
        if (pipelineTable.getColumnModel().getColumnCount() > 0) {
            pipelineTable.getColumnModel().getColumn(0).setResizable(false);
        }

        fileMenu.setText("File");

        loadCodeMenuItem.setText("Load code from file..");
        loadCodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCodeMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadCodeMenuItem);

        mainMenuBar.add(fileMenu);

        editMenu.setText("Edit");

        editRegistersMenuItem.setText("Edit Registers..");
        editRegistersMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRegistersMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editRegistersMenuItem);

        editMemoryMenuItem.setText("Edit Memory..");
        editMemoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMemoryMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editMemoryMenuItem);

        mainMenuBar.add(editMenu);

        runMenu.setText("Run");

        runSingleMenuItem.setText("Run Single");
        runSingleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSingleMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(runSingleMenuItem);

        runFullMenuItem.setText("Run Full");
        runFullMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runFullMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(runFullMenuItem);

        resetMenuItem.setText("Reset");
        resetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(resetMenuItem);

        mainMenuBar.add(runMenu);

        aboutMenu.setText("About");

        aboutThisProjectMenuItem.setText("About this Project..");
        aboutThisProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutThisProjectMenuItemActionPerformed(evt);
            }
        });
        aboutMenu.add(aboutThisProjectMenuItem);

        mainMenuBar.add(aboutMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codeAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(codeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(opcodeLabel)
                            .addComponent(opcodeAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(internalRegLabel)
                            .addComponent(internalRegScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pipelineLabel)
                            .addComponent(pipelineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codeLabel)
                    .addComponent(opcodeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(opcodeAreaScrollPane)
                    .addComponent(codeAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(internalRegLabel)
                    .addComponent(pipelineLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(internalRegScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(pipelineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editRegistersMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRegistersMenuItemActionPerformed
        EditRegister er = new EditRegister();
        er.setVisible(true);
    }//GEN-LAST:event_editRegistersMenuItemActionPerformed

    private void editMemoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMemoryMenuItemActionPerformed
        EditMemory em = new EditMemory();
        em.setVisible(true);
    }//GEN-LAST:event_editMemoryMenuItemActionPerformed

    private void runSingleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSingleMenuItemActionPerformed
        if (finished) return;
        Boolean noInitialError = true;
        if (processor == null) noInitialError = startRun();
        if (!noInitialError) return;
        Boolean success = singleStep();
        if (!success)
        {
            if (processor.getErrors().size() > 0)
            {
                ErrorLog el = new ErrorLog(processor.getErrors());
                el.setVisible(true);
            }
            finished = true;
            processor = null;
        }
        else
        {
            updateUIAfterSingleStep();
        }
    }//GEN-LAST:event_runSingleMenuItemActionPerformed

    private void runFullMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runFullMenuItemActionPerformed
        if (finished) return;
        Boolean noInitialError = true;
        if (processor == null) noInitialError = startRun();
        if (!noInitialError) return;
        try
        {
            Thread runMultiple = new Thread((new Runnable(){
                @Override
                public void run(){
                    setUIEnabled(false);
                    while(singleStep()){
                        SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run(){
                                updateUIAfterSingleStep();
                            }
                        });
                        try { Thread.sleep(200); }
                        catch(Exception e) {}
                    }
                    if (processor.getErrors().size() > 0)
                    {
                        ErrorLog el = new ErrorLog(processor.getErrors());
                        el.setVisible(true);
                    }
                    finished = true;
                    processor = null;
                    setUIEnabled(true);
                }
            }));
            runMultiple.start();
        }
        catch (Exception e)
        {
            ErrorLog el = new ErrorLog(new ErrorLogData(-1, "Unhandled Thread Exception!"));
            el.setVisible(true);
            finished = true;
            processor = null;
        }
    }//GEN-LAST:event_runFullMenuItemActionPerformed

    private void aboutThisProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutThisProjectMenuItemActionPerformed
        About a = new About();
        a.setVisible(true);
    }//GEN-LAST:event_aboutThisProjectMenuItemActionPerformed

    private void resetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMenuItemActionPerformed
        reset();
    }//GEN-LAST:event_resetMenuItemActionPerformed

    private void loadCodeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCodeMenuItemActionPerformed
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File codeFile = fc.getSelectedFile();
            try
            {
                FileReader fr = new FileReader(codeFile);
                BufferedReader br = new BufferedReader(fr);
                
                String codeLine = "";
                StringBuilder sb = new StringBuilder();
                while ((codeLine = br.readLine()) != null)
                {
                    sb.append(codeLine);
                    sb.append("\n");
                }
                
                codeArea.setText(sb.toString());
            }
            catch (Exception e) {}
        }
    }//GEN-LAST:event_loadCodeMenuItemActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    
    private Boolean startRun()
    {
        Parser p = new Parser();
        if(p.parseCode(codeArea.getText()))
        {
            opcodeArea.setText(p.getOpcodeText());
            processor = new Processor();
            return true;
        }
        else
        {
            ErrorLog el = new ErrorLog(p.getErrors());
            el.setVisible(true);
            return false;
        }
    }
    
    private Boolean singleStep()
    {
        if (processor == null || !processor.singleStep()) return false;
        return true;
    }
    
    private void updateUIAfterSingleStep()
    {
        if (processor == null) return;
        printInternalRegisters();
        printPipeline();
    }
    
    private void printInternalRegisters()
    {
        internalRegTextArea.setText(processor.irs.printRegisters());
    }
    
    private void printPipeline()
    {
        TreeMap<Integer, InstructionPipeline> pipeline = (TreeMap)processor.pipeline;
        
        Vector columns = new Vector();
        Vector rows = new Vector();
        Vector row = new Vector();
        
        columns.add("Instruction");
        
        for (int i = 0; i < pipelineTable.getColumnCount(); i++)
        {
            columns.add(i + 1);
        }
        
        for (int i = 0; i < pipelineTable.getRowCount(); i++)
        {
            row = new Vector();
            for (int j = 0; j < pipelineTable.getColumnCount(); j++)
            {
                Object value = pipelineTable.getValueAt(i, j);
                row.add(value == null ? "" : value);
            }
            if (pipeline.containsKey(i + 1))
            {
                InstructionPipeline curr = pipeline.get(i + 1);
                if (curr.getCurrentStep() != null && curr.getCurrentStep() != Step.END && (i+1 < processor.getCurrentStall() && processor.getCurrentStall() >= 0 || processor.getCurrentStall() < 0))
                {
                    row.add(curr.getCurrentStep());
                }
                else
                {
                    row.add("");
                }
            }
            rows.add(row);
        }
        
        if (pipeline.lastEntry().getKey() > pipelineTable.getRowCount())
        {
            row = new Vector();
            String instType = "";
            if (pipeline.lastEntry().getValue().getInstructionType() != null) instType = pipeline.lastEntry().getValue().getInstructionType().toString();
            row.add(instType);
            for (int i = 1; i < pipelineTable.getColumnCount(); i++)
            {
                row.add("");
            }
            InstructionPipeline curr = pipeline.lastEntry().getValue();
            if (curr.getCurrentStep() != null && curr.getCurrentStep() != Step.END && (pipeline.lastEntry().getKey() < processor.getCurrentStall() && processor.getCurrentStall() >= 0 || processor.getCurrentStall() < 0))
            {
                row.add(curr.getCurrentStep());
            }
            else
            {
                row.add("");
            }
            rows.add(row);
        }
        
        DefaultTableModel pipelineTableModel = new DefaultTableModel(rows, columns);
        pipelineTable.setModel(pipelineTableModel);
        pipelineTable.scrollRectToVisible(pipelineTable.getCellRect(pipelineTable.getRowCount()-1, pipelineTable.getColumnCount(), true));
    }
    
    private void reset()
    {
        opcodeArea.setText("");
        internalRegTextArea.setText("");
        resetPipeline();
        processor = null;
        finished = false;
    }
    
    private void resetPipeline()
    {
        Vector columns = new Vector();
        columns.add("Instruction");
        Vector rows = new Vector();
        DefaultTableModel model = new DefaultTableModel(rows, columns);
        pipelineTable.setModel(model);
    }
    
    private void setUIEnabled(Boolean enabled)
    {
        fileMenu.setEnabled(enabled);
        editMenu.setEnabled(enabled);
        runMenu.setEnabled(enabled);
        aboutMenu.setEnabled(enabled);
        codeArea.setEnabled(enabled);
        runSingleMenuItem.setEnabled(enabled);
        runFullMenuItem.setEnabled(enabled);
        resetMenuItem.setEnabled(enabled);
        loadCodeMenuItem.setEnabled(enabled);
        editRegistersMenuItem.setEnabled(enabled);
        editMemoryMenuItem.setEnabled(enabled);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem aboutThisProjectMenuItem;
    private javax.swing.JTextArea codeArea;
    private javax.swing.JScrollPane codeAreaScrollPane;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JMenuItem editMemoryMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editRegistersMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel internalRegLabel;
    private javax.swing.JScrollPane internalRegScrollPane;
    private javax.swing.JTextArea internalRegTextArea;
    private javax.swing.JMenuItem loadCodeMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JTextArea opcodeArea;
    private javax.swing.JScrollPane opcodeAreaScrollPane;
    private javax.swing.JLabel opcodeLabel;
    private javax.swing.JLabel pipelineLabel;
    private javax.swing.JScrollPane pipelineScrollPane;
    private javax.swing.JTable pipelineTable;
    private javax.swing.JMenuItem resetMenuItem;
    private javax.swing.JMenuItem runFullMenuItem;
    private javax.swing.JMenu runMenu;
    private javax.swing.JMenuItem runSingleMenuItem;
    // End of variables declaration//GEN-END:variables
}
