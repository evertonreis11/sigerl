package br.com.linkcom.wms.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.wms.util.recebimento.AgendaVO;

public class ExportAgendaExcelView extends HSSFWorkbook {
	
	private final List<AgendaVO> dadosExportacao;
	private final static String[] COLUNAS = {"Pedido", "Código", "Descrição", "Qtde.", "Valor unitário" };

	public ExportAgendaExcelView(List<AgendaVO> list) {
		this.dadosExportacao = list;
		buildExcelDocument();
	}

	@SuppressWarnings("unchecked")
	private void buildExcelDocument() {

		short currentRow = 0;

		//Criando a planilha
		Sheet sheet = this.createSheet("Dados Logísticos");
		sheet.setDefaultColumnWidth((short) 12);
		
		//Fonte dos titulos dos detalhes
		Font detalheFont = this.createFont();
		detalheFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style = this.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(detalheFont);
		
		Font titleFont = this.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short)16);
		CellStyle titleStyle = this.createCellStyle();
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setFont(titleFont);

		Row titulo = sheet.createRow(currentRow++);
		Cell cell = titulo.createCell(0);
		cell.setCellStyle(titleStyle);
		cell.setCellValue("Relatório de agendamentos");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, COLUNAS.length));
		
		HSSFCellStyle styleCurrencyFormat = this.createCellStyle();
		HSSFDataFormat dataFormat = this.createDataFormat();
		styleCurrencyFormat.setDataFormat(dataFormat.getFormat("_(R$ * #,##0.00_);_(R$ * (#,##0.00);_(R$ * \"-\"??_);_(@_)"));

		int numColunas = COLUNAS.length;
		
		AgendaVO itemAnterior = null;
		for (AgendaVO item : dadosExportacao){
			Row row = sheet.createRow(currentRow++);
			
			if (itemAnterior == null || !itemAnterior.getNumeroAgendamento().equals(item.getNumeroAgendamento())){
				createLabelCell(item, row, 0, "Data:", item.getData());				
				createLabelCell(item, row, 2, "Previsão de vencimento financeiro:", item.getDtprevisao());				

				row = sheet.createRow(currentRow++);
				createLabelCell(item, row, 0, "Agendamento:", item.getNumeroAgendamento());				
				createLabelCell(item, row, 2, "Status:", item.getStatus());				
				createLabelCell(item, row, 4, "Tipo de carga:", item.getTipoCarga());				
				
				int linhaFornecedor = currentRow++;
				row = sheet.createRow(linhaFornecedor);
				createLabelCell(item, row, 0, "Fornecedor:", item.getFornecedor());
				sheet.addMergedRegion(new CellRangeAddress(linhaFornecedor, linhaFornecedor, 1, COLUNAS.length));
				
				Row titulos = sheet.createRow(currentRow++);
				for (short col = 0; col < numColunas; col++){
					Cell celula = titulos.createCell(col);
					celula.setCellStyle(style);
					celula.setCellValue(COLUNAS[col]);
				}
				
				row = sheet.createRow(currentRow++);
			}
			
			Cell cellPedido = row.createCell(0);
			cellPedido.setCellValue(item.getNumeroPedido());
			
			Cell cellCodigo = row.createCell(1);
			cellCodigo.setCellValue(item.getCodigoProduto());

			Cell cellDescricao = row.createCell(2);
			cellDescricao.setCellValue(item.getDescricaoProduto());

			Cell cellQtde = row.createCell(3);
			cellQtde.setCellValue(item.getQtdeProduto());

			Cell cellValor = row.createCell(4);
			cellValor.setCellStyle(styleCurrencyFormat);
			cellValor.setCellValue(item.getValor().doubleValue());

			itemAnterior = item;
		}
	}

	private void createLabelCell(AgendaVO item, Row row, int col, String label, Object value) {
		Cell cellLabel = row.createCell(col);
		cellLabel.setCellValue(label);				
		Cell cellValue = row.createCell(col + 1);
		
		if (value instanceof Integer)
			cellValue.setCellValue((Integer) value);
		else if (value instanceof Long)
			cellValue.setCellValue((Long) value);
		else
			cellValue.setCellValue(value.toString());
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