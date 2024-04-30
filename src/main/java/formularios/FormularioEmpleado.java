package formularios;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import paneles.PanelEmpleados;
import resultados.ResultadosEmpleados;

import org.example.EmployeeClass;
import org.example.HibernateUtils;

import general.Comprobaciones;
import general.ConfiguracionManager;
import general.Conversiones;
import general.VentanaPrincipal;


@SuppressWarnings("serial")
public class FormularioEmpleado extends FormularioBase {
	public JLabel lblNombreEmpleado;
	public JTextField tfNombreEmpleado;
	
	public JLabel lblPuesto;
	public JTextField tfPuesto;
	
	public JLabel lblFechaNacimiento;
	public JTextField tfFechaNacimiento;
	
	public JLabel lblDireccion;
	public JTextField tfDireccion;
	
	public JLabel lblTelefono;
	public JTextField tfTelefono;
	
	public JLabel lblMovil;
	public JTextField tfMovil;
	
	public JLabel lblEmail;
	public JTextField tfEmail;
	
	public JLabel lblFechaContrato;
	public JTextField tfFechaContrato;
	
	public JButton btnSCHContratacion;
	public JButton btnSCHNacimiento;
	public JTextField tfPais;
	public JTextField tfApellidoEmpleado;
	public JLabel lblJefe;
	public JTextField tfJefe;
	
	public JButton btnJefe;
	public VentanaPrincipal ventanaPrincipal;
	public JTextField tfCodigoPostal;
	public JTextField tfCiudad;
	public JTextField tfEstado;
	

	public FormularioEmpleado(ConfiguracionManager configuracionManager, PanelEmpleados panelEmpleados, VentanaPrincipal ventanaPrincipal) {
		super(configuracionManager,panelEmpleados);
		this.ventanaPrincipal = ventanaPrincipal;
		setLayout(new MigLayout("", "[][][][][][grow][52.00][][grow][grow][]", "[][][][][][][][][][]"));
		
		lblNombreEmpleado = new JLabel("Nombre del empleado");
		add(lblNombreEmpleado, "cell 0 0 6 1,aligny center");
		
		tfNombreEmpleado = new JTextField();
		tfNombreEmpleado.setToolTipText("NOMBRE");
		add(tfNombreEmpleado, "cell 5 0 2 1,gapx 10,height 20!,grow");
		
		tfApellidoEmpleado = new JTextField();
		tfApellidoEmpleado.setToolTipText("APELLIDO");
		add(tfApellidoEmpleado, "cell 7 0 4 1,growx,height 20!");
		
		lblPuesto = new JLabel("Puesto");
		add(lblPuesto, "cell 0 1 2 1,aligny center");
		tfPuesto = new JTextField();
		tfPuesto.setToolTipText("");
		add(tfPuesto, "cell 2 1 5 1,gapx 10,height 20!,grow");
		
		lblJefe = new JLabel("Jefe");
		add(lblJefe, "cell 7 1,alignx trailing");
		
		tfJefe = new JTextField();
		tfJefe.setToolTipText("ID EMPLEADO");
		add(tfJefe, "flowx,cell 8 1 2 1,growx,gapx 10");
		tfJefe.setColumns(10);
		
		lblFechaNacimiento = new JLabel("Fecha de nacimiento");
		add(lblFechaNacimiento, "cell 0 2 5 1,aligny center");
		tfFechaNacimiento = new JTextField();
		tfFechaNacimiento.setToolTipText("");
		add(tfFechaNacimiento, "cell 5 2 2 1,height 20!,grow");
		
		btnSCHNacimiento = new JButton("SCH");
		add(btnSCHNacimiento, "cell 7 2,growy,height 20!");
		
		lblDireccion = new JLabel("Dirección");
		add(lblDireccion, "cell 0 3 3 1,aligny center");
		tfDireccion = new JTextField();
		tfDireccion.setToolTipText("DIRECCIÓN");
		add(tfDireccion, "cell 3 3 8 1,gapx 10,height 20!,grow");
		
		tfCiudad = new JTextField();
		tfCiudad.setToolTipText("CIUDAD");
		tfCiudad.setColumns(10);
		add(tfCiudad, "cell 3 4 5 1,growx,gapx 10");
		
		tfCodigoPostal = new JTextField();
		tfCodigoPostal.setToolTipText("CÓDIGO POSTAL");
		tfCodigoPostal.setColumns(10);
		add(tfCodigoPostal, "cell 8 4 3 1,growx");
		
		tfEstado = new JTextField();
		tfEstado.setToolTipText("ESTADO");
		tfEstado.setColumns(10);
		add(tfEstado, "cell 3 5 5 1,growx,gapx 10");
		
		tfPais = new JTextField();
		tfPais.setToolTipText("PAIS");
		add(tfPais, "cell 8 5 3 1,growx");
		tfPais.setColumns(10);

		lblTelefono = new JLabel("Telefono");
		add(lblTelefono, "cell 0 6 3 1,aligny center");
		tfTelefono = new JTextField();
		tfTelefono.setToolTipText("");
		add(tfTelefono, "cell 3 6 3 1,gapx 10,height 20!,grow");
		
		lblMovil = new JLabel("Movil");
		add(lblMovil, "cell 0 7,aligny center");
		tfMovil = new JTextField();
		tfMovil.setToolTipText("");
		add(tfMovil, "cell 1 7 5 1,gapx 10,height 20!,grow");
		
		lblEmail = new JLabel("Email");
		add(lblEmail, "cell 0 8,aligny center");
		tfEmail = new JTextField();
		tfEmail.setToolTipText("");
		add(tfEmail, "cell 1 8 6 1,gapx 10,height 20!,grow");

		lblFechaContrato = new JLabel("Fecha de contratación");
		add(lblFechaContrato, "cell 0 9 5 1,aligny center");
		tfFechaContrato = new JTextField();
		tfFechaContrato.setToolTipText("");
		add(tfFechaContrato, "cell 5 9 2 1,gapx 10,height 20!,grow");
		
		btnSCHContratacion = new JButton("SCH");
		add(btnSCHContratacion, "cell 7 9,height 20!");
		
		btnJefe = new JButton("EM");
		add(btnJefe, "cell 10 1");
		
		btnSCHNacimiento.addActionListener(e -> mostrarCalendario(btnSCHNacimiento, tfFechaNacimiento));
		btnSCHContratacion.addActionListener(e -> mostrarCalendario(btnSCHContratacion, tfFechaContrato));
		
		btnJefe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(panelEmpleados.btnEditar.getText().equals("Guardar")) {
					mostrarPosiblesJefes();
				} else {
					abrirVentanaJefe();
				}
			}
			
		});
		
		modoEdicion(false);
		setVisible(true);
	}
	
	private void mostrarPosiblesJefes() {
		List<EmployeeClass> resultados = HibernateUtils.buscarTodosEmpleados();
    	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(FormularioEmpleado.this);
    	EmployeeClass resultadoElegido = ResultadosEmpleados.mostrarDialog(framePadre, resultados, "all", configuracionManager);
    	if(resultadoElegido != null) {
    		tfJefe.setText(String.valueOf(resultadoElegido.getEmployeeId()));
    	}
	}


	public void rellenar(EmployeeClass resultado) {
		if(resultado!= null) {
		    tfNombreEmpleado.setText(resultado.getFirstName());
		    tfApellidoEmpleado.setText(resultado.getLastName());
		    tfPuesto.setText(resultado.getTitle());
		    tfJefe.setText(resultado.getReportsTo() != null ? resultado.getReportsTo().toString() : null);
		    tfFechaNacimiento.setText(resultado.getBirthDate() != null ? Conversiones.cambiarFormatoFecha(resultado.getBirthDate().toLocalDateTime().toLocalDate().toString(), "yyyy-MM-dd", "dd-MM-yyyy") : null);
		    tfDireccion.setText(resultado.getAddress());
		    tfTelefono.setText(resultado.getPhone());
		    tfMovil.setText(resultado.getFax());
		    tfEmail.setText(resultado.getEmail());
		    tfFechaContrato.setText(resultado.getHireDate() != null ? Conversiones.cambiarFormatoFecha(resultado.getHireDate().toLocalDateTime().toLocalDate().toString(), "yyyy-MM-dd", "dd-MM-yyyy") : null);
		    tfCodigoPostal.setText(resultado.getPostalCode());
		    tfCiudad.setText(resultado.getCity());
		    tfEstado.setText(resultado.getState());
		    tfPais.setText(resultado.getCountry());
		}
	}
	
	public void abrirVentanaJefe() {
		String apellidoEmpleado = tfApellidoEmpleado.getText();
		String idJefe = Conversiones.textoVacioEnNull(tfJefe.getText());
		if(idJefe != null) {  //Si el jefe es null es jefe superior
			List<EmployeeClass> jefes = HibernateUtils.buscarEmpleadoPorId(idJefe);
			
			if (!jefes.isEmpty()) {
				EmployeeClass jefe = jefes.get(0);
	            PanelEmpleados panelEmpleados = (PanelEmpleados) ventanaPrincipal.panelesMap.get(VentanaPrincipal.TipoMenu.EMPLEADOS);
	            panelEmpleados.mostrarEmpleado("EMPLOYEEID: "+idJefe+" [REPORTEDBY: "+apellidoEmpleado+"]", jefe);
	            ventanaPrincipal.mostrarPanel(VentanaPrincipal.TipoMenu.EMPLEADOS);
	        }
		} else {
        	JOptionPane.showMessageDialog(this, "Es su propio jefe", "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public boolean comprobarDatosEmpleado() {
		boolean esValido = true;
		
		//Volver a pintar los textField del color del modo, para no acumular rojos
		restaurarEstiloCampos();
		

		if(!Comprobaciones.esStringValido(tfNombreEmpleado.getText(), 20, false)) {
			tfNombreEmpleado.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfApellidoEmpleado.getText(), 20, false)) {
			tfApellidoEmpleado.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esStringValido(tfPuesto.getText(), 30, true)) {
			tfPuesto.setBackground(Color.RED);
			esValido = false;
		} 
		
		//Hay que comprobar que existe ese numero de jefe?
		if(!Comprobaciones.esIntValido(tfJefe.getText(), true)) {
			tfJefe.setBackground(Color.RED);
			esValido = false;
		} 
		
		if(!Comprobaciones.esFechaValida(tfFechaNacimiento.getText(), "dd-MM-yyyy", true)) {
			tfFechaNacimiento.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esFechaValida(tfFechaContrato.getText(), "dd-MM-yyyy", true)) {
			tfFechaNacimiento.setBackground(Color.RED);
			esValido = false;
		}
		
		if(!Comprobaciones.esStringValido(tfDireccion.getText(), 70, true)) {
			tfCiudad.setBackground(Color.RED);
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

		if(!Comprobaciones.esStringValido(tfEmail.getText(), 60, true)) {
			tfMovil.setBackground(Color.RED);
			esValido = false;
		} 
		
		return esValido;
	}
	
	@Override
    public void modoEdicion(boolean editando) {
		super.modoEdicion(editando);
		tfJefe.setEnabled(false);
	}
}
