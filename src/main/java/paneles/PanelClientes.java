package paneles;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.example.CustomerClass;
import org.example.HibernateUtils;
import formularios.FormularioCliente;
import formularios.FormularioBase;
import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;
import resultados.ResultadosClientes;

@SuppressWarnings("serial")
public class PanelClientes extends PanelBase {
	
    public PanelClientes(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	super(nombreTipo, filtros, configuracionManager, ventanaPrincipal);
        iniciarComponentes();
    }

    @Override
    public void iniciarComponentes() {
    	super.iniciarComponentes();
        btnBuscar.addActionListener(e -> buscarCliente(comboBox.getSelectedIndex(), tfBuscador.getText().trim()));
    }

    public void buscarCliente(int indexBusqueda, String terminoBuscado) {
    	restaurarEstiloBuscador();

	    //Indica al usuario si la búsqueda es válida o no (rojo)
	    if (terminoBuscado.isBlank()) {
	        tfBuscador.setBackground(Color.RED);
	        return;
	    }

        //Realiza la búsqueda, almacena posibles resultados
	    List<CustomerClass> resultados = null;

	    if (indexBusqueda == 0) {
	        resultados = HibernateUtils.buscarClientesPorApellido(terminoBuscado);
	    } else if (indexBusqueda == 1 && Comprobaciones.esIntValido(terminoBuscado, false)) {
	        resultados = HibernateUtils.buscarClientesPorId(terminoBuscado);
	    }

	    if (resultados == null || resultados.isEmpty()) {
	        tfBuscador.setBackground(Color.RED);
	        return;
	    }

	    if (resultados.size() == 1) { //Un resultado
	        CustomerClass resultado = resultados.get(0);
	        String titulo = indexBusqueda == 0 ? filtros[0].toUpperCase() + ": " + terminoBuscado : filtros[1].toUpperCase() + ": " + terminoBuscado;
	        mostrarCliente(titulo, resultado);
	    } else { //Varios resultados
	        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(PanelClientes.this);
	        CustomerClass resultadoElegido = ResultadosClientes.mostrarDialog(framePadre, resultados, filtros[indexBusqueda], configuracionManager);
	        if (resultadoElegido != null) {
	            String titulo = (indexBusqueda == 0)
	                ? filtros[0].toUpperCase() + ": " + terminoBuscado + " [" + filtros[1].toUpperCase() + ": " + resultadoElegido.getCustomerId() + "]"
	                : filtros[1].toUpperCase() + ": " + terminoBuscado + " [" + filtros[0].toUpperCase() + ": " + resultadoElegido.getLastName() + "]";
	            mostrarCliente(titulo, resultadoElegido);
	        }
	    }
    }
    
    public void mostrarCliente(String titulo, CustomerClass resultado) {
    	int id = resultado.getCustomerId();
        Component pestana = pestanasMostradas.get(id);

        if (pestana != null) {
            //Si la pestaña ya está abierta, la selecciona
            tabbedPanel.setSelectedComponent(pestana);
        } else { 
        	FormularioCliente cliente = new FormularioCliente(configuracionManager, this);
        	cliente.rellenar(resultado);
            tabbedPanel.add(titulo, cliente);
            tabbedPanel.setSelectedComponent(cliente);
            
            pestanasMostradas.put(id, cliente);
            agregarBotonCerrar(id);        
        }
        
        tabbedPanel.revalidate();
        tabbedPanel.repaint();
    }
    
    @Override
    public FormularioBase crearFormularioEspecifico() {
    	return new FormularioCliente(configuracionManager, this);
    }
    
    @Override
    public int crearRegistro() {
    	FormularioCliente datosCliente = (FormularioCliente) tabbedPanel.getSelectedComponent();
    	if(datosCliente.comprobarDatosCliente()) {
        	CustomerClass nuevoCliente = new CustomerClass();
    		nuevoCliente.setFirstName(Conversiones.textoVacioEnNull(datosCliente.tfNombreCliente.getText()));
    		nuevoCliente.setLastName(Conversiones.textoVacioEnNull(datosCliente.tfApellidoCliente.getText()));
    		nuevoCliente.setCompany(Conversiones.textoVacioEnNull(datosCliente.tfEmpresa.getText()));
    		nuevoCliente.setAddress(Conversiones.textoVacioEnNull(datosCliente.tfDireccion.getText()));
    		nuevoCliente.setCity(Conversiones.textoVacioEnNull(datosCliente.tfCiudad.getText()));
    		nuevoCliente.setState(Conversiones.textoVacioEnNull(datosCliente.tfEstado.getText()));
    		nuevoCliente.setCountry(Conversiones.textoVacioEnNull(datosCliente.tfPais.getText()));
    		nuevoCliente.setPostalCode(Conversiones.textoVacioEnNull(datosCliente.tfCodigoPostal.getText()));
    		nuevoCliente.setPhone(Conversiones.textoVacioEnNull(datosCliente.tfTelefono.getText()));
    		nuevoCliente.setFax(Conversiones.textoVacioEnNull(datosCliente.tfMovil.getText()));
    		nuevoCliente.setEmail(Conversiones.textoVacioEnNull(datosCliente.tfEmail.getText()));
    		nuevoCliente.setSupportRepId(Conversiones.textoEnIntONull(datosCliente.tfSoporte.getText()));
    		nuevoCliente.setEntryDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosCliente.tfFechaAlta.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
    		
    		return HibernateUtils.crearCliente(nuevoCliente);
    	} else {
    		return -1;
    	}
    }
    
    @Override
    public void crearPestanaRegistro(int idRegistro) {
    	CustomerClass clienteRegistrado = HibernateUtils.buscarClientesPorId(String.valueOf(idRegistro)).get(0);
    	mostrarCliente("CUSTOMERID: "+idRegistro, clienteRegistrado);
    }
    
    @Override
    public boolean editarRegistro() {
    	FormularioCliente datosCliente = (FormularioCliente) tabbedPanel.getSelectedComponent();
    	Integer idCliente = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());

    	if(datosCliente.comprobarDatosCliente() && idCliente!=null) {
        	CustomerClass cliente = new CustomerClass();
        	cliente.setCustomerId(Conversiones.textoEnIntONull(idCliente.toString()));
        	cliente.setFirstName(Conversiones.textoVacioEnNull(datosCliente.tfNombreCliente.getText()));
        	cliente.setLastName(Conversiones.textoVacioEnNull(datosCliente.tfApellidoCliente.getText()));
        	cliente.setCompany(Conversiones.textoVacioEnNull(datosCliente.tfEmpresa.getText()));
        	cliente.setAddress(Conversiones.textoVacioEnNull(datosCliente.tfDireccion.getText()));
        	cliente.setCity(Conversiones.textoVacioEnNull(datosCliente.tfCiudad.getText()));
        	cliente.setState(Conversiones.textoVacioEnNull(datosCliente.tfEstado.getText()));
        	cliente.setCountry(Conversiones.textoVacioEnNull(datosCliente.tfPais.getText()));
        	cliente.setPostalCode(Conversiones.textoVacioEnNull(datosCliente.tfCodigoPostal.getText()));
        	cliente.setPhone(Conversiones.textoVacioEnNull(datosCliente.tfTelefono.getText()));
        	cliente.setFax(Conversiones.textoVacioEnNull(datosCliente.tfMovil.getText()));
        	cliente.setEmail(Conversiones.textoVacioEnNull(datosCliente.tfEmail.getText()));
        	cliente.setSupportRepId(Conversiones.textoEnIntONull(datosCliente.tfSoporte.getText()));
        	cliente.setEntryDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosCliente.tfFechaAlta.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
    		
    		return HibernateUtils.actualizarCliente(cliente);
    	} else {
    		return false;
    	}
    }
    


}