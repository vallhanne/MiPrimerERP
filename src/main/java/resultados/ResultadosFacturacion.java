package resultados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.example.InvoiceClass;
import general.ConfiguracionManager;

@SuppressWarnings("serial")
public class ResultadosFacturacion extends ResultadosBase<Object[]> {
    private Object[] resultadoElegido;

    public ResultadosFacturacion(Frame padre, List<Object[]> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
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

        configurarModeloTabla(model);

        centrarValoresCeldas(table);
        configurarBotonSeleccionar(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void configurarModeloTabla(DefaultTableModel model) {
        if (nombreFiltro.equals("customerId")) {
            model.addColumn("ID de la factura");
            resultados.forEach(resultado -> {
                InvoiceClass invoice = (InvoiceClass) resultado[0];
                model.addRow(new Object[]{invoice.getInvoiceId()});
            });
        } else if (nombreFiltro.equals("invoiceId")) {
            model.addColumn("ID del cliente");
            resultados.forEach(resultado -> {
                InvoiceClass invoice = (InvoiceClass) resultado[0];
                model.addRow(new Object[]{invoice.getCustomerId()});
            });
        } else if (nombreFiltro.equals("tracks")) {
        	model.addColumn("Tema");
        	model.addColumn("Album");
        	model.addColumn("Precio ud.");
        	
            resultados.forEach(resultado -> {
                model.addRow(new Object[]{resultado[1], resultado[2], resultado[3]});
            });
        }
        
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

    public Object[] obtenerResultadoElegido() {
        return resultadoElegido;
    }

    public static Object[] mostrarDialog(JFrame padre, List<Object[]> resultados, String nombreColumna, ConfiguracionManager configuracionManager) {
        ResultadosFacturacion dialog = new ResultadosFacturacion(padre, resultados, nombreColumna, configuracionManager);
        dialog.setVisible(true);
        return dialog.obtenerResultadoElegido();
    }
}

//package dialogos;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.util.List;
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.text.JTextComponent;
//
//import org.example.InvoiceClass;
//
//import general.ConfiguracionManager;
//
//import javax.swing.border.Border;
//import javax.swing.border.EmptyBorder;
//
//@SuppressWarnings("serial")
//public class ResultadosFacturacion extends JDialog {
//    private final List<Object[]> resultados;
//    private final JPanel panel;
//    private Object[] resultadoElegido;
//	private String nombreFiltro;
//	private ConfiguracionManager configuracionManager;
//    
//    public ResultadosFacturacion(Frame padre, List<Object[]> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
//        super(padre, "Resultados", true);
//        this.resultados = resultados;
//        this.nombreFiltro = nombreFiltro;
//        this.panel = new JPanel(new BorderLayout());
//        this.configuracionManager = configuracionManager;
//        
//        iniciarComponentes();
//        iniciarCierreVentana();
//        aplicarEsquemaColores();
//    }
//    
//    
//    public Object[] obtenerResultadoElegido() {
//        return resultadoElegido;
//    }
//    
//
//    private void iniciarComponentes() {
//        DefaultTableModel model = new DefaultTableModel();
//        JTable table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//        
//    	if(nombreFiltro.equals("customerId")) {
//    		model.addColumn("ID de la factura");
//            for (Object[] resultado : resultados) {
//            	InvoiceClass invoice = (InvoiceClass) resultado[0];
//            	model.addRow(new Object[]{invoice.getInvoiceId()});
//            }
//    	} else if(nombreFiltro.equals("invoiceId")) {
//    		model.addColumn("ID del cliente");
//            for (Object[] resultado : resultados) {
//            	InvoiceClass invoice = (InvoiceClass) resultado[0];
//            	model.addRow(new Object[]{invoice.getCustomerId()});
//            }
//    	}
//
//    	centrarValoresCeldas(table, scrollPane);
//    }
//    
//    
//    public static Object[] mostrarDialog(JFrame padre, List<Object[]> resultados, String nombreColumna, ConfiguracionManager configuracionManager) {
//        ResultadosFacturacion dialog = new ResultadosFacturacion(padre, resultados, nombreColumna, configuracionManager);
//        dialog.setVisible(true);
//        return dialog.obtenerResultadoElegido();
//    }
//    
//    private void iniciarCierreVentana() {
//        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                if (resultadoElegido != null) {
//                    dispose();
//                } else {
//                	UIManager.put("OptionPane.yesButtonText", "Si");
//                    int opcion = JOptionPane.showConfirmDialog(panel, "¿Deseas cerrar sin seleccionar un resultado?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
//                    if (opcion == JOptionPane.YES_OPTION) {
//                        dispose();
//                    }
//                }
//            }
//        });
//    }
//    
//    private void centrarValoresCeldas(JTable table, JScrollPane scrollPane) {
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        table.setDefaultRenderer(Object.class, centerRenderer);
//        
//        JButton btnSeleccionar = new JButton("Seleccionar");
//        btnSeleccionar.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int selectedRow = table.getSelectedRow();
//                if (selectedRow != -1) {
//                	resultadoElegido = resultados.get(selectedRow);
//                    dispose();
//                } else {
//                    JOptionPane.showMessageDialog(panel, "Por favor, seleccione un resultado.", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        
//        Border border = panel.getBorder();
//        Border marginBorder = new EmptyBorder(10, 10, 10, 10);
//        panel.setBorder(BorderFactory.createCompoundBorder(border, marginBorder));
//
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(btnSeleccionar, BorderLayout.SOUTH);
//        getContentPane().add(panel);
//        pack();
//        setLocationRelativeTo(null);
//    }
//    
//    private void aplicarEsquemaColores() {
//        boolean modoOscuro = configuracionManager.obtenerPropiedad("modoOscuro").equals("true");
//        cambiarColoresComponentes(this, modoOscuro ? Color.DARK_GRAY : Color.WHITE, modoOscuro ? Color.WHITE : Color.BLACK);
//    }
//    
//    public void cambiarColoresComponentes(Component comp, Color fondo, Color texto) {
//    	comp.setBackground(fondo);
//        comp.setForeground(texto);
//
//        if (comp instanceof JLabel || comp instanceof JButton || comp instanceof JComboBox || comp instanceof JTextComponent || comp instanceof JTable || comp instanceof JOptionPane) {
//            comp.setForeground(texto);
//        } else if (comp instanceof Container) {
//            for (Component c : ((Container) comp).getComponents()) {
//                cambiarColoresComponentes(c, fondo, texto);
//            }
//        }
//    }
//}
