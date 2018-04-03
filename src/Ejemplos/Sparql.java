package Ejemplos;

import java.util.ArrayList;
import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

public class Sparql {

    public static void main(String[] args) {
        LogCtl.setCmdLogging();
        consultaDatos();
        //consultaRemota();
        consultaCombinada();
    }

    private static void consultaDatos() {
        Model model = RDFDataMgr.loadModel("ficheros-ejemplo-sparql/datos-ejemplo-sparql.ttl");
        String strConsulta = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
                + "PREFIX eg: <http://www.si2.com/si2/>\n"
                + "\n"
                + "select ?nombre ?amigo\n"
                + "where{\n"
                + "	?x foaf:name ?nombre.\n"
                + "	?x foaf:knows/foaf:name ?amigo.\n"
                + "}";
        ResultSet resultados = null;
        try {
            Query consulta = QueryFactory.create(strConsulta);
            try (QueryExecution ejecucion = QueryExecutionFactory.create(consulta, model)) {
                resultados = ejecucion.execSelect();
                resultados = ResultSetFactory.copyResults(resultados);
//            for (;resultados.hasNext();) {
//                QuerySolution solucion = resultados.nextSolution();
//                System.out.println("Solucion -> " + solucion);
//                RDFNode node = solucion.get("nombre");
//                System.out.println("El nombre es: " + PrintUtil.print(node));
//            }
            }
        } catch (QueryParseException e) {
            System.out.println("Error");
        }
        ResultSetFormatter.out(System.out, resultados);

    }

    private static void consultaRemota() {
        String strConsulta = "select distinct ?Concept where {[] a ?Concept}";

        Query consulta = QueryFactory.create(strConsulta);
        
        ResultSet resultados = null;
        try (QueryExecution ejecucion = QueryExecutionFactory.sparqlService("http://aemet.linkeddata.es/sparql", consulta)) {
            resultados = ejecucion.execSelect();
            resultados = ResultSetFactory.copyResults(resultados);
            ResultSetFormatter.out(System.out, resultados);
        }

    }

    private static void consultaCombinada() {
        ArrayList<String> nombreGrafos = new ArrayList<>();
        nombreGrafos.add("ficheros-ejemplo-sparql/datos-ejemplo-sparql.ttl");
        nombreGrafos.add("ficheros-ejemplo-sparql/datos-ejemplo-sparql-2.ttl");
        Dataset dataset = DatasetFactory.create(nombreGrafos);
        
        String strConsulta = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
                + "PREFIX eg: <http://www.si2.com/si2/>\n"
                + "\n"
                + "select ?nombre ?amigo\n"
                + "where{\n"
                + "	?x foaf:name ?nombre.\n"
                + "	?x foaf:knows/foaf:name ?amigo.\n"
                + "}";
        ResultSet resultados = null;
        try {
            Query consulta = QueryFactory.create(strConsulta);
            try (QueryExecution ejecucion = QueryExecutionFactory.create(consulta, dataset)) {
                resultados = ejecucion.execSelect();
                resultados = ResultSetFactory.copyResults(resultados);
            }
        } catch (QueryParseException e) {
            System.out.println("Error");
        }
        ResultSetFormatter.out(System.out, resultados);
    }

}
