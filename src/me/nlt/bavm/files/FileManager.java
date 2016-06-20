package me.nlt.bavm.files;

import me.nlt.bavm.BAVM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class FileManager
{
    public File storageFile = null;

    private Document document;
    private Element players;
    private Element teams;
    private Element coaches;
    private Element matches;

    public boolean firstStart = false;

    /**
     * FileManager constructor
     */
    public FileManager()
    {
        this.checkFiles();
    }

    /**
     * Wordt bij opstarten aangeroepen
     * Controleerd of het databestand al bestaat en maakt deze als het nodig is
     */
    private void checkFiles()
    {
        try
        {
            // Huidige directory krijgen en een nieuw bestand maken
            String mainDir = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/') + "/BAVM";
            storageFile = new File(mainDir + "/data.xml/");

            storageFile.getParentFile().mkdirs();

            // Document builder aanroepen (We gebruiken DOM, achteraf blee SAX misschien handiger)
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Controleren of het bestand al bestaat
            if (!storageFile.exists())
            {
                // Flag firstStart omzetten naar try en bestanden maken
                BAVM.getDisplay().appendText("Bestanden aan het maken ...");
                firstStart = true;

                document = docBuilder.newDocument();

                // Elementen maken
                Element rootElement = document.createElement("data");
                players = document.createElement("players");
                teams = document.createElement("teams");
                coaches = document.createElement("coaches");
                matches = document.createElement("matches");
                Element player = document.createElement("player");
                Element team = document.createElement("team");
                Element coach = document.createElement("coach");
                Element match = document.createElement("match");

                // Elementen vullen
                document.appendChild(rootElement);
                rootElement.appendChild(players);
                rootElement.appendChild(teams);
                rootElement.appendChild(coaches);
                rootElement.appendChild(matches);
                players.appendChild(player);
                players.setAttribute("amount", "0");
                teams.appendChild(team);
                teams.setAttribute("amount", "0");
                coaches.setAttribute("amount", "0");
                coaches.appendChild(coach);
                matches.setAttribute("amount", "0");
                matches.setAttribute("week", "0");
                matches.setAttribute("season", "0");
                matches.appendChild(match);
                player.setAttribute("id", "-1");
                team.setAttribute("id", "-1");
                coach.setAttribute("id", "-1");
                match.setAttribute("id", "-1");

                // Placeholders neerzetten
                Element playerString = document.createElement("dataString");
                Element teamString = document.createElement("dataString");
                Element coachString = document.createElement("dataString");
                Element matchString = document.createElement("dataString");

                playerString.appendChild(document.createTextNode("NULL <-> PH"));
                teamString.appendChild(document.createTextNode("NULL <-> PH"));
                coachString.appendChild(document.createTextNode("NULL <-> PH"));
                matchString.appendChild(document.createTextNode("NULL <-> PH"));

                // Children aan parents koppeleen
                player.appendChild(playerString);
                team.appendChild(teamString);
                coach.appendChild(coachString);
                match.appendChild(matchString);

                // Naar XML bestand schrijven
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(storageFile);

                transformer.transform(source, result);
            } else
            {
                // Bestaande document parsen
                document = docBuilder.parse(storageFile);

                // Alle nodes verkrijgen
                NodeList nodeList = document.getElementsByTagName("data");

                // Door de nodeList loopen
                for (int i = 0; i < nodeList.getLength(); i++)
                {
                    // ChildNodes verkrijgen
                    NodeList list = nodeList.item(i).getChildNodes();

                    // Door alle children loopen
                    for (int j = 0; j < list.getLength(); j++)
                    {
                        // Node verkrijgen
                        Node node = list.item(j);

                        // Checken of de huidige node een element node is
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            // Node 'omzetten' naar een element
                            Element element = (Element) node;

                            // Checken welke node dit is en dan koppelen aan de juiste globale variable
                            switch (element.getNodeName())
                            {
                                case "players":
                                    players = element;
                                    break;
                                case "teams":
                                    teams = element;
                                    break;
                                case "coaches":
                                    coaches = element;
                                    break;
                                case "matches":
                                    matches = element;
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (URISyntaxException | ParserConfigurationException | TransformerException | SAXException | IOException e)
        {
            // Error printen in eigen console
            BAVM.getDisplay().printException(e);
        }
    }

    /**
     * Data uit geheugen naar XML-bestand schrijven
     */
    public void saveData()
    {
        try
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(storageFile);

            transformer.transform(source, result);
        } catch (Exception e)
        {
            BAVM.getDisplay().printException(e);
        }
    }

    /**
     * Data naar geheugen schrijven
     *
     * @param tag        De naam van de juiste node
     * @param dataString De string die alle data over het object bevast
     * @param ID         De ID van het object
     */
    public void writeData(String tag, String dataString, int ID)
    {
        // Lijst met nodes krijgen die horen bij de gegeven tag
        NodeList nodeList = document.getElementsByTagName(tag);
        Node node = null;

        // Door de nodeList loopen
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            // Subnode verkrijgen
            Node subNode = nodeList.item(i);

            // Checken of de subnode een element is
            if (subNode.getNodeType() == Node.ELEMENT_NODE)
            {
                // Subnode 'omzetten' naar een element
                Element element = (Element) subNode;

                // Checken of het gegeven ID overeenkomt met die van het huidige element
                if (Integer.parseInt(element.getAttribute("id")) == ID)
                {
                    node = subNode;
                }
            }

        }

        // Geen node gevonden, dus er bestaat nog geen node met dit ID
        if (node == null)
        {
            // Element maken met de gegeven tag
            Element element = document.createElement(tag);
            Element dataElement = document.createElement("dataString");

            // ID voor het element koppelen
            element.setAttribute("id", ID + "");

            // Data in element zetten
            dataElement.appendChild(document.createTextNode(dataString));

            // Data aan element koppelen
            element.appendChild(dataElement);

            int newAmount;

            // Checken in welke nodeList we zitten zodat we het attribuut 'amount' kunnen aanpassen
            switch (tag)
            {
                case "player":
                    players.appendChild(element);

                    newAmount = Integer.parseInt(players.getAttribute("amount")) + 1;
                    players.removeAttribute("amount");
                    players.setAttribute("amount", newAmount + "");
                    break;
                case "team":
                    teams.appendChild(element);

                    newAmount = Integer.parseInt(teams.getAttribute("amount")) + 1;
                    teams.removeAttribute("amount");
                    teams.setAttribute("amount", newAmount + "");
                    break;
                case "coach":
                    coaches.appendChild(element);

                    newAmount = Integer.parseInt(coaches.getAttribute("amount")) + 1;
                    coaches.removeAttribute("amount");
                    coaches.setAttribute("amount", newAmount + "");
                    break;
                case "match":
                    matches.appendChild(element);

                    newAmount = Integer.parseInt(matches.getAttribute("amount")) + 1;
                    matches.removeAttribute("amount");
                    matches.setAttribute("amount", newAmount + "");
            }
        } else
        {
            // Childnodes verkrijgen
            NodeList nodeChildren = node.getChildNodes();

            // Door de childnodes loopen
            for (int i = 0; i < nodeChildren.getLength(); i++)
            {
                // Childnode verkrijgen
                Node child = nodeChildren.item(i);

                // Checken of dit de dataString node is
                if (child.getNodeName().equals("dataString"))
                {
                    // Mocht dit zo zijn wordt de data van die node vervangen met de gegeven dataString
                    child.setTextContent(dataString);
                }
            }
        }
    }

    /**
     * Methode om het bestand te verwijderen
     */
    public void deleteData()
    {
        storageFile.delete();
    }

    /**
     * Methode om een week of seizoen toe te voegen
     *
     * @param tag       Naam van het attribuut
     * @param newSeason Boolean die aangeeft of de week gereset moet worden
     */
    public void addDate(String tag, boolean newSeason)
    {
        // Nieuwe waarde maken
        int newWeek = Integer.parseInt(matches.getAttribute(tag)) + 1;

        // Nieuwe waarde naar geheugen schrijven
        matches.removeAttribute(tag);
        matches.setAttribute(tag, (newSeason ? 0 : newWeek) + "");
    }

    /**
     * Methode om te kijken in welke week of seizoen we ons bevinden
     *
     * @param tag Naam van het attribuut
     * @return Int die staat voor de week of maand
     */
    public int getDateNumber(String tag)
    {
        try
        {
            // Betand normalizen
            document.getDocumentElement().normalize();

            // Nodelist van matches verkijgen
            NodeList nodeList = document.getElementsByTagName("matches");

            // Door de nodeList loopen
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                // Node verkrijgen
                Node node = nodeList.item(i);

                // Checken of de huidige node een element is
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    // Node 'omzetten' naar een element
                    Element element = (Element) node;

                    // Integer parsen en returnen
                    return Integer.parseInt(element.getAttribute(tag));
                }
            }
        } catch (Exception e)
        {
            // Error printen
            BAVM.getDisplay().printException(e);
        }

        return -1;
    }

    /**
     * Methode om het 'amount'-attribuut te lezen
     *
     * @param tag Naam van de node
     * @return Hoeveel elementen van een partentNode
     */
    public int readAmount(String tag)
    {
        try
        {
            // Betand normalizen
            document.getDocumentElement().normalize();

            // Nodelist verkijgen
            NodeList nodeList = document.getElementsByTagName(tag);

            // Door de nodeList loopen
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                // Node verkrijgen
                Node node = nodeList.item(i);

                // Checken of de huidige node een element is
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    // Node 'omzetten' naar een element
                    Element element = (Element) node;

                    // Integer parsen en returnen
                    return Integer.parseInt(element.getAttribute("amount"));
                }
            }
        } catch (Exception e)
        {
            // Error printen
            BAVM.getDisplay().printException(e);
        }

        return -1;
    }

    /**
     * Methode om de dataString van een element te lezen
     *
     * @param tag Naam van de node
     * @param ID  ID van het object
     * @return De dataString die bij het element hoort
     */
    public String readData(String tag, int ID)
    {
        try
        {
            // Betand normalizen
            document.getDocumentElement().normalize();

            // Nodelist verkijgen
            NodeList nodeList = document.getElementsByTagName(tag);

            // Door de nodeList loopen
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                // Node verkrijgen
                Node node = nodeList.item(i);

                // Checken of de huidige node een element is
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    // Node 'omzetten' naar een element
                    Element element = (Element) node;

                    // Checken of het 'ID'-attribuut overeenkomt met het gegeven ID
                    if (Integer.parseInt(element.getAttribute("id")) == ID)
                    {
                        // Integer parsen en returnen
                        return element.getElementsByTagName("dataString").item(0).getTextContent();
                    }
                }

            }
        } catch (Exception e)
        {
            // Error printen
            BAVM.getDisplay().printException(e);
        }

        return null;
    }

    /**
     * Methode om snel de matchlogs van een bepaalde match te krijgen
     *
     * @param matchID Het ID van de match
     * @return De matchlog die uit het bestand is gehaald
     */
    public ArrayList<String> getMatchLog(int matchID)
    {
        // Data lezen van de desbtreffende match
        ArrayList<String> matchLog = new ArrayList<>();
        String matchString = this.readData("match", matchID).trim().substring(6).replaceAll("}", "");

        // Door de dataString loopen
        for (String matchData : matchString.split(","))
        {
            String data = matchData.split("=")[1];

            // Kijken of de huidige sectie de logs bevat
            switch (matchData.split("=")[0])
            {
                case "logs":
                    // Door de logs loopen
                    for (String logMessage : data.split("@"))
                    {
                        // Bepaalde tekens vervangen en dan in de arrayList doen
                        matchLog.add(logMessage.replaceAll("_", " ").replaceAll("%", ",").replaceAll("~", "="));
                    }
                    break;
                default:
                    break;
            }
        }

        // Matchlogs returnen
        return matchLog;
    }

    /**
     * Methode om alle data naar het geheugen schrijven
     */
    public void saveAll()
    {
        // Eerst controleren of er data is en dan naar het geheugen schrijven
        if (BAVM.getTeamManager() != null && BAVM.getTeamManager().dataLoaded)
            BAVM.getTeamManager().saveManageables(false);
        if (BAVM.getPlayerManager() != null && BAVM.getPlayerManager().dataLoaded)
            BAVM.getPlayerManager().saveManageables(false);
        if (BAVM.getCoachManager() != null && BAVM.getCoachManager().dataLoaded)
            BAVM.getCoachManager().saveManageables(false);
        if (BAVM.getMatchManager() != null && BAVM.getMatchManager().dataLoaded)
            BAVM.getMatchManager().saveManageables(false);

        // Data van het geheugen naar het databestand schrijven
        this.saveData();
    }
}
