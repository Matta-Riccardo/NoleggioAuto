package it.polito.tdp.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.PriorityQueue;

import it.polito.tdp.noleggio.model.Event.EventType;

public class Simulatore {
	
	// Parametri di ingresso
	private int NC ;   //number of cars
	private Duration T_IN = Duration.ofMinutes(10) ;  // minuti che trascorrono tra l'arrivo di un potenziale cliente e il successivo
	private Duration T_TRAVEL = Duration.ofHours(1) ; // rappresenta la durata di un noleggio che però può essere un valore anche 1, 2, o 3 volte tanto
	
	// Valori calcolati in uscita: io come risultato voglio andare a vedere il numero di clienti che sono rimasti insoddisfatti
	private int nClientiTot ;
	private int nClientiInsoddisfatti ;
	
	// Variabili che rappresentano lo Stato del mondo
	private int autoDisponibili ;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue ;
	
	// Costruttore: in modo da specificare NC nel momento in cui vado a costruire la classe
	public Simulatore(int NC) {
		this.NC = NC ;
		this.queue = new PriorityQueue<>();
		this.autoDisponibili = NC ; // al tempo 0 tutte le macchine sono disponibili
	}
	
	
//	METODO CHE ESEGUE LA SIMULAZIONE
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll() ; 	// poll() ottiene e rimuove la testa della coda o ritorna null se la coda è vuota, in questo caso questo controllo sulla coda io lo sto facendo nella clause del while
			processEvent(e); 	// Metodo a cui run() delega il processamento del evento
		}
		
	}
	
// QUI POPOLO LA CODA DEGLI EVENTI
	public void caricaEventi() {
		LocalTime ora = LocalTime.of(8, 0) ; // Parto dalle 8:00 del mattino fino alle 16:00 di pomeriggio
		while(ora.isBefore(LocalTime.of(16,0))) {
			this.queue.add(new Event(ora, EventType.NUOVO_CLIENTE)) ;
			ora = ora.plus(T_IN) ; // Ogni dieci minuti decido di far arrivare un nuovo cliente
		}
	}

	private void processEvent(Event e) {

		// 		Ogni volta estraggo un evento quello è ciò che mi dice al tempo attuale
		switch(e.getType()) {
			case NUOVO_CLIENTE:
				this.nClientiTot++ ;
				if(this.autoDisponibili>0) {
					this.autoDisponibili-- ;
					// Qua faccio una la previsione su quando penso rientrerà l'auto: imposto quindi l'evento di rientro
					// calcolando un numero di ore casuale.					
					int ore = (int)(Math.random()*3)+1;
					LocalTime oraRientro = e.getTime().plus(Duration.ofHours(ore * T_TRAVEL.toHours())) ; // L'ora di rientro dell'auto sarà pari all'ora attuale, plus la durata appena calcolata del noleggio
					this.queue.add(new Event(oraRientro, EventType.AUTO_RESTITUITA));
				} else {
					this.nClientiInsoddisfatti++ ;
				}
				break;
				
			case AUTO_RESTITUITA:
				this.autoDisponibili++ ;
				break;
		}
	}

	public int getnClientiTot() {
		return nClientiTot;
	}

	public int getnClientiInsoddisfatti() {
		return nClientiInsoddisfatti;
	}

	public void setNC(int nC) {
		NC = nC;
	}

	public void setT_IN(Duration t_IN) {
		T_IN = t_IN;
	}

	public void setT_TRAVEL(Duration t_TRAVEL) {
		T_TRAVEL = t_TRAVEL;
	}
	
	
	

}
