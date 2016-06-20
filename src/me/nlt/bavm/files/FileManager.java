package me.nlt.bavm.files;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import me.nlt.bavm.BAVM;

public class FileManager
{
    private File storageFile = null;

    private Document document;
    private Element players;
    private Element teams;
    private Element coaches;
    private Element matches;

    public boolean firstStart = false;

    public FileManager()
    {
        this.checkFiles();
    }

    private void checkFiles()
    {
        try
        {
            String mainDir = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/') + "/BAVM";
            storageFile = new File(mainDir + "/data.xml/");

            storageFile.getParentFile().mkdirs();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            if (!storageFile.exists())
            {
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
                document = docBuilder.parse(storageFile);

                NodeList nodeList = document.getElementsByTagName("data");

                for (int i = 0; i < nodeList.getLength(); i++)
                {
                    NodeList list = nodeList.item(i).getChildNodes();

                    for (int j = 0; j < list.getLength(); j++)
                    {
                        Node node = list.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element element = (Element) node;

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
            BAVM.getDisplay().printException(e);
        }
    }

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

    public void writeData(String tag, String dataString, int ID)
    {
        NodeList nodeList = document.getElementsByTagName(tag);
        Node node = null;

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node subNode = nodeList.item(i);

            if (subNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) subNode;

                if (Integer.parseInt(element.getAttribute("id")) == ID)
                {
                    node = subNode;
                }
            }

        }

        if (node == null)
        {
            Element element = document.createElement(tag);
            Element dataElement = document.createElement("dataString");

            element.setAttribute("id", ID + "");
            dataElement.appendChild(document.createTextNode(dataString));
            element.appendChild(dataElement);

            int newAmount;
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
            NodeList nodeChildren = node.getChildNodes();

            for (int i = 0; i < nodeChildren.getLength(); i++)
            {
                Node child = nodeChildren.item(i);

                if (child.getNodeName().equals("dataString"))
                {
                    child.setTextContent(dataString);
                }
            }
        }
    }

    public void deleteData()
    {
        storageFile.delete();
    }

    public void addWeek(boolean newSeason)
    {
        int newWeek = Integer.parseInt(matches.getAttribute("week")) + 1;

        matches.removeAttribute("week");
        matches.setAttribute("week", (newSeason ? 0 : newWeek) + "");
    }

    public int getWeekNumber()
    {
        try
        {
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("matches");

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

                    return Integer.parseInt(element.getAttribute("week"));
                }
            }
        } catch (Exception e)
        {
            BAVM.getDisplay().printException(e);
        }

        return -1;
    }

    public int readAmount(String tag)
    {
        try
        {
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(tag);

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

                    return Integer.parseInt(element.getAttribute("amount"));
                }
            }
        } catch (Exception e)
        {
            BAVM.getDisplay().printException(e);
        }

        return -1;
    }

    public String readData(String tag, int ID)
    {
        try
        {
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(tag);

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

                    if (Integer.parseInt(element.getAttribute("id")) == ID)
                    {
                        return element.getElementsByTagName("dataString").item(0).getTextContent();
                    }
                }

            }
        } catch (Exception e)
        {
            BAVM.getDisplay().printException(e);
        }

        return null;
    }
    
    public ArrayList<String> getMatchLog(int matchID)
    {
    	ArrayList<String> matchLog = new ArrayList<>();
    	String matchString = this.readData("match", matchID).trim().substring(6).replaceAll("}", "");
    	
    	for (String matchData : matchString.split(","))
        {
            String data = matchData.split("=")[1];

            switch (matchData.split("=")[0])
            {
                case "logs":
                    for (String logMessage : data.split("@"))
                    {
                        matchLog.add(logMessage.replaceAll("_", " ").replaceAll("%", ",").replaceAll("~", "="));
                    }
                    break;
                default:
                	break;
            }
        }
    	
    	return matchLog;
    }

    public void saveAll()
    {
    	if (BAVM.getTeamManager() != null && BAVM.getTeamManager().dataLoaded) BAVM.getTeamManager().saveManageables(false);
    	if (BAVM.getPlayerManager() != null && BAVM.getPlayerManager().dataLoaded) BAVM.getPlayerManager().saveManageables(false);
    	if (BAVM.getCoachManager() != null && BAVM.getCoachManager().dataLoaded) BAVM.getCoachManager().saveManageables(false);
    	if (BAVM.getMatchManager() != null && BAVM.getMatchManager().dataLoaded) BAVM.getMatchManager().saveManageables(false);
        
        this.saveData();
    }
}
