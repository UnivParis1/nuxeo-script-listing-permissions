package proectNuxeoDocument;

import java.util.Collections;
import java.util.List;


import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.Repository;
import org.nuxeo.client.objects.acl.ACP;

import com.sun.mail.imap.ACL;

import org.nuxeo.client.objects.Document;

public class MyApp {


	static String url = "http://nuxeo-test-as:8080/nuxeo";
	static String Path = "/default-domain/";


	static NuxeoClient nuxeoClient = new NuxeoClient.Builder()
			.url(url)
			.authentication("Administrator", "Nux30@dm1n")
			.schemas("*")
			.connect();

	static Repository repository = nuxeoClient.repository();

	public static void main(String[] args) {

		System.out.println("Début traitement");

		//		Documents documents = nuxeoClient.repository().query("SELECT * FROM Document WHERE dc:created BETWEEN DATE '2022-01-21' AND DATE '2022-02-23'");
		//
		//		System.out.println("Documents : "+documents.getCurrentPageSize());


		Document document = repository.fetchDocumentByPath("/default-domain/workspaces/Espaces%20collaboratifs/Espace%20de%20test");
		String title = document.getPropertyValue("dc:title"); // equals to folder
		System.out.println("Documents : "+title);
		
		displayPermission(document);
		

	}




	public static void displayPermission(Document doc) {



		Documents children = repository.fetchChildrenById(doc.getId());
		List<Document> childrenList = children.getEntries();

		for(Document d : childrenList )  displayPermission(d);

       
		Document docParent = repository.fetchDocumentById(doc.getParentRef());
		  ACP acpParent = docParent.fetchPermissions();
		  ACP acpDoc = doc.fetchPermissions();
		  
		  List<org.nuxeo.client.objects.acl.ACL> aclParentList=acpParent.getAcls();
		  List<org.nuxeo.client.objects.acl.ACL> aclDocList=acpDoc.getAcls();
		  //Collections.sort(aclParentList);
		  //Collections.sort(aclDocList);
		  
			if(!acpParent.getAcls().equals(acpDoc.getAcls())) {
				
				//for(){
        
				System.out.println( doc.getTitle()+"," +doc.getId()+","+acpDoc.getAcls()+acpParent.getAcls());
             

			}

			

		}




	//	public static List<Document> fetchChildren(Document folder) {
	//		
	//		Documents children = repository.fetchChildrenById(folder.getId());
	//		List<Document> childrenList = children.getEntries();
	//        
	//        return childrenList;
	//    }

	protected boolean PermissionInheritanceblocked(Document doc) {


		ACP acp = doc.fetchPermissions();


		//		if(acp.getAcls().get.equals(acp.getAcls())) {
		//			
		//			System.out.println("permission : "+acpChild.getAcls().get(0).getName());
		//			
		//			
		//		}

		return false;
	}



}
