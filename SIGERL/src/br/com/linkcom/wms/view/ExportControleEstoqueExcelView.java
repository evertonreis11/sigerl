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
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Controleestoque;
import br.com.linkcom.wms.geral.bean.Controleestoqueproduto;
import br.com.linkcom.wms.util.WmsUtil;

public class ExportControleEstoqueExcelView extends HSSFWorkbook {
	
	private final Controleestoque controleestoque;

	public ExportControleEstoqueExcelView(Controleestoque controleestoque) {
		this.controleestoque = controleestoque;
		buildExcelDocument();
	}

	@SuppressWarnings("unchecked")
	private void buildExcelDocument() {

		//Criando a planilha
		Sheet sheet = this.createSheet("Comparação de estoque");
		sheet.setDefaultColumnWidth((short) 12);
		
		short currentRow = 0;

		List<String> colunas = new ArrayList<String>();
		colunas.add("Código");
		colunas.add("Descrição");
		colunas.add("Linha do Produto");
		colunas.add("Qtde. Avaria");
		colunas.add("Qtde. WMS");
		colunas.add("Qtde. ERP");
		colunas.add("(WMS - ERP)");
		
		Row titulo = sheet.createRow(currentRow++);
		Font fontTitulo = this.createFont();
		fontTitulo.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitulo.setFontHeightInPoints((short)14);
		CellStyle styleTitulo = this.createCellStyle();
		styleTitulo.setAlignment(CellStyle.ALIGN_CENTER);
		styleTitulo.setFont(fontTitulo);
		Cell celula = titulo.createCell(0);
		celula.setCellStyle(styleTitulo);
		celula.setCellValue("Relatório de comparação de estoque");
		sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 0, colunas.size() - 1));
		
		Font fontCabecalho = this.createFont();
		fontCabecalho.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle styleCabecalho = this.createCellStyle();
		styleCabecalho.setAlignment(CellStyle.ALIGN_LEFT);
		styleCabecalho.setFont(fontCabecalho);

		Row depositoRow = sheet.createRow(currentRow++);
		Cell celulaDeposito = depositoRow.createCell(0);
		celulaDeposito.setCellStyle(styleCabecalho);
		celulaDeposito.setCellValue("Depósito:");
		celulaDeposito = depositoRow.createCell(1);
		celulaDeposito.setCellStyle(styleCabecalho);
		celulaDeposito.setCellValue(WmsUtil.getDeposito().getNome());
		sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 1, colunas.size() - 1));
		
		Row dataRow = sheet.createRow(currentRow++);
		Cell celulaData = dataRow.createCell(0);
		celulaData.setCellStyle(styleCabecalho);
		celulaData.setCellValue("Data:");
		celulaData = dataRow.createCell(1);
		celulaData.setCellStyle(styleCabecalho);
		celulaData.setCellValue(NeoFormater.getInstance().format(controleestoque.getDtcontroleestoque()));

		Cell celulaStatus = dataRow.createCell(2);
		celulaStatus.setCellStyle(styleCabecalho);
		celulaStatus.setCellValue("Status:");
		celulaStatus = dataRow.createCell(3);
		celulaStatus.setCellStyle(styleCabecalho);
		celulaStatus.setCellValue(controleestoque.getControleestoquestatus().getNome());
		sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 3, colunas.size() - 1));

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
		
		for (Controleestoqueproduto item : controleestoque.getListaControleestoqueproduto()){
			Row row = sheet.createRow(currentRow);

			Cell cell = row.createCell(0);
			cell.setCellValue(item.getProduto().getCodigo());

			cell = row.createCell(1);
			cell.setCellValue(item.getProduto().getDescricao());
			
			cell = row.createCell(2);
			cell.setCellValue(item.getProduto().getProdutoclasse().getNome());
			
			cell = row.createCell(3);
			cell.setCellValue(item.getQtdeavaria());

			cell = row.createCell(4);
			cell.setCellValue(item.getQtdeesperada());
			
			cell = row.createCell(5);
			cell.setCellValue(item.getQtde());
			
			cell = row.createCell(6);
			cell.setCellValue(item.getQtdeesperada() - item.getQtdeavaria() - item.getQtde());
			
			currentRow++;
		}
	}
	
	/**
	 * Método que retorna Resource. Este resource é para arquivos Excel 97-2003
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 * @author Tomás Rabelo
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