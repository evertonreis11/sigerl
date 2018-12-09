<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
	<jsp:include page="../../inputTextAreaPage.jsp"></jsp:include>
	
	<div class="row">
        <div class="form-group col-xs-12" style="margin-top: 2%;">
            
        	<div class="col-xs-4 col-xs-offset-5">
            	<button class="btn btn-primary btn-lg" type="button" id="buttonLimpar" onclick="clearForm();">Limpar</button>
            </div>
        </div> 
     </div>
</n:form>