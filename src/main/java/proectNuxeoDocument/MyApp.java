package proectNuxeoDocument;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.Repository;
import org.nuxeo.client.objects.acl.ACE;
import org.nuxeo.client.objects.acl.ACL;
import org.nuxeo.client.objects.acl.ACP;

import com.opencsv.CSVWriter;

import org.nuxeo.client.objects.Document;

public class MyApp {

	//Délimiteurs qui doivent être dans le fichier CSV
	  private static final String DELIMITER = ",";
	    private static final String SEPARATOR = "\n";
	    
	    //En-tête de fichier
	    private static final String HEADER = "Titre,Id,Permissions, Utilisateurs, Permissions du doc parent, Utilisateurs";
	    
	    
	static String url = "http://nuxeo-as:8080/nuxeo";
	static String Path = "/default-domain/";


	static NuxeoClient nuxeoClient = new NuxeoClient.Builder()
			.url(url)
			.authentication("*********", "*********")
			.schemas("*")
			.connect();

	static Repository repository = nuxeoClient.repository();

	public static void main(String[] args) {

		Document document = repository.fetchDocumentByPath("/default-domain/workspaces/Espaces%20administratifs/DSIUN/SIS");
		String title = document.getPropertyValue("dc:title"); // equals to folder
		System.out.println("Documents : "+title);

		displayPermission(document);


	}

	public static void displayPermission(Document doc) {



		Documents children = repository.fetchChildrenById(doc.getId());
		List<Document> childrenList = children.getEntries();
		FileWriter file = null;
		


		Document docParent = repository.fetchDocumentById(doc.getParentRef());
		ACP acpParent = docParent.fetchPermissions();
		ACP acpDoc = doc.fetchPermissions();

		//ACP acpParent = repository.getgetACP(doc.getParentRef());


		List<org.nuxeo.client.objects.acl.ACL> aclParentList=acpParent.getAcls();
		List<org.nuxeo.client.objects.acl.ACL> aclDocList=acpDoc.getAcls();
		
		

		for(ACL aclParent : aclParentList) {

			for(ACL aclDoc : aclDocList) {

				if(!aclDoc.getName().equals(ACL.INHERITED_ACL)) 
					
					System.out.println( doc.getTitle()+"," +doc.getId()+","+aclDoc.getAces().get(0).getPermission()+","+aclDoc.getAces().get(0).getUsername()+","+aclParent.getAces().get(0).getPermission()+","+aclParent.getAces().get(0).getUsername());
                    
//				try
//			      {
//			        file = new FileWriter("/home/amel/Documents/nuxeo-web-services/workspace/permissions1.csv");
//			        //Ajouter l'en-tête
//			        file.append(HEADER);
//			        //Ajouter une nouvelle ligne après l'en-tête
//			        file.append(SEPARATOR);
//			        //Itérer bookList
//			        Iterator it = aclDocList.iterator();
//			        
//			        file.append(doc.getTitle());
//			          file.append(DELIMITER);
//			          file.append(doc.getId());
//			          file.append(DELIMITER);
//			          file.append(aclDoc.getAces().get(0).getPermission());
//			          file.append(DELIMITER);
//			          file.append(aclDoc.getAces().get(0).getUsername());
//			          file.append(DELIMITER);
//			          file.append(aclParent.getAces().get(0).getPermission());
//			          file.append(DELIMITER);
//			          file.append(aclParent.getAces().get(0).getUsername());
//			        
//			        
//			        
////			        while(it.hasNext())
////			        {
////			        	aclDoc =  (ACL) it.next();
////			          file.append(doc.getTitle());
////			          file.append(DELIMITER);
////			          file.append(doc.getId());
////			          file.append(DELIMITER);
////			          file.append(aclDoc.getAces().get(0).getPermission());
////			          file.append(DELIMITER);
////			          file.append(aclDoc.getAces().get(0).getUsername());
////			          file.append(DELIMITER);
////			          file.append(aclParent.getAces().get(0).getPermission());
////			          file.append(DELIMITER);
////			          file.append(aclParent.getAces().get(0).getUsername());
////			        }
////			      
//			        file.close();
//			      }
//			      catch(Exception e)
//			      {
//			        e.printStackTrace();
//			      }
				
				
			
				
			}		

		}
		for(Document d : childrenList )  displayPermission(d);
	}
	
	
	
	
	



}
