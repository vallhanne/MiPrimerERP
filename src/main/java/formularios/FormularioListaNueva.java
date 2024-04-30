package formularios;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.example.HibernateUtils;

import general.ConfiguracionManager;
import general.Conversiones;
import paneles.PanelListas;
import resultados.ResultadosListas;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FormularioListaNueva extends FormularioLista {

	public JButton btnAnadir;
	public List<Integer> idsCancionesAgregadas;
	
	public FormularioListaNueva(ConfiguracionManager configuracionManager, PanelListas panelListas) {
		super(configuracionManager, panelListas);

		btnAnadir = new JButton("Añadir");
		add(btnAnadir, "cell 0 3 3 1,alignx center");
		btnAnadir.addActionListener(e -> agregarLineaTabla());
		idsCancionesAgregadas = new ArrayList<Integer>();
		//modelarTabla();
		copiarDatos(this);
	}
	
	public void agregarLineaTabla() {
		List<Object[]> temasAElegir = HibernateUtils.buscarCancionesParaLista();
     	JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
     	Object[] temaElegido = ResultadosListas.mostrarDialog(framePadre, temasAElegir, "tracks", configuracionManager);
     	
     	if(temaElegido != null) {
     		int idTemaNuevo = (int) temaElegido[0];
            boolean agregado = false;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                int idEnTabla = Integer.parseInt(String.valueOf(model.getValueAt(i, 0)));
                if (idEnTabla == idTemaNuevo) {
                	agregado = true;
                    break;
                }
            }

            if (!agregado) {
                Object[] fila = {idTemaNuevo, temaElegido[1], temaElegido[2], Conversiones.convertirMilisegundos(Integer.parseInt(temaElegido[3].toString()))};
                model.addRow(fila);
                actualizarDuracionTotal((int) temaElegido[3]);
            } else {
                JOptionPane.showMessageDialog(this, "La canción seleccionada ya ha sido añadida anteriormente.");
            }
            
//            if (!idsCancionesAgregadas.contains(idTemaNuevo)) {
//                Object[] fila = {idTemaNuevo, temaElegido[1], temaElegido[2], Conversiones.convertirMilisegundos(Integer.parseInt(temaElegido[3].toString()))};
//                model.addRow(fila);
//                idsCancionesAgregadas.add(idTemaNuevo);
//                actualizarDuracionTotal((int) temaElegido[3]);
//            } else {
//                JOptionPane.showMessageDialog(this, "La canción seleccionada ya ha sido añadida anteriormente.");
//            }
     	}
	}
	
	//Cambiar bigdecimal a formateo de mins y s desde ms
	private void actualizarDuracionTotal(int msNuevaCancion) {	
	    if(model.getRowCount() == 0) {
		    tfDuracion.setText("0 min 0 s");

	    } else {
		    int duracionTotal = Conversiones.convertirAMilisegundos(tfDuracion.getText());

		    for (int i = 0; i < model.getRowCount(); i++) {
		        int duracionCancion = Conversiones.convertirAMilisegundos(String.valueOf(model.getValueAt(i, model.findColumn("Duración"))));
		        duracionTotal += duracionCancion;
		    }
		   
		    tfDuracion.setText(Conversiones.convertirMilisegundos(duracionTotal));
	    }
	}
	
}
