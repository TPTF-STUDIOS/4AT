package xyz.paintingthefish.chatti;

// IO and configuration stuff
import org.ini4j.Wini;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import xyz.paintingthefish.chatti.Shared;
import xyz.paintingthefish.chatti.FixedSizeOffHeapIntMap;

// Graphics
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 *
 * @author To Paint The Fish Studios
 * @version 0.0.0
 */
public class Chatti {
    static Wini main_cfg;
    static JFrame window;

    public static void init_window() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window = new JFrame("Chatti");
        window.setName("Chatti");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize((int) Math.round(screenSize.width / 2.0), (int) Math.round(screenSize.height / 2.0));
        window.setLayout(new FlowLayout());
        JLabel loadingText = new JLabel("Loading...");
        loadingText.setPreferredSize(new Dimension(200, 50));
        window.add(loadingText);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("nux")) {
            main_cfg = Shared.get_ini_from_fpath(Paths.get(System.getProperty("user.home") + "/.chatti/config.ini"));
        }
        init_window();
    }
}
