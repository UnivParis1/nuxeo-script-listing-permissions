package nuxeo.document.display.permission;

import java.util.List;
import java.util.ArrayList;


import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Documents;
import org.nuxeo.client.objects.Repository;
import org.nuxeo.client.objects.acl.ACE;
import org.nuxeo.client.objects.acl.ACL;
import org.nuxeo.client.objects.acl.ACP;

import org.nuxeo.client.objects.Document;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class DocumentSpecifiqueNuxeo {

	static Repository repository;	
   
	public static void main(String[] args) {
		 // Vérifiez si les arguments requis sont présents
	    if (args.length < 4) {
	        System.out.println("Usage: java -jar MonFichier.jar url_nuxeo password path_doc");
	        System.exit(1);
	    }
		
		System.out.println( "Début!" );
		NuxeoClient nuxeoClient = new NuxeoClient.Builder()
				.url(args[0])
				.authentication("Administrator",  args[1])
				.schemas("*")
				.connect();

		repository = nuxeoClient.repository();

		Document document = repository.fetchDocumentByPath(args[2]);

		List<String> titres = new ArrayList<>();
		List<String> titresParents = new ArrayList<>();
		List<String> proprietaires = new ArrayList<>();
		List<String> membres = new ArrayList<>();
		List<String> visiteurs = new ArrayList<>();

		collectPermissions(document, titres, proprietaires, membres, visiteurs, titresParents);

		//writeCSV(titres, proprietaires, membres, visiteurs, titresParents);
		String[] pathSegments = args[2].split("/");
		String lastWord = pathSegments[pathSegments.length - 1];
		try (PrintWriter writer = new PrintWriter(new File("permissions_" + lastWord + ".csv"))) {
			writer.println("Document; Document Parent ;Proprietaires ;Membres ;Visiteurs ");

			int maxListSize = Math.max(titres.size(), Math.max(titresParents.size(),
	                Math.max(proprietaires.size(), Math.max(membres.size(), visiteurs.size()))));

			for (int i = 0; i < maxListSize; i++) {
				StringBuilder line = new StringBuilder();

				line.append(i < titres.size() ? titres.get(i) : "").append(";");
	            line.append(i < titresParents.size() ? titresParents.get(i) : "").append(";");
	            line.append(i < proprietaires.size() ? proprietaires.get(i) : "").append(";");
	            line.append(i < membres.size() ? membres.get(i) : "").append(";");
	            line.append(i < visiteurs.size() ? visiteurs.get(i) : "");

				writer.println(line.toString());
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Le fichier permissions.csv a été généré avec succès !");
	}

	public static void collectPermissions(Document doc, List<String> titres, List<String> proprietaires,
			List<String> membres, List<String> visiteurs, List<String> titresParents) {
		Documents children = repository.fetchChildrenById(doc.getId());
	    List<Document> childrenList = children.getEntries();

	    Document docParent = repository.fetchDocumentById(doc.getParentRef());
	    ACP acpParent = docParent.fetchPermissions();
	    ACP acpDoc = doc.fetchPermissions();

	    List<ACL> aclParentList = acpParent.getAcls();
	    List<ACL> aclDocList = acpDoc.getAcls();

	    for (ACL aclParent : aclParentList) {

	        for (ACL aclDoc : aclDocList) {

	            if (!aclDoc.getName().equals(ACL.INHERITED_ACL)) {

	                StringBuilder proprietairesStr = new StringBuilder();
	                StringBuilder membresStr = new StringBuilder();
	                StringBuilder visiteursStr = new StringBuilder();

	                for (ACE ace : aclDoc.getAces()) {
	                    String permission = ace.getPermission();
	                    String username = ace.getUsername();

	                    if (permission.equals("Everything")) {
	                        if (proprietairesStr.length() > 0) {
	                            proprietairesStr.append(", ");
	                        }
	                        proprietairesStr.append(username);
	                    } else if (permission.equals("ReadWrite")) {
	                        if (membresStr.length() > 0) {
	                            membresStr.append(", ");
	                        }
	                        membresStr.append(username);
	                    } else if (permission.equals("Read")) {
	                        if (visiteursStr.length() > 0) {
	                            visiteursStr.append(", ");
	                        }
	                        visiteursStr.append(username);
	                    }
	                }

	                titres.add(doc.getTitle());
	                proprietaires.add(proprietairesStr.toString());
	                membres.add(membresStr.toString());
	                visiteurs.add(visiteursStr.toString());
	                
	                Document parent = repository.fetchDocumentById(doc.getParentRef());
	        	    if (parent != null) {
	        	        titresParents.add(parent.getTitle());
	        	    } else {
	        	        titresParents.add("");
	        	    }
	            }
	        }
	    }

	    for (Document d : childrenList) {
	    	collectPermissions(d, titres, proprietaires, membres, visiteurs,titresParents);
	    }
	}


//	public static void writeCSV(List<String> titres, List<String> proprietaires, List<String> membres,
//			List<String> visiteurs, List<String> titresParents) {
//
//		String[] pathSegments = docPath.split("/");
//		String lastWord = pathSegments[pathSegments.length - 1];
//		try (PrintWriter writer = new PrintWriter(new File("permissions_" + lastWord + ".csv"))) {
//			writer.println("Document; Document Parent ;Proprietaires ;Membres ;Visiteurs ");
//
//			int maxListSize = Math.max(titres.size(), Math.max(titresParents.size(),
//	                Math.max(proprietaires.size(), Math.max(membres.size(), visiteurs.size()))));
//
//			for (int i = 0; i < maxListSize; i++) {
//				StringBuilder line = new StringBuilder();
//
//				line.append(i < titres.size() ? titres.get(i) : "").append(";");
//	            line.append(i < titresParents.size() ? titresParents.get(i) : "").append(";");
//	            line.append(i < proprietaires.size() ? proprietaires.get(i) : "").append(";");
//	            line.append(i < membres.size() ? membres.get(i) : "").append(";");
//	            line.append(i < visiteurs.size() ? visiteurs.get(i) : "");
//
//				writer.println(line.toString());
//			}
//
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


}










