package resultados;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

import general.ConfiguracionManager;

@SuppressWarnings("serial")
public abstract class ResultadosBase<T> extends JDialog {
    protected List<T> resultados;
    protected JPanel panel;
    protected String nombreFiltro;
    protected ConfiguracionManager configuracionManager;

    public ResultadosBase(Frame padre, List<T> resultados, String nombreFiltro, ConfiguracionManager configuracionManager) {
        super(padre, "Resultados", true);
        this.resultados = resultados;
        this.nombreFiltro = nombreFiltro;
        this.configuracionManager = configuracionManager;
        this.panel = new JPanel(new BorderLayout());
        getContentPane().add(panel);

        iniciarComponentes();
        iniciarCierreVentana();
    }

    protected abstract void iniciarComponentes();

    protected void iniciarCierreVentana() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarCierre();
            }
        });
    }

    protected void confirmarCierre() {
        UIManager.put("OptionPane.yesButtonText", "Si");
        int opcion = JOptionPane.showConfirmDialog(panel, "¿Deseas cerrar sin seleccionar un resultado?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    protected void centrarValoresCeldas(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    protected abstract void configurarBotonSeleccionar(JTable table);
}
