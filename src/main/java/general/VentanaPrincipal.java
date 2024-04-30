package general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

import org.example.*;
import org.pushingpixels.substance.api.skin.SubstanceNightShadeLookAndFeel;

import org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel;

import paneles.PanelClientes;
import paneles.PanelEmpleados;
import paneles.PanelFacturacion;
import paneles.PanelListas;
import paneles.PanelTemas;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {
    JPanel panelGeneral;
    CardLayout paneles;
    JMenu menuClientes;
    JMenu menuEmpleados;
    JMenu menuFacturacion;
    JMenu menuMusica;
    JMenu menuConfiguracion;
    
    public Map<TipoMenu, JPanel> panelesMap;
    public boolean enModoEdicion = false;
    
    public enum TipoMenu {
        CLIENTES("Clientes"),
        EMPLEADOS("Empleados"),
        FACTURACION("Facturación"),
        MUSICA("Música"),
        TEMAS("Temas"),
        LISTAS("Listas");

        public final String nombre;

        TipoMenu(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
    
	ConfiguracionManager configuracionManager = new ConfiguracionManager();

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame ventanaPrincipal = new VentanaPrincipal();
                ventanaPrincipal.setMinimumSize(new Dimension(700, 500));
                ventanaPrincipal.setPreferredSize(new Dimension(700, 500));
                ventanaPrincipal.pack();
                ventanaPrincipal.setVisible(true);
                ventanaPrincipal.setLocationRelativeTo(null);
            }
        });
    }
    
    public VentanaPrincipal() {
        super("Mi primer ERP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        panelesMap = new HashMap<>();
        
        //Crea la barra de menú y sus elementos
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        menuClientes = crearMenu(TipoMenu.CLIENTES);
        menuBar.add(menuClientes);
        
        menuEmpleados = crearMenu(TipoMenu.EMPLEADOS);
        menuBar.add(menuEmpleados);
        
        menuFacturacion = crearMenu(TipoMenu.FACTURACION);
        menuBar.add(menuFacturacion);
        
        menuMusica = new JMenu(TipoMenu.MUSICA.getNombre());
        menuMusica.add(crearMenuItem(TipoMenu.TEMAS));
        menuMusica.add(crearMenuItem(TipoMenu.LISTAS));
        menuBar.add(menuMusica);
        
        menuConfiguracion = crearMenuConfiguracion();
        menuBar.add(menuConfiguracion);

        setJMenuBar(menuBar);

        //Alberga los diferentes paneles de los menús
        panelGeneral = new JPanel();
        panelGeneral.setLayout(new CardLayout(0, 0));
        paneles = (CardLayout) panelGeneral.getLayout();
        getContentPane().add(panelGeneral);
                
        for (TipoMenu tipoMenu : TipoMenu.values()) {
            JPanel panel = crearPanel(tipoMenu);
            panelesMap.put(tipoMenu, panel);
            panelGeneral.add(panel, "panel" + tipoMenu.getNombre());
        }
        
        //Gestiona las opciones del menú de configuración
        aplicarModoOscuro(configuracionManager.obtenerPropiedad("modoOscuro").equals("true"));
        //aplicarModoEdicion(configuracionManager.obtenerPropiedad("modoEditable").equals("true"));
        
        HibernateUtils.iniciar();
    }
    

    public void modoEdicion(boolean editando) {
    	enModoEdicion = editando;
        menuClientes.setEnabled(!enModoEdicion);
        menuEmpleados.setEnabled(!enModoEdicion);
        menuFacturacion.setEnabled(!enModoEdicion);
        menuMusica.setEnabled(!enModoEdicion);
        menuConfiguracion.setEnabled(!enModoEdicion);
    }
    
    //Cuando seleccionas un menú muestra su panel, si no existe lo crea
    public void mostrarPanel(TipoMenu TipoMenu) {
    	if(enModoEdicion) {
    		return;
    	}
    	
        String nombrePanel = "panel" + TipoMenu.getNombre();
        JPanel panel = panelesMap.get(TipoMenu);
        if (panel == null) {
            panel = crearPanel(TipoMenu);
            panelesMap.put(TipoMenu, panel);
            panelGeneral.add(panel, nombrePanel);
        }
        
        paneles.show(panelGeneral, nombrePanel);
        panelGeneral.revalidate();
        panelGeneral.repaint();
    }

    private JPanel crearPanel(TipoMenu TipoMenu) {
        String[] filtros = obtenerFiltros(TipoMenu);
        switch (TipoMenu) {
            case CLIENTES:
                return new PanelClientes(TipoMenu.getNombre(), filtros, configuracionManager,this);
            case EMPLEADOS:
                return new PanelEmpleados(TipoMenu.getNombre(), filtros, configuracionManager, this);
            case FACTURACION:
                return new PanelFacturacion(TipoMenu.getNombre(), filtros, configuracionManager, this);
            case TEMAS:
                return new PanelTemas(TipoMenu.getNombre(), filtros, configuracionManager, this);
            case LISTAS:
                return new PanelListas(TipoMenu.getNombre(), filtros, configuracionManager, this);
            default:
                return new JPanel();
        }
    }
    
    private JMenu crearMenu(TipoMenu TipoMenu) {
        JMenu menu = new JMenu(TipoMenu.getNombre());
        menu.setMnemonic(TipoMenu.getNombre().charAt(0));

        menu.addMenuListener(new MenuListener() {
            public void menuCanceled(MenuEvent arg0) {}
            public void menuDeselected(MenuEvent arg0) {}
            public void menuSelected(MenuEvent arg0) {
                mostrarPanel(TipoMenu);
            }
        });

        return menu;
    }
    
    private JMenuItem crearMenuItem(TipoMenu TipoMenu) {
        JMenuItem menuItem = new JMenuItem(TipoMenu.getNombre());
        menuItem.setAccelerator(KeyStroke.getKeyStroke(TipoMenu.getNombre().charAt(0), InputEvent.ALT_DOWN_MASK));
        menuItem.addActionListener(e -> mostrarPanel(TipoMenu));

        return menuItem;
    }
    
    private JMenu crearMenuConfiguracion() {
        JMenu menu = new JMenu("Configuración");
        menu.setMnemonic('o');

        JCheckBoxMenuItem chkModoOscuro = new JCheckBoxMenuItem("Modo oscuro", configuracionManager.obtenerPropiedad("modoOscuro").equals("true"));
        chkModoOscuro.addActionListener(e -> aplicarModoOscuro(chkModoOscuro.isSelected()));

        //JCheckBoxMenuItem chkModoEditable = new JCheckBoxMenuItem("Modo editable", configuracionManager.obtenerPropiedad("modoEditable").equals("true"));
        //chkModoEditable.addActionListener(e -> aplicarModoEdicion(chkModoEditable.isSelected()));

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
        menu.add(chkModoOscuro);
        menu.add(itemCerrarSesion);
        
        return menu;
    }

    public static String[] obtenerFiltros(TipoMenu tipo) {
        switch (tipo) {
            case CLIENTES:
                return new String[]{"lastName", "customerId"};
            case EMPLEADOS:
                return new String[]{"lastName", "title"};
            case FACTURACION:
                return new String[]{"customerId", "invoiceId"};
            case TEMAS:
                return new String[]{"name", "title"};
            case LISTAS:
                return new String[]{"playlistId", "name"};
            default:
                return new String[]{"",""};
        }
    }
     
    private void aplicarModoOscuro(boolean activado) {
        try {
            if (activado) {
                UIManager.setLookAndFeel(new SubstanceNightShadeLookAndFeel());
            } else {
                UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
            }
            SwingUtilities.updateComponentTreeUI(this);          
            
        	configuracionManager.guardarPropiedad("modoOscuro", Boolean.toString(activado));
        } catch (Exception e) {
            configuracionManager.guardarPropiedad("modoOscuro", Boolean.toString(activado));
        }
    }
    
    
//    private void aplicarModoEdicion(boolean activado) {
//        configuracionManager.guardarPropiedad("modoEditable", Boolean.toString(activado));
//    }
}
