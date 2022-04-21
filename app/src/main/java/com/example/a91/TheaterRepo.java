package com.example.a91;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TheaterRepo {

    private ArrayList<Theater> theaters;
    private ArrayList<String> stringTheaters;


    private static TheaterRepo repo = new TheaterRepo();

    private TheaterRepo(){
        theaters = new ArrayList<Theater>();
    }

    public static TheaterRepo getInstance(){
        return repo;
    }

    public ArrayList<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(ArrayList<Theater> theaters) {
        this.theaters = theaters;
    }

    public void teatteritToString(){
        stringTheaters = new ArrayList<>();
        for (Theater t : theaters){
            stringTheaters.add(t.getLocation());
        }
    }

    public ArrayList<String> getStringTheaters() {
        repo.teatteritToString();
        return stringTheaters;
    }

    public void readTheaters() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            for (int i = 0; i< nList.getLength(); i++){
                Node node = nList.item(i);
                //System.out.println("Elements in this: "+node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    theaters.add(new Theater(element.getElementsByTagName("ID").item(0).getTextContent(),element.getElementsByTagName("Name").item(0).getTextContent()));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("###########THEATERS DONE###########");
        }
    }

    public void readMovies(String date, int choice) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = theaters.get(choice).getId();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area="+id+"&dt="+date;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            ArrayList<String> movies = new ArrayList<>();


            for (int i = 0; i< nList.getLength(); i++){
                Node node = nList.item(i);
                //System.out.println("Elements in this: "+node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String aika = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String saika = aika.substring(11,16);
                    movies.add(element.getElementsByTagName("Title").item(0).getTextContent()+" "+saika);

                }
            }

            theaters.get(choice).setMovies(movies);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("###########MOVIES DONE###########");
        }
    }

}
