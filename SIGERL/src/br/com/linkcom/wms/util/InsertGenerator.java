package br.com.linkcom.wms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertGenerator {
	
	private static Connection con;
	private static PreparedStatement pstm ;

	
	public static final String contrato = "728";
	public static String DB_URL= "jdbc:postgresql://piedade/w3_linkcom";
	public static final String DB_PASSWORD = "gre4POL2";
	public static final String DB_USERNAME = "postgres";
	public static final String selectPacote = "select cdpacote from pacote where UPPER(identificador) LIKE '%'||?||'%'";
	public static final String InsertCodigoFonte = "insert into codigofonte ('cdcodigofonte','cdcontrato','url','classe') values (nextval('gen_codigofonte'),'?','?','?');";
	public static void main(String[] args) {
//		try {
//			Class<?>[] allClasses = ClassRegister.getClassManager().getAllClasses();
//			for (Class<?> class1 : allClasses) {
//				System.out.println(makeSelect(class1));
//			}
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//		List<Class<?>> listaClasses = new ArrayList<Class<?>>();
//		listaClasses.add(Pessoa.class);
//		for (Class<?> class1 : listaClasses) {
//			System.out.println(makeSelect(class1));
//		}
		
		try {
			connect();
			System.out.println(getCdPacote("UC007"));
			disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static String makeSelect(Class<?> clazz){
		Package pacote = clazz.getPackage();
		return "insert into codigofonte ('cdcodigofonte','cdcontrato','url','classe') values (nextval('gen_codigofonte'),'"+contrato+"','"+pacote.getName()+"','"+clazz.getSimpleName()+"');";
	}
	
	public static void connect() throws SQLException	{
//		DriverManager.registerDriver(new org.postgresql.Driver());
		con = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
//		con.setAutoCommit(false);
	}
	
	public static void disconnect() throws SQLException {
		con.close();
	}
	
	public static Integer getCdPacote(String identificador) throws SQLException{
		pstm = con.prepareStatement(selectPacote, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		pstm.setString(1,identificador.toUpperCase());
		ResultSet rs = pstm.executeQuery();
		rs.beforeFirst();
		if(rs.next()){
			return rs.getInt("cdpacote");
		}
		else{
			return null;
		}
	}
	
	public static List<String> findAllContrato() throws SQLException {
		String sql = "select distinct associado.cdassociado cd from contrato contrato " +
					"join associado associado on associado.cdassociado = contrato.cdassociado " +
					"join membro membro on membro.cdassociado = associado.cdassociado " +
					"join colaborador colaborador on colaborador.cdcolaborador = membro.cdcolaborador " +
					"where contrato.dtfim is null and contrato.projeto = 1 " +
					"order by associado.nome ";
		
		pstm = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = pstm.executeQuery();
		List<String> lista = new ArrayList<String>();
		while (rs.next()) {
			lista.add(rs.getString("cd"));
		}
		return lista;
	}
}
