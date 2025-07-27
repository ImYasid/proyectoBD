package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CambiosColorBoton {
    
    public static void configurarCambiosColor(JLabel label, JPanel panel) {
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                panel.setBackground(new Color(211,180,122));
                label.setForeground(new Color(0,0,0));
                Font font = label.getFont();
                label.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                panel.setBackground(new Color(253,239,213)); 
                label.setForeground(new Color(0,0,0));
                Font font = label.getFont();
                label.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
            }
        });
    }
    
    public static void configurarCambiosColorDos(JLabel label, JPanel panel) {
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                panel.setBackground(new Color(255,102,0));
                label.setForeground(new Color(255,255,255));
                Font font = label.getFont();
                label.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                panel.setBackground(new Color(255,51,0)); 
                label.setForeground(new Color(255,255,255));
                Font font = label.getFont();
                label.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
            }
        });
    }
}

