package formularios;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import com.toedter.calendar.JCalendar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;

import general.ConfiguracionManager;
import paneles.PanelBase;

@SuppressWarnings("serial")
public abstract class FormularioBase extends JPanel {
	protected ConfiguracionManager configuracionManager;
	protected PanelBase panelBase;
    
	protected FormularioBase(ConfiguracionManager configuracionManager, PanelBase panelBase) {
		this.configuracionManager = configuracionManager;
		this.panelBase = panelBase;
	}

    public void modoEdicion(boolean editando) {
        Component[] components = this.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setEditable(editando);
            } else if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals("SCH")) {
                    button.setEnabled(editando);
                }
            }
        }
    }
	
	protected void mostrarCalendario(JButton btn, JTextField tf) {
	    // Crear un JDialog no modal y sin decoración
		JDialog dialog = new JDialog();
		dialog.setModal(false);
		dialog.setUndecorated(true);

	    dialog.setUndecorated(true);
	    dialog.setLayout(new BorderLayout());

	    JCalendar calendario = new JCalendar();
	    dialog.add(calendario, BorderLayout.CENTER);

	    // Botón para confirmar la selección de la fecha
	    JButton btnAceptar = new JButton("Aceptar");
	    btnAceptar.addActionListener(e -> {
	        Date selectedDate = calendario.getDate();

	        // Establecer la fecha en el JTextField
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	        tf.setText(dateFormat.format(selectedDate));

	        dialog.dispose(); // Cerrar el diálogo
	    });

	    JPanel bottomPanel = new JPanel(); // Panel para contener el botón
	    bottomPanel.add(btnAceptar);
	    dialog.add(bottomPanel, BorderLayout.SOUTH);

	    dialog.pack();

	    // Posicionar el diálogo justo debajo del botón que se presionó
	    Point btnLocation = btn.getLocationOnScreen();
	    dialog.setLocation(btnLocation.x, btnLocation.y + btn.getHeight());

	    dialog.setVisible(true);
	}
	
    protected void mensajeError(String mensajeError) {
        JOptionPane.showMessageDialog(this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void restaurarEstiloCampos() {
    	for(Component componente: this.getComponents()) {
    		if(componente instanceof JTextComponent) {
    			JTextComponent campo = (JTextComponent) componente;
    			campo.setBackground(UIManager.getColor("TextField.background"));
    		}
    	}
    }
}