package paneles;


import javax.swing.*;

import org.example.HibernateUtils;
import org.example.InvoiceClass;
import org.example.InvoiceLineClass;

import formularios.FormularioFacturacion;
import formularios.FormularioBase;
import formularios.FormularioFacturaNueva;
import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;
import resultados.ResultadosFacturacion;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("serial")
public class PanelFacturacion extends PanelBase {
	protected VentanaPrincipal ventanaPrincipal;

    public PanelFacturacion(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	super(nombreTipo, filtros, configuracionManager, ventanaPrincipal);
    	this.ventanaPrincipal = ventanaPrincipal;
        iniciarComponentes();
    }
    
    @Override
    public void iniciarComponentes() {
    	super.iniciarComponentes();
        btnBuscar.addActionListener(e -> buscarFactura(comboBox.getSelectedIndex(), tfBuscador.getText().trim()));
    }

	public void buscarFactura(int indexBusqueda, String terminoBuscado) {
    	restaurarEstiloBuscador();

        //Indica al usuario si la búsqueda es válida o no
        if (terminoBuscado.isBlank() || terminoBuscado == null) {
            tfBuscador.setBackground(Color.RED);
            return;
        }
        
        //Realiza la búsqueda, almacena posibles resultados
        List<Object[]> resultados = null;
        
        if(indexBusqueda == 0 && Comprobaciones.esIntValido(terminoBuscado, false)) {	//customer.customerId
        	resultados = HibernateUtils.buscarFacturaPorClienteId(terminoBuscado);
        } else if(indexBusqueda == 1 && Comprobaciones.esIntValido(terminoBuscado, false)) {	//invoice.invoiceId
        	resultados = HibernateUtils.buscarFacturaPorFacturaId(terminoBuscado);
        }
        
        if (resultados == null || resultados.isEmpty()) {
        	tfBuscador.setBackground(Color.RED);
            return;
        } 
	    
	    if (resultados.size() == 1) {  //Un resultado
	        Object[] resultado = resultados.get(0);
	        String titulo = indexBusqueda == 0 ? filtros[0].toUpperCase() + ": " + terminoBuscado : filtros[1].toUpperCase() + ": " + terminoBuscado;
	        mostrarFactura(titulo, resultado);
	    } else { //Varios resultados
	        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(PanelFacturacion.this);
	        Object[] resultadoElegido = ResultadosFacturacion.mostrarDialog(framePadre, resultados, filtros[indexBusqueda], configuracionManager);
	        if (resultadoElegido != null && resultadoElegido[0] != null) {
	        	InvoiceClass invoice = (InvoiceClass) resultadoElegido[0];
	            String titulo = (indexBusqueda == 0)
	                ? filtros[0].toUpperCase() + ": " + terminoBuscado + " [" + filtros[1].toUpperCase() + ": " + invoice.getInvoiceId() + "]"
	                : filtros[1].toUpperCase() + ": " + terminoBuscado + " [" + filtros[0].toUpperCase() + ": " + invoice.getCustomerId() + "]";
	            mostrarFactura(titulo, resultadoElegido);
	        }
	    }
    }
    
    public void mostrarFactura(String titulo, Object[] resultado) {
    	InvoiceClass invoice = (InvoiceClass) resultado[0];
    	int id = invoice.getInvoiceId();
        Component pestana = pestanasMostradas.get(id);

        if (pestana != null) {
            //Si la pestaña ya está abierta, la selecciona
            tabbedPanel.setSelectedComponent(pestana);
        } else { 
        	FormularioFacturacion factura = new FormularioFacturacion(configuracionManager, this, ventanaPrincipal);
        	factura.rellenar(resultado);
            tabbedPanel.add(titulo, factura);
            tabbedPanel.setSelectedComponent(factura);
            
            pestanasMostradas.put(id, factura);
            agregarBotonCerrar(id);
        }
        
        tabbedPanel.revalidate();
        tabbedPanel.repaint();
    }
    
	@Override
	protected FormularioBase crearFormularioEspecifico() {
		return new FormularioFacturaNueva(configuracionManager, this);
	}

	@Override
	protected int crearRegistro() {
		FormularioFacturaNueva datosFacturacion = (FormularioFacturaNueva) tabbedPanel.getSelectedComponent();
		//Crear factura primero
		int idFactura = crearFactura(datosFacturacion);
		
		//Crear una invoiceline por cada linea de la tabla
		int numLineasFactura = datosFacturacion.model.getRowCount();
		for(int i=0; i<numLineasFactura; i++) {
			int lineaCreada = crearLineaFactura(datosFacturacion, idFactura, i);
			if(lineaCreada == -1) return -1;
		}
		return idFactura;
	}
	
	protected int crearFactura(FormularioFacturaNueva datosFacturacion) {
		if(datosFacturacion.comprobarDatosFacturacion()) {
			InvoiceClass nuevaFactura = new InvoiceClass();
			nuevaFactura.setCustomerId(Conversiones.textoEnIntONull(String.valueOf(datosFacturacion.getIdCliente())));
			nuevaFactura.setInvoiceDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosFacturacion.tfFechaFactura.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
			nuevaFactura.setBillingAddress(Conversiones.textoVacioEnNull(datosFacturacion.tfDireccion.getText()));
			nuevaFactura.setBillingCity(Conversiones.textoVacioEnNull(datosFacturacion.tfCiudad.getText()));
			nuevaFactura.setBillingState(Conversiones.textoVacioEnNull(datosFacturacion.tfEstado.getText()));
			nuevaFactura.setBillingCountry(Conversiones.textoVacioEnNull(datosFacturacion.tfPais.getText()));
			nuevaFactura.setBillingPostalCode(Conversiones.textoVacioEnNull(datosFacturacion.tfCodigoPostal.getText()));
			nuevaFactura.setTotal(Conversiones.textoEnDecimalONull(datosFacturacion.tfImporte.getText()));
			
			return HibernateUtils.crearFactura(nuevaFactura);
		} else {
			return -1;
		}
	}
	
	protected int crearLineaFactura(FormularioFacturaNueva datosFacturacion, int idFactura, int indexTema) {
		//La tabla está diseñada para que los datos sean válidos
		InvoiceLineClass nuevaLinea = new InvoiceLineClass();
		nuevaLinea.setInvoiceId(idFactura);
		nuevaLinea.setTrackId(datosFacturacion.idsCancionesAgregadas.get(indexTema));
		nuevaLinea.setUnitPrice((BigDecimal) datosFacturacion.model.getValueAt(indexTema, 2));
		nuevaLinea.setQuantity((int) datosFacturacion.model.getValueAt(indexTema, 3));

		return HibernateUtils.crearLineaFactura(nuevaLinea);
	}

	@Override
	protected void crearPestanaRegistro(int idRegistro) {
		Object[] facturaRegistrada = HibernateUtils.buscarFacturaPorFacturaId(String.valueOf(idRegistro)).get(0);
		mostrarFactura("INVOICEID: "+idRegistro, facturaRegistrada);
	}

	@Override
	protected boolean editarRegistro() {
		FormularioFacturacion datosFactura = (FormularioFacturacion) tabbedPanel.getSelectedComponent();
		Integer idFactura = obtenerIdPestana(pestanasMostradas, tabbedPanel.getSelectedComponent());
		
		if(datosFactura.comprobarDatosFacturacion() && idFactura!=null && datosFactura.getIdCliente() != -1) {
			InvoiceClass factura = new InvoiceClass();
			factura.setInvoiceId(Conversiones.textoEnIntONull(idFactura.toString()));
			factura.setCustomerId(Conversiones.textoEnIntONull(String.valueOf(datosFactura.getIdCliente())));
			factura.setInvoiceDate(Conversiones.textoEnTimestamp(Conversiones.cambiarFormatoFecha(datosFactura.tfFechaFactura.getText(), "dd-MM-yyyy", "yyyy-MM-dd")));
			factura.setBillingAddress(Conversiones.textoVacioEnNull(datosFactura.tfDireccion.getText()));
			factura.setBillingCity(Conversiones.textoVacioEnNull(datosFactura.tfCiudad.getText()));
			factura.setBillingState(Conversiones.textoVacioEnNull(datosFactura.tfEstado.getText()));
			factura.setBillingCountry(Conversiones.textoVacioEnNull(datosFactura.tfPais.getText()));
			factura.setBillingPostalCode(Conversiones.textoVacioEnNull(datosFactura.tfCodigoPostal.getText()));
			factura.setTotal(Conversiones.textoEnDecimalONull(datosFactura.tfImporte.getText()));

			return HibernateUtils.actualizarFactura(factura);
		} else {
			return false;
		}
	}
	
}