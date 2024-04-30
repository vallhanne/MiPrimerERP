package resultados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.example.PlaylistClass;
import general.ConfiguracionManager;
import general.Conversiones;

@SuppressWarnings("serial")
public class ResultadosListas extends ResultadosBase<Object[]> {
    private Object[] resultadoElegido;
    
    public ResultadosListas(Frame padre, List<Object[]> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
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
        
    	if(nombreFiltro.equals("playlistId")) {
    		model.addColumn("Nombre de lista");
            resultados.forEach(resultado -> {
            	PlaylistClass playlist = (PlaylistClass) resultado[0];
            	model.addRow(new Object[]{playlist.getName()});
            });
    	} else if(nombreFiltro.equals("name")) {
    		model.addColumn("ID de lista");
            resultados.forEach(resultado -> {
            	PlaylistClass playlist = (PlaylistClass) resultado[0];
            	model.addRow(new Object[]{playlist.getPlaylistId()});
            });
    	} else if(nombreFiltro.equals("tracks")) {
    		model.addColumn("ID");
    		model.addColumn("Nombre de la canción");
    		model.addColumn("Nombre del album");
    		model.addColumn("Duración");

            resultados.forEach(resultado -> {
            	model.addRow(new Object[]{resultado[0], resultado[1], resultado[2], Conversiones.convertirMilisegundos(Long.parseLong(resultado[3].toString()))});
            });
            
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
    
    public Object[] obtenerResultadoElegido() {
        return resultadoElegido;
    }
    
    public static Object[] mostrarDialog(JFrame padre, List<Object[]> resultados, String nombreColumna, ConfiguracionManager configuracionManager) {
        ResultadosListas dialog = new ResultadosListas(padre, resultados, nombreColumna, configuracionManager);
        dialog.setVisible(true);
        return dialog.obtenerResultadoElegido();
    }
}
