package it.polito.tdp.noleggio.model;

public class TestSimulatore {

	public static void main(String[] args) {
		Simulatore sim = new Simulatore(12); // Faccio la mia simulazione pensando di avere 12 automobili totali, consideranto che arriveranno 6 clienti all'ora che tengono in media l'auto impiegata due ore 

		sim.caricaEventi();
		sim.run();

		System.out.println(
				"Clienti: " + sim.getnClientiTot() + " di cui " + 
				sim.getnClientiInsoddisfatti() + " insoddisfatti\n");

	}

}
