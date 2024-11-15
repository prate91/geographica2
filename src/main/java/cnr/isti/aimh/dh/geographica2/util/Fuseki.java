package cnr.isti.aimh.dh.geographica2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.fuseki.geosparql.GeosparqlServer;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.update.Update;


public class Fuseki {

    private String dataset_url;
    private String sparql_endpoint = dataset_url + "/sparql";
    private String sparql_update = dataset_url + "/update";
    private String graph_store = dataset_url + "/data";

    private String username;
    private String password;

    public Fuseki(String dataset_url, String username, String password){
        this.dataset_url = dataset_url;
        this.username = username;
        this.password = password;
    }

    public Boolean InsertModelIntoGraph(OntModel model, String graph_name){
        // RDFConnection conneg = RDFConnectionFactory.connectPW(this.dataset_url,this.username, this.password);
        RDFConnection conneg = RDFConnectionFactory.connect(this.dataset_url);
        String graph_url = this.dataset_url + "/" + graph_name;
        System.out.println(graph_url);
        conneg.put(graph_url, model);
        return true;
    }

    public Boolean InsertModelIntoGraph(String filename, String graph_name){
        RDFConnection conneg = RDFConnectionFactory.connectPW(this.dataset_url,this.username, this.password);
        // RDFConnection conneg = RDFConnectionFactory.connect(this.dataset_url);
        String graph_url = this.dataset_url + "/" + graph_name;
        
        System.out.println(graph_url);
        
        conneg.put(graph_url, filename);
        return true;
    }

    

    public Boolean DropGraph(String graph_name){
        RDFConnection conneg = RDFConnectionFactory.connect(this.dataset_url);
        String graph_url = this.dataset_url + "/" + graph_name;
        System.out.println(graph_url);
        conneg.delete(graph_url);
        
        return true;
    }

    public QueryExecution queryExec (String query){
        RDFConnection conneg = RDFConnectionFactory.connect(this.dataset_url);
        QueryExecution qExec = conneg.query("SELECT DISTINCT ?s { ?s ?p ?o }") ;
        return qExec;
    }

     public static void executeQuery(String serviceURI, String queryString) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qe = QueryExecution.service(serviceURI, query)) {
            ResultSet results = qe.execSelect();
            // Print the results to the console
            ResultSetFormatter.out(System.out, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to execute a SPARQL query loaded from a file and measure execution time
    public static void executeQueryFromFile(String serviceURI, String filePath) {
        try {
            // Read the SPARQL query from file
            String queryString = loadQueryFromFile(filePath);

            // Create a query object
            Query query = QueryFactory.create(queryString);

            // Measure the start time
            long startTime = System.nanoTime();

            // Execute the query
            
            try (QueryExecution qe = QueryExecution.service(serviceURI, query)) {
                ResultSet results = qe.execSelect();

                // Measure the end time
                long endTime = System.nanoTime();

                // Calculate the time taken in milliseconds
                double executionTime = (endTime - startTime) / 1_000_000;

                // Print the results to the console
                // ResultSetFormatter.out(System.out, results);

                // Print the time taken
                System.out.println(String.format("Query executed in: %.2f seconds", executionTime));
                // Write to CSV
                try (FileWriter writer = new FileWriter("results.csv", true)) {
                    writer.append(filePath).append(",").append(String.valueOf(executionTime)).append("\n");
                    // System.out.println("Execution time logged in CSV: " + executionTime + " ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load a SPARQL query from a file
    public static String loadQueryFromFile(String filePath) throws IOException {
        StringBuilder queryBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queryBuilder.append(line).append("\n");
            }
        }
        return queryBuilder.toString();
    }


    // Method to iterate through all query files in a directory and execute them
    public static void executeQueriesFromDirectory(String serviceURI, String directoryPath) {
        File dir = new File(directoryPath);
        File[] queryFiles = dir.listFiles((dir1, name) -> name.endsWith(".rq"));  // Filters files with .rq extension

        if (queryFiles != null) {
            for (File queryFile : queryFiles) {
                System.out.println("Executing query from file: " + queryFile.getName());
                executeQueryFromFile(serviceURI, queryFile.getAbsolutePath());
            }
        } else {
            System.out.println("No query files found in directory: " + directoryPath);
        }
    }

    // public Boolean InsertModelIntoGraph(String m, String graph_name){
    //     // RDFConnection conneg = RDFConnectionFactory.connectPW(this.dataset_url,this.username, this.password);
    //     RDFConnection conneg = RDFConnectionFactory.connect(this.dataset_url);
    //     String graph_url = this.dataset_url + "/" + graph_name;
    //     System.out.print(graph_url);
    //     conneg.put(graph_url, m);
    //     return true;
    // }

    public String getDataset_url() {
        return dataset_url;
    }

    public void setDataset_url(String dataset_url) {
        this.dataset_url = dataset_url;
    }

    public String getSparql_endpoint() {
        return sparql_endpoint;
    }

    public void setSparql_endpoint(String sparql_endpoint) {
        this.sparql_endpoint = sparql_endpoint;
    }

    public String getSparql_update() {
        return sparql_update;
    }

    public void setSparql_update(String sparql_update) {
        this.sparql_update = sparql_update;
    }

    public String getGraph_store() {
        return graph_store;
    }

    public void setGraph_store(String graph_store) {
        this.graph_store = graph_store;
    }


    public static void main(String[] args) throws IOException{

      
		
        // Read file config.properties
        ConfigProperties prop = new ConfigProperties("config.properties");

        Fuseki fusekiKB = new Fuseki(prop.getDataset_url(), prop.getFuseki_user(), prop.getFuseki_pw());

	
		// System.out.println(fusekiKB.queryExec(""));
		 // Start the GeoSPARQL Fuseki server
        String datasetName = "geographica";
        // Dataset dataset = DatasetFactory.createTxnMem(); // In-memory dataset
		String tdb2Directory = "run/dataset/geographica";
        Dataset dataset = TDB2Factory.connectDataset(tdb2Directory);  // Persistent TDB2 dataset
        int port = 3030;
        boolean loopbackOnly = true;
        boolean allowUpdate = true;

        GeosparqlServer server = new GeosparqlServer(port, datasetName, loopbackOnly, dataset, allowUpdate);
        server.start();

		// try {
		// 	// Wait for 30 seconds (30,000 milliseconds)
		// 	Thread.sleep(30000);
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }
		
        System.out.println("GeoSPARQL server running at http://localhost:" + port + "/" + datasetName);

       

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
		// fusekiKB.InsertModelIntoGraph("kb/census.new_york_roads.rdf.NO_CRS.nt", "cencus/ontology#");
		// fusekiKB.InsertModelIntoGraph("kb/generator_512.nt", "syntetic");
        // Stop the timer
        long endTime = System.nanoTime();

		// Calculate the elapsed time in seconds
        double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;

        // Print the elapsed time
        System.out.println("Time taken for operation: " + elapsedTimeInSeconds + " seconds");

	
        }

    
}
