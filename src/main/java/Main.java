import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> readExcel(String fileName, String sheetName, int recordId) throws IOException {
        File file = new File(fileName);
        List<String> records = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        DataFormatter formatter = new DataFormatter();
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if (fileExtensionName.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (fileExtensionName.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

        Row row = sheet.getRow(recordId);

        for (int j = 0; j < row.getLastCellNum(); j++) {

            records.add(formatter.formatCellValue(row.getCell(j)));

        }

        return records;
    }


    public static void main(String[] args) throws IOException{

        TextToExcel temp = new TextToExcel();
        temp.changeFormat("agency.txt","agency.xls",",");
        temp.changeFormat("calendar_dates.txt","calendar_dates.xls",",");
        temp.changeFormat("calendar_dates.txt","calendar_dates.xls",",");
        temp.changeFormat("feed_info.txt","feed_info.xls",",");
        temp.changeFormat("routes.txt","routes.xls",",");
        temp.changeFormat("stop_times.txt","stop_times.xls",",");
        temp.changeFormat("stops.txt","stops.xls",",");
        temp.changeFormat("transfers.txt","transfers.xls",",");
        temp.changeFormat("trips.txt","trips.xls",",");

        /*******************************DataProperty and ClassNames Extraction**************************************/
        File calendarDatesFile = new File("calendar_dates.txt");
        Scanner CalendarDatesFileReader = new Scanner(calendarDatesFile);
        String calendarClassName = calendarDatesFile.getName();
        calendarClassName = calendarClassName.replaceAll(".txt","");
        String data = CalendarDatesFileReader.nextLine();
        String[] calendarVariables = data.split(",");
        CalendarDatesFileReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File agencyFile = new File("agency.txt");
        Scanner agencyReader = new Scanner(agencyFile);
        String agencyClassName = agencyFile.getName();
        agencyClassName = agencyClassName.replaceAll(".txt","");
        data = agencyReader.nextLine();
        String[] agencyVariables = data.split(",");
        agencyReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File feedInfoFile = new File("feed_info.txt");
        Scanner feedInfoReader = new Scanner(feedInfoFile);
        String feedInfoClassName = feedInfoFile.getName();
        feedInfoClassName = feedInfoClassName.replaceAll(".txt","");
        data = feedInfoReader.nextLine();
        String[] feedInfoVariables = data.split(",");
        feedInfoReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File routesFile = new File("routes.txt");
        Scanner routesReader = new Scanner(routesFile);
        String routesClassName = routesFile.getName();
        routesClassName = routesClassName.replaceAll(".txt","");
        data = routesReader.nextLine();
        String[] routesVariables = data.split(",");
        routesReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File stopTimesFile = new File("stop_times.txt");
        Scanner stopTimesReader = new Scanner(stopTimesFile);
        String stopTimesClassName = stopTimesFile.getName();
        stopTimesClassName = stopTimesClassName.replaceAll(".txt","");
        data = stopTimesReader.nextLine();
        String[] stopTimesVariables = data.split(",");
        stopTimesReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File stopsFile = new File("stops.txt");
        Scanner stopsReader = new Scanner(stopsFile);
        String stopsClassName = stopsFile.getName();
        stopsClassName = stopsClassName.replaceAll(".txt","");
        data = stopsReader.nextLine();
        String[] stopsVariables = data.split(",");
        stopsReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File transfersFile = new File("transfers.txt");
        Scanner transfersReader = new Scanner(transfersFile);
        String transfersClassName = transfersFile.getName();
        transfersClassName = transfersClassName.replaceAll(".txt","");
        data = transfersReader.nextLine();
        String[] transfersVariables = data.split(",");
        transfersReader.close();
        /**-----------------------------------------------------------------------------------------------------**/
        File tripsFile = new File("trips.txt");
        Scanner tripsReader = new Scanner(tripsFile);
        String tripsClassName = tripsFile.getName();
        tripsClassName = tripsClassName.replaceAll(".txt","");
        data = tripsReader.nextLine();
        String[] tripsVariables = data.split(",");
        tripsReader.close();
        /**-----------------------------------------------------------------------------------------------------**/



        /********************************************************************************************/
        //http://www.semanticweb.org/bisho/ontologies/2021/11/untitled-ontology-13#
        // some definitions
        String baseUri = "http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#";
        // create an empty Model
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        OntClass agencyClass = model.createClass( baseUri + agencyClassName );
        OntClass calendarDatesClass = model.createClass( baseUri + calendarClassName );
        OntClass feedInfoClass = model.createClass(baseUri + feedInfoClassName);
        OntClass routesClass = model.createClass(baseUri + routesClassName);
        OntClass stopTimesClass = model.createClass(baseUri + stopTimesClassName);
        OntClass stopClass = model.createClass(baseUri + stopsClassName);
        OntClass transfersClass = model.createClass(baseUri + transfersClassName);
        OntClass tripsClass = model.createClass(baseUri + tripsClassName);
        OntClass citiesClass = model.createClass(baseUri + "cities");



        //object Properties
        ObjectProperty hasAgency =  model.createObjectProperty(baseUri+"hasAgency_id");
        hasAgency.addDomain(routesClass);
        hasAgency.addRange(agencyClass);
        ObjectProperty hasRoute =  model.createObjectProperty(baseUri+"hasRoute_id");
        hasRoute.addDomain(tripsClass);
        hasRoute.addRange(routesClass);
        ObjectProperty hasTripID =  model.createObjectProperty(baseUri+"hasTrip_id");
        hasTripID.addDomain(stopTimesClass);
        hasTripID.addRange(tripsClass);
        ObjectProperty hasStopID =  model.createObjectProperty(baseUri+"hasStop_id");
        hasStopID.addDomain(stopTimesClass);
        hasStopID.addRange(stopClass);











        /////// dataProperties

        for (int i=0; i<agencyVariables.length; i++)
        {
            DatatypeProperty agencyData = model.createDatatypeProperty( baseUri + agencyVariables[i] );
            agencyData.addDomain( agencyClass );
            agencyData.addRange( XSD.xstring );
            //agency.addProperty(agencyData,agencyRecord.get(i));

        }
        for (int i=0; i<calendarVariables.length; i++)
        {
            DatatypeProperty calendarData = model.createDatatypeProperty( baseUri + calendarVariables[i] );
            calendarData.addDomain( calendarDatesClass );
            calendarData.addRange( XSD.xstring );
        }
        for (int i=0; i<feedInfoVariables.length; i++)
        {
            DatatypeProperty feedData = model.createDatatypeProperty( baseUri + feedInfoVariables[i] );
            feedData.addDomain( feedInfoClass );
            feedData.addRange( XSD.xstring );
        }
        for (int i=0; i<routesVariables.length; i++)
        {
            if(!routesVariables[i].equals("agency_id")) {
                DatatypeProperty routesData = model.createDatatypeProperty(baseUri + routesVariables[i]);
                routesData.addDomain(routesClass);
                routesData.addRange(XSD.xstring);
            }

        }
        for (int i=0; i<stopTimesVariables.length; i++)
        {
            if(!stopTimesVariables[i].equals("trip_id"))
            {
                if(!stopTimesVariables[i].equals("stop_id"))
                {
                    DatatypeProperty stopTimeData = model.createDatatypeProperty( baseUri + stopTimesVariables[i] );
                    stopTimeData.addDomain( stopTimesClass );
                    stopTimeData.addRange( XSD.xstring );
                }

            }
            else if(!stopTimesVariables[i].equals("stop_id"))
            {
                if(!stopTimesVariables[i].equals("trip_id"))
                {
                    DatatypeProperty stopTimeData = model.createDatatypeProperty( baseUri + stopTimesVariables[i] );
                    stopTimeData.addDomain( stopTimesClass );
                    stopTimeData.addRange( XSD.xstring );
                }

            }


        }
        for (int i=0; i<stopsVariables.length; i++)
        {
            if(i==3||i==4)
            {
                DatatypeProperty stopsData = model.createDatatypeProperty( baseUri + stopsVariables[i] );
                stopsData.addDomain( stopClass );
                stopsData.addRange( XSD.xfloat );
            }
            else
            {
                DatatypeProperty stopsData = model.createDatatypeProperty( baseUri + stopsVariables[i] );
                stopsData.addDomain( stopClass );
                stopsData.addRange( XSD.xstring );
            }

        }
        for (int i=0; i<transfersVariables.length; i++)
        {
            DatatypeProperty transfersData = model.createDatatypeProperty( baseUri + transfersVariables[i] );
            transfersData.addDomain( transfersClass );
            transfersData.addRange( XSD.xstring );
        }
        for (int i=0; i< tripsVariables.length; i++)
        {
            if(!tripsVariables[i].equals("route_id"))
            {
                DatatypeProperty tripsData = model.createDatatypeProperty( baseUri + tripsVariables[i] );
                tripsData.addDomain( tripsClass );
                tripsData.addRange( XSD.xstring );
            }
        }
        List<String> citiesProperties = readExcel("cities.xlsx","cities",0);
        for (int i=0; i< citiesProperties.size(); i++)
        {
            if(i==1 || i==2)
            {
                DatatypeProperty citiesData = model.createDatatypeProperty( baseUri + citiesProperties.get(i) );
                citiesData.addDomain( citiesClass );
                citiesData.addRange( XSD.xfloat );
            }
            else
            {
                DatatypeProperty citiesData = model.createDatatypeProperty( baseUri + citiesProperties.get(i) );
                citiesData.addDomain( citiesClass );
                citiesData.addRange( XSD.xstring );
            }



        }
/**---------------------------------------------------------------------------------------------------------------------**/

        /**Agency**/
        for (int j=1; j<=2; j++) {
            List<String> agencyRecord1 = readExcel("agency.xls","sheet1",j);
            Individual agency1 = agencyClass.createIndividual(baseUri+"agency"+j);
            for (int i=0; i< agencyRecord1.size();i++)
            {
                agency1.addProperty(model.getProperty(baseUri+agencyVariables[i]),agencyRecord1.get(i));
            }
        }
        /**Calendar dates**/
        for (int j=1; j<=10; j++)
        {
            List<String> calendarRecord1 = readExcel("calendar_dates.xls","sheet1",j);
            Individual calendarDates1 = calendarDatesClass.createIndividual(baseUri+"calendar_dates"+j);
            for (int i=0; i< calendarRecord1.size();i++)
            {
                calendarDates1.addProperty(model.getProperty(baseUri+calendarVariables[i]),calendarRecord1.get(i));
            }
        }
        /**Feed info**/

        List<String> feedInfoRecord = readExcel("feed_info.xls","sheet1",1);
        Individual feedInfo = feedInfoClass.createIndividual(baseUri+"feed_info"+1);
        for (int i=0; i< feedInfoRecord.size();i++)
        {
            feedInfo.addProperty(model.getProperty(baseUri+feedInfoVariables[i]),feedInfoRecord.get(i));
        }
        /**Routes**/
        for (int j=1; j<=10; j++)
        {
            List<String> routesRecord = readExcel("routes.xls","sheet1",390+j);
            Individual routes = routesClass.createIndividual(baseUri+"routes"+j);
            for (int i=0; i< routesRecord.size();i++)
            {
                if(i==1)
                {

                }
                else
                    routes.addProperty(model.getProperty(baseUri+routesVariables[i]),routesRecord.get(i));
            }
        }
        /**stop times***/
        for (int j=1; j<=10; j++)
        {
            List<String> stopTimesRecord = readExcel("stop_times.xls","sheet1",j);
            Individual stopTimes = stopTimesClass.createIndividual(baseUri+"stop_times"+j);
            for (int i=0; i< stopTimesRecord.size();i++)
            {
                if (i==0||i==3)
                {

                }
                else
                    stopTimes.addProperty(model.getProperty(baseUri+stopTimesVariables[i]),stopTimesRecord.get(i));
            }
        }
        /**Stops***/
        for (int j=1; j<=10; j++)
        {
            List<String> stopsRecord = readExcel("stops.xls","sheet1",4090+j);
            Individual stops = stopClass.createIndividual(baseUri+"stops"+j);
            for (int i=0; i< stopsRecord.size();i++)
            {
                if (i==3|| i==4)
                {
                    Property p = model.getProperty(baseUri+stopsVariables[i]);
                    stops.addLiteral(p,Float.parseFloat(stopsRecord.get(i)));
                }
                else {
                    stops.addProperty(model.getProperty(baseUri+stopsVariables[i]),stopsRecord.get(i));
                }

            }
        }

        /**Transfers****/

        for (int j=1; j<=10; j++)
        {
            List<String> transfersRecord = readExcel("transfers.xls","sheet1",j);
            Individual transfers = transfersClass.createIndividual(baseUri+"transfers"+j);
            for (int i=0; i< transfersRecord.size();i++)
            {
                transfers.addProperty(model.getProperty(baseUri+transfersVariables[i]),transfersRecord.get(i));
            }
        }
        /**Trips**/
        for (int j=1; j<=10; j++)
        {
            List<String> tripsRecord = readExcel("trips.xls","sheet1",j);
            Individual trips = tripsClass.createIndividual(baseUri+"trips"+j);
            for (int i=0; i< tripsRecord.size();i++)
            {
                if(i==0)
                {

                }
                else
                    trips.addProperty(model.getProperty(baseUri+tripsVariables[i]),tripsRecord.get(i));
            }
        }
        /**Cities**/
        for (int j=1; j<=10; j++)
        {
            List<String> citiesRecords = readExcel("cities.xlsx","cities",j);
            Individual cities = citiesClass.createIndividual(baseUri+"cities"+j);
            for (int i=0; i< citiesRecords.size();i++)
            {
                if(i==1 || i==2) {
                    Property p = model.getProperty(baseUri + citiesProperties.get(i));
                    cities.addLiteral(p,Float.parseFloat(citiesRecords.get(i))) ;
                }
                else
                    cities.addProperty(model.getProperty(baseUri + citiesProperties.get(i)), citiesRecords.get(i));

            }
        }

        /**Object properties data entries**/
       //routes has agency
        for (int j=1; j<=10; j++)
        {
            Individual routes = model.getIndividual(baseUri+"routes"+j);
            routes.addProperty(hasAgency,model.getIndividual(baseUri+"agency2"));

        }
        //stop times has trips and stops
        for (int j=1; j<=10; j++)
        {

            Individual stopTimes =  model.getIndividual(baseUri+"stop_times"+j);
            List<String> stopTimesRecord = readExcel("stop_times.xls","sheet1",j);
            for (int i=1;i<10;i++)
            {
                Individual trips =  model.getIndividual(baseUri+"trips"+i);
                String id = trips.getProperty(model.getProperty(baseUri+"trip_id")).toString();
                String[] ids = id.split(",");
                String dataIDs;
                String s = ids[2].substring(2,ids[2].length()-2);
                dataIDs=s;
                if(stopTimesRecord.get(0).equals(dataIDs))
                {
                    stopTimes.addProperty(hasTripID,trips);
                }
            }
            for (int i=1;i<10;i++)
            {
                Individual stops= model.getIndividual(baseUri+"stops"+i);
                String id = stops.getProperty(model.getProperty(baseUri+"stop_id")).toString();
                String[] ids = id.split(",");
                String dataIDs;
                String s = ids[2].substring(2,ids[2].length()-2);
                dataIDs=s;

                if(stopTimesRecord.get(3).equals(dataIDs))
                {

                    stopTimes.addProperty(hasStopID,stops);
                }
            }

        }
        //trips has routes
        for (int j=1; j<=10; j++)
        {


            List<String> tripsRecord = readExcel("trips.xls","sheet1",j);
            Individual trips = model.getIndividual(baseUri+"trips"+j);

            for (int i=1;i<10;i++)
            {
                Individual routes =  model.getIndividual(baseUri+"routes"+i);
                String id = routes.getProperty(model.getProperty(baseUri+"route_id")).toString();
                String[] ids = id.split(",");
                String dataIDs;
                String s = ids[2].substring(2,ids[2].length()-2);
                dataIDs=s;
                if(tripsRecord.get(0).equals(dataIDs))
                {
                    trips.addProperty(hasRoute,routes);
                }
            }
        }








/**-----------------------------------------------------------------------------------------------------------------------**/



        try {
            File myFile = new File("RDF.owl");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating file.");
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter("RDF.owl");
            model.write(writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing in file.");
            e.printStackTrace();
        }

        Model model2 = ModelFactory.createDefaultModel();
        InputStream in = new FileInputStream("RDF.owl");
        model2.read(in, null);
        in.close();


        String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX uri: < http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#\n";

        String queryString = prefix + "SELECT ?subject ?object" +
                "WHERE {?subject rdfs:subClassOf ?object }";

        //Query query = QueryFactory.create(queryString);
       /* try(QueryExecution qexec = QueryExecutionFactory.create(query,
                model2)) {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(rs) ;
        }*/

        /**
         PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
         PREFIX owl: <http://www.w3.org/2002/07/owl#>
         PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
         PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         PREFIX uri: <http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#>

         SELECT ?a ?b
         WHERE { ?a uri:stop_name  ?b

         }
       --------------------------------------------

         ----------------------------------
         PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
         PREFIX owl: <http://www.w3.org/2002/07/owl#>
         PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
         PREFIX lib:<lib/sparql/algebra/expression.rb#>


         PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         PREFIX uri: <http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#>

         SELECT ?city ?stop
         {?cityClass uri:lat ?cityLat  ; uri:long ?cityLong . ?stopClass uri:stop_lat ?stopLat ; uri:stop_lon ?stopLong.
         FILTER (?cityLat > 0 &&  ?stopLat >0 && ?cityLong >0 && ?stopLong >0  && ?cityLat - ?stopLat  < 5 &&  ?cityLong - ?stopLong < 5 )


         ?cityClass uri:s ?city . ?stopClass uri:stop_name ?stop  }

         -------------------------------
         PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
         PREFIX owl: <http://www.w3.org/2002/07/owl#>
         PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
         PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         PREFIX uri: <http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#>

         SELECT ?city ?stop
         {?cityClass uri:lat ?cityLat  ; uri:long ?cityLong . ?stopClass uri:stop_lat ?stopLat ; uri:stop_lon ?stopLong.
         FILTER(?cityLat < 0 &&  ?stopLat <0 && ?cityLong <0 && ?stopLong <0  && ?cityLat - ?stopLat  < 5 &&  ?cityLong - ?stopLong < 5 )

         ?cityClass uri:s ?city . ?stopClass uri:stop_name ?stop  }
         -------------------------------------------
         PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
         PREFIX owl: <http://www.w3.org/2002/07/owl#>
         PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
         PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         PREFIX uri: <http://www.semanticweb.org/bisho/ontologies/2021/12/untitled-ontology-14#>

         SELECT ?city ?stop
         {?cityClass uri:lat ?cityLat  ; uri:long ?cityLong . ?stopClass uri:stop_lat ?stopLat ; uri:stop_lon ?stopLong.
         FILTER ((?cityLat > 0 &&  ?stopLat >0 && ?cityLong >0 && ?stopLong >0  && ?cityLat - ?stopLat  < 5 &&  ?cityLong - ?stopLong < 5 )||(?cityLat < 0 &&  ?stopLat <0 && ?cityLong <0 && ?stopLong <0  && ?cityLat - ?stopLat  < 5 &&  ?cityLong - ?stopLong < 5 ))


         ?cityClass uri:s ?city . ?stopClass uri:stop_name ?stop  }

         **/
    }
}

