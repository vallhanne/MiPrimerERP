package elementosPersonalizados;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class CustomTabbedPane extends JTabbedPane {
    public CustomTabbedPane() {
        super();
    }

    @Override
    public void addTab(String title, final Component component) {
        super.addTab(title, component);

        int index = indexOfComponent(component);

        // Agrega el botón de cierre en la solapa de la pestaña
        JButton closeButton = new JButton("X");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarPestana(component);
            }
        });

        // Crea un panel que contiene el título de la pestaña y el botón de cierre
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tabPanel.setOpaque(false);
        tabPanel.add(new JLabel(title));
        tabPanel.add(closeButton);

        // Establece el panel personalizado como el componente de la solapa
        setTabComponentAt(index, tabPanel);
    }

    private void cerrarPestana(Component componentePestana) {
        int index = indexOfComponent(componentePestana);
        if (index != -1) {
            removeTabAt(index);
        }
    }
}