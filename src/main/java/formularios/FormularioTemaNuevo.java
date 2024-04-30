package formularios;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.example.AlbumClass;
import org.example.ArtistClass;
import org.example.GenreClass;
import org.example.HibernateUtils;
import org.example.MediaTypeClass;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import net.miginfocom.swing.MigLayout;
import paneles.PanelTemas;
import resultados.ResultadosAlbum;
import resultados.ResultadosArtista;
import resultados.ResultadosGenero;
import resultados.ResultadosSoporte;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FormularioTemaNuevo extends FormularioBase {
	public JLabel lblNombreTema;
	public JTextField tfNombreTema;
	
	public JLabel lblAlbum;
	public JLabel lblArtista;
	public JLabel lblGenero;
	public JLabel lblSoporte;
	
	public JLabel lblCompositor;
	public JTextField tfGenero;
	public JTextField tfSoporte;
	public JLabel lblDuracion;
	public JLabel lblPrecio;
	public JTextField tfPrecio;
	public JTextField tfAlbum;
	public JTextField tfArtista;
	
	public JButton btnElegirAlbum;
	public JButton btnElegirArtista;
	public JButton btnElegirGenero;
	public JButton btnElegirSoporte;
	public JLabel lblMin;
	public JTextField tfSegs;
	public JLabel lblSegs;
	public JTextField tfMin;
	
	private JButton btnNuevoAlbum;
	private JButton btnNuevoArtista;
	private JButton btnNuevoGenero;
	private JButton btnNuevoSoporte;
	public JTextField tfCompositor;
	
	public Integer albumId;
	public Integer artistaId;
	public Integer generoId;
	public Integer soporteId;
	
	public FormularioTemaNuevo(ConfiguracionManager configuracionManager, PanelTemas panelTemas) {
		super(configuracionManager, panelTemas);
		setLayout(new MigLayout("", "[25.00][9.00][30.00][60.00][86.00][grow][grow]", "[][][][][][][]"));
		
		lblNombreTema = new JLabel("Nombre de la canción");
		add(lblNombreTema, "cell 0 0 3 1,aligny center");
		tfNombreTema = new JTextField();
		add(tfNombreTema, "cell 3 0 4 1,height 20!,grow");
		
		lblAlbum = new JLabel("Album");
		add(lblAlbum, "cell 0 1,aligny center");
		
		btnElegirAlbum = new JButton("Elegir");
		add(btnElegirAlbum, "flowx,cell 1 1 2 1");
		
		btnNuevoAlbum = new JButton("Nuevo");
		add(btnNuevoAlbum, "cell 3 1");
		
		tfAlbum = new JTextField();
		tfAlbum.setEnabled(false);
		add(tfAlbum, "cell 4 1 3 1,height 20!,grow");
		tfAlbum.setColumns(10);
		
		lblArtista = new JLabel("Artista");
		add(lblArtista, "cell 0 2,aligny center");
		
		btnElegirArtista = new JButton("Elegir");
		add(btnElegirArtista, "cell 1 2 2 1");
		
		btnNuevoArtista = new JButton("Nuevo");
		add(btnNuevoArtista, "cell 3 2");
		
		tfArtista = new JTextField();
		tfArtista.setEnabled(false);
		add(tfArtista, "cell 4 2 3 1,growx");
		tfArtista.setColumns(10);
		
		lblGenero = new JLabel("Genero");
		add(lblGenero, "cell 0 3,aligny center");
		
		btnElegirGenero = new JButton("Elegir");
		add(btnElegirGenero, "cell 1 3 2 1");
		
		btnNuevoGenero = new JButton("Nuevo");
		add(btnNuevoGenero, "cell 3 3");
		tfGenero = new JTextField();
		tfGenero.setEnabled(false);
		add(tfGenero, "cell 4 3 3 1,height 20!,grow");
		
		lblSoporte = new JLabel("Soporte");
		add(lblSoporte, "cell 0 4,aligny center");
		
		btnElegirSoporte = new JButton("Elegir");
		add(btnElegirSoporte, "cell 1 4 2 1,gapx 10");
		
		btnNuevoSoporte = new JButton("Nuevo");
		add(btnNuevoSoporte, "cell 3 4,gapx 10");
		tfSoporte = new JTextField();
		tfSoporte.setEnabled(false);
		add(tfSoporte, "cell 4 4 3 1,gapx 10,height 20!,grow");
		
		lblCompositor = new JLabel("Compositor");
		add(lblCompositor, "cell 0 5 2 1,aligny center");
		
		tfCompositor = new JTextField();
		add(tfCompositor, "cell 2 5 3 1,growx,gapx 10");
		tfCompositor.setColumns(10);
		
		lblDuracion = new JLabel("Duración");
		add(lblDuracion, "flowx,cell 5 5,gapx 10,aligny center");
		
		tfMin = new JTextField();
		add(tfMin, "cell 5 5,growx");
		tfMin.setColumns(10);
		
		lblPrecio = new JLabel("Precio");
		add(lblPrecio, "cell 0 6");
		tfPrecio = new JTextField();
		add(tfPrecio, "cell 1 6 3 1,height 20!,grow");
		
		tfSegs = new JTextField();
		add(tfSegs, "flowx,cell 6 5 2 1,growx");
		tfSegs.setColumns(10);
		
		lblSegs = new JLabel("s");
		add(lblSegs, "cell 6 5 2 1");
		
		lblMin = new JLabel("min");
		add(lblMin, "cell 5 5");
		
        btnElegirAlbum.addActionListener(e -> elegirAlbum());
        btnElegirArtista.addActionListener(e -> elegirArtista());
		btnElegirGenero.addActionListener(e -> elegirGenero());
		btnElegirSoporte.addActionListener(e -> elegirSoporte());
        
        btnNuevoAlbum.addActionListener(e -> nuevoSubregistro(btnNuevoAlbum, tfAlbum));
        btnNuevoArtista.addActionListener(e -> nuevoSubregistro(btnNuevoArtista, tfArtista));
        btnNuevoSoporte.addActionListener(e -> nuevoSubregistro(btnNuevoSoporte, tfSoporte));
        btnNuevoGenero.addActionListener(e -> nuevoSubregistro(btnNuevoGenero, tfGenero));

		setVisible(true);
	}


	public boolean comprobarDatosTema() {
		boolean esValido = true;
		restaurarEstiloCampos();
		
		if(!Comprobaciones.esStringValido(tfNombreTema.getText(), 200, false)) {
			tfNombreTema.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfArtista.getText(), 120, false) || artistaId == null) {
			System.out.println(artistaId);
			tfArtista.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfAlbum.getText(), 160, false) || albumId == null) {
			tfAlbum.setBackground(Color.RED);
			esValido = false;
		}

		if(!Comprobaciones.esStringValido(tfGenero.getText(), 120, false) || generoId == null) {
			tfGenero.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfSoporte.getText(), 120, false) || tfSoporte == null) {
			tfSoporte.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfCompositor.getText(), 220, true)) {
			tfCompositor.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esIntValido(tfMin.getText(), false)) {
			tfMin.setBackground(Color.RED);
			esValido = false;
		} else {
			int min = Integer.parseInt(tfMin.getText());
			if(min<0) {
				tfMin.setBackground(Color.RED);
				esValido = false;
			}
		}
		
		if(!Comprobaciones.esIntValido(tfSegs.getText(), false)) {
			tfSegs.setBackground(Color.RED);
			esValido = false;
		} else {
			int segs = Integer.parseInt(tfSegs.getText());
			if(segs<0 || segs>59) {
				tfMin.setBackground(Color.RED);
				esValido = false;
			}
		}
		
		if(!Comprobaciones.esDecimalValido(tfPrecio.getText(), 10, 2, false)) {
			tfPrecio.setBackground(Color.RED);
			esValido = false;
		}
		
		return esValido;
	}
	
	public void elegirAlbum() {
		List<AlbumClass> listaAlbumes = HibernateUtils.buscarTodosAlbumes();
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        AlbumClass albumElegido = ResultadosAlbum.mostrarDialog(framePadre, listaAlbumes, "all", configuracionManager);
        
        if(albumElegido != null) {
        	albumId = albumElegido.getAlbumId();
        	tfAlbum.setText(albumElegido.getTitle());
        	
        	//Automaticamente rellena artista
        	ArtistClass artistaAlbum = HibernateUtils.buscarArtistaPorId(albumElegido.getArtistId());
        	if(artistaAlbum != null) {
            	artistaId = artistaAlbum.getArtistId();
            	tfArtista.setText(artistaAlbum.getName());
        	}
        }
	}
	
	public void elegirArtista() {
		List<ArtistClass> listaArtistas = HibernateUtils.buscarTodosArtistas();
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        ArtistClass artistaElegido = ResultadosArtista.mostrarDialog(framePadre, listaArtistas, "all", configuracionManager);
        
        if(artistaElegido != null) {
        	artistaId = artistaElegido.getArtistId();
        	tfArtista.setText(artistaElegido.getName());
        }
	}
	
	public void elegirSoporte() {
		List<MediaTypeClass> listaSoportes = HibernateUtils.buscarTodosSoportes();
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        MediaTypeClass soporteElegido = ResultadosSoporte.mostrarDialog(framePadre, listaSoportes, "all", configuracionManager);
        
        if(soporteElegido != null) {
        	soporteId = soporteElegido.getMediaTypeId();
        	tfSoporte.setText(soporteElegido.getName());
        }
	}
	
	public void elegirGenero() {
		List<GenreClass> listaGeneros = HibernateUtils.buscarTodosGeneros();
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        GenreClass generoElegido = ResultadosGenero.mostrarDialog(framePadre, listaGeneros, "all", configuracionManager);
        
        if(generoElegido != null) {
        	generoId = generoElegido.getGenreId();
        	tfGenero.setText(generoElegido.getName());
        }
	}
	
	public void nuevoSubregistro(JButton btn, JTextField tf) {
		if(btn.getText().equals("Nuevo")) {
			accesoRestoComponentes(btn, tf, false);
			tf.setText("");
			btn.setText("Guardar");
		} else {
			if(Conversiones.textoVacioEnNull(tf.getText()) == null) {
				accesoRestoComponentes(btn, tf, false);
			} else {
				accesoRestoComponentes(btn, tf, true);
				
	            if (btn == btnNuevoAlbum) {
	                albumId = 0;
	            } else if (btn == btnNuevoArtista) {
	                artistaId = 0;
	            } else if (btn == btnNuevoGenero) {
	                generoId = 0;
	            } else if (btn == btnNuevoSoporte) {
	                soporteId = 0;
	            }
	            
				btn.setText("Nuevo");
			}
		}
	}
	
	public void accesoRestoComponentes(JButton btn, JTextField tf, boolean accesible) {
	    for (Component comp : this.getComponents()) {
	        if (comp instanceof JButton && comp != btn) {
	            comp.setEnabled(accesible);
	        }
	        
	        if (comp instanceof JTextField && comp != tf) {
	            comp.setEnabled(accesible);
	        }
	    }
	    
        //Siempre inhablitido por defecto
        tfAlbum.setEnabled(false);
        tfArtista.setEnabled(false);
        tfGenero.setEnabled(false);
        tfSoporte.setEnabled(false);
	    
        //Siempre habilitado
        btn.setEnabled(true);
        
        //Resto casos según accesibilidad
	    panelBase.btnEditar.setEnabled(accesible);  
	    
	    if(!accesible) {
		    tf.setEnabled(true);
	    }
	}
}