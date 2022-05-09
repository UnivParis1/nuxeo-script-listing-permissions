package proectNuxeoDocument;

import java.util.Collections;
import java.util.List;


import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.Repository;
import org.nuxeo.client.objects.acl.ACE;
import org.nuxeo.client.objects.acl.ACL;
import org.nuxeo.client.objects.acl.ACP;

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

		//ACP acpParent = repository.getgetACP(doc.getParentRef());


		List<org.nuxeo.client.objects.acl.ACL> aclParentList=acpParent.getAcls();
		List<org.nuxeo.client.objects.acl.ACL> aclDocList=acpDoc.getAcls();

		for(ACL aclParent : aclParentList) {

			for(ACL aclDoc : aclDocList) {

				//System.out.println( "Document Parent : "+aclParent.getAces().get(0).getPermission()+","+aclParent.getAces().get(0).getUsername()+","+aclParent.getAces().get(0).getStatus());
				//System.out.println( "Document child : "+doc.getTitle()+" : "+aclDoc.getAces().get(0).getPermission()+","+aclDoc.getAces().get(0).getUsername()+","+aclDoc.getAces().get(0).getStatus());
				//if(!acpParent.getAcls().equals(acpDoc.getAcls())) {
				if(!aclParent.getName().equals(aclDoc.getName())) {
					//System.out.println( aclDoc.getName()+","+aclParent.getName());
					
					System.out.println( doc.getTitle()+"," +doc.getId()+","+aclDoc.getAces().get(0).getPermission()+","+aclDoc.getAces().get(0).getUsername()+","+aclParent.getAces().get(0).getPermission()+","+aclParent.getAces().get(0).getUsername());

				}

			}

		}
	}



}
