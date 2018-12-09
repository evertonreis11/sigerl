package br.com.ricardoeletro.coletor.constantes;

public interface ColetorConstantes {
	interface Produto{
		interface ConsultaProduto{
			String PATH = "process/consultaProduto";
			String LABEL = "Informe o produto:";
		}
	}
	interface Recebimento{
		interface ConferenciaRecebimento{
			String PATH_INICIAL = "process/conferenciaRecebimento";
			String PATH_COLETA_PRODUTO = "process/coletarProdutoConferencia";
			String PATH_COLETA_QTDE = "process/coletarQuantidadeConferencia";
			String LABEL_INICIAL = "Recebimento:";
			String LABEL_COLETA_PRODUTO = "Informe o produto:";
			String LABEL_COLETA_QTDE = "Quantidade:";
		}
		
		interface ReconferenciaRecebimento{
			String PATH_INICIAL = "process/conferenciaRecebimento";
			String PATH_COLETA_PRODUTO = "process/coletarProdutoReconferencia";
			String PATH_COLETA_QTDE = "process/coletarQuantidadeConferencia";
			String LABEL_INICIAL = "Recebimento:";
			String LABEL_COLETA_PRODUTO = "Informe o produto:";
			String LABEL_COLETA_QTDE = "Quantidade:";
		}
	}
}
