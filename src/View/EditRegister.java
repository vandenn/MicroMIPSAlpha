
package View;

import Model.Converter;
import Model.Database;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class EditRegister extends javax.swing.JFrame {

    private Database db;
    
    /**
     * Creates new form EditRegister
     */
    public EditRegister() {
        initComponents();
        db = Database.getInstance();
        initializeRegisterTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registerScrollPane = new javax.swing.JScrollPane();
        registerTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Registers");

        registerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Register", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        registerScrollPane.setViewportView(registerTable);
        if (registerTable.getColumnModel().getColumnCount() > 0) {
            registerTable.getColumnModel().getColumn(0).setMinWidth(50);
            registerTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            registerTable.getColumnModel().getColumn(0).setMaxWidth(100);
            registerTable.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(registerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(registerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void initializeRegisterTable()
    {
        Map<Integer, Long> registers = db.getRegisters();
        DefaultTableModel registerTableModel = (DefaultTableModel)registerTable.getModel();
        
        registerTableModel.setRowCount(0);
        
        int i = 0;
        Object[] data = new Object[2];
        for (Map.Entry<Integer, Long> entry : registers.entrySet())
        {
            data[0] = entry.getKey();
            String value = Converter.longToHex(entry.getValue(), 16);
            value = value.substring(0, 4) + " " + 
                        value.substring(4, 8) + " " +
                        value.substring(8, 12) + " " + 
                        value.substring(12, 16);
            data[1] = value;
            registerTableModel.addRow(data);
            i++;
        } 
        
        registerTableModel.addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent e)
            {
                String registerValuePattern = "^([0-9A-Fa-f]{4} {1}){3}[0-9A-Fa-f]{4}$";
                
                int row = e.getFirstRow();
                int col = e.getColumn();
                
                String value = (String)registerTableModel.getValueAt(row, col);
                int register = (int)registerTableModel.getValueAt(row, 0);
                Boolean success = false;
                if (value.matches(registerValuePattern))
                {
                    value = value.replaceAll("\\s","");
                    long registerValue = Converter.hexToLong(value);
                    success = db.editRegister(register, registerValue);
                }
                
                if (!success)
                {
                    Map registers = db.getRegisters();
                    String recoveryValue = Long.toString(Database.DEFAULT_REGISTER_VALUE);
                    if(registers.containsKey(register))
                    {
                        recoveryValue = Converter.longToHex((long)registers.get(register), 16);
                        recoveryValue = recoveryValue.substring(0, 4) + " " + 
                                recoveryValue.substring(4, 8) + " " +
                                recoveryValue.substring(8, 12) + " " + 
                                recoveryValue.substring(12, 16);
                    }

                    registerTableModel.setValueAt(recoveryValue, row, col);
                }
            }
        });
        
        registerTable.setModel(registerTableModel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane registerScrollPane;
    private javax.swing.JTable registerTable;
    // End of variables declaration//GEN-END:variables
}