package formularios;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import paneles.PanelTemas;

import org.example.HibernateUtils;
import org.example.PlaylistClass;
import org.example.TrackClass;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;

import javax.swing.JTable;


@SuppressWarnings("serial")
public class FormularioTema extends FormularioBase {
	public JLabel lblNombreTema;
	public JTextField tfNombreTema;
	
	public JLabel lblAlbum;
	public JTextField tfAlbumId;
	
	public JLabel lblArtista;
	public JTextField tfArtistaId;
	
	public JLabel lblGenero;
	public JTextField tfGeneroId;
	
	public JLabel lblSoporte;
	public JTextField tfSoporteId;
	
	public JLabel lblCompositor;
	public JTextField tfCompositor;
	public JTextField tfGenero;
	public JTextField tfSoporte;
	public JLabel lblDuracion;
	public JTextField tfDuracion;
	public JLabel lblPrecio;
	public JTextField tfPrecio;
	public JTextField tfAlbum;
	public JTextField tfArtista;
	
	public JTable table;
	public JScrollPane scrollPane;
	public DefaultTableModel model;

	
	public FormularioTema(ConfiguracionManager configuracionManager, PanelTemas panelTemas) {
		super(configuracionManager, panelTemas);
		setLayout(new MigLayout("", "[][20.00][29.00][grow][grow]", "[][][][][][][][grow]"));
		
		lblNombreTema = new JLabel("Nombre de la canción");
		add(lblNombreTema, "cell 0 0 2 1,aligny center");
		tfNombreTema = new JTextField();
		add(tfNombreTema, "cell 2 0 3 1,gapx 10,height 20!,grow");
		
		lblAlbum = new JLabel("Album");
		add(lblAlbum, "cell 0 1,aligny center");
		tfAlbumId = new JTextField();
		add(tfAlbumId, "cell 1 1,height 20!,grow");
		
		lblArtista = new JLabel("Artista");
		add(lblArtista, "cell 0 2,aligny center");
		tfArtistaId = new JTextField();
		add(tfArtistaId, "cell 1 2,height 20!,grow");
		
		lblGenero = new JLabel("Genero");
		add(lblGenero, "cell 0 3,aligny center");
		tfGeneroId = new JTextField();
		add(tfGeneroId, "cell 1 3,height 20!,grow");
		
		lblSoporte = new JLabel("Soporte");
		add(lblSoporte, "cell 0 4,aligny center");
		tfSoporteId = new JTextField();
		add(tfSoporteId, "cell 1 4,height 20!,grow");
		
		lblCompositor = new JLabel("Compositor");
		add(lblCompositor, "cell 0 5,aligny center");
		tfGenero = new JTextField();
		add(tfGenero, "cell 2 3 3 1,height 20!,grow");
		tfSoporte = new JTextField();
		add(tfSoporte, "cell 2 4 3 1,height 20!,grow");
		tfCompositor = new JTextField();
		add(tfCompositor, "cell 1 5 2 1,gapx 10,height 20!,grow");
		
		lblDuracion = new JLabel("Duración");
		add(lblDuracion, "flowx,cell 3 5,alignx right");
		
		tfDuracion = new JTextField();
		add(tfDuracion, "cell 4 5,growx,gapx 10");
		tfDuracion.setColumns(10);
		
		lblPrecio = new JLabel("Precio");
		add(lblPrecio, "cell 0 6");
		
		tfAlbum = new JTextField();
		add(tfAlbum, "cell 2 1 3 1,height 20!,grow");
		tfAlbum.setColumns(10);
		
		tfArtista = new JTextField();
		add(tfArtista, "cell 2 2 3 1,growx");
		tfArtista.setColumns(10);
		
		tfPrecio = new JTextField();
		add(tfPrecio, "cell 1 6 2 1,height 20!,grow");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 7 5 1,gapy 20,grow");
		modelarTabla();

		modoEdicion(false);
		setVisible(true);
	}

    
	public void rellenar(Object[] resultado) {
	    TrackClass track = resultado[0] != null ? (TrackClass) resultado[0] : null;
	    
	    tfNombreTema.setText(track != null ? track.getName() : null);
	    tfAlbumId.setText(track != null ? track.getAlbumId().toString() : null);
	    tfGeneroId.setText(track != null ? track.getGenreId().toString() : null);
	    tfSoporteId.setText(track != null ? String.valueOf(track.getMediaTypeId()) : null);
	    tfCompositor.setText(track != null ? track.getComposer() : null);
	    tfDuracion.setText(track != null ? String.valueOf(Conversiones.convertirMilisegundos(track.getMilliseconds())) : null);
	    tfPrecio.setText(track != null ? track.getUnitPrice().toString() : null);

	    tfAlbum.setText(resultado[1] != null ? (String) resultado[1] : null);
	    tfArtistaId.setText(resultado[2] != null ? resultado[2].toString() : null);
	    tfArtista.setText(resultado[3] != null ? (String) resultado[3] : null);
	    tfGenero.setText(resultado[4] != null ? (String) resultado[4] : null);
	    tfSoporte.setText(resultado[5] != null ? (String) resultado[5] : null);	
	
	    //Rellenar tabla de listas
	    if(track != null) {
		    List<PlaylistClass> listasConTema = HibernateUtils.buscarPlaylistsConTrackId(String.valueOf(track.getTrackId()));
            listasConTema.forEach(lista -> {
                model.addRow(new Object[]{lista.getPlaylistId(), lista.getName()});
            });
	    } 
	}
	
	private void modelarTabla() {
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel(new Object[][] {},
				new String[] {"ID", "Nombre de listas donde aparece"}) {
				boolean[] columnEditables = new boolean[] {false, false};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(20);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	public boolean comprobarDatosTema() {
		boolean esValido = true;
		restaurarEstiloCampos();
		
		if(!Comprobaciones.esStringValido(tfNombreTema.getText(), 200, false)) {
			tfNombreTema.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfCompositor.getText(), 220, true)) {
			tfCompositor.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esDecimalValido(tfPrecio.getText(), 10, 2, false)) {
			tfPrecio.setBackground(Color.RED);
			esValido = false;
		}
		
		return esValido;
	}

}
	

