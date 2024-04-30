package formularios;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.example.HibernateUtils;
import org.example.PlaylistClass;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import net.miginfocom.swing.MigLayout;
import paneles.PanelListas;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class FormularioLista extends FormularioBase {
	public JLabel lblNombreLista;
	public JTextField tfNombreLista;
	
	public JLabel lblDuracion;
	public JTextField tfDuracion;
	
	public JTable table;
	public JScrollPane scrollPane;
	public DefaultTableModel model;
	
	public FormularioLista(ConfiguracionManager configuracionManager, PanelListas panelListas) {
		super(configuracionManager, panelListas);
		setLayout(new MigLayout("", "[][][grow]", "[][][grow]"));

		lblNombreLista = new JLabel("Nombre de la lista");
		add(lblNombreLista, "cell 0 0 2 1,aligny center");
		tfNombreLista = new JTextField();
		add(tfNombreLista, "cell 2 0,gapx 10,height 20!,grow");
		
		lblDuracion = new JLabel("Duración total");
		add(lblDuracion, "flowx,cell 0 1,aligny center");
		tfDuracion = new JTextField();
		add(tfDuracion, "cell 1 1 2 1,gapx 10,height 20!,grow");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 2 3 1,gapy 20,grow");
		modelarTabla();

		modoEdicion(false);
		setVisible(true);
	}

	
	 public boolean comprobarDatosLista() {
		 boolean esValido = true;
		 
		 if(!Comprobaciones.esStringValido(tfNombreLista.getText(), 120, true)) {
			 tfNombreLista.setBackground(Color.RED);
			 esValido = false;
		 }
		 
		 return esValido;
	 }
    
	public void rellenar(Object[] resultado) {
		PlaylistClass playlist = (PlaylistClass) resultado[0] != null ? (PlaylistClass) resultado[0] : null;
	    tfNombreLista.setText(playlist != null ? playlist.getName() : null);		
	    tfDuracion.setText(resultado[1] != null ? Conversiones.convertirMilisegundos(Long.parseLong(resultado[1].toString())) : null);
	
	    //Rellenar tabla de canciones
	    if(playlist != null) {
		    List<Object[]> cancionesEnLista = HibernateUtils.buscarCancionesEnPlaylist(String.valueOf(playlist.getPlaylistId()));
		    cancionesEnLista.forEach(cancion -> {
                model.addRow(new Object[]{cancion[0].toString(), cancion[1].toString(), cancion[2].toString(), Conversiones.convertirMilisegundos(Long.parseLong(cancion[3].toString()))});
            });
	    }
	}
	
	private void modelarTabla() {
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel(new Object[][] {},
				new String[] {"ID", "Nombre de la canción", "Nombre del album", "Duración"}) {
				boolean[] columnEditables = new boolean[] {false, false, false, false};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			
		table.setModel(model);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(80);
		table.getColumnModel().getColumn(3).setMaxWidth(400);
		
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	public void copiarDatos(FormularioListaNueva formularioNueva) {
	    formularioNueva.tfNombreLista.setText(tfNombreLista.getText());
	    formularioNueva.tfDuracion.setText(tfDuracion.getText());

	    // Copiar los datos de la tabla
	    DefaultTableModel modelNuevo = (DefaultTableModel) formularioNueva.table.getModel();
	    modelNuevo.setRowCount(0); // Limpiar la tabla antes de copiar los datos

	    for (int i = 0; i < model.getRowCount(); i++) {
	        Object[] fila = new Object[model.getColumnCount()];
	        for (int j = 0; j < model.getColumnCount(); j++) {
	            fila[j] = model.getValueAt(i, j);
	        }
	        modelNuevo.addRow(fila);
	    }
	}

}
