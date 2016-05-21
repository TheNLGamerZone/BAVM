package me.nlt.bavm;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Display
{
    private int width, height;
    public JTextArea textArea;

    private Object lockObject;

    /**
     * Display constructor
     *
     * @param width  Width
     * @param height Height
     */
    public Display(int width, int height, Object lockObject)
    {
        this.width = width;
        this.height = height;
        this.lockObject = lockObject;

        this.initDisplay();
    }

    /**
     * Hiermee wordt de display 'opgestart'
     */
    private void initDisplay()
    {
        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();
        this.textArea = new JTextArea(width, height);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        DefaultCaret defaultCaret = (DefaultCaret) this.textArea.getCaret();

        // JPanel, JTextArea en JScrollPane 'configureren'
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.textArea.setEditable(false);
        jPanel.setBorder(new TitledBorder(new EtchedBorder(), "Actie venster"));
        jPanel.add(jScrollPane);

        // Hier zorgen we dat onClose() wordt aangeroepen als het programma stopt
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // Op deze manier kunnen we data opslaan voordat het programma stopt
                onClose();
            }
        });

        // Wat standaard dingen opzetten
        jFrame.setTitle("BAVM");
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        // Object unlocken
        this.resumeLoading();
    }

    /**
     * Unlocked het lockObject zodat de main thread verder gaat met laden
     */
    private void resumeLoading()
    {
        // Object unlocken in een synchronized block zodat we de juiste MonitorState krijgen
        synchronized (lockObject)
        {
            lockObject.notify();
        }
    }

    /**
     * Wordt aangeroepen als de gebruiker het scherm wegklikt zodat we dingen kunnen opslaan
     */
    private void onClose()
    {
        System.out.println("Afsluiten ..");
        System.exit(1);
    }

    /**
     * Hiermee kan tekst worden toegevoegd aan de JTextArea en dus worden getoond
     *
     * @param strings
     */
    public void appendText(String... strings)
    {
        // Op EDT uitvoeren
        EventQueue.invokeLater(() -> {
            // Door alle strings lopen en die printen in het actie venster
            for (String string : strings)
            {
                textArea.append(string + "\n");
            }
        });

    }
}
