package me.nlt.bavm;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Display
{
    private int width, height;
    private boolean clear;

    public JTextArea textArea;
    public JTextField jTextField;

    private JFrame jFrame;
    private JScrollPane jScrollPane;
    private DefaultCaret defaultCaret;

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

    public void printException(Exception exception)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        String string;

        exception.printStackTrace(printStream);
        string = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);

        appendText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ",
                "De volgende fout is opgetreden: ", string, "Laat het ons weten en we zullen het zo spoedig mogelijk oplossen!",
                "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }

    /**
     * Hiermee wordt de display 'opgestart'
     */
    private void initDisplay()
    {
        jFrame = new JFrame();
        JPanel jPanel = new JPanel();

        this.textArea = new JTextArea(width, height);
        this.jTextField = new JTextField();
        this.jScrollPane = new JScrollPane(textArea);
        this.defaultCaret = (DefaultCaret) this.textArea.getCaret();

        // JPanel, JTextArea en JScrollPane 'configureren'
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // JLabel en textArea 'configureren'
        this.textArea.setEditable(false);
        this.jTextField.setEnabled(false);
        this.jTextField.setFont(new Font("Verdana", Font.ITALIC, 12));
        this.jTextField.setText("Input box");
        this.jTextField.setPreferredSize(new Dimension(500, 30));
        this.jFrame.setResizable(false);

        // Focuslistener voor jTextField voor de input hint
        jTextField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                jTextField.setText("Input box");
            }

            @Override
            public void focusGained(FocusEvent e)
            {
                jTextField.setText("");
            }
        });

        // Borders zetten
        jPanel.setBorder(new TitledBorder(new EtchedBorder(), "Actie venster"));

        // Componenten toevoegen
        jPanel.add(jScrollPane);
        jPanel.add(jTextField);

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
    public void onClose()
    {
        System.out.println("Afsluiten ..");
        appendText("\nAfsluiten .. Forceer het programma niet af te sluiten, opslaan kan even duren!");

        BAVM.getFileManager().saveAll();
        
        if (BAVM.getFileManager().firstStart && 
        		(BAVM.getTeamManager() == null ||
        		!BAVM.getTeamManager().dataLoaded || 
        		BAVM.getPlayerManager() == null || 
        		!BAVM.getPlayerManager().dataLoaded || 
        		!BAVM.getCoachManager().dataLoaded || 
        		BAVM.getMatchManager() == null ||
        		!BAVM.getMatchManager().dataLoaded))
        {
        	System.out.println("Bestanden en managers zijn nog niet geladen tijdens aanmaken objecten, alles is verwijderd!");
        	BAVM.getFileManager().storageFile.delete();
        }
        
        System.exit(1);
    }

    public void clearText()
    {
        clear = true;
        textArea.setText("");
    }

    /**
     * Hiermee kan tekst worden toegevoegd aan de JTextArea en dus worden getoond
     *
     * @param strings De strings die toegevoegd moeten worden
     */
    public void appendText(String... strings)
    {
        this.appendText(true, strings);
    }

    /**
     * Hiermee kan je ook strings toevoegen alleen heb je hier ook nog de keuze of de string op een nieuwe regel moet
     *
     * @param newLine De boolean die aangeeft of het op een nieuwe regel moet of niet
     * @param strings De strings die toegevoegd moeten worden
     */
    public void appendText(boolean newLine, String... strings)
    {
        // Op EDT uitvoeren
        EventQueue.invokeLater(() -> {
            // Door alle strings lopen en die printen in het actie venster
            for (String string : strings)
            {
                if (this.clear)
                {
                    textArea.append("" + string);
                    this.clear = false;
                } else
                {
                    textArea.append((newLine ? "\n" : "") + string);
                }

                textArea.setCaretPosition(textArea.getDocument().getLength());
            }

            jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        });
    }

    public void appendText(int delay, String... strings)
    {
        this.appendText(delay, true, strings);
    }

    public void appendText(int delay, boolean newLine, String... strings)
    {
        for (String string : strings)
        {
            this.appendText(newLine, string);

            try {
                Thread.sleep(delay);
            } catch(InterruptedException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }
    }

    /**
     * Hiermee kan je de input van de gebruiker lezen zonder eerst een vraag te stellen
     *
     * @return De input van de gebruiker
     */
    public String readLine()
    {
        return this.readLine("");
    }

    public String readLine(boolean printAnswer, String questionString)
    {
        final String[] inputLine = {null};

        // Zet de textField aan zodat mensen dingen kunnen typen
        jTextField.setEnabled(true);
        jTextField.requestFocus();

        // Stel de vraag mits die er is
        if (!questionString.trim().equals(""))
        {
            appendText(questionString);
        }

        // Action listener maken
        ActionListener actionListener = e -> {
            // Even checken of de gebruiker wel iets heeft getypt en stuur zo nodig een bericht waarin gevraagd wordt of hij/zij dat wilt doen
            if (!jTextField.getText().trim().equals(""))
            {
                // Zet de tekst in de textField in een array (variable moeten constant/final zijn om in een lambda te kunnen worden gebruikt)
                inputLine[0] = jTextField.getText();

                // De input van de gebruiker ook even 'printen'
                if (printAnswer)
                {
                    appendText(false, "   --> " + inputLine[0]);
                }

                // Unlock het lockObject weer zodat de Thread die deze methode aanvroeg weer verder kan met het resultaat
                synchronized (inputLine)
                {
                    inputLine.notify();
                }
            } else
            {
                appendText("Je dient wel iets in te typen!");
            }
        };

        // Ga verder op EDT en wacht op het punt dat de gebruiker de 'enter' toets indrukt
        EventQueue.invokeLater(() -> jTextField.addActionListener(actionListener));

        // Lock het lockObject zodat deze moet wachten tot de gebruiker iets heeft ingetypt
        synchronized (inputLine)
        {
            try
            {
                inputLine.wait();
            } catch (InterruptedException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }

        // Reset de waarden weer
        jTextField.removeActionListener(actionListener);
        jTextField.setText("Input box");
        jTextField.setEnabled(false);

        return inputLine[0];
    }

    /**
     * Hiermee kan je de input van de gebruiker lezen
     *
     * @param questionString De vraag die wordt gesteld aan de gebruiker
     * @return De input van de gebruiker
     */
    public String readLine(String questionString)
    {
        return readLine(true, questionString);
    }

    /**
     * Hiermee kan je om een nummer vragen van de gebruiker zonder een vraag te stellen
     *
     * @return Het resultaat in de vorm van een double
     */
    public double readDouble()
    {
        return this.readDouble(true, "");
    }

    public double readDouble(boolean printValue)
    {
        return this.readDouble(printValue, "");
    }

    /**
     * Hiermee kan je om een nummer vragen van de gebruiker
     * Het nummer wordt automatisch geparsed en mocht dat niet kunnen wordt er een bericht gestuurd
     *
     * @param questionString De vraag die er aan de gebruiker wordt gevraagd
     * @return Het resultaat in de vorm van een double
     */
    public double readDouble(boolean printValue, String questionString)
    {
        double result = Double.MIN_VALUE;

        // Net zo lang blijven vragen totdat de gebruiker een waarde heeft gegeven waar we iets mee kunnen
        while (result == Double.MIN_VALUE)
        {
            // Even kijken of de input wel een nummer is
            try
            {
                result = Double.parseDouble(readLine(printValue, questionString));
            } catch (NumberFormatException e)
            {
                appendText("Er wordt om een nummer gevraagd in het format '1.0' of '1'!");
            }
        }

        return result;
    }
}
