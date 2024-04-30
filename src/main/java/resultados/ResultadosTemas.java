package resultados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.example.TrackClass;
import general.ConfiguracionManager;


@SuppressWarnings("serial")
public class ResultadosTemas extends ResultadosBase<Object[]> {
    private Object[] resultadoElegido;
    
    public ResultadosTemas(Frame padre, List<Object[]> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
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
    
    protected void configurarModeloTabla(DefaultTableModel model) {
    	if(nombreFiltro.equals("name")) {
    		model.addColumn("Título del disco");
            resultados.forEach(resultado -> {
            	model.addRow(new Object[]{resultado[1]});

            });
    	} else if(nombreFiltro.equals("title")) {
    		model.addColumn("Nombre de la canción");
    		resultados.forEach(resultado -> {
            	TrackClass track = (TrackClass)resultado[0];
            	model.addRow(new Object[]{track.getName()});
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
        ResultadosTemas dialog = new ResultadosTemas(padre, resultados, nombreColumna, configuracionManager);
        dialog.setVisible(true);
        return dialog.obtenerResultadoElegido();
    }
}
