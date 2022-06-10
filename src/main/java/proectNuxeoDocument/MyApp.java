package proectNuxeoDocument;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
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


	static String url = "http://nuxeo-as:8080/nuxeo";
	static String Path = "/default-domain/";


	static NuxeoClient nuxeoClient = new NuxeoClient.Builder()
			.url(url)
			.authentication("Administrator", "Nux30@dm1n")
			.schemas("*")
			.connect();

	static Repository repository = nuxeoClient.repository();

	public static void main(String[] args) {

		//System.out.println("Début traitement");

		//		Documents documents = nuxeoClient.repository().query("SELECT * FROM Document WHERE dc:created BETWEEN DATE '2022-01-21' AND DATE '2022-02-23'");
		//
		//		System.out.println("Documents : "+documents.getCurrentPageSize());


		Document document = repository.fetchDocumentByPath("/default-domain/workspaces/Espaces%20administratifs/DSIUN/SIS");
		String title = document.getPropertyValue("dc:title"); // equals to folder
		System.out.println("Documents : "+title);

		displayPermission(document);


	}

	public static void displayPermission(Document doc) {



		Documents children = repository.fetchChildrenById(doc.getId());
		List<Document> childrenList = children.getEntries();

		


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

			}

		}
		for(Document d : childrenList )  displayPermission(d);
	}
	
	
	
	
	



}
