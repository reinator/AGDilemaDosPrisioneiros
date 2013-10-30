import java.util.ArrayList;

public class Cromossomo {

	private Object valor;
	private double fitness;
	private ArrayList<Object> genes;
	
	MersenneTwister random = new MersenneTwister(System.nanoTime());
	public Cromossomo(ArrayList<Object> genes){
		this.genes=genes;
	}
	public Cromossomo(int num_genes, int tipo_representacao){
		
		genes = new ArrayList<Object>();
		switch(tipo_representacao){
		case 0:{
			for(int i=0; i<num_genes; i++)
				genes.add(random.nextBoolean());
	
		}
		case 1:{
			for(int i=0; i<num_genes; i++)
				genes.add(random.nextInt(1000));
		}
		case 2:{
			for(int i=0; i<num_genes; i++)
				genes.add(random.nextDouble());								
		}
		}		
		
	}
	public ArrayList<Object> retornaGenes(){
		
		return genes;
	}
	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
}
