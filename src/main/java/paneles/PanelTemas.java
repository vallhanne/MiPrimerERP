package paneles;


import javax.swing.*;

import org.example.AlbumClass;
import org.example.ArtistClass;
import org.example.GenreClass;
import org.example.HibernateUtils;
import org.example.MediaTypeClass;
import org.example.TrackClass;

import formularios.FormularioBase;
import formularios.FormularioTema;
import formularios.FormularioTemaNuevo;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;
import resultados.ResultadosTemas;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class PanelTemas extends PanelBase {
	protected VentanaPrincipal ventanaPrincipal;
	
    public PanelTemas(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	super(nombreTipo, filtros, configuracionManager, ventanaPrincipal);
        iniciarComponentes();
    }

    public void iniciarComponentes() {
    	super.iniciarComponentes();
        btnBuscar.addActionListener(e -> buscarTema(comboBox.getSelectedIndex(), tfBuscador.getText().trim()));
    }
    
    @Override
    public void modoEdicion(boolean activado) {
    	super.modoEdicion(activado);
    	
        indicePestanaEditando = tabbedPanel.getSelectedIndex();
        if(indicePestanaEditando >= 0) {
        	if(!tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("[Nuevo]")) {
                FormularioTema pestanaEditar = (FormularioTema) tabbedPanel.getSelectedComponent();
                pestanaEditar.modoEdicion(editando);
                pestanaEditar.tfAlbumId.setEditable(false);
                pestanaEditar.tfAlbum.setEditable(false);
                pestanaEditar.tfArtistaId.setEditable(false);
                pestanaEditar.tfArtista.setEditable(false);
                pestanaEditar.tfSoporteId.setEditable(false);
                pestanaEditar.tfSoporte.setEditable(false);
                pestanaEditar.tfGeneroId.setEditable(false);
                pestanaEditar.tfGenero.setEditable(false);
                pestanaEditar.tfDuracion.setEditable(false);
        	}
        }
    }

	public void buscarTema(int indexBusqueda, String terminoBuscado) {
    	restaurarEstiloBuscador();

        //Indica al usuario si la búsqueda es válida o no
        if (terminoBuscado.isBlank() || terminoBuscado == null) {
            tfBuscador.setBackground(Color.RED);
            return;
        }
        
        //Realiza la búsqueda, almacena posibles resultados
        List<Object[]> resultados = null;
        
        if(indexBusqueda == 0) {  //track.name
        	resultados = HibernateUtils.buscarCancionPorNombre(terminoBuscado);
        } else if(indexBusqueda == 1) {	//album.title
        	resultados = HibernateUtils.buscarCancionPorAlbum(terminoBuscado);
        }
        
        if (resultados == null || resultados.isEmpty()) {
        	tfBuscador.setBackground(Color.RED);
            return;
        } 
        
		if (resultados.size() == 1){  //Un resultado
	        Object[] resultado = resultados.get(0);
	        String titulo = indexBusqueda == 0 ? filtros[0].toUpperCase() + ": " + terminoBuscado : filtros[1].toUpperCase() + ": " + terminoBuscado;
	        mostrarTema(titulo, resultado);
		
        } else {  //Varios resultados
        	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(PanelTemas.this);
        	Object[] resultadoElegido = ResultadosTemas.mostrarDialog(framePadre, resultados, filtros[1], configuracionManager);	//track.name[album.title]
        	if(resultadoElegido != null && resultadoElegido[0] != null) {
        		TrackClass track = (TrackClass) resultadoElegido[0];
	            String titulo = (indexBusqueda == 0)
		                ? filtros[0].toUpperCase() + ": " + terminoBuscado + " [" + filtros[1].toUpperCase() + ": " + resultadoElegido[1] + "]"
		                : filtros[1].toUpperCase() + ": " + terminoBuscado + " [" + filtros[0].toUpperCase() + ": " + track.getName() + "]";
		            mostrarTema(titulo, resultadoElegido);
        	}
        }
    }
    
    public void mostrarTema(String titulo, Object[] resultado) {
        TrackClass track = (TrackClass) resultado[0];
        int id = track.getTrackId();
        Component pestana = pestanasMostradas.get(id);

        if (pestana != null) {
            //Si la pestaña ya está abierta, la selecciona
            tabbedPanel.setSelectedComponent(pestana);
        } else { 
            FormularioTema tema = new FormularioTema(configuracionManager, this);
            tema.rellenar(resultado);
            tabbedPanel.add(titulo, tema);
            tabbedPanel.setSelectedComponent(tema);

            pestanasMostradas.put(id, tema);
            agregarBotonCerrar(id);
        }
        
        tabbedPanel.revalidate();
        tabbedPanel.repaint();
    }
    
	@Override
	protected FormularioBase crearFormularioEspecifico() {
		return new FormularioTemaNuevo(configuracionManager, this);
	}
	
	@Override
	protected int crearRegistro() {
		FormularioTemaNuevo datosTema = (FormularioTemaNuevo) tabbedPanel.getSelectedComponent();
		
		if(datosTema.comprobarDatosTema()) {
			//Comprobar Artista
			int artistaId = crearArtista(datosTema);
			if(artistaId == -1) {
				datosTema.tfArtista.setBackground(Color.RED);
				return -1;
			}

			//Comprobar Album
			int albumId = crearAlbum(datosTema, artistaId);
			if(albumId == -1) {
				datosTema.tfAlbum.setBackground(Color.RED);
				return -1;
			}
			
			//Comprobar Genero
			int generoId = crearGenero(datosTema);
			if(generoId == -1) {
				datosTema.tfGenero.setBackground(Color.RED);
				return -1;
			}
			
			//Comprobar Soporte
			int soporteId = crearSoporte(datosTema);
			if(soporteId == -1) {
				datosTema.tfSoporte.setBackground(Color.RED);
				return -1;
			}
			
			//Si todo sale bien crear tema
			TrackClass nuevoTema = new TrackClass();
			nuevoTema.setName(Conversiones.textoVacioEnNull(datosTema.tfNombreTema.getText()));
			nuevoTema.setAlbumId(albumId);
			nuevoTema.setMediaTypeId(soporteId);
			nuevoTema.setGenreId(generoId);
			nuevoTema.setComposer(Conversiones.textoVacioEnNull(datosTema.tfCompositor.getText()));
			
			int min = Conversiones.textoEnIntONull(datosTema.tfMin.getText());
			int segs = Conversiones.textoEnIntONull(datosTema.tfSegs.getText());
			nuevoTema.setMilliseconds(Conversiones.convertirAMilisegundos(min, segs));
			//Falta bytes, no se indica en niguna parte
			nuevoTema.setUnitPrice(Conversiones.textoEnDecimalONull(datosTema.tfPrecio.getText()));

			return HibernateUtils.crearCancion(nuevoTema);
		} else {
			return -1;
		}
	}
	
	protected int crearArtista(FormularioTemaNuevo datosTema) {
		if(datosTema.artistaId > 0) {	//Se supone que ya existe
			if(!HibernateUtils.existeArtistaPorId(datosTema.artistaId)) return -1;
		} else {	//Intenta crear uno
			ArtistClass nuevoArtista = new ArtistClass();
			nuevoArtista.setName(datosTema.tfArtista.getText());
			return HibernateUtils.crearArtista(nuevoArtista);
		}
		return datosTema.artistaId;
	}
	
	protected int crearAlbum(FormularioTemaNuevo datosTema, int artistaId) {
		if(datosTema.albumId > 0) {
			if(!HibernateUtils.existeAlbumPorId(datosTema.albumId)) {
				return -1;
			} else {	//Existe pero no cuadra el artista
				if(!HibernateUtils.esAlbumDeArtistaId(datosTema.albumId, artistaId)) {	
					AlbumClass nuevoAlbum = new AlbumClass();
					nuevoAlbum.setTitle(datosTema.tfArtista.getText());
					nuevoAlbum.setArtistId(artistaId);
					return HibernateUtils.crearAlbum(nuevoAlbum); //Crea album con ese nombre para el artista
				}
			}
		} else {	//Intenta crear uno
			AlbumClass nuevoAlbum = new AlbumClass();
			nuevoAlbum.setTitle(datosTema.tfArtista.getText());
			nuevoAlbum.setArtistId(artistaId);
			return HibernateUtils.crearAlbum(nuevoAlbum);
		}
		return datosTema.albumId;
	}
	
	protected int crearGenero(FormularioTemaNuevo datosTema) {
		if(datosTema.generoId > 0) {
			if(!HibernateUtils.existeGeneroPorId(datosTema.generoId)) return -1;
		} else {
			GenreClass nuevoGenero = new GenreClass();
			nuevoGenero.setName(datosTema.tfGenero.getText());
			return HibernateUtils.crearGenero(nuevoGenero);
		}
		
		return datosTema.generoId;
	}
	
	protected int crearSoporte(FormularioTemaNuevo datosTema) {
		if(datosTema.soporteId > 0) {
			if(!HibernateUtils.existeSoportePorId(datosTema.soporteId)) return -1;
		} else {
			MediaTypeClass nuevoSoporte = new MediaTypeClass();
			nuevoSoporte.setName(datosTema.tfSoporte.getText());
			return HibernateUtils.crearSoporte(nuevoSoporte);
		}
		
		return datosTema.soporteId;
	}
	
	@Override
	protected void crearPestanaRegistro(int idRegistro) {
		Object[] temaRegistrado = HibernateUtils.buscarCancionPorId(String.valueOf(idRegistro)).get(0);
		mostrarTema("TRACKID: "+idRegistro, temaRegistrado);
	}

	@Override
	protected boolean editarRegistro() {
		FormularioTema datosTema = (FormularioTema) tabbedPanel.getSelectedComponent();
		Integer idTema = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());
		
		if(datosTema.comprobarDatosTema() && idTema!=null) {
			TrackClass tema = new TrackClass();
			tema.setTrackId(idTema);
			tema.setName(datosTema.tfNombreTema.getText());
			tema.setComposer(datosTema.tfCompositor.getText());
			tema.setUnitPrice(Conversiones.textoEnDecimalONull(datosTema.tfPrecio.getText()));
			return HibernateUtils.actualizarCancion(tema);
		} else {
			return false;
		}
	}

}