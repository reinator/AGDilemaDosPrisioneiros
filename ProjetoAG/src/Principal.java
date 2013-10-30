import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.swing.JOptionPane;

public class Principal 
{
	private static Populacao populacao = new Populacao();
	private static ArrayList<Cromossomo> ListaCromossomo = new ArrayList<Cromossomo>();
	private static int numGeracao=0;
	private static int casoDeTeste=2;//Escolhe-se qual caso de teste deseja-se aplicar:
									//0, 1 ou 2
	//private static String StringCSV = "";
	public static void main(String[] args) throws IOException{		
		FileWriter x = new FileWriter("MediaFitness.csv");   
        PrintWriter gravarX = new PrintWriter(x);
        
        FileWriter y = new FileWriter("MaiorFitness.csv");   
        PrintWriter gravarY = new PrintWriter(y);
        
		geraPopulacao();
		System.out.println("População INICIAL");
		mostraPopulacao(populacao);
		
		int count=0;
		calcularFitness();
		calculaBonificação();
		double mediafitness=calcularMediaFitness();
		double maiorfitness = calculaMaiorFitness();
		String format = ""+mediafitness;
		format = format.replace(".",",");		
		System.out.println("Media Fitness "+numGeracao+": "+mediafitness);
		gravarX.println(format);
		
		format = ""+maiorfitness;
		format = format.replace(".",",");
		gravarY.println(format);
		while(count<5){
		
			aplicaTorneio();
			//calcularFitness();
			aplicaCruzamento();
			//calcularFitness();
			aplicaMutacao();
			
			calcularFitness();
			calculaBonificação();
			double mediafitness2=calcularMediaFitness();
			maiorfitness=calculaMaiorFitness();
			format = ""+mediafitness2;
			format=format.replace(".", ",");
			      
		    
			numGeracao++;
			System.out.println("Media Fitness "+numGeracao+": "+mediafitness2);
			gravarX.println(format);
			
			format = ""+maiorfitness;
			format = format.replace(".", ",");
			gravarY.println(format);
			
				
				while(Math.abs(mediafitness-mediafitness2)>0.1){
					mediafitness=calcularMediaFitness();
					aplicaTorneio();
					//calcularFitness();
					aplicaCruzamento();
					//calcularFitness();
					aplicaMutacao();
					
					calcularFitness();
					calculaBonificação();
					mediafitness2 = calcularMediaFitness();
					numGeracao++;
					System.out.println("Media Fitness "+numGeracao+":"+mediafitness2);
					format = ""+mediafitness2;
					format = format.replace(".",",");
					gravarX.println(format);
					
					maiorfitness=calculaMaiorFitness();
					format = ""+maiorfitness;
					format = format.replace(".", ",");
					gravarY.println(format);
					count=0;
					
				}
				count++;
		}
		x.close();
		y.close();
		mostraPopulacao(populacao);
		
		
	}		
    static void geraPopulacao(){
		populacao.cria_individuos();
	}   
    static void mostraPopulacao(Populacao populacao){
    	String texto = "";
    	ArrayList<Cromossomo> CromoLista = populacao.retornaCromossomoList();
    	ListaCromossomo = CromoLista;
    	texto+="POPULACAÇÂO "+numGeracao+" GERADA\n\n";
    	//umGeracao++;
    	for(int i=0;i<populacao.getTamanho_populacao();i++){
    		texto+="Indivíduo "+i+": [";
    		Cromossomo individuo = CromoLista.get(i);
    		ArrayList<Object> genes = individuo.retornaGenes();
    		for(int j=0;j<populacao.getNum_genes();j++){
    			if(populacao.getRepresentacao()==0){
    				if((boolean) genes.get(j))
    					texto+="D, ";
    				else
    					texto+="C, ";		
    			}
    			else
    				texto+=genes.get(j)+", ";
    		}
    		texto+="]\n\n";
    	}
    	System.out.println(texto);
        //tentando criar arquivo    
    	Gravar();
    	
    }
    static public void Gravar(){  
        try{  
            // o true significa q o arquivo será constante  
            FileWriter x = new FileWriter("Populacao.txt");   
            PrintWriter gravarX = new PrintWriter(x);
            
            ArrayList<Cromossomo> CromoLista = populacao.retornaCromossomoList();
	    	gravarX.println("POPULACAÇÂO GERADA\n");
	    	for(int i=0;i<populacao.getTamanho_populacao();i++){
	    		gravarX.print("Indivíduo "+i+": [");
	    		Cromossomo individuo = CromoLista.get(i);
	    		ArrayList<Object> genes = individuo.retornaGenes();
	    		for(int j=0;j<populacao.getNum_genes();j++){
	    			gravarX.print(genes.get(j)+", ");
	    		}
	    		gravarX.println("]\n");
	    	} 
	    	x.close();
        }  
        catch(Exception e){  
        }  
    } 

	static void aplicaMutacao() {
		// TODO aplica-se a mutacao à cada individuo, se desejada
		double taxa_mutacao = populacao.getTaxa_mutacao();
		ArrayList<Cromossomo> CromoLista = populacao.retornaCromossomoList();
		for (int i = 0; i < populacao.getTamanho_populacao(); i++) {
			Cromossomo individuo = CromoLista.get(i);
			ArrayList<Object> genes = individuo.retornaGenes();
			for (int j = 0; j < populacao.getNum_genes(); j++) {
				MersenneTwister random = new MersenneTwister(System.nanoTime());
				if (random.nextDouble() < taxa_mutacao)
					populacao.getCromossomoList().get(i).retornaGenes()
							.set(j, !(boolean) genes.get(j));
			}

		}
	}
    
	static ArrayList<Cromossomo> aplicaCrossover(Cromossomo pai, Cromossomo mae) {
		// TODO aplica-se o cruzamento entre dois cromossomos
		MersenneTwister random = new MersenneTwister();
		int numGenes = populacao.getNum_genes();
		int pontoCrossover = random.nextInt(numGenes - 1);
		// List geneFilho = new ArrayList();

		ArrayList<Object> genePai = pai.retornaGenes();
		ArrayList<Object> geneMae = mae.retornaGenes();
		ArrayList<Object> geneFilho = new ArrayList<Object>(genePai.subList(0,
				pontoCrossover));
		geneFilho.addAll(geneMae.subList(pontoCrossover, numGenes));
		ArrayList<Object> geneFilha = new ArrayList<Object>(geneMae.subList(0,
				pontoCrossover));
		geneFilha.addAll(genePai.subList(pontoCrossover, numGenes));

		Cromossomo filho = new Cromossomo(geneFilho);
		Cromossomo filha = new Cromossomo(geneFilha);
		ArrayList<Cromossomo> filhos = new ArrayList<Cromossomo>();

		filhos.add(filho);
		filhos.add(filha);
		return filhos;
	}

	static void aplicaCruzamento() {
		// TODO dependendo da forma de seleção, reduzirá-se a população aos
		// indivíduos mais fortes
		ArrayList<Cromossomo> CromoLista = populacao.retornaCromossomoList();
		ListaCromossomo = CromoLista;
		double taxa_cruzamento = populacao.getTaxa_cruzamento();
		int tamPop = populacao.getTamanho_populacao();
		int tamPopAux = tamPop;
		ArrayList<Cromossomo> novaLista = new ArrayList<Cromossomo>();
		for (int i = 0; i < tamPop / 2; i++) {
			MersenneTwister random = new MersenneTwister(System.nanoTime());

			int pai1 = random.nextInt(tamPopAux);
			Cromossomo cromo1 = ListaCromossomo.get(pai1);
			ListaCromossomo.remove(pai1);
			tamPopAux--;

			int pai2 = random.nextInt(tamPopAux);
			Cromossomo cromo2 = ListaCromossomo.get(pai2);
			ListaCromossomo.remove(pai2);
			tamPopAux--;

			if (random.nextDouble() < taxa_cruzamento) {
				novaLista.addAll(aplicaCrossover(cromo1, cromo2));
			} else {
				novaLista.add(cromo1);
				novaLista.add(cromo2);
			}

		}
		Populacao novaPopulacao = new Populacao(novaLista);
		populacao = novaPopulacao;
	}
    
	static void aplicaTorneio() {
		// TODO dependendo da forma de seleção, reduzirá-se a população aos
		// indivíduos mais fortes
		ArrayList<Cromossomo> lista = new ArrayList<Cromossomo>(
				populacao.retornaCromossomoList());
		int ringue = (int) Math.round(populacao.getTamanho_populacao() * 0.2);
		ArrayList<Cromossomo> novaLista = new ArrayList<Cromossomo>();
		int tamPop = populacao.getTamanho_populacao();
		while (novaLista.size() != tamPop) {
			MersenneTwister random = new MersenneTwister(System.nanoTime());
			int indice = random.nextInt(tamPop);
			int maiorIndice = indice;
			double maior = lista.get(indice).getFitness();
			for (int i = 0; i < ringue - 1; i++) {
				random = new MersenneTwister(System.nanoTime());
				random.setSeed(System.nanoTime());
				indice = random.nextInt(tamPop);
				if (lista.get(indice).getFitness() > maior) {
					maiorIndice = indice;
					maior = lista.get(indice).getFitness();
				}
			}
			novaLista.add(lista.get(maiorIndice));
		}
		Populacao novaPopulacao = new Populacao(novaLista);
		populacao = novaPopulacao;

	}
    
	static void calcularFitness() {
		ArrayList<Cromossomo> testeLista = new ArrayList<Cromossomo>(
				populacao.retornaCromossomoList());
		int tamPopAux = populacao.getTamanho_populacao();
		switch (casoDeTeste) {
		case 0: {
			for (int i = 0; i < populacao.getTamanho_populacao() / 2; i++) {
				MersenneTwister random = new MersenneTwister(System.nanoTime());

				int pai1 = random.nextInt(tamPopAux);
				Cromossomo cromo1 = testeLista.get(pai1);
				testeLista.remove(pai1);
				tamPopAux--;
				int pai2 = random.nextInt(tamPopAux);
				Cromossomo cromo2 = testeLista.get(pai2);
				testeLista.remove(pai2);
				tamPopAux--;

				double fitness1 = 0, fitness2 = 0;
				ArrayList<Object> genes1 = cromo1.retornaGenes(), genes2 = cromo2
						.retornaGenes();

				for (int j = 0; j < populacao.getNum_genes(); j++) {
					if (genes1.get(j) == genes2.get(j))
						if ((boolean) genes1.get(j)) {
							fitness1 += 5;
							fitness2 += 5;
						} else {
							fitness1 += 0.5;
							fitness2 += 0.5;
						}
					else {
						if ((boolean) genes1.get(j)) {
							fitness1 += 10;
							fitness2 += 0;
						} else {
							fitness1 += 0;
							fitness2 += 10;
						}
					}

				}
				pai1 = populacao.retornaCromossomoList().indexOf(cromo1);
				populacao.retornaCromossomoList().get(pai1)
						.setFitness(fitness1);

				pai2 = populacao.retornaCromossomoList().indexOf(cromo2);
				populacao.retornaCromossomoList().get(pai2)
						.setFitness(fitness2);

			}
		}
		case 1: {
			for (int p = 0; p < populacao.getTamanho_populacao(); p++) {
				MersenneTwister random = new MersenneTwister(System.nanoTime());
				tamPopAux = populacao.getTamanho_populacao();
				testeLista = new ArrayList<Cromossomo>(
						populacao.retornaCromossomoList());
				Cromossomo cromo1 = testeLista.get(p);
				testeLista.remove(p);
				tamPopAux--;
				double fitness1 = 0;
				for (int i = 0; i < populacao.getTamanho_populacao() / 2; i++) {

					int pai2 = random.nextInt(tamPopAux);
					// System.out.println("pai 2 "+pai2);

					Cromossomo cromo2 = testeLista.get(pai2);
					testeLista.remove(pai2);
					tamPopAux--;

					ArrayList<Object> genes1 = cromo1.retornaGenes(), genes2 = cromo2
							.retornaGenes();

					for (int j = 0; j < populacao.getNum_genes(); j++) {
						if (genes1.get(j) == genes2.get(j))
							if ((boolean) genes1.get(j)) {
								fitness1 += 5;

							} else {
								fitness1 += 0.5;

							}
						else {
							if ((boolean) genes1.get(j)) {
								fitness1 += 10;

							} else {
								fitness1 += 0;

							}
						}

					}
				}
				populacao.retornaCromossomoList().get(p).setFitness(fitness1);
			}
		}
		case 2: {
			for (int p = 0; p < populacao.getTamanho_populacao(); p++) {
				MersenneTwister random = new MersenneTwister(System.nanoTime());
				tamPopAux = populacao.getTamanho_populacao();
				testeLista = new ArrayList<Cromossomo>(
						populacao.retornaCromossomoList());
				Cromossomo cromo1 = testeLista.get(p);
				// testeLista.remove(p);
				tamPopAux--;
				double fitness1 = 0;
				for (int i = 0; i < populacao.getTamanho_populacao(); i++) {

					// int pai2 = random.nextInt(tamPopAux);
					// System.out.println("pai 2 "+pai2);
					if (i != p) {

						Cromossomo cromo2 = testeLista.get(i);
						// testeLista.remove(i);

						ArrayList<Object> genes1 = cromo1.retornaGenes(), genes2 = cromo2
								.retornaGenes();

						for (int j = 0; j < populacao.getNum_genes(); j++) {
							if (genes1.get(j) == genes2.get(j))
								if ((boolean) genes1.get(j)) {
									fitness1 += 5;

								} else {
									fitness1 += 0.5;

								}
							else {
								if ((boolean) genes1.get(j)) {
									fitness1 += 10;

								} else {
									fitness1 += 0;

								}
							}

						}
					}
					populacao.retornaCromossomoList().get(p)
							.setFitness(fitness1);
				}
			}
		}
		}

	}
    
    static double calcularMediaFitness(){
    	double soma=0;
    	for(int i=0;i<populacao.getTamanho_populacao();i++){
    		soma+=populacao.retornaCromossomoList().get(i).getFitness();
    	}
    	double media = soma/populacao.getTamanho_populacao();
    	//System.out.println("Media Fitness: "+media);
    	return media;
    }
    
	static void calculaBonificação() {
		ArrayList<Cromossomo> testeLista = new ArrayList<Cromossomo>(
				populacao.retornaCromossomoList());
		for (int i = 0; i < populacao.getTamanho_populacao(); i++) {
			ArrayList<Object> gene = testeLista.get(i).retornaGenes();
			int bonus = 0;
			for (int j = 0; j < populacao.getNum_genes(); j++) {
				if (!(boolean) gene.get(j))
					bonus++;
				else
					bonus = 0;
				if (bonus == 3) {
					double fit = testeLista.get(i).getFitness();
					fit++;
					populacao.retornaCromossomoList().get(i).setFitness(fit);

				}
			}
		}
	}

	static double calculaMaiorFitness() {
		ArrayList<Cromossomo> testeLista = new ArrayList<Cromossomo>(
				populacao.retornaCromossomoList());
		double maiorFit = testeLista.get(0).getFitness();
		int maiorInd = 0;
		for (int i = 1; i < populacao.getTamanho_populacao(); i++) {
			if (testeLista.get(i).getFitness() > maiorFit) {
				maiorInd = i;
				maiorFit = testeLista.get(i).getFitness();
			}
		}
		return maiorFit;
	}

}
