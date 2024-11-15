package cnr.isti.aimh.dh.geographica2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.fuseki.geosparql.GeosparqlServer;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.tdb2.DatabaseMgr;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.geosparql.*;
import org.apache.jena.query.*;

import cnr.isti.aimh.dh.geographica2.util.ConfigProperties;
import cnr.isti.aimh.dh.geographica2.util.Fuseki;

public class Main {
    
    
    public static void main(String[] args) throws IOException{

      
		
        // Read file config.properties
        ConfigProperties prop = new ConfigProperties("config.properties");

        Fuseki fusekiKB = new Fuseki(prop.getDataset_url(), prop.getFuseki_user(), prop.getFuseki_pw());

		// Start the timer
        // long startTime = System.nanoTime();

        // // Perform the specific operation
        // fusekiKB.InsertModelIntoGraph("kb/corine.nt", "clc");
		// fusekiKB.InsertModelIntoGraph("kb/geonames.nt", "geonames");
		// fusekiKB.InsertModelIntoGraph("kb/linkedgeodata.nt", "lgd");
		// fusekiKB.InsertModelIntoGraph("kb/gag.nt", "gag");
		// fusekiKB.InsertModelIntoGraph("kb/dbpedia.nt", "dbpedia");
		// fusekiKB.InsertModelIntoGraph("kb/hotspots.nt", "hotspots");
		// fusekiKB.InsertModelIntoGraph("kb/census.new_york_roads.rdf.NO_CRS.nt", "cencus");
		fusekiKB.InsertModelIntoGraph("kb/generator_512.nt", "syntetic");
		// fusekiKB.InsertModelIntoGraph("kb/dataset.rdf", "gsbenchmark");
        // Stop the timer
        // long endTime = System.nanoTime();

        

		// fusekiKB.DropGraph("clc");
		// fusekiKB.DropGraph("geonames");
		// fusekiKB.DropGraph("lgd");
		// fusekiKB.DropGraph("gag");
		// fusekiKB.DropGraph("dbpedia");
		// fusekiKB.DropGraph("hotspots");
		// fusekiKB.DropGraph("cencus");
		// System.out.println(fusekiKB.queryExec(""));
		 // Start the GeoSPARQL Fuseki server
        String datasetName = "geographica";
        // Dataset dataset = DatasetFactory.createTxnMem(); // In-memory dataset
		String tdb2Directory = "run/dataset/geographica";
        Dataset dataset = TDB2Factory.connectDataset(tdb2Directory);  // Persistent TDB2 dataset
        int port = 3030;
        boolean loopbackOnly = true;
        boolean allowUpdate = true;

        // GeosparqlServer server = new GeosparqlServer(port, datasetName, loopbackOnly, dataset, allowUpdate);
        // server.start();

		// try {
		// 	// Wait for 30 seconds (30,000 milliseconds)
		// 	Thread.sleep(30000);
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }
		
        // System.out.println("GeoSPARQL server running at http://localhost:" + port + "/" + datasetName);

       

        // // Add a shutdown hook to stop the server on exit
        // Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

		// Start the timer
        long startTime = System.nanoTime();

		

        // // Perform the specific operation
        // fusekiKB.InsertModelIntoGraph("kb/corine.nt", "clc");
		// fusekiKB.InsertModelIntoGraph("kb/geonames.nt", "geonames");
		// fusekiKB.InsertModelIntoGraph("kb/linkedgeodata.nt", "lgd");
		// fusekiKB.InsertModelIntoGraph("kb/gag.nt", "gag");
		// fusekiKB.InsertModelIntoGraph("kb/dbpedia.nt", "dbpedia");
		// fusekiKB.InsertModelIntoGraph("kb/hotspots.nt", "hotspots");
		// fusekiKB.InsertModelIntoGraph("kb/census.new_york_roads.rdf.NO_CRS.nt", "cencus");
		// fusekiKB.InsertModelIntoGraph("kb/generator_512.nt", "syntetic");
		// fusekiKB.InsertModelIntoGraph("kb/dataset.rdf", "gsbenchmark");
        // Stop the timer
        long endTime = System.nanoTime();

		// Calculate the elapsed time in seconds
        double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;

        // Print the elapsed time
        System.out.println("Time taken for operation: " + elapsedTimeInSeconds + " seconds");

		 // Perform a query after the server is started
		 String queryString = "PREFIX geof: <http://www.opengis.net/def/function/geosparql/> \r\n" + //
		 "\r\n" + //
		 "SELECT (geof:boundary(?o1) AS ?ret) \r\n" + //
		 "WHERE { \r\n" + //
		 "\tGRAPH <http://localhost:3030/geographica/clc> {\r\n" + //
		 "\t\t?s1 <http://geo.linkedopendata.gr/corine/ontology#asWKT> ?o1\r\n" + //
		 "\t} \r\n" + //
		 "} ";  // Example query

		// Method to execute the query
		// Fuseki.executeQuery("http://localhost:3030/geographica", queryString);

		// Path to the file containing the SPARQL query
        // String filePath = "queries/micro/nontopological/query-r01.rq";
        // Fuseki.executeQueryFromFile("http://localhost:3030/geographica", filePath);

		 // Directory containing SPARQL query files
		 String dirMicroNT = "queries/micro/nontopological/";
		 String dirMicroSS = "queries/micro/spatialselections/";
		 String dirMicroSJ = "queries/micro/spatialjoins/";
		 String dirMicroAG = "queries/micro/aggregations/";

		 String dirMacroGC = "queries/macro/geocoding/";
		 String dirMacroRGC = "queries/macro/reversegeocoding/";
		 String dirMacroMS = "queries/macro/mapsearch/";
		 String dirMacroRM = "queries/macro/rapidmapping/";
		 String dirMacroCS = "queries/macro/computestatistics/";

		 String dirSynt = "queries/syntetic/";

		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMicroNT);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMicroSS);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMicroSJ);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMicroAG);

		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMacroGC);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMacroRGC);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMacroMS);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMacroRM);
		//  Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirMacroCS);
		 
		// Fuseki.executeQueriesFromDirectory("http://localhost:3030/geographica", dirSynt);
    

        
             

        


		//read json file data to String
// 		byte[] jsonData = Files.readAllBytes(Paths.get("toponyms.json"));
		
// 		//create ObjectMapper instance
// 		ObjectMapper objectMapper = new ObjectMapper();
		
// 		//convert json string to object
// //		LemmaPrincipale emp = objectMapper.readValue(jsonData, LemmaPrincipale.class);
// 		List<Toponym> emp = objectMapper.readValue(jsonData, new TypeReference<List<Toponym>>(){});
		

//         OntModel model = ModelToponyms.populateModel(ModelToponyms.importModel("https://imagoarchive.it/onto/IMAGO-270222.ttl"), emp);

// 		// model.write(System.out, "TTL") ;

// 		// for (Toponym e : emp) {
// 		// 	  // code block to be executed
// 		// 		System.out.println("Lemma Object\n"+e );
// 		// 	}

//         // Controllo se il modello � valido
// 		// InfModel infmodel = ModelFactory.createRDFSModel(model);
// 		// ValidityReport validity = infmodel.validate();
// 		// if (validity.isValid()) {
// 		// 	// System.out.println("VALIDAZIONE - Il modello è valido\n");
// 		// }
// 		// else {
// 		// 	// STAMPA ERRORI DI VALIDAZIONE
// 		// 	Iterator<Report> reports = validity.getReports();
// 		// 	while(reports.hasNext()) {
// 		// 		// System.out.println("\nERRORE - Il modello non � valido: " + reports.next() + "\n");
// 		// 	}
// 		// }

//         String datasetURL = "https://imagoarchive.it/fuseki/imago";
// 		// String sparqlEndpoint = datasetURL + "/sparql";
// 		// String sparqlUpdate = datasetURL + "/update";
// 		// String graphStore = datasetURL + "/data";
		
// 		RDFConnection conneg = RDFConnectionFactory.connectPW(datasetURL,"fusekitomcat", "C4mbiam1Adess0" );
// 		conneg.update("CLEAR DEFAULT" );
// 		conneg.put(datasetURL+"/toponyms", model);
//         // conneg.load(model); // add the content of model to the triplestore

        }
}
