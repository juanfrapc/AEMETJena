package Ejemplos;

import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.VCARD;

public class Jena {
    
    static String libroURI = "http://web.com/libro1234";
    static String titulo = "Crepúsculo";
    static String fecha = "2005";
    
    static String creatorURI = "http://web.com/creator1234";
    static String nombre = "Stephanie";
    static String apellido = "Meyer";
    
    public static void main(String[] args) {
        LogCtl.setCmdLogging();
        Model modelo = ModelFactory.createDefaultModel();
        modelo.setNsPrefix("web", "http://web.com/");
        
        Resource creator1 = modelo.createResource(creatorURI);
        creator1.addProperty(VCARD.NAME, nombre);
        creator1.addProperty(VCARD.Family, apellido);
        
        Resource libro1 = modelo.createResource(libroURI);
        libro1.addProperty(DC_11.title, titulo);
        libro1.addProperty(DC_11.date, fecha);
        libro1.addProperty(DC_11.creator, creator1);
        
        StmtIterator iter = modelo.listStatements();
        while(iter.hasNext()){
            Statement declaration = iter.next();
            System.out.println("Declaracion: " + declaration);
            Resource subject = declaration.getSubject();
            Property predicate = declaration.getPredicate();
            RDFNode object = declaration.getObject();
            System.out.println("\tSujeto: " + subject);
            System.out.println("\tPredicado: " + predicate);
            if (object instanceof Resource) {
                System.out.println("\tObjeto tipo recurso: " + object);
            }else{
                System.out.println("\tObjeto literal: " + object);
            }
        }
        
        System.out.println("");
        Resource resource = modelo.getResource(libroURI);
        System.out.println("Recurso: " + resource);
        System.out.println("\t Titulo: " + resource.getProperty(DC_11.title).getString());
        System.out.println("\t Autor: " + resource.getProperty(DC_11.creator).getResource().getProperty(VCARD.NAME).getString());
        System.out.println("\t Fecha: " + resource.getProperty(DC_11.date).getString());
        
// Serializar el grafo
//        RDFDataMgr.write(System.out, modelo, RDFFormat.TURTLE_PRETTY);
        Model modelo2 = RDFDataMgr.loadModel("ejemplo.ttl");
//        RDFDataMgr.write(System.out, modelo2, RDFFormat.TURTLE_PRETTY);
        Model union = modelo.union(modelo2);
        
        System.out.println("");
        resource = union.getResource("http://www.si2.com/si/libro4");
        System.out.println("Recurso: " + resource);
        System.out.println("\t Titulo: " + resource.getProperty(DC_11.title).getString());
        System.out.println("\t Autor: " + resource.getProperty(DC_11.creator).getResource().getProperty(VCARD.NAME).getString());
        System.out.println("\t Fecha: " + resource.getProperty(DC_11.date).getString());
    }
    
}
