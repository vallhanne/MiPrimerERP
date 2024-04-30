package paneles;


import javax.swing.*;

import org.example.HibernateUtils;
import org.example.PlaylistClass;

import formularios.FormularioBase;
import formularios.FormularioLista;
import formularios.FormularioListaNueva;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;
import resultados.ResultadosListas;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class PanelListas extends PanelBase {
	protected VentanaPrincipal ventanaPrincipal;
    public PanelListas(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	super(nombreTipo, filtros, configuracionManager, ventanaPrincipal);
        iniciarComponentes();
    }
    
    public void iniciarComponentes() {
    	super.iniciarComponentes();
    	iniciarListeners();
    }
    
    
    public void iniciarListeners() {
        btnBuscar.addActionListener(e -> buscarLista(comboBox.getSelectedIndex(), tfBuscador.getText().trim()));
    }
    
    @Override
    public void modoEdicion(boolean activado) {
    	super.modoEdicion(activado);
    	
        indicePestanaEditando = tabbedPanel.getSelectedIndex();
        if(indicePestanaEditando >= 0) {
        	if(tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("[Nuevo]")) {
                FormularioListaNueva pestanaEditar = (FormularioListaNueva) tabbedPanel.getSelectedComponent();
                pestanaEditar.modoEdicion(editando);
                pestanaEditar.tfDuracion.setEditable(false);
        	} else {
        		if(btnEditar.getText().equals("Guardar")) {
                    FormularioLista formularioLista = (FormularioLista) tabbedPanel.getSelectedComponent();
                    String titulo = tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
                    Integer id = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());
                    FormularioListaNueva formularioListaNueva = new FormularioListaNueva(configuracionManager, this);
                    formularioLista.copiarDatos(formularioListaNueva);
                    pestanasMostradas.remove(id);
                    tabbedPanel.remove(tabbedPanel.getSelectedComponent());
                    
                    tabbedPanel.add(titulo, formularioListaNueva);
                    pestanasMostradas.put(id, formularioListaNueva);
                    tabbedPanel.setSelectedComponent(formularioListaNueva);
                    agregarBotonCerrar(id);
                    
                    formularioListaNueva.modoEdicion(editando);
                    formularioListaNueva.tfDuracion.setEditable(false);
        		} else {
                    String titulo = tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
                    Integer id = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());
        			Object[] datosLista = HibernateUtils.buscarPlaylistPorPlaylistId(String.valueOf(id)).get(0);
        			pestanasMostradas.remove(id, tabbedPanel.getSelectedComponent());
        			tabbedPanel.remove(tabbedPanel.getSelectedComponent());
              
                    mostrarLista(titulo, datosLista);
        		}

        	}
        }
    }
    
    public void buscarLista(int indexBusqueda, String terminoBuscado) {
    	restaurarEstiloBuscador();
        //Indica al usuario si la búsqueda es válida o no
        if (terminoBuscado.isBlank() || terminoBuscado == null) {
            tfBuscador.setBackground(Color.RED);
            return;
        }
        
        //Realiza la búsqueda, almacena posibles resultados
        List<Object[]> resultados = null;
        
        if(indexBusqueda == 0 && terminoBuscado.matches("[0-9]+") && terminoBuscado.length() <= 9) {	//playlist.playlistId
            resultados = HibernateUtils.buscarPlaylistPorPlaylistId(terminoBuscado);
        } else if(indexBusqueda == 1) {	//playlist.name
            resultados = HibernateUtils.buscarPlaylistPorNombre(terminoBuscado);
        }
        
        
        if (resultados == null || resultados.isEmpty()) {
        	tfBuscador.setBackground(Color.RED);
            return;
        } 

        if (resultados.size() == 1){  //Un resultado
	        Object[] resultado = resultados.get(0);
	        String titulo = indexBusqueda == 0 ? filtros[0].toUpperCase() + ": " + terminoBuscado : filtros[1].toUpperCase() + ": " + terminoBuscado;
	        mostrarLista(titulo, resultado);
        } else {	//Varios resultados
        	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(PanelListas.this);
        	Object[] resultadoElegido = ResultadosListas.mostrarDialog(framePadre, resultados, filtros[indexBusqueda], configuracionManager);
        
        	if(resultadoElegido != null && resultadoElegido[0] != null) {	//playlistId[name]
        		PlaylistClass playlist = (PlaylistClass) resultadoElegido[0];
	            String titulo = (indexBusqueda == 0)
		                ? filtros[0].toUpperCase() + ": " + terminoBuscado + " [" + filtros[1].toUpperCase() + ": " + playlist.getName() + "]"
		                : filtros[1].toUpperCase() + ": " + terminoBuscado + " [" + filtros[0].toUpperCase() + ": " + playlist.getPlaylistId() + "]";
	            mostrarLista(titulo, resultadoElegido);
        	}
        }
    }
    
    public void mostrarLista(String titulo, Object[] resultado) {
    	PlaylistClass playlist = (PlaylistClass) resultado[0];
    	int id = playlist.getPlaylistId();
        Component pestana = pestanasMostradas.get(id);

        if (pestana != null) {
            //Si la pestaña ya está abierta, la selecciona
            tabbedPanel.setSelectedComponent(pestana);
        } else { 
        	FormularioLista lista = new FormularioLista(configuracionManager, this);
        	lista.rellenar(resultado);
            tabbedPanel.add(titulo, lista);
            tabbedPanel.setSelectedComponent(lista);
            
            pestanasMostradas.put(id, lista);
            agregarBotonCerrar(id);
        }
    }
    
	@Override
	protected FormularioBase crearFormularioEspecifico() {
		return new FormularioListaNueva(configuracionManager, this);
	}

	@Override
	protected int crearRegistro() {
		FormularioListaNueva datosLista = (FormularioListaNueva) tabbedPanel.getSelectedComponent();
		//Crear lista primero
		int idLista = crearLista(datosLista);
		
		//Crear una invoiceline por cada linea de la tabla
		int numCancionesLista = datosLista.model.getRowCount();
		for(int i=0; i<numCancionesLista; i++) {
			boolean creado = HibernateUtils.crearPlaylistTrack(idLista, (int) datosLista.model.getValueAt(i, 0));
			if(!creado) return -1;
		}
		return idLista;
	}
	
	protected int crearLista(FormularioListaNueva datosLista) {
		PlaylistClass lista = new PlaylistClass();
		lista.setName(datosLista.tfNombreLista.getText());
		return HibernateUtils.crearPlaylist(lista);
	}
	

	@Override
	protected void crearPestanaRegistro(int idRegistro) {
		Object[] listaRegistrada = HibernateUtils.buscarPlaylistPorPlaylistId(String.valueOf(idRegistro)).get(0);
		mostrarLista("PLAYLISTID: "+idRegistro, listaRegistrada);
	}

	@Override
	protected boolean editarRegistro() {
		FormularioLista datosLista = (FormularioLista) tabbedPanel.getSelectedComponent();
		Integer idLista = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());

		if(datosLista.comprobarDatosLista()) {
			PlaylistClass lista = new PlaylistClass();
			lista.setPlaylistId(idLista);
			lista.setName(Conversiones.textoVacioEnNull(datosLista.tfNombreLista.getText()));
			
			if(HibernateUtils.actualizarPlaylist(lista)) {
				int numCancionesLista = datosLista.model.getRowCount();
				for(int i=0; i<numCancionesLista; i++) {
					if(!HibernateUtils.yaExistePlaylistTrack(idLista, Integer.parseInt(String.valueOf(datosLista.model.getValueAt(i, 0))))) {
						boolean creado = HibernateUtils.crearPlaylistTrack(idLista, Integer.parseInt(String.valueOf(datosLista.model.getValueAt(i, 0))));
						if(!creado) return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}