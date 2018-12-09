package br.com.linkcom.wms.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.resource.Resource;

public class ExportExcelView extends HSSFWorkbook {
	
	private SqlRowSet dadosExportacao;
	private List<SqlRowSet> listaDadosExportacao = new ArrayList<SqlRowSet>();

	public ExportExcelView(SqlRowSet dadosExportacao,String titulo) {
		this.dadosExportacao = dadosExportacao;
		buildExcelDocument(titulo);
	}

	public ExportExcelView(List<SqlRowSet> listaDadosExportacao, String titulo) {
		this.listaDadosExportacao.addAll(listaDadosExportacao);
		buildExcelListDocument(titulo);
	}
	
	@SuppressWarnings("unchecked")
	private void buildExcelDocument(String titulo) {

		//Criando a planilha
		Sheet sheet = this.createSheet(titulo);
		sheet.setDefaultColumnWidth((short) 12);
		
		int numColunas = dadosExportacao.getMetaData().getColumnCount();
		
		Row titulos = sheet.createRow(0);
		Font font = this.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style = this.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
		for (short col = 0; col < numColunas; col++){
			Cell celula = titulos.createCell(col);
			celula.setCellStyle(style);
			celula.setCellValue(dadosExportacao.getMetaData().getColumnLabel(col + 1));
		}
		
		DataFormat dataFormat = this.createDataFormat();
		
		short currentRow = 1;
		while (dadosExportacao.next()){
			Row row = sheet.createRow(currentRow);

			for (short col = 0; col < numColunas; col++){
				if (dadosExportacao.getObject(col + 1) == null)
					continue;
				
				int type = dadosExportacao.getMetaData().getColumnType(col + 1);

				Cell cell = row.createCell(col);
			
				if (type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP) {				
					CellStyle cellStyle = this.createCellStyle();
					cellStyle.setDataFormat(dataFormat.getFormat("m/d/yy"));
					cell.setCellStyle(cellStyle);
					cell.setCellValue(dadosExportacao.getDate(col + 1));
				}else if (type == Types.DOUBLE || type == Types.FLOAT	|| type == Types.NUMERIC)
					cell.setCellValue(dadosExportacao.getDouble(col + 1));
				else
					cell.setCellValue(dadosExportacao.getString(col + 1));
			}
			currentRow++;
		}
	}
	
	/**
	 * Método identico ao buildExcelDocument, porém esse recebe uma lista de SqlRowSet como parametro.
	 * @author Filipe Santos
	 * @since 12/01/2012
	 * @see #buildExcelDocument()
	 */
	@SuppressWarnings("unchecked")
	private void buildExcelListDocument(String titulo) {

		//Criando a planilha
		Sheet sheet = this.createSheet(titulo);
		sheet.setDefaultColumnWidth((short) 12);
		
		int numColunas = listaDadosExportacao.get(0).getMetaData().getColumnCount();
		
		Row titulos = sheet.createRow(0);
		Font font = this.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style = this.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
		for (short col = 0; col < numColunas; col++){
			Cell celula = titulos.createCell(col);
			celula.setCellStyle(style);
			celula.setCellValue(listaDadosExportacao.get(0).getMetaData().getColumnLabel(col + 1));
		}
		int currentRow = 1;
		DataFormat dataFormat = this.createDataFormat();
		
		for (SqlRowSet dadosexportacao : listaDadosExportacao) {					
			
			while (dadosexportacao.next()){
				Row row = sheet.createRow(currentRow);
	
				for (short col = 0; col < numColunas; col++){
					if (dadosexportacao.getObject(col + 1) == null)
						continue;
					
					int type = dadosexportacao.getMetaData().getColumnType(col + 1);
	
					Cell cell = row.createCell(col);
				
					if (type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP) {				
						CellStyle cellStyle = this.createCellStyle();
						cellStyle.setDataFormat(dataFormat.getFormat("m/d/yy"));
						cell.setCellStyle(cellStyle);
						cell.setCellValue(dadosexportacao.getDate(col + 1));
					}else if (type == Types.DOUBLE || type == Types.FLOAT	|| type == Types.NUMERIC)
						cell.setCellValue(dadosexportacao.getDouble(col + 1));
					else
						cell.setCellValue(dadosexportacao.getString(col + 1));
				}
				currentRow++;
			}
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