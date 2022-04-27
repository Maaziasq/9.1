package com.example.a91;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import java.util.Collections;

public class TheaterRepo {

    private ArrayList<Theater> theaters;
    private ArrayList<String> stringTheaters;
    private static TheaterRepo repo = new TheaterRepo();
    int searchCounter = 0;

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

    //Reads theater information from Finnkino XML
    public void readTheaters() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            for (int i = 0; i< nList.getLength(); i++){
                Node node = nList.item(i);


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

    //Reads movies from selected theater from Finnkino XML and OMDB
    public void readMovies(String date, int choice) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = theaters.get(choice).getId();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area="+id+"&dt="+date;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            ArrayList<Movie> movies = new ArrayList<>();


            OMDBClient omdb = new OMDBClient();

            for (int i = 0; i< nList.getLength(); i++){
                Node node = nList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String aika = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String saika = aika.substring(11,16);
                    //String pubYear = element.getElementsByTagName("PubDate").item(0).getTextContent().substring(0,4);
                    //String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String originalTitle = element.getElementsByTagName("OriginalTitle").item(0).getTextContent();
                    String omdbJson = omdb.searchMovieByTitle(originalTitle,"d70c841d");
                    JSONObject omdbSearch = new JSONObject(omdbJson);
                    searchCounter++;
                    String response = omdbSearch.getString("Response");

                    if(response.equals("True")){
                        JSONArray search = omdbSearch.getJSONArray("Search");
                        //int n = search.length();
                        //for (int x = 0; i<n ; i++)
                        {
                            JSONObject movie = search.getJSONObject(0);
                            String imdbID = movie.getString("imdbID");
                            String omdbJsonID = omdb.searchMovieById(imdbID, "d70c841d");
                            JSONObject omdbIdSearch = new JSONObject(omdbJsonID);
                            searchCounter++;
                            String rating = omdbIdSearch.getString("imdbRating");
                            String title = omdbIdSearch.getString("Title");
                            movies.add(new Movie(title, saika, rating));
                    }

                    }

                }
            }

            theaters.get(choice).setMovies(movies);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Searches performed: "+searchCounter);
            System.out.println("###########MOVIES DONE###########");
        }
    }

    //Reads movies from all theaters
    public ArrayList<String> readAll(String nimi) {
        ArrayList<String> all= new ArrayList<>();
        all.add(0,nimi);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ArrayList<Integer> areas = new ArrayList<Integer>();
            Collections.addAll(areas,1014 ,1015 ,1016, 1017, 1041, 1018, 1019, 1021, 1022);

            for(int i = 0; i<areas.size();i++){


                String urlString = "https://www.finnkino.fi/xml/Schedule/?Area="+areas.get(i).toString();
                Document doc = builder.parse(urlString);
                doc.getDocumentElement().normalize();
                //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");



                for (int j = 0; j< nList.getLength(); j++){
                    Node node = nList.item(j);
                    //System.out.println("Elements in this: "+node.getNodeName());

                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;

                        String time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                        String stringTime = time.substring(11,16);
                        String title = element.getElementsByTagName("Title").item(0).getTextContent();
                        String location = element.getElementsByTagName("Theatre").item(0).getTextContent();
                        if (title.equals(nimi)){
                            all.add(location + " " + stringTime);
                        }

                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("###########MOVIES DONE###########");
        }
        return all;
    }


}
/*
1014
1015
1016
1017
1041
1018
1019
1021
1022
 */
