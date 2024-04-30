package resultados;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.example.GenreClass;
import general.ConfiguracionManager;

@SuppressWarnings("serial")
public class ResultadosGenero extends ResultadosBase<GenreClass> {
	private GenreClass resultadoElegido;

    public ResultadosGenero(Frame padre, List<GenreClass> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
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

        if (nombreFiltro.equals("all")) {
        	model.addColumn("ID");
        	model.addColumn("Nombre");
        	
        	for(GenreClass genero: resultados) {
        		model.addRow(new Object[]{genero.getGenreId(), genero.getName()});
        	}
        }
        
        table.getColumnModel().getColumn(0).setMaxWidth(40);

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

    public GenreClass obtenerResultadoElegido() {
        return resultadoElegido;
    }

    public static GenreClass mostrarDialog(JFrame padre, List<GenreClass> resultados, String nombreColumna, ConfiguracionManager configuracionManager) {
        ResultadosGenero dialog = new ResultadosGenero(padre, resultados, nombreColumna, configuracionManager);
        dialog.setVisible(true);
        return dialog.obtenerResultadoElegido();
    }
}