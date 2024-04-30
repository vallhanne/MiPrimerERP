package formularios;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.example.CustomerClass;
import org.example.HibernateUtils;

import general.Comprobaciones;
import general.ConfiguracionManager;
import net.miginfocom.swing.MigLayout;
import paneles.PanelFacturacion;
import resultados.ResultadosClientes;
import resultados.ResultadosFacturacion;

import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;


@SuppressWarnings("serial")
public class FormularioFacturaNueva extends FormularioBase {
	public JLabel lblNombreCliente;
	public JTextField tfNombreCliente;
	
	public JLabel lblFechaFactura;
	public JTextField tfFechaFactura;
	
	public JLabel lblDireccion;
	public JTextField tfDireccion;
	
	public JLabel lblImporte;
	public JTextField tfImporte;
	public JTextField tfCodigoPostal;
	public JTextField tfEstado;
	public JTextField tfPais;
	public JTextField tfApellidoCliente;
	
	public JTable table;
	public JScrollPane scrollPane;
	public DefaultTableModel model;
	
	public JButton btnCliente;
	int idCliente = -1;
	public JButton btnAgregarLinea;

	public List<Integer> idsCancionesAgregadas;
	public JTextField tfCiudad;

	
	public FormularioFacturaNueva(ConfiguracionManager configuracionManager, PanelFacturacion panelFacturacion) {
		super(configuracionManager, panelFacturacion);
		setLayout(new MigLayout("", "[][][][][grow][grow]", "[][][][][][][grow][][][]"));
		
		lblNombreCliente = new JLabel("Nombre del cliente");
		add(lblNombreCliente, "cell 0 0 3 1,aligny center");
		
		btnCliente = new JButton("CL");
		add(btnCliente, "cell 3 0, height 20!");
	    btnCliente.addActionListener(e -> elegirCliente());
		
		tfNombreCliente = new JTextField();
		tfNombreCliente.setToolTipText("NOMBRE");
		add(tfNombreCliente, "flowx,cell 4 0,height 20!,grow");
		tfNombreCliente.setEnabled(false);
		
		lblFechaFactura = new JLabel("Fecha de la factura");
		add(lblFechaFactura, "cell 0 1 3 1,aligny center");
		tfFechaFactura = new JTextField();
		add(tfFechaFactura, "cell 3 1 3 1,height 20!,grow");
		tfFechaFactura.setEnabled(false);
		
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
		add(tfCodigoPostal, "cell 5 3,growx,height 20!");
		
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
		tfImporte.setEnabled(false);
		
		tfApellidoCliente = new JTextField();
		tfApellidoCliente.setToolTipText("APELLIDO");
		add(tfApellidoCliente, "cell 5 0,height 20!,grow");
		tfApellidoCliente.setColumns(10);
		tfApellidoCliente.setEnabled(false);
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 6 6 1,gapy 20,grow");
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		btnAgregarLinea = new JButton("Añadir");
		add(btnAgregarLinea, "cell 0 7 6 1,alignx center");
		btnAgregarLinea.addActionListener(e -> agregarLineaTabla());
		modelarTabla();
		idsCancionesAgregadas = new ArrayList<Integer>();
		
		setVisible(true);
	}
	
    public int getIdCliente() {
    	return idCliente;
    }
	
    
	public void elegirCliente() {
		List<CustomerClass> listaClientes = HibernateUtils.buscarTodosClientes();
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        CustomerClass clienteElegido = ResultadosClientes.mostrarDialog(framePadre, listaClientes, "all", configuracionManager);
        if (clienteElegido != null) {
        	idCliente = clienteElegido.getCustomerId();
        	tfNombreCliente.setText(clienteElegido.getFirstName() != null ? clienteElegido.getFirstName() : null);
        	tfApellidoCliente.setText(clienteElegido.getLastName() != null ? clienteElegido.getLastName() : null);
        	tfFechaFactura.setText(obtenerFechaActual());
        	tfDireccion.setText(clienteElegido.getAddress() != null ? clienteElegido.getAddress() : null);
        	tfEstado.setText(clienteElegido.getState() != null ? clienteElegido.getState() : null);
        	tfPais.setText(clienteElegido.getCountry() != null ? clienteElegido.getCountry() : null);
        	tfCodigoPostal.setText(clienteElegido.getPostalCode() != null ? clienteElegido.getPostalCode() : null);
        	tfCiudad.setText(clienteElegido.getCity() != null ? clienteElegido.getCity() : null);
        	
        	actualizarImporteTotal();
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
	
    public static String obtenerFechaActual() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(fecha);
    }
    
	
	private void modelarTabla() {
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel(new Object[][] {},
				new String[] {"Tema", "Album", "Precio ud.", "Cantidad", "Subtotal"}) {
				boolean[] columnEditables = new boolean[] {false, false, false, true, false};
	            @Override
	            public Class<?> getColumnClass(int columnIndex) {
	                if (columnIndex == getColumnIndex("Cantidad")) {
	                    return Integer.class;
	                }
	                if (columnIndex == getColumnIndex("Precio ud.") || columnIndex == getColumnIndex("Subtotal")) {
	                    return BigDecimal.class;
	                }
	                return super.getColumnClass(columnIndex);
	            }
				
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			
		table.setModel(model);
		
        table.getColumnModel().getColumn(getColumnIndex("Cantidad")).setCellEditor(new SpinnerEditor());
		
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
		
		table.getModel().addTableModelListener(new TableModelListener() {
	        @Override
	        public void tableChanged(TableModelEvent e) {
	            if (e.getColumn() == getColumnIndex("Cantidad")) {
	                int rowIndex = e.getFirstRow();
	                int cantidad = 1; // Valor por defecto
	                try {
	                    cantidad = Integer.parseInt(model.getValueAt(rowIndex, getColumnIndex("Cantidad")).toString());
	                } catch (NumberFormatException ex) {
	                    ex.printStackTrace();
	                }

	                BigDecimal precioUnitario = BigDecimal.ZERO;
	                try {
	                    precioUnitario = new BigDecimal(model.getValueAt(rowIndex, getColumnIndex("Precio ud.")).toString());
	                } catch (NumberFormatException ex) {
	                    ex.printStackTrace();
	                }

	                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
	                subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
	                model.setValueAt(subtotal, rowIndex, getColumnIndex("Subtotal"));
	                actualizarImporteTotal();
	            }
	        }

	    });
	}
	
	private int getColumnIndex(String columnName) {
	    for (int i = 0; i < table.getColumnCount(); i++) {
	        if (table.getColumnName(i).equals(columnName)) {
	            return i;
	        }
	    }
	    return -1;
	}
	
	public void agregarLineaTabla() {
		List<Object[]> temasAElegir = HibernateUtils.buscarTemasParaFactura();
     	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
     	Object[] temaElegido = ResultadosFacturacion.mostrarDialog(framePadre, temasAElegir, "tracks", configuracionManager);
     	
     	if(temaElegido != null) {
     		int idTemaNuevo = (int) temaElegido[0];

     		
            if (!idsCancionesAgregadas.contains(idTemaNuevo)) {
                Object[] fila = {temaElegido[1], temaElegido[2], temaElegido[3], 1, temaElegido[3]};
                model.addRow(fila);
                idsCancionesAgregadas.add(idTemaNuevo);
                actualizarImporteTotal();
            } else {
                JOptionPane.showMessageDialog(this, "La canción seleccionada ya ha sido añadida anteriormente.");
            }
     	}
	
	}
	
	private void actualizarImporteTotal() {
	    BigDecimal importeTotal = BigDecimal.ZERO;
	    if(model.getRowCount() == 0) {
		    tfImporte.setText("0");

	    } else {
		    for (int i = 0; i < model.getRowCount(); i++) {
		        BigDecimal subtotal = (BigDecimal) model.getValueAt(i, model.findColumn("Subtotal"));
		        importeTotal = importeTotal.add(subtotal);
		    }
		    tfImporte.setText(importeTotal.toString());
	    }
	}


    private class SpinnerEditor extends DefaultCellEditor {
        private JSpinner spinner;

        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(1, 1, 999999999, 1));
            editorComponent = spinner;
            delegate = new EditorDelegate() {
                @Override
                public void setValue(Object value) {
                    spinner.setValue(value);
                }
                @Override
                public Object getCellEditorValue() {
                    return spinner.getValue();
                }
            };
            spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fireEditingStopped();
                }
            });
        }
    }
}
