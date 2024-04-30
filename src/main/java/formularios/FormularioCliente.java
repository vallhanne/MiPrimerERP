package formularios;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.JButton;


import net.miginfocom.swing.MigLayout;
import paneles.PanelClientes;

import org.example.CustomerClass;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;


@SuppressWarnings("serial")
public class FormularioCliente extends FormularioBase {
	public JLabel lblNombreCliente;
	public JTextField tfNombreCliente;
	
	public JLabel lblEmpresa;
	public JTextField tfEmpresa;
	
	public JLabel lblFechaAlta;
	public JTextField tfFechaAlta;
	
	public JLabel lblDireccion;
	public JTextField tfDireccion;
	
	public JLabel lblTelefono;
	public JTextField tfTelefono;
	
	public JLabel lblMovil;
	public JTextField tfMovil;
	
	public JLabel lblEmail;
	public JTextField tfEmail;
	
	public JLabel lblSoporte;
	public JTextField tfSoporte;
	
	public JButton btnSOP;
	public JButton btnSCH;
	public JButton btnCOM;
	public JTextField tfCodigoPostal;
	public JTextField tfCiudad;
	public JTextField tfEstado;
	public JTextField tfPais;
	public JTextField tfApellidoCliente;
	public JButton btnCliente;
		
	public FormularioCliente(ConfiguracionManager configuracionManager, PanelClientes panelClientes) {
		super(configuracionManager, panelClientes);
		setLayout(new MigLayout("", "[][][14.00,leading][][40.00,grow][29.00,grow][57.00,grow][grow]", "[][][][][][][][][][]"));
		
		lblNombreCliente = new JLabel("Nombre del cliente");
		add(lblNombreCliente, "cell 0 0 4 1,height 20!,aligny center");
		tfNombreCliente = new JTextField();
		tfNombreCliente.setToolTipText("NOMBRE");
		add(tfNombreCliente, "flowx,cell 4 0 2 1,height 20!,grow");
		
		tfApellidoCliente = new JTextField();
		tfApellidoCliente.setToolTipText("APELLIDO");
		add(tfApellidoCliente, "flowx,cell 6 0 2 1,height 20!,grow");
		
		lblEmpresa = new JLabel("Empresa");
		add(lblEmpresa, "cell 0 1 2 1,aligny center");
		tfEmpresa = new JTextField();
		add(tfEmpresa, "flowx,cell 2 1 5 1,gapx 10,height 20!,grow");
		
		btnCOM = new JButton("COM");
		btnCOM.setEnabled(false);
		add(btnCOM, "cell 7 1,height 20!");
		
		lblFechaAlta = new JLabel("Fecha de alta");
		add(lblFechaAlta, "cell 0 2 3 1,aligny center");
		tfFechaAlta = new JTextField();
		add(tfFechaAlta, "flowx,cell 3 2 3 1,gapx 10,height 20!,grow");
		
		btnSCH = new JButton("SCH");
		add(btnSCH, "cell 6 2,height 20!");
		
		lblDireccion = new JLabel("Dirección");
		add(lblDireccion, "cell 0 3 2 1,aligny center");
		tfDireccion = new JTextField();
		tfDireccion.setToolTipText("DIRECCIÓN");
		add(tfDireccion, "cell 2 3 6 1,gapx 10,height 20!,grow");
		
		tfCiudad = new JTextField();
		tfCiudad.setToolTipText("CIUDAD");
		add(tfCiudad, "cell 2 4 4 1,height 20!,growx,gapx 10");
		
		tfCodigoPostal = new JTextField();
		tfCodigoPostal.setToolTipText("CÓDIGO POSTAL");
		add(tfCodigoPostal, "cell 6 4 2 1,growx,height 20!");
		
		tfEstado = new JTextField();
		tfEstado.setToolTipText("ESTADO");
		add(tfEstado, "cell 2 5 4 1,height 20!,growx,gapx 10");
		
		tfPais = new JTextField();
		tfPais.setToolTipText("PAIS");
		add(tfPais, "cell 6 5 2 1,height 20!,growx");
		
		lblTelefono = new JLabel("Teléfono");
		add(lblTelefono, "cell 0 6 2 1,aligny center");
		tfTelefono = new JTextField();
		tfTelefono.setToolTipText("");
		add(tfTelefono, "cell 2 6 3 1,gapx 10,height 20!,grow");
		
		lblMovil = new JLabel("Móvil");
		add(lblMovil, "cell 0 7,aligny center");
		tfMovil = new JTextField();
		tfMovil.setToolTipText("");
		add(tfMovil, "cell 1 7 4 1,gapx 10,height 20!,grow");
		
		lblEmail = new JLabel("Email");
		add(lblEmail, "cell 0 8,aligny center");
		tfEmail = new JTextField();
		tfEmail.setToolTipText("");
		add(tfEmail, "cell 1 8 5 1,gapx 10,height 20!,grow");
		
		lblSoporte = new JLabel("Soporte");
		add(lblSoporte, "cell 0 9 2 1,aligny center");
		tfSoporte = new JTextField();
		tfSoporte.setToolTipText("");
		add(tfSoporte, "cell 2 9 3 1,gapx 10,height 20!,grow");
		
		btnSOP = new JButton("SOP");
		btnSOP.setEnabled(false);
		add(btnSOP, "cell 5 9,height 20!");
		
		btnCliente = new JButton("CL");
		add(btnCliente, "cell 7 0,height 20!");
		
		btnSCH.addActionListener(e -> mostrarCalendario(btnSCH, tfFechaAlta));
		
		modoEdicion(false);
		setVisible(true);
	}

    
	public void rellenar(CustomerClass resultado) {	
		if(resultado != null) {
		    tfNombreCliente.setText(resultado.getFirstName() != null ? resultado.getFirstName() : null);
		    tfApellidoCliente.setText(resultado.getLastName() != null ? resultado.getLastName() : null);
		    tfEmpresa.setText(resultado.getCompany() != null ? resultado.getCompany() : null);
		    tfDireccion.setText(resultado.getAddress() != null ? resultado.getAddress() : null);
		    tfTelefono.setText(resultado.getPhone() != null ? resultado.getPhone() : null);
		    tfMovil.setText(resultado.getFax() != null ? resultado.getFax() : null);
		    tfEmail.setText(resultado.getEmail() != null ? resultado.getEmail() : null);
		    tfCodigoPostal.setText(resultado.getPostalCode() != null ? resultado.getPostalCode() : null);
		    tfCiudad.setText(resultado.getCity() != null ? resultado.getCity() : null);
		    tfEstado.setText(resultado.getState() != null ? resultado.getState() : null);
		    tfPais.setText(resultado.getCountry() != null ? resultado.getCountry() : null);
		    tfSoporte.setText(resultado.getSupportRepId() != null ? resultado.getSupportRepId().toString() : null);
		    tfFechaAlta.setText(resultado.getEntryDate() != null ? Conversiones.cambiarFormatoFecha(resultado.getEntryDate().toLocalDateTime().toLocalDate().toString(), "yyyy-MM-dd", "dd-MM-yyyy") : null);
		}
	}
	
	public boolean comprobarDatosCliente() {
		boolean esValido = true;
		
		//Volver a pintar los textField del color del modo, para no acumular rojos
		restaurarEstiloCampos();
		
		if(!Comprobaciones.esStringValido(tfNombreCliente.getText(), 40, false)) {
			tfNombreCliente.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfApellidoCliente.getText(), 20, false)) {
			tfApellidoCliente.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfEmpresa.getText(), 80, true)) {
			tfEmpresa.setBackground(Color.RED);
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
		
		if(!Comprobaciones.esStringValido(tfTelefono.getText(), 24, true)) {
			tfTelefono.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfMovil.getText(), 24, true)) {
			tfMovil.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfEmail.getText(), 60, false)) {
			tfEmail.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esIntValido(tfSoporte.getText(), true)) {
			tfSoporte.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esFechaValida(tfFechaAlta.getText(), "dd-MM-yyyy", true)) {
			tfFechaAlta.setBackground(Color.RED);
			esValido = false;
		}
		
		return esValido;
	}
}
