package br.com.linkcom.wms.util;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.wms.util.expedicao.CarregamentoVO;

/**
 * Monta as imagens que serão utilizadas no TreeGrid a partir dos dados dos pais.
 * 
 * @author Pedro Gonçalves
 *
 */
public class TreeGridBuilder {
	
	private List<List<Integer>> mapa = new ArrayList<List<Integer>>();
	private String[] mapb;
	private Integer num;
	
	private String openImg = WmsUtil.getContex()+"/imagens/treetable/tv-collapsable.gif"; 
	private String shutImg = WmsUtil.getContex()+"/imagens/treetable/tv-expandable.gif"; 
	private String leafImg =  WmsUtil.getContex()+"/imagens/treetable/tv-item.gif";
	private String lastOpenImg = WmsUtil.getContex()+"/imagens/treetable/tv-collapsable-last.gif"; 
	private String lastLeafImg = WmsUtil.getContex()+"/imagens/treetable/tv-item-last.gif"; 
	private String lastShutImg = WmsUtil.getContex()+"/imagens/treetable/tv-expandable-last.gif"; 
	private String vertLineImg = WmsUtil.getContex()+"/imagens/treetable/vertline.gif"; 
	private String blankImg = WmsUtil.getContex()+"/imagens/treetable/blank.gif";
	
	/*
	 * openImg: "tv-collapsable.gif",
		shutImg: "tv-expandable.gif",
		leafImg: "tv-item.gif",
		lastOpenImg: "tv-collapsable-last.gif",
		lastShutImg: "tv-expandable-last.gif",
		lastLeafImg: "tv-item-last.gif",
		vertLineImg: "vertline.gif",
	 */
	
	/**
	 * @param map - Lista com o pai de cada nodo. Caso seja raiz, o valor deve ser 0.
	 */
	public TreeGridBuilder(Integer[] map) {
		initList(map); //inicializa a lista.
		mapb = new String[map.length];
	}
	
	/**
	 * Agrupa os filhos em uma única lista.
	 * 
	 * @param map
	 */
	private void initList(Integer[] map){
		mapa = new ArrayList<List<Integer>>(map.length);
		
		for (int x=0,xl=map.length; x<xl;x++){
			num = map[x];
			if (mapa.indexOf(num) < 0){
        		//mapa[num] = new Integer[0];
        		mapa.add(new ArrayList<Integer>());
      		}
      		mapa.get(num).add(x+1);
    	}
	}
	
	/**
	 * Monta a lista de imagens para ser concatenada a coluna das árvores.
	 * 
	 * @param parno - Nodo para ser varrido.
	 * @param preStr - String de cada ítem contendo as imagens.
	 */
	public void buildText(Integer parno, String preStr, List<CarregamentoVO> listaCarregamentoVO){
		int ro = 0,y,yl;
		List<Integer> mp = mapa.get(parno);
		String pre,img,pref;
		for (y = 0, yl = mp.size() ; y < yl; y++) {
			ro = mp.get(y);
			
			if(mapa.size() > ro && mapa.get(ro).size() > 0){
				if (y == yl-1){
					pre = blankImg;
					if("collapsed".equals(listaCarregamentoVO.get(ro).getCollapsed()))
						img = lastShutImg;
					else
						img = lastOpenImg;
				} else {
					pre = vertLineImg;
					if("collapsed".equals(listaCarregamentoVO.get(ro).getCollapsed()))
						img = shutImg;
					else 
						img = openImg;
				}
				//(elm.src.substr(elm.src.lastIndexOf("/")+1)==opts.lastOpenImg)? opts.lastShutImg: opts.shutImg
				mapb[ro-1] = preStr + "<img src=\""+img+"\" onclick=\"openCloseNode(this)\" class=\"parimg\" id=\"r"+ro+"\">";
     			pref = preStr + "<img src=\""+pre+"\" class=\"preimg\">";
     			buildText(ro, pref, listaCarregamentoVO);
			} else {
       			img = (y == yl-1)? lastLeafImg: leafImg;
       			mapb[ro-1] = preStr + "<img src=\""+img+"\" class=\"ttimage\">";
    		}
		}
	}
	
	/**
	 * Lista que contém as imagens que serão concatenadas à coluna da árvore.
	 * 
	 * @return
	 */
	public String[] getMapb() {
		return mapb;
	}
	
}
