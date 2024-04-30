package paneles;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.example.EmployeeClass;
import org.example.HibernateUtils;
import formularios.FormularioEmpleado;
import formularios.FormularioBase;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;
import resultados.ResultadosEmpleados;

@SuppressWarnings("serial")
public class PanelEmpleados extends PanelBase {

    public PanelEmpleados(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	super(nombreTipo, filtros, configuracionManager, ventanaPrincipal);
        iniciarComponentes();
    }
    
    @Override
    public void iniciarComponentes() {
    	super.iniciarComponentes();
        btnBuscar.addActionListener(e -> buscarEmpleado(comboBox.getSelectedIndex(), tfBuscador.getText().trim()));

    }
    
    public void buscarEmpleado(int indexBusqueda, String terminoBuscado) {
    	restaurarEstiloBuscador();

        //Indica al usuario si la búsqueda es válida o no (rojo)
        if (terminoBuscado.isBlank() || terminoBuscado == null) {
            tfBuscador.setBackground(Color.RED);
            return;
        }

        //Realiza la búsqueda, almacena posibles resultados
        List<EmployeeClass> resultados = null;
        
        if(indexBusqueda == 0) {	//employee.lastName
            resultados = HibernateUtils.buscarEmpleadoPorApellido(terminoBuscado);
        } else if(indexBusqueda == 1) {	//employee.title
        	resultados = HibernateUtils.buscarEmpleadoPorPuesto(terminoBuscado);
        }

        if (resultados == null || resultados.isEmpty()) {
        	tfBuscador.setBackground(Color.RED);
            return;
        }
	    
	    if(resultados.size() == 1) { //Un resultado
	    	EmployeeClass resultado =  resultados.get(0);
	    	String titulo = indexBusqueda == 0 ? filtros[0].toUpperCase() + ": " + terminoBuscado : filtros[1].toUpperCase() + ": " + terminoBuscado;
	    	mostrarEmpleado(titulo, resultado);
        } else {  //Varios resultados
        	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(PanelEmpleados.this);
        	EmployeeClass resultadoElegido = ResultadosEmpleados.mostrarDialog(framePadre, resultados, filtros[indexBusqueda], configuracionManager);
        	if(resultadoElegido != null) {
	            String titulo = (indexBusqueda == 0)
	                ? filtros[0].toUpperCase() + ": " + terminoBuscado + " [" + filtros[1].toUpperCase() + ": " + resultadoElegido.getTitle() + "]"
	                : filtros[1].toUpperCase() + ": " + terminoBuscado + " [" + filtros[0].toUpperCase() + ": " + resultadoElegido.getLastName() + "]";
	            mostrarEmpleado(titulo, resultadoElegido);
        	}
        }
    }
    
    public void mostrarEmpleado(String titulo, EmployeeClass resultado) {
    	int id = resultado.getEmployeeId();
        Component pestana = pestanasMostradas.get(id);

        if (pestana != null) {
            //Si la pestaña ya está abierta, la selecciona
            tabbedPanel.setSelectedComponent(pestana);
        } else { 
        	FormularioEmpleado empleado = new FormularioEmpleado(configuracionManager, this, ventanaPrincipal);
        	empleado.rellenar(resultado);
            tabbedPanel.add(titulo, empleado);
            tabbedPanel.setSelectedComponent(empleado);
            
            pestanasMostradas.put(id, empleado);
            agregarBotonCerrar(id);        
        }
        
        tabbedPanel.revalidate();
        tabbedPanel.repaint();
    }

	@Override
	protected FormularioBase crearFormularioEspecifico() {
		return new FormularioEmpleado(configuracionManager, this, ventanaPrincipal);
	}
	
	@Override
    public int crearRegistro() {
    	FormularioEmpleado datosEmpleado = (FormularioEmpleado) tabbedPanel.getSelectedComponent();
    	if(datosEmpleado.comprobarDatosEmpleado()) {
        	EmployeeClass nuevoEmpleado = new EmployeeClass();
        	nuevoEmpleado.setFirstName(Conversiones.textoVacioEnNull(datosEmpleado.tfNombreEmpleado.getText()));
        	nuevoEmpleado.setLastName(Conversiones.textoVacioEnNull(datosEmpleado.tfApellidoEmpleado.getText()));
        	nuevoEmpleado.setTitle(Conversiones.textoVacioEnNull(datosEmpleado.tfPuesto.getText()));
        	nuevoEmpleado.setReportsTo(Conversiones.textoEnIntONull(datosEmpleado.tfJefe.getText()));
        	nuevoEmpleado.setBirthDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosEmpleado.tfFechaNacimiento.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
        	nuevoEmpleado.setHireDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosEmpleado.tfFechaContrato.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
        	nuevoEmpleado.setAddress(Conversiones.textoVacioEnNull(datosEmpleado.tfDireccion.getText()));
        	nuevoEmpleado.setCity(Conversiones.textoVacioEnNull(datosEmpleado.tfCiudad.getText()));
        	nuevoEmpleado.setState(Conversiones.textoVacioEnNull(datosEmpleado.tfEstado.getText()));
        	nuevoEmpleado.setCountry(Conversiones.textoVacioEnNull(datosEmpleado.tfPais.getText()));
        	nuevoEmpleado.setPostalCode(Conversiones.textoVacioEnNull(datosEmpleado.tfCodigoPostal.getText()));
        	nuevoEmpleado.setPhone(Conversiones.textoVacioEnNull(datosEmpleado.tfTelefono.getText()));
        	nuevoEmpleado.setFax(Conversiones.textoVacioEnNull(datosEmpleado.tfMovil.getText()));
    		nuevoEmpleado.setEmail(Conversiones.textoVacioEnNull(datosEmpleado.tfEmail.getText()));
    	
    		return HibernateUtils.crearEmpleado(nuevoEmpleado);
    	} else {
    		return -1;
    	}
    	
    }

	@Override
	protected void crearPestanaRegistro(int idRegistro) {
		EmployeeClass empleadoRegistrado = HibernateUtils.buscarEmpleadoPorId(String.valueOf(idRegistro)).get(0);	
    	mostrarEmpleado("EMPLOYEEID: "+idRegistro, empleadoRegistrado);
	}

	@Override
	protected boolean editarRegistro() {
    	FormularioEmpleado datosEmpleado = (FormularioEmpleado) tabbedPanel.getSelectedComponent();
    	Integer idEmpleado = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());

    	if(datosEmpleado.comprobarDatosEmpleado() && idEmpleado!=null) {
        	EmployeeClass empleado = new EmployeeClass();
        	empleado.setEmployeeId(Conversiones.textoEnIntONull(idEmpleado.toString()));
        	empleado.setFirstName(Conversiones.textoVacioEnNull(datosEmpleado.tfNombreEmpleado.getText()));
        	empleado.setLastName(Conversiones.textoVacioEnNull(datosEmpleado.tfApellidoEmpleado.getText()));
        	empleado.setTitle(Conversiones.textoVacioEnNull(datosEmpleado.tfPuesto.getText()));
        	empleado.setReportsTo(Conversiones.textoEnIntONull(datosEmpleado.tfJefe.getText()));
        	empleado.setBirthDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosEmpleado.tfFechaNacimiento.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
        	empleado.setHireDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosEmpleado.tfFechaContrato.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
        	empleado.setAddress(Conversiones.textoVacioEnNull(datosEmpleado.tfDireccion.getText()));
        	empleado.setCity(Conversiones.textoVacioEnNull(datosEmpleado.tfCiudad.getText()));
        	empleado.setState(Conversiones.textoVacioEnNull(datosEmpleado.tfEstado.getText()));
        	empleado.setCountry(Conversiones.textoVacioEnNull(datosEmpleado.tfPais.getText()));
        	empleado.setPostalCode(Conversiones.textoVacioEnNull(datosEmpleado.tfCodigoPostal.getText()));
        	empleado.setPhone(Conversiones.textoVacioEnNull(datosEmpleado.tfTelefono.getText()));
        	empleado.setFax(Conversiones.textoVacioEnNull(datosEmpleado.tfMovil.getText()));
        	empleado.setEmail(Conversiones.textoVacioEnNull(datosEmpleado.tfEmail.getText()));
    		
    		return HibernateUtils.actualizarEmpleado(empleado);
    	} else {
    		return false;
    	}
	}
}