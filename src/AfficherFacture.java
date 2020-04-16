
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AfficherFacture {

	/**
	 * affiche la facture de chacune des clients avec les erreurs
	 * 
	 * @param fileNameAndExtension
	 * 
	 */

	public AfficherFacture(String fileNameAndExtension) {

		try {

			// Ceci crée un nouveau fichier avec le nom complet du fichier
			File file = new File(fileNameAndExtension);
			/*
			 * Ces lignes lisent chaque ligne et les mettent dans un arrClients, arrPlats et
			 * arrCommandes - Fin
			 */

			Scanner inputStream = new Scanner(file);

			int typeData = 0;
			String listeClients = "";
			String listePlats = "";
			String listeCommandes = "";

			int nbLigneCourante = 0;
			while (inputStream.hasNext()) {
				String lineRead = inputStream.nextLine();
				lineRead = lineRead.trim().replaceAll("\\s+", " "); // enleve les spaces de trop dans la ligne
				nbLigneCourante += 1;

				if (lineRead.contains("Clients :")) {
					typeData += 1;
				} else if (lineRead.contains("Plats :")) {
					typeData += 1;
				} else if (lineRead.contains("Commandes :")) {
					typeData += 1;
				} else if (typeData == 1 && !(lineRead.contains("Clients :"))) {
					if (verifierNomClient(lineRead)) {
						listeClients += lineRead + ";";
					} else {
						System.out.println("**********************************************************");
						System.out.println("Le nom du client est incorrect dans la ligne " + nbLigneCourante
								+ " pour le client : " + lineRead + ", car le nom contient des chiffres");
						System.out.println("**********************************************************");
					}
				} else if (typeData == 2 && !(lineRead.contains("Plats :"))) {

					if (verifierLignePlatEtComm(lineRead, 2)) {
						listePlats += lineRead + ";";

					} else {
						System.out.println("**********************************************************");
						System.out.println("Il y a plus ou moins que deux elements dans la ligne " + nbLigneCourante
								+ " pour le plat : " + lineRead);
						System.out.println("**********************************************************");
					}
				} else if (typeData == 3 && !(lineRead.contains("Commandes :")) && !(lineRead.contains("Fin"))) {
					if (verifierLignePlatEtComm(lineRead, 3)) {
						listeCommandes += lineRead + ";";

					} else {
						System.out.println("**********************************************************");
						System.out.println("Il y a plus ou moins que trois elements dans la ligne " + nbLigneCourante
								+ " pour la commande : " + lineRead);
						System.out.println("**********************************************************");
					}

				}

			}

			String[] arrClients = listeClients.split(";");
			String[] arrPlats = listePlats.split(";");
			String[] arrCommandes = listeCommandes.split(";");
			int nbClients = arrClients.length;

			Client[] listeClient = creerChqClient(arrClients, arrPlats, arrCommandes, nbClients);

			for (int i = 0; i < listeClient.length; i++) {
				Commande[] commandeArray = listeClient[i].getCommande();
				if (commandeArray.length != 0) {
					System.out.println(afficherUnClient(listeClient[i]));
					// create a new txt file for every client that has bought something 
					try {
						FileWriter myWriter = new FileWriter( listeClient[i].getNomClient()  + ".txt" );
						myWriter.write(afficherUnClient(listeClient[i]) );
						myWriter.close();
						System.out.println("Successfully wrote to the file.");
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}

				}

			}

		} catch (FileNotFoundException e) {

			System.out.println("Le Fichier " + fileNameAndExtension + " n'existe pas ");

		}
	}

	/**
	 * Vérifie si le client continent des chiffres
	 * 
	 * @param lineRead
	 * @return vrai ou faux
	 */
	private boolean verifierNomClient(String lineRead) {
		boolean verifierNomClient = true;
		if (lineRead.matches(".*\\d.*")) {
			verifierNomClient = false;
		}
		return verifierNomClient;
	}

	/**
	 * Vérifie si la commande et les plats ont des données illégaux
	 * 
	 * @param lineRead
	 * @return vrai ou faux
	 */
	private boolean verifierLignePlatEtComm(String lineRead, int nbDonne) {
		boolean verifierLigne = true;
		String[] arrLigneSplited = lineRead.split(" ");
		if (arrLigneSplited.length != nbDonne) {
			verifierLigne = false;
		}
		return verifierLigne;
	}

	/**
	 * Crée la facture pour chaque client
	 * 
	 * @param arrClients
	 * @param arrPlats
	 * @param arrCommandes
	 * @param nbClients
	 * @return Facture[] arrayFacture
	 * 
	 * 
	 */

	private Client[] creerChqClient(String[] arrClients, String[] arrPlats, String[] arrCommandes, int nbClients) {

		Client[] arrayClient = null;
		ArrayList<Client> clientArrayList = new ArrayList<Client>();

		for (int i = 0; i < arrClients.length; i++) {
			String nomClient = arrClients[i];

			Plat[] tousLesPlatsMenu = tousPlats(arrPlats);

			// Ce bloque crée tous les nomPlat , nbPlat et nomClient
			ArrayList<String> nomPlatArryList = new ArrayList<String>();
			ArrayList<Integer> nbPlatArryList = new ArrayList<Integer>();
			ArrayList<String> nomClientArryList = new ArrayList<String>();
			for (int j = 0; j < arrCommandes.length; j++) {
				String arrCommandeCurrent = arrCommandes[j];
				// System.out.println("arrCommandes[j] : " + arrCommandes[j] );
				String[] arrCommandeSplited = arrCommandeCurrent.split(" ");
				// System.out.println("arrCommandeSplited[0] :" + arrCommandeSplited[0]);
				if (nomClient.contains(arrCommandeSplited[0])) {
					// System.out.println("" + arrCommandeSplited[1]);
					nomPlatArryList.add(arrCommandeSplited[1]);
					nbPlatArryList.add(Integer.parseInt(arrCommandeSplited[2]));
					nomClientArryList.add(arrCommandeSplited[0]);
				}
			}
			String[] nomPlatCommandeClient = nomPlatArryList.toArray(new String[0]);
			int[] quantiteCommandeClient = nbPlatArryList.stream().mapToInt(Integer::intValue).toArray();
			String[] nomClientCommandeClient = nomClientArryList.toArray(new String[0]);

			// Ce bloque crée toutes les commandes avec
			ArrayList<Commande> commandeArrayList = new ArrayList<Commande>();
			for (int j = 0; j < nomPlatCommandeClient.length; j++) {
				// System.out.println( nomPlatCommandeClient[j] + " X" +
				// quantiteCommandeClient[j] );

				for (int x = 0; x < tousLesPlatsMenu.length; x++) {
					// System.out.println("platchqCommandeDuClient[x].getNomPlats() : " +
					// platchqCommandeDuClient[x].getNomPlats() );
					/*
					 * System.out.println(
					 * "**********************************************************************");
					 * System.out.println( platchqCommandeDuClient[x].getNomPlats() + " " +
					 * platchqCommandeDuClient[x].getPrixPlats() );
					 * System.out.println("nomPlatCommandeClient[j] : " + nomPlatCommandeClient[j]);
					 */
					if (tousLesPlatsMenu[x].getNomPlats().contains(nomPlatCommandeClient[j])) {
						/*
						 * System.out.
						 * println("platchqCommandeDuClient[x].getNomPlats() == nomPlatCommandeClient[j] "
						 * ); System.out.println(platchqCommandeDuClient[x].getNomPlats() + " == " +
						 * nomPlatCommandeClient[j]);
						 */
						commandeArrayList.add(new Commande(tousLesPlatsMenu[x], quantiteCommandeClient[j],
								nomClientCommandeClient[j]));
					}
				}
			}
			Commande[] CommandeDeChqClient = commandeArrayList.toArray(new Commande[0]);

			// Cette ligne crée le Client current
			clientArrayList.add(new Client(nomClient, CommandeDeChqClient));
		}

		arrayClient = clientArrayList.toArray(new Client[0]);
		return arrayClient;
	}

	public Plat[] tousPlats(String[] arrPlats) {
		// Ce bloque crée chaque plat existant
		ArrayList<Plat> platsArrayList = new ArrayList<Plat>();
		// int nbPlats = arrPlats.length ;
		for (int j = 0; j < arrPlats.length; j++) {
			String arrPlatCurrent = arrPlats[j];
			String[] arrPlatSplited = arrPlatCurrent.split(" ");
			// System.out.println("arrPlatSplited[0] : " + arrPlatSplited[0] );
			String nomPlat = arrPlatSplited[0];
			// System.out.println("arrPlatSplited[1] : " + arrPlatSplited[1] );
			double prixPlat = Double.parseDouble(arrPlatSplited[1]);
			platsArrayList.add(new Plat(nomPlat, prixPlat));
			// Plat plat = new Plat("poutine", 23.0 ) ;
		}
		// the next tranforms array list Plat into an array of Plat
		Plat[] platchqCommandeDuClient = platsArrayList.toArray(new Plat[0]);

		return platchqCommandeDuClient;
	}

	public String afficherUnClient(Client client) {
		String commandePourUnClient = "";
		Commande[] commandeArray = client.getCommande();
		double prixTotalPartiel = 0;

		commandePourUnClient = "____________________________________\n";
		commandePourUnClient += "****************\n";
		commandePourUnClient += "Nom du Client :  " + client.getNomClient() + "\n";

		for (int i = 0; i < commandeArray.length; i++) {

			commandePourUnClient += commandeArray[i].getPlats().getNomPlats() + " X" + commandeArray[i].getNbPlats()
					+ "\n";
			commandePourUnClient += "Prix par plat : " + commandeArray[i].getPlats().getPrixPlats() + "\n";
			prixTotalPartiel += (commandeArray[i].getPlats().getPrixPlats() * commandeArray[i].getNbPlats());

			commandePourUnClient += "****************" + "\n";

		}
		commandePourUnClient += "Total Partiel : " + prixTotalPartiel + "\n";
		commandePourUnClient += "Total " + arrondir(
				caluclerTotal(prixTotalPartiel, caluclerTPS(prixTotalPartiel), caluclerTVQ(prixTotalPartiel)), 2)
				+ "\n";

		return commandePourUnClient;
	}

	/*
	 * Cette section est pour calculer les taxes et bieb plus
	 */

	private double caluclerTPS(double totalPartiel) {
		double pourcentageTPS = 0.05;
		return totalPartiel * pourcentageTPS;
	}

	private double caluclerTVQ(double totalPartiel) {
		double pourcentageTVQ = 0.09975;
		return totalPartiel * pourcentageTVQ;
	}

	private double caluclerTotal(double totalPartiel, double tps, double tvq) {
		return totalPartiel + tps + tvq;
	}

	public static double arrondir(double valeurPorArrondir, int chiffresApresVirgule) {
		if (chiffresApresVirgule < 0) {
			throw new IllegalArgumentException();
		}
		long factor = (long) Math.pow(10, chiffresApresVirgule);
		valeurPorArrondir = valeurPorArrondir * factor;
		long tmp = Math.round(valeurPorArrondir);
		return (double) tmp / factor;
	}

}
