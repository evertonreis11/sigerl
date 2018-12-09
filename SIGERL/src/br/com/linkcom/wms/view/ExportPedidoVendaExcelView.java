package br.com.linkcom.wms.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.wms.geral.bean.view.Vpedidovendareport;
import br.com.linkcom.wms.util.WmsUtil;

public class ExportPedidoVendaExcelView extends HSSFWorkbook {

	private List<Vpedidovendareport> listaPedidovendas = new ArrayList<Vpedidovendareport>();

	public ExportPedidoVendaExcelView(List<Vpedidovendareport> listaPedidovendas) {
		this.listaPedidovendas = listaPedidovendas;
		buildExcelDocument();
	}

	private void buildExcelDocument() {
		
		Sheet sheet = this.createSheet("Pedidos de Venda");
		sheet.setDefaultColumnWidth((short) 12);
		
		short currentRow = 0;
		
		List<String> colunas = new ArrayList<String>();
		colunas.add("Dep�sito");						
		colunas.add("Data da Venda");					 
		colunas.add("Data de Chegada (WMS)");			 
		colunas.add("Previs�o de Entrega");		
		colunas.add("Data de Faturamento");
		colunas.add("Turno de Entrega");
		colunas.add("N� do Pedido");					
		colunas.add("C�digo do Produto");				
		colunas.add("Descrica��o do Produto");			
		colunas.add("Quantidade");						
		colunas.add("Cubagem");							
		colunas.add("Tipo de Pedido");					 
		colunas.add("Carga");			
		colunas.add("Status da Carga");
		colunas.add("Box");								
		colunas.add("Status (Box)");
		colunas.add("Cliente");
		colunas.add("Rota");
		colunas.add("Valor do Produto");
		
		Row titulo = sheet.createRow(currentRow++);
		Font fontTitulo = this.createFont();
		fontTitulo.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitulo.setFontHeightInPoints((short)14);
		CellStyle styleTitulo = this.createCellStyle();
		styleTitulo.setAlignment(CellStyle.ALIGN_CENTER);
		styleTitulo.setFont(fontTitulo);
		Cell celula = titulo.createCell(0);
		celula.setCellStyle(styleTitulo);
		celula.setCellValue("Relat�rio de pedidos de venda");
		sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 0, colunas.size() - 1));
		
		Font fontCabecalho = this.createFont();
		fontCabecalho.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle styleCabecalho = this.createCellStyle();
		styleCabecalho.setAlignment(CellStyle.ALIGN_LEFT);
		styleCabecalho.setFont(fontCabecalho);

		Font fontDetalhe = this.createFont();
		fontDetalhe.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle styleDetalhe = this.createCellStyle();
		styleDetalhe.setAlignment(CellStyle.ALIGN_CENTER);
		styleDetalhe.setFont(fontDetalhe);

		currentRow++;
		Row tituloDetalhe = sheet.createRow(currentRow++);
		for (short col = 0; col < colunas.size(); col++){
			Cell celulaDetalhe = tituloDetalhe.createCell(col);
			celulaDetalhe.setCellStyle(styleDetalhe);
			celulaDetalhe.setCellValue(colunas.get(col));
		}
		
		if(listaPedidovendas!=null && !listaPedidovendas.isEmpty()){
			for (Vpedidovendareport vpvr : listaPedidovendas){
				
				Row row = sheet.createRow(currentRow);
	
				Cell cell = row.createCell(0);						//D�posito
				cell.setCellValue(vpvr.getDeposito()!=null?vpvr.getDeposito():"");
	
				cell = row.createCell(1);							//Data de Emiss�o do Pedido
				cell.setCellValue(vpvr.getDtemissao()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtemissao().toString(),"yyyy-MM-dd"):"");	
				
				cell = row.createCell(2);							//Data de Chegada (WMS)
				cell.setCellValue(vpvr.getDtchegadaerp()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtchegadaerp().toString(), "yyyy-MM-dd"):"");	 
				
				cell = row.createCell(3);							//Periodo de Entrega
				cell.setCellValue(vpvr.getDtprevisaoentrega()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtprevisaoentrega().toString(), "yyyy-MM-dd"):"");	 
				
				cell = row.createCell(4);							//Data de Faturamento
				if(vpvr.getCdcarregamentostatus()!=null && vpvr.getCdcarregamentostatus() == 6)
					cell.setCellValue(vpvr.getDtfaturamento()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtfaturamento().toString(), "yyyy-MM-dd"):"CORTADO");	
				else
					cell.setCellValue(vpvr.getDtfaturamento()!=null?WmsUtil.stringToDefaulDateFormat(vpvr.getDtfaturamento().toString(), "yyyy-MM-dd"):"");
				
				cell = row.createCell(5);							//Turno de Entrega
				cell.setCellValue(vpvr.getTurnodeentrega()!=null?vpvr.getTurnodeentrega():"");
				
				cell = row.createCell(6);							//N� do Pedido
				cell.setCellValue(vpvr.getNumero()!=null?vpvr.getNumero().toString():"");	 
				
				cell = row.createCell(7);							//C�digo do Produto
				cell.setCellValue(vpvr.getCodigoproduto()!=null?vpvr.getCodigoproduto():"");	 
				
				cell = row.createCell(8);							//Descri��o do Produto
				cell.setCellValue(vpvr.getDescricaoproduto()!=null?vpvr.getDescricaoproduto():"");	 
	
				cell = row.createCell(9);							//Qtde vendida
				cell.setCellValue(vpvr.getQtde()!=null?vpvr.getQtde().toString():"");	 
				
				cell = row.createCell(10);							//Cubagem
				try{
					Double cubagem = Double.parseDouble(vpvr.getCubagem()) * vpvr.getQtde(); 
					cell.setCellValue(cubagem.toString());
				}catch (Exception e) {
					cell.setCellValue("");
				}
				
				cell = row.createCell(11);							//Tipo de Pedido
				cell.setCellValue(vpvr.getTipooperacao()!=null?vpvr.getTipooperacao():"");	 
				
				cell = row.createCell(12);							//Carga
				cell.setCellValue(vpvr.getCdcarregamento()!=null?vpvr.getCdcarregamento().toString():"");	
				
				cell = row.createCell(13);							//Status da Carga
				cell.setCellValue(vpvr.getCarregamentostatus()!=null?vpvr.getCarregamentostatus():"");	
				
				cell = row.createCell(14);							//Box
				cell.setCellValue(vpvr.getBox()!=null?vpvr.getBox().toString():"");	
				
				cell = row.createCell(15);							//Status do Box
				cell.setCellValue(vpvr.getBoxstatus()!=null?vpvr.getBoxstatus():"");	
				
				cell = row.createCell(16);							//Cliente
				cell.setCellValue(vpvr.getCliente()!=null?vpvr.getCliente():"");	
				
				cell = row.createCell(17);							//Rota
				cell.setCellValue(vpvr.getRota()!=null?vpvr.getRota():"");	

				cell = row.createCell(18);							//Valor do Produto
				cell.setCellValue(vpvr.getValorproduto()!=null?vpvr.getValorproduto().toString():"");
				
				currentRow++;
			}
		}
	}
	
	/**
	 * M�todo que retorna Resource. Este resource � para arquivos Excel 97-2003
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 * @author Tom�s Rabelo
	 */
	public Resource getWorkBookResource(String name) throws IOException{
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		this.write(byteArray);
		
		Resource resource = new Resource();
        resource.setContentType("application/msexcel");
        resource.setFileName(name);
        resource.setContents(byteArray.toByteArray());
		return resource;
	}

}