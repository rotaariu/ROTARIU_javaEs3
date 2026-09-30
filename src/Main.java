import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

enum Livello {
    CRITICO(1), ALTO(2), MEDIO(3), BASSO(4);

    private final int peso;
    Livello(int peso) { this.peso = peso; }
    public int getPeso() { return peso; }
}

class Ticket implements Comparable<Ticket> {
    String id;
    String descrizione;
    Livello livello;
    long timestampArrivo;

    public Ticket(String id, String descrizione, Livello livello, long timestampArrivo) {
        this.id = id;
        this.descrizione = descrizione;
        this.livello = livello;
        this.timestampArrivo = timestampArrivo;
    }

    @Override
    public int compareTo(Ticket altro) {
        int compLivello = Integer.compare(this.livello.getPeso(), altro.livello.getPeso());
        if (compLivello != 0) {
            return compLivello;
        }
        return Long.compare(this.timestampArrivo, altro.timestampArrivo);
    }

    @Override
    public String toString() {
        return id + " [" + livello + "] - " + descrizione;
    }
}

public class GestoreTicket {

    public static void main(String[] args) {
        PriorityQueue<Ticket> codaTicket = new PriorityQueue<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("ticket.csv"))) {
            String riga;
            reader.readLine();

            while ((riga = reader.readLine()) != null) {
                String[] campi = riga.split(",");
                if (campi.length < 4) continue;

                try {
                    String id = campi[0].trim();
                    String desc = campi[1].trim();
                    Livello liv = Livello.valueOf(campi[2].trim().toUpperCase());
                    long ts = Long.parseLong(campi[3].trim());

                    codaTicket.add(new Ticket(id, desc, liv, ts));
                } catch (Exception e) {
                    System.out.println("[LOG ERRORE] Riga non valida nel CSV: " + riga);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file: " + e.getMessage());
        }

        System.out.println("--- PRIMI TICKET ESTRATTI DALLA CODA ---");
        while (!codaTicket.isEmpty()) {
            Ticket t = codaTicket.poll();
            System.out.println("In lavorazione: " + t);
        }
    }
}