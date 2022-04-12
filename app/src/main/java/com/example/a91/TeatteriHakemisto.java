package com.example.a91;

import android.os.StrictMode;

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
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TeatteriHakemisto {

    private ArrayList<Teatteri> teatterit;
    private ArrayList<String> stringTeatterit;


    private static TeatteriHakemisto hakemisto = new TeatteriHakemisto();

    private TeatteriHakemisto(){
        teatterit = new ArrayList<Teatteri>();
    }

    public static TeatteriHakemisto getInstance(){
        return hakemisto;
    }

    public ArrayList<Teatteri> getTeatterit() {
        return teatterit;
    }

    public void setTeatterit(ArrayList<Teatteri> teatterit) {
        this.teatterit = teatterit;
    }

    public void teatteritToString(){
        stringTeatterit = new ArrayList<>();
        for (Teatteri t :teatterit){
            stringTeatterit.add(t.getPaikka());
        }
    }

    public ArrayList<String> getStringTeatterit() {
        hakemisto.teatteritToString();
        return stringTeatterit;
    }

    public void lueTeatterit() {
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
                    teatterit.add(new Teatteri(element.getElementsByTagName("ID").item(0).getTextContent(),element.getElementsByTagName("Name").item(0).getTextContent()));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("###########TEATTERIT DONE###########");
        }
    }

    public void lueElokuvat(String date, int valinta) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = teatterit.get(valinta).getId();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area="+id+"&dt="+date;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            ArrayList<String> elokuvat = new ArrayList<>();


            for (int i = 0; i< nList.getLength(); i++){
                Node node = nList.item(i);
                //System.out.println("Elements in this: "+node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String aika = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String saika = aika.substring(11,16);
                    elokuvat.add(element.getElementsByTagName("Title").item(0).getTextContent()+" "+saika);

                }
            }

            teatterit.get(valinta).setElokuvat(elokuvat);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("###########ELOKUVAT DONE###########");
        }
    }

}
