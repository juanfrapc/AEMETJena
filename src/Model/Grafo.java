package Model;

import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.JSONException;
import org.json.JSONObject;

public class Grafo {

    private static final String si2 = "http://www.si2.com/";
    private static final String aemet = "http://aemet.linkeddata.es/ontology/";
    private final Model modelo;
    private Property nombre;
    
    public Grafo() {
        LogCtl.setCmdLogging();
        modelo = ModelFactory.createDefaultModel();
        modelo.setNsPrefix("si2", si2);
        nombre = ResourceFactory.createProperty(aemet , "stationName" );
    }
    
    public boolean addStation(JSONObject node) throws JSONException{
        Resource resource = modelo.createResource(si2 + node.getString("indicativo"));
        resource.addLiteral(nombre, node.getString("nombre"));
        return true;
    }
    
    public void print(){
        RDFDataMgr.write(System.out, modelo, RDFFormat.TURTLE_PRETTY);
    }
    
}
