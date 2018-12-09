package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.service.OrdemservicoService;

/**
 * 
 * @author Leonardo Guimarães
 *
 */
public class ReabastecimentoWindow extends TransferenciaWindow{
	
	
	private static final String REABASTECIMENTO_CORRETIVO = "Reabastecimento corretivo";
	private static final String REABASTECIMENTO_PREVENTIVO = "Reabastecimento preventivo";

	@Override
	public List<Ordemservico> carregarListaOS() {
		return OrdemservicoService.getInstance().findForReabastecimento(this.linhaseparacao,this.deposito, this.usuario);
	}
	
	@Override
	public String getTitulo() {
		if (this.ordemservico.getReabastecimentolote() != null)
			return REABASTECIMENTO_PREVENTIVO;
		else
			return REABASTECIMENTO_CORRETIVO;
	}
	
	@Override
	public boolean transferirNaoUMA(Ordemservicoproduto osp) throws IOException {
		Boolean enderecoValido = verificaEnderco(osp, this.enderecoOrigem, "Informe o endereço de origem:");
		if(enderecoValido){
			 if(produtoValido(osp)){
				if(verificaEnderco(osp,this.enderecoDestino,"Informe o endereço de destino:")){
					saveTransferencia(osp,usuario);
					return true;
				}
			 }
		}
		return false;
	}
	
	@Override
	public void updateTransferencia(Transferenciastatus transferenciastatus) {
	}
	
	@Override
	public void alertConclusao() throws IOException {
		writeOnCenter("Reabastecimento concluído com sucesso.", null, true, true);
	}
	
}
