package com.cooperativabeb;

import com.cooperativabeb.view.LoginFrame;
import com.cooperativabeb.view.SplashScreen;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            SplashScreen splash = new SplashScreen();
            splash.mostrar(() ->
                SwingUtilities.invokeLater(() ->
                    new LoginFrame().setVisible(true)
                )
            );
        });
    }
}
