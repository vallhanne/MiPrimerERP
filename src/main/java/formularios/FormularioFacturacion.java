package formularios;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import paneles.PanelClientes;
import paneles.PanelFacturacion;

import org.example.CustomerClass;
import org.example.HibernateUtils;
import org.example.InvoiceClass;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;

import javax.swing.JScrollPane;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class FormularioFacturacion extends FormularioBase {
	public JLabel lblNombreCliente;
	public JTextField tfNombreCliente;
	
	public JLabel lblFechaFactura;
	public JTextField tfFechaFactura;
	
	public JLabel lblDireccion;
	public JTextField tfDireccion;
	
	public JLabel lblImporte;
	public JTextField tfImporte;
	public JTextField tfEstado;
	public JTextField tfPais;
	public JTextField tfApellidoCliente;
	
	public JTable table;
	public JScrollPane scrollPane;
	public DefaultTableModel model;
	
	public JButton btnCliente;
	int idCliente = -1;
	public VentanaPrincipal ventanaPrincipal;
	public JTextField tfCiudad;
	public JTextField tfCodigoPostal;

	
	
	public FormularioFacturacion(ConfiguracionManager configuracionManager, PanelFacturacion panelFacturacion, VentanaPrincipal ventanaPrincipal) {
		super(configuracionManager, panelFacturacion);
		this.ventanaPrincipal = ventanaPrincipal;
		setLayout(new MigLayout("", "[][][][][grow][grow]", "[][][][][][][grow]"));
		
		lblNombreCliente = new JLabel("Nombre del cliente");
		add(lblNombreCliente, "cell 0 0 3 1,aligny center");
		
		btnCliente = new JButton("CL");
		add(btnCliente, "cell 3 0, height 20!");
		
		tfNombreCliente = new JTextField();
		tfNombreCliente.setToolTipText("NOMBRE");
		add(tfNombreCliente, "flowx,cell 4 0,height 20!,grow");
		
		lblFechaFactura = new JLabel("Fecha de la factura");
		add(lblFechaFactura, "cell 0 1 3 1,aligny center");
		tfFechaFactura = new JTextField();
		tfFechaFactura.setToolTipText("");
		add(tfFechaFactura, "cell 3 1 3 1,height 20!,grow");
		
		lblDireccion = new JLabel("Dirección");
		add(lblDireccion, "cell 0 2,aligny center");
		tfDireccion = new JTextField();
		tfDireccion.setToolTipText("DIRECCIÓN FACTURACIÓN");
		add(tfDireccion, "cell 1 2 5 1,gapx 10,height 20!,grow");
		
		tfCiudad = new JTextField();
		tfCiudad.setToolTipText("CIUDAD FACTURACIÓN");
		add(tfCiudad, "cell 1 3 4 1,growx,gapx 10");
		
		tfCodigoPostal = new JTextField();
		tfCodigoPostal.setToolTipText("CÓDIGO POSTAL FACTURACIÓN");
		add(tfCodigoPostal, "cell 5 3,growx");
		
		tfEstado = new JTextField();
		tfEstado.setToolTipText("ESTADO FACTURACIÓN");
		add(tfEstado, "cell 1 4 4 1,growx,gapx 10");
		
		tfPais = new JTextField();
		tfPais.setToolTipText("PAIS FACTURACIÓN");
		add(tfPais, "cell 5 4,growx");
		
		lblImporte = new JLabel("Importe total");
		add(lblImporte, "cell 0 5 2 1,aligny center");
		tfImporte = new JTextField();
		add(tfImporte, "cell 2 5 3 1,gapx 10,height 20!,grow");
		
		tfApellidoCliente = new JTextField();
		tfApellidoCliente.setToolTipText("APELLIDO");
		add(tfApellidoCliente, "cell 5 0,height 20!,grow");
		tfApellidoCliente.setColumns(10);
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 6 6 1,gapy 20,grow");
		
		table = new JTable();
		scrollPane.setViewportView(table);
		modelarTabla();
		
		modoEdicion(false);
		setVisible(true);
	}
	
    public int getIdCliente() {
    	return idCliente;
    }

    
	//No se muestran varios en primer filtro
	public void rellenar(Object[] resultado) {
	    InvoiceClass invoice = resultado[0] != null ? (InvoiceClass) resultado[0] : null;
	    
	    tfNombreCliente.setText(resultado[1] != null ? (String) resultado[1] : null);
	    tfApellidoCliente.setText(resultado[2] != null ? (String) resultado[2] : null);
	    tfFechaFactura.setText(invoice != null ? Conversiones.cambiarFormatoFecha(invoice.getInvoiceDate().toLocalDateTime().toLocalDate().toString(), "yyyy-MM-dd", "dd-MM-yyyy") : null);
	    tfDireccion.setText(invoice != null ? invoice.getBillingAddress() : null);
	    tfEstado.setText(invoice != null ? invoice.getBillingState() : null);
	    tfPais.setText(invoice != null ? invoice.getBillingCountry() : null);
	    tfCiudad.setText(invoice != null ? invoice.getBillingCity() : null);
	    tfCodigoPostal.setText(invoice != null ? invoice.getBillingPostalCode() : null);
	    tfImporte.setText(invoice != null ? invoice.getTotal().toString() : null);
	    
	    //Rellenar tabla de canciones
	    if(invoice != null) {
		    List<Object[]> lineasDeFactura = HibernateUtils.buscarLineasDeFactura(String.valueOf(invoice.getInvoiceId()));
		    lineasDeFactura.forEach(linea -> {
                model.addRow(new Object[]{linea[0].toString(), linea[1].toString(), linea[2].toString(), linea[3].toString(), linea[4].toString()});
            });
	    }
	    
	    btnCliente.addActionListener(e -> abrirEnVentanaCliente(invoice));
	}
	
	
	private void modelarTabla() {
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel(new Object[][] {},
				new String[] {"Tema", "Album", "Precio ud.", "Cantidad", "Subtotal"}) {
				boolean[] columnEditables = new boolean[] {false, false, false, false, false};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			
		table.setModel(model);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(50);
		table.getColumnModel().getColumn(2).setMaxWidth(200);
		
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(50);
		table.getColumnModel().getColumn(3).setMaxWidth(200);
		
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setMinWidth(50);
		table.getColumnModel().getColumn(4).setMaxWidth(200);
		
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	private void abrirEnVentanaCliente(InvoiceClass invoice) {
		if(invoice != null) {
			idCliente = invoice.getCustomerId();
			List<CustomerClass> clientes = HibernateUtils.buscarClientesPorId(String.valueOf(idCliente));
			
			if (!clientes.isEmpty()) {
	            CustomerClass cliente = clientes.get(0);
	            PanelClientes panelClientes = (PanelClientes) ventanaPrincipal.panelesMap.get(VentanaPrincipal.TipoMenu.CLIENTES);
	            panelClientes.mostrarCliente("CUSTOMERID: "+cliente.getCustomerId()+" [INVOICEID: "+invoice.getInvoiceId()+"]", cliente);
	            
	            ventanaPrincipal.mostrarPanel(VentanaPrincipal.TipoMenu.CLIENTES);

	        }		
		}
	}
	
	public boolean comprobarDatosFacturacion() {
		boolean esValido = true;
		restaurarEstiloCampos();
		
		if(!Comprobaciones.esStringValido(tfNombreCliente.getText(), 40, false)) {
			tfNombreCliente.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfApellidoCliente.getText(), 20, false)) {
			tfApellidoCliente.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esFechaValida(tfFechaFactura.getText(), "dd-MM-yyyy", false)) {
			tfFechaFactura.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfDireccion.getText(), 70, true)) {
			tfDireccion.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfCiudad.getText(), 40, true)) {
			tfCiudad.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfEstado.getText(), 40, true)) {
			tfEstado.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfPais.getText(), 40, true)) {
			tfEstado.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfCodigoPostal.getText(), 10, true)) {
			tfCodigoPostal.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esDecimalValido(tfImporte.getText(), 10, 2, false)) {
			tfImporte.setBackground(Color.RED);
			esValido = false;
		}
		
		return esValido;
	}
	
	@Override
    public void modoEdicion(boolean editando) {
        super.modoEdicion(editando);
                
    	btnCliente.setEnabled(!editando);
    	tfNombreCliente.setEnabled(false);
        tfApellidoCliente.setEnabled(false);
        tfFechaFactura.setEnabled(false);
    	tfImporte.setEnabled(false);  

        
    }
}
