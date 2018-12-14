package br.com.ricardoeletro.sigerl.constantes;

public interface AppConstantes {
	interface Recebimento{
		String PATH = "process/recebimentoLoja";
		String LABEL = "Informe o código EAN do produto:";
	}
	
	interface Expedicao{
		String PATH = "process/expedicaoLoja";
		String LABEL = "Informe a chave da Nota Fiscal:";
	}
}
