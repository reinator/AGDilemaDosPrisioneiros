import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Populacao {
	
	private int tamanho_populacao;
	private ArrayList<Cromossomo> CromossomoList = new ArrayList<Cromossomo>();
	private double taxa_mutacao;
	private int num_genes;
	private double taxa_cruzamento;
	private int representacao; //0 - binário, 1 - inteiro, 2 - real
	private Properties config = new Properties();
	private String arquivo = "config.ini";
	
	public Populacao(){
		try {
			config.load(new FileInputStream(arquivo));
			tamanho_populacao = Integer.parseInt(config.getProperty("tam_populacao"));
			num_genes = Integer.parseInt(config.getProperty("num_genes"));
			representacao = Integer.parseInt(config.getProperty("representacao"));
			taxa_mutacao = Double.parseDouble(config.getProperty("taxa_mutacao"));
			taxa_cruzamento = Double.parseDouble(config.getProperty("taxa_cruzamento"));

			} catch (IOException ex) {
			Logger.getLogger(Populacao.class.getName()).log(Level.SEVERE, null, ex);
			}
		
	}
	public Populacao(ArrayList<Cromossomo> ListaCromossomo){
		
		try {
			this.CromossomoList = ListaCromossomo;
			config.load(new FileInputStream(arquivo));
			tamanho_populacao = Integer.parseInt(config.getProperty("tam_populacao"));
			num_genes = Integer.parseInt(config.getProperty("num_genes"));
			representacao = Integer.parseInt(config.getProperty("representacao"));
			taxa_mutacao = Double.parseDouble(config.getProperty("taxa_mutacao"));
			taxa_cruzamento = Double.parseDouble(config.getProperty("taxa_cruzamento"));

			} catch (IOException ex) {
			Logger.getLogger(Populacao.class.getName()).log(Level.SEVERE, null, ex);
			}
		
	}
	
	public int getTamanho_populacao() {
		return tamanho_populacao;
	}
	public void setTamanho_populacao(int tamanho_populacao) {
		this.tamanho_populacao = tamanho_populacao;
	}
	public double getTaxa_mutacao() {
		return taxa_mutacao;
	}
	public void setTaxa_mutacao(double taxa_mutacao) {
		this.taxa_mutacao = taxa_mutacao;
	}
	public double getTaxa_cruzamento() {
		return taxa_cruzamento;
	}
	public void setTaxa_cruzamento(double taxa_cruzamento) {
		this.taxa_cruzamento = taxa_cruzamento;
	}
	public int getRepresentacao() {
		return representacao;
	}
	public void setRepresentacao(int representacao) {
		this.representacao = representacao;
	}
	
	public void cria_individuos(){
		/*TODO cria-se os indivíduos (cromossomos) de acordo com a representação e
		limitados pelo tamanho*/
		for(int i=0;i<tamanho_populacao;i++){
			Cromossomo individuo = new Cromossomo(num_genes, representacao);
			CromossomoList.add(individuo);
			
		}
	}
	public ArrayList<Cromossomo> retornaCromossomoList(){
		return CromossomoList;
	}
	public int getNum_genes() {
		return num_genes;
	}
	public void setNum_genes(int num_genes) {
		this.num_genes = num_genes;
	}
	public ArrayList<Cromossomo> getCromossomoList() {
		return CromossomoList;
	}
	public void setCromossomoList(ArrayList<Cromossomo> cromossomoList) {
		CromossomoList = cromossomoList;
	}

}
