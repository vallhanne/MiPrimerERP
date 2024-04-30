package resultados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.example.CustomerClass;
import general.ConfiguracionManager;

@SuppressWarnings("serial")
public class ResultadosClientes extends ResultadosBase<CustomerClass> {
    private CustomerClass resultadoElegido;

    public ResultadosClientes(Frame padre, List<CustomerClass> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
        super(padre, resultados, nombreFiltro, configuracionManager);
    }

    @Override
    protected void iniciarComponentes() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        if (nombreFiltro.equals("lastName")) {
            model.addColumn("Apellido del cliente");
            for (CustomerClass cliente : resultados) {
                model.addRow(new Object[]{cliente.getLastName()});
            }
        } else if (nombreFiltro.equals("customerId")) {
            model.addColumn("ID del cliente");
            for (CustomerClass cliente : resultados) {
                model.addRow(new Object[]{cliente.getCustomerId()});
            }
            
        } else if (nombreFiltro.equals("all")) {
        	model.addColumn("ID del cliente");
        	model.addColumn("Nombre del cliente");
        	model.addColumn("Apellido del cliente");
        	
        	for(CustomerClass cliente: resultados) {
        		model.addRow(new Object[]{cliente.getCustomerId(), cliente.getFirstName(), cliente.getLastName()});
        	}
        	
            table.getColumnModel().getColumn(0).setMaxWidth(40);
        }
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        resultadoElegido = resultados.get(selectedRow);
                        dispose();
                    }
                }
            }
        });
        
        centrarValoresCeldas(table);
        configurarBotonSeleccionar(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    protected void configurarBotonSeleccionar(JTable table) {
        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                resultadoElegido = resultados.get(selectedRow);
                dispose();
            } else {
                JOptionPane.showMessageDialog(panel, "Por favor, seleccione un resultado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnSeleccionar, BorderLayout.SOUTH);
    }

    public CustomerClass obtenerResultadoElegido() {
        return resultadoElegido;
    }

    public static CustomerClass mostrarDialog(JFrame padre, List<CustomerClass> resultados, String nombreColumna, ConfiguracionManager configuracionManager) {
        ResultadosClientes dialog = new ResultadosClientes(padre, resultados, nombreColumna, configuracionManager);
        dialog.setVisible(true);
        return dialog.obtenerResultadoElegido();
    }
}