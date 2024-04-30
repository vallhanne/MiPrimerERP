package paneles;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import general.ConfiguracionManager;
import general.VentanaPrincipal;

import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import formularios.FormularioBase;

import javax.swing.BorderFactory;

@SuppressWarnings("serial")
public abstract class PanelBase extends JPanel {
	public JLabel lblTitulo;
	public JPanel buscadorPanel;
	public JTextField tfBuscador;
	public JButton btnAnadir;
	public JButton btnBuscar;
    public JButton btnEditar;
    public JTabbedPane tabbedPanel;
    public JLabel lblBuscarPor;
    public JComboBox<String> comboBox;
    
    public String nombreTipo;
    public String[] filtros;
    
    public Map<Integer, Component> pestanasMostradas;
    public ConfiguracionManager configuracionManager;
    public VentanaPrincipal ventanaPrincipal;
    
    public boolean editando = false;
    int indicePestanaEditando;
    
    public Map<JTextField, String> valoresOriginales = new HashMap<>();

    public PanelBase(String nombreTipo, String[] filtros, ConfiguracionManager configuracionManager, VentanaPrincipal ventanaPrincipal) {
    	this.nombreTipo = nombreTipo;
    	this.filtros = filtros;
    	this.configuracionManager = configuracionManager;
    	this.ventanaPrincipal = ventanaPrincipal;
    }
    
    public String[] getFiltros() {
    	return this.filtros;
    }  
    
    public void iniciarComponentes() {
        setLayout(new MigLayout("", "[687.00,grow,fill]", "[60px][420.00px,grow]"));
        pestanasMostradas = new HashMap<>();
        
        lblTitulo = new JLabel(nombreTipo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        Border espacioInterior = BorderFactory.createEmptyBorder(5, 5, 5, 5); //5 píxeles de espacio en todos los lados
        Border bordeInferior = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY); //2 es el grosor del borde
        Border bordeCompuesto = BorderFactory.createCompoundBorder(bordeInferior, espacioInterior);
        lblTitulo.setBorder(bordeCompuesto);
        add(lblTitulo, "dock north");


        buscadorPanel = new JPanel();
        add(buscadorPanel, "cell 0 0,grow");
        buscadorPanel.setLayout(new MigLayout("", "[214.00,grow]", "[]"));
        lblBuscarPor = new JLabel("Buscar por:");
        buscadorPanel.add(lblBuscarPor, "flowx,cell 0 0");
        
        comboBox = new JComboBox<String>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{filtros[0].toUpperCase(), filtros[1].toUpperCase()}));

        buscadorPanel.add(comboBox, "cell 0 0,growy");

        tfBuscador = new JTextField();
        tfBuscador.setFont(new Font("Dialog", Font.PLAIN, 14));
        buscadorPanel.add(tfBuscador, "cell 0 0,grow");
        tfBuscador.setColumns(10);

        btnBuscar = new JButton("");
        btnBuscar.setIcon(new ImageIcon(PanelClientes.class.getResource("/elementosPersonalizados/lupa_mini.png")));
        buscadorPanel.add(btnBuscar, "cell 0 0,growy");

        btnAnadir = new JButton("Añadir");
        buscadorPanel.add(btnAnadir, "cell 0 0,growy");
        
        btnAnadir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String funcionBoton = btnAnadir.getText();
        		
        		if(funcionBoton.equals("Cancelar")) {
        			//Si se estaba creando un registro cierra el formulario
                    if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("[Nuevo]")) {
                        tabbedPanel.remove(tabbedPanel.getSelectedIndex());
                    //Si se estaba editando recupera sus valores anteriores
                    } else {
                    	restaurarValoresOriginales();
                    }
        			modoEdicion(false);
        		} else {
        			crearFormulario();
        			modoEdicion(true);
        		}
        	}
        });

        btnEditar = new JButton("Editar");
        buscadorPanel.add(btnEditar, "cell 0 0,growy");
        btnEditar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String funcionBoton = btnEditar.getText();
        		
        		if(funcionBoton.equals("Guardar")) {
        			//Si se estaba rellenando un registro lo intenta crear
                    if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("[Nuevo]")) {
                    	int idRegistro = crearRegistro();
                    
                    	if(idRegistro == -1) {
                    		mensajeError("Comprueba los datos introducidos.");
                    		modoEdicion(true);
                    	} else {
                    		tabbedPanel.remove(tabbedPanel.getSelectedIndex());
                    		mensajeInfo("El registro se creó con éxito.");                        	
                        	//Eliminar [nuevo] y hacer una pestaña mostrando el registro O cambiarle el titulo y añadirlo a PestanasMostradas
                        	crearPestanaRegistro(idRegistro);
                        	modoEdicion(false);
                    	}
                    //Si estaba editando un registro lo intenta actualizar
                    } else {
            			guardarCambios();
            			//modoEdicion(true);
                    }
        		} else {
        			if(tabbedPanel.getSelectedIndex() != -1) {
        	        	almacenarValoresOriginales();
        				modoEdicion(true);
        			}
        		}
        	}
        });
        
        tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPanel, "cell 0 1,grow");
        
        
        tabbedPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (editando) {
                    //Comprobar si el índice de la pestaña editando es válido
                    if (indicePestanaEditando >= 0 && indicePestanaEditando < tabbedPanel.getTabCount()) {
                        //Si se intenta cambiar de pestaña durante la edición vuelve a seleccionar la que se está editando
                        if (tabbedPanel.getSelectedIndex() != indicePestanaEditando) {
                            SwingUtilities.invokeLater(() -> tabbedPanel.setSelectedIndex(indicePestanaEditando));
                        }
                    } else {
                        //En caso de cierre de ventana o indice inválido
                        editando = false;
                    }
                }
            }
        });

    }

    protected void modoEdicion(boolean activado) {
        //Cambia el modo
        editando = activado;

        if (editando) { 
        	btnAnadir.setText("Cancelar");
        	btnEditar.setText("Guardar");
        	btnBuscar.setEnabled(false);
        } else { 
            btnAnadir.setText("Añadir");
            btnEditar.setText("Editar");
        	btnBuscar.setEnabled(true);
        }

        ventanaPrincipal.modoEdicion(editando);
        
    	//Obtiene pestaña que va a ser editable, si es -1 es una creación
        indicePestanaEditando = tabbedPanel.getSelectedIndex();
        if(indicePestanaEditando >= 0) {
            FormularioBase pestanaEditar = (FormularioBase) tabbedPanel.getSelectedComponent();
            pestanaEditar.modoEdicion(editando);
        }

    }
     
    protected abstract FormularioBase crearFormularioEspecifico();
    
    protected void crearFormulario() {
        FormularioBase formulario = crearFormularioEspecifico();
        tabbedPanel.add("[Nuevo]", formulario);
        tabbedPanel.setSelectedComponent(formulario);
    }
    
    protected void guardarCambios() {
    	if(editarRegistro()) {
    		mensajeInfo("Se han guardado los cambios correctamente.");
    		modoEdicion(false);
    	} else {
    		mensajeError("No ha sido posible guardar los datos.");
    		modoEdicion(true);
    	}
    }
        
    protected abstract int crearRegistro();
    protected abstract void crearPestanaRegistro(int indexPestana);

    protected abstract boolean editarRegistro();
    
    protected int buscarPestana(int id) {
        Component pestana = pestanasMostradas.get(id);
        if (pestana != null) {
            //Si la pestaña está abierta, devuelve su índice en el tabbedPanel
            return tabbedPanel.indexOfComponent(pestana);
        }
        return -1; //La pestaña no existe
    }

    protected void agregarBotonCerrar(int idPestana) {
        Component pestana = pestanasMostradas.get(idPestana);
        if (pestana != null) {
            int index = tabbedPanel.indexOfComponent(pestana);
            if (index != -1) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                JLabel label = new JLabel(tabbedPanel.getTitleAt(index));
                
                JButton btnCerrar = new JButton("x");
                btnCerrar.setOpaque(false);
                btnCerrar.setContentAreaFilled(false);
                btnCerrar.setBorderPainted(false);
                
                panel.add(label);
                panel.add(btnCerrar);
                panel.setOpaque(false);
                
                btnCerrar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        cerrarPestana(idPestana);
                    }
                });
                
                tabbedPanel.setTabComponentAt(index, panel);
            }
        }
    }

    protected void cerrarPestana(int idPestana) {
        Component pestana = pestanasMostradas.get(idPestana);
        if (pestana != null && editando == false) {
            tabbedPanel.remove(pestana);
            pestanasMostradas.remove(idPestana);
        }
    }
    
    public void restaurarEstiloBuscador() {
        tfBuscador.setBackground(UIManager.getColor("TextField.background"));
        SwingUtilities.updateComponentTreeUI(tfBuscador);
    }
    
    
    protected void mensajeError(String mensajeError) {
        JOptionPane.showMessageDialog(this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void mensajeInfo(String mensajeExito) {
    	JOptionPane.showMessageDialog(this, mensajeExito, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static Integer obtenerIdPestana(Map<Integer, Component> mostradas, Component pestana) {
        for (Map.Entry<Integer, Component> entry : mostradas.entrySet()) {
            if (entry.getValue().equals(pestana)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public void almacenarValoresOriginales() {
    	FormularioBase datos = (FormularioBase) tabbedPanel.getSelectedComponent();
	    for (Component comp : datos.getComponents()) {
	        if (comp instanceof JTextField) {
	            JTextField textField = (JTextField) comp;
	            valoresOriginales.put(textField, textField.getText());
	        }
	    }
    }
    
    private void restaurarValoresOriginales() {
        for (Map.Entry<JTextField, String> valor : valoresOriginales.entrySet()) {
        	valor.getKey().setText(valor.getValue());
        	valor.getKey().setBackground(UIManager.getColor("TextField.background"));
        	SwingUtilities.updateComponentTreeUI(tfBuscador);
        }
    }
}
