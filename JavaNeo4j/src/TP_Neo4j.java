
import java.util.Scanner;
import org.neo4j.driver.v1.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author malik
 */
public class TP_Neo4j {
    
    
    static Driver driver ;
    static Session session ;
    public static void main(String[] args) {
        // TODO code application logic here
        
        //connexion server Neo4j
        driver = GraphDatabase.driver("bolt://192.168.56.50");
        session = driver.session();
        
        //menu generale de l'application 
        String selection = "";
        do {
           
            System.out.println("1 - Liste des films");
            System.out.println("2 - Liste des personnes");
            System.out.println("3 - 3 films les mieux notés");
            System.out.println("4 - 5 film proches");
            System.out.println("5 - Déconnexion");
            
            Scanner sc = new Scanner(System.in);
            selection = sc.next();
            switch(selection) {
                case "1":
                    ListeFilm();
                    break;
                case "2":
                    ListePers();
                    break;
                case "3":
                    Mieux();
                    break;
                case "4":
                    Proche();
                    break;
                case "5":
                    deconnexion();
                    break;
                default:
                    System.out.println("Selectionner une option entre 1 et 5 !");
            }
         
        }while (!selection.equals("5"));
        
    }
    
    // affiche la liste des films 
    static void ListeFilm(){
        // effectue la requete sur Noe4j
        StatementResult result = session.run("MATCH (m:Movie) return m.title,m.released,m.tagline order by m.released DESC");
        // lire le resultat 
        while(result.hasNext()){
            // lecture de la ligne suivante 
            Record record = result.next();
            // affiche le resultat d'une ligne 
            System.out.println(record.get("m.title").asString()+" - "+record.get("m.released").asInt()+" ("+record.get("m.tagline").asString()+")");
        }
        
    }
    //affiche la liste des personnes 
    static void ListePers(){
         String annee;
         String nom ="";
         String type ="";
         int born;
        StatementResult result = session.run("MATCH (p:Person)-[r]-(m:Movie) return distinct p.name,p.born,type(r) ,m.title order by p.name,type(r),m.title ASC");
        // lire le resultat 
        while(result.hasNext()){
            // lecture de la ligne suivante 
            Record record = result.next();
            try {
                born = record.get("p.born").asInt();
                annee ="" + born;
            } catch (Exception e) {
                born = 0;
                annee = "?";
            }
            if (nom.equals(record.get("p.name").asString())){
            // affiche le resultat d'une ligne
                if(type.equals(record.get("type(r)").asString())){
                    System.out.print(", "+record.get("m.title").asString());
                }else{
                    System.out.println("'] ");
                    System.out.println("    "+record.get("type(r)").asString()+" ['"+record.get("m.title").asString()+" "); 
                }
            }else{
                if(nom != ""){
                    System.out.println("'] ");
                    System.out.println(" ");
                }
                System.out.println(record.get("p.name").asString()+" ("+annee+")");
                System.out.print("    "+record.get("type(r)").asString()+" ['"+record.get("m.title").asString()+" ");
            }
            nom =record.get("p.name").asString();
            type =record.get("type(r)").asString();
        }
        System.out.println("'] ");
    }
     
    static void Mieux(){
        StatementResult result = session.run("MATCH (p:Person)-[r:REVIEWED]->(m:Movie) return m.title, r.rating order by r.rating desc limit 3");
        // lire le resultat 
        while(result.hasNext()){
            // lecture de la ligne suivante 
            Record record = result.next();
            // affiche le resultat d'une ligne 
            System.out.println(record.get("m.title").asString()+" - "+record.get("r.rating").asInt());
        }
    }
    static void Proche(){
        
        String MovieName;
        System.out.println("Donner le titre du film :");
        Scanner sc2 = new Scanner(System.in);
        MovieName = sc2.nextLine();
        // verifie si ce film existe 
        StatementResult verif = session.run("match (m:Movie) where m.title = '"+ MovieName +"' return count(m)");
        if (verif.hasNext()){
            StatementResult result = session.run("match (m:Movie)-[a:ACTED_IN]-(p:Person)-[a2:ACTED_IN]-(m2:Movie) where m.title = '"+ MovieName +"' return distinct m.title,m2.title,count(m) as cpt order by cpt DESC limit 5");
            System.out.println("Au plus 5 films proches de '"+MovieName+"' :");
            // lire le resultat 
            while(result.hasNext()){
                // lecture de la ligne suivante 
                Record record = result.next();
                // affiche le resultat d'une ligne 
                System.out.println(record.get("m2.title").asString()+" - "+record.get("cpt").asInt());
            }
        }else{
            System.out.println("Le film '"+MovieName+"' n'existe pas dans la base...");
        }
    }
    static void deconnexion(){
        // fermeture de la session 
        session.close();
        // fermeture de la connexion
        driver.close();
    }
}
