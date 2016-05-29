package me.nlt.bavm.files;

import me.nlt.bavm.BAVM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.URISyntaxException;

public class FileManager
{
    private String mainDir = "";
    private File mainDirectory = null;
    private File storageFile = null;

    public boolean firstStart = false;

    public FileManager()
    {
        this.checkFiles();
    }

    private void checkFiles()
    {
        try
        {
            mainDir = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/') + "/BAVM";
            mainDirectory = new File(mainDir);
            storageFile = new File(mainDir + "/data.xml/");

            storageFile.getParentFile().mkdirs();

            if (!storageFile.exists())
            {
                BAVM.getDisplay().appendText("Bestanden aan het maken ...");
                firstStart = true;

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document document = docBuilder.newDocument();

                // Elementen maken
                Element rootElement = document.createElement("data");
                Element players = document.createElement("players");
                Element teams = document.createElement("teams");
                Element player = document.createElement("player");
                Element team = document.createElement("team");

                // Elementen vullen
                document.appendChild(rootElement);
                rootElement.appendChild(players);
                rootElement.appendChild(teams);
                players.appendChild(player);
                teams.appendChild(team);
                player.setAttribute("id", "-1");
                team.setAttribute("id", "-1");

                // Placeholders neerzetten
                Element playerString = document.createElement("dataString");
                Element teamString = document.createElement("dataString");

                playerString.appendChild(document.createTextNode("NULL <-> PH"));
                teamString.appendChild(document.createTextNode("NULL <-> PH"));
                player.appendChild(playerString);
                team.appendChild(teamString);

                // Naar XML bestand schrijven
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(storageFile);

                transformer.transform(source, result);
            }
        } catch (URISyntaxException | ParserConfigurationException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    public String readPlayerData(int ID)
    {
        return "";
    }
}
