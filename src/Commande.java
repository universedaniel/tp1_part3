
public class Commande {

	private Plat plats;
	private int nbPlats;
	private String nomClient;

	public Commande(Plat plat, int nbPlats, String nomClient) {
		this.plats = plat;
		this.nbPlats = nbPlats;
		this.nomClient = nomClient;
	}

	// Section pour avoir et changer le Plats plats - Début
	public void setPlats(Plat plats) {
		this.plats = plats;
	}

	public Plat getPlats() {
		return this.plats;
	}
	// Section pour avoir et changer le Plats plats- Fin

	// Section pour avoir et changer le int nbPlats - Début
	public void setNbPlats(int nbPlats) {
		this.nbPlats = nbPlats;
	}

	public int getNbPlats() {
		return this.nbPlats;
	}
	// Section pour avoir et changer le int nbPlats - Fin

	// Section pour avoir et changer le nomClient - Début
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getNomClient() {
		return this.nomClient;
	}
	// Section pour avoir et changer le nomClient - Fin
}
