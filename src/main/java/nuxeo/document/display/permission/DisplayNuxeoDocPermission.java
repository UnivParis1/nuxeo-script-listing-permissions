package nuxeo.document.display.permission;

import java.util.List;


import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.Repository;
import org.nuxeo.client.objects.acl.ACE;
//import org.nuxeo.client.objects.acl.ACE;
import org.nuxeo.client.objects.acl.ACL;
import org.nuxeo.client.objects.acl.ACP;

import org.nuxeo.client.objects.Document;

public class DisplayNuxeoDocPermission {
  
	static Repository repository;

	public static void main(String[] args) {

		NuxeoClient nuxeoClient = new NuxeoClient.Builder()
				.url(args[0])
				.authentication("Administrator", args[1])
				.schemas("*")
				.connect();

		repository = nuxeoClient.repository();

		Document document = repository.fetchDocumentByPath(args[2]);
		
		
		displayPermission(document);


	}

	public static void displayPermission(Document doc) {



		Documents children = repository.fetchChildrenById(doc.getId());
		List<Document> childrenList = children.getEntries();
				

		Document docParent = repository.fetchDocumentById(doc.getParentRef());
		ACP acpParent = docParent.fetchPermissions();
		ACP acpDoc = doc.fetchPermissions();

    
		List<org.nuxeo.client.objects.acl.ACL> aclParentList=acpParent.getAcls();
		List<org.nuxeo.client.objects.acl.ACL> aclDocList=acpDoc.getAcls();
		
		

		for(ACL aclParent : aclParentList) {

			for(ACL aclDoc : aclDocList) {

				if(!aclDoc.getName().equals(ACL.INHERITED_ACL)) {
					
					
					//System.out.println( doc.getTitle()+"," +doc.getId()+","+aclDoc.getAces().get(0).getPermission()+","+aclDoc.getAces().get(0).getUsername()+","+aaaclParent.getAces().get(0).getPermission()+","+aclParent.getAces().get(0).getUsername());
				     
				System.out.println( doc.getTitle()+"," +doc.getId()+",");
				
				for (ACE ace : aclDoc.getAces() )  System.out.println("Les permissons du document  : "+ace.getPermission()+"," +ace.getUsername()+",");
				for (ACE ace : aclParent.getAces() )  System.out.println("Les permissons du doc parent  : "+ ace.getPermission()+"," +ace.getUsername()+",");
				
				
			}
		}
		for(Document d : childrenList )  displayPermission(d);
	}
	
	}
	
	
	



}
