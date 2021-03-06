/*
 * Sistema de Ouvidoria: um canal através do qual os usuários
 * podem encaminhar suas reclamações, elogios e sugestões.
 * 
 * Copyright (C) 2011 SERPRO
 * 
 * Este programa é software livre; você pode redistribuí-lo e/ou
 * modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 * publicada pela Free Software Foundation; tanto a versão 2 da
 * Licença como (a seu critério) qualquer versão mais nova.
 * 
 * Este programa é distribuído na expectativa de ser útil, mas SEM
 * QUALQUER GARANTIA; sem mesmo a garantia implícita de
 * COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
 * PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
 * detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU,
 * sob o título "LICENCA.txt", junto com esse programa. Se não,
 * acesse o Portal do Software Público Brasileiro no endereço
 * http://www.softwarepublico.gov.br/ ou escreva para a Fundação do
 * Software Livre (FSF) Inc., 51 Franklin St, Fifth Floor, Boston,
 * MA 02111-1301, USA.
 * 
 * Contatos através do seguinte endereço internet:
 * http://www.serpro.gov.br/sistemaouvidoria/
 */
package br.gov.serpro.ouvidoria.struts.action.gerencial.administracao;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import br.gov.serpro.ouvidoria.controller.gerencial.administracao.ScriptCtrl;
import br.gov.serpro.ouvidoria.dao.DaoException;
import br.gov.serpro.ouvidoria.model.Orgao;
import br.gov.serpro.ouvidoria.model.Script;
import br.gov.serpro.ouvidoria.struts.ActionSupport;

/**
 * Objetivo: Exibir detalhes do Script consultado ou permitir entrada de novo
 * script
 * 
 * @author SERPRO
 * @version $Revision: 1.1.2.5 $, $Date: 2011/10/21 12:51:36 $
 * @version 0.1, 2004/12/27
 */
public class ExibirEntrarScriptAction extends ActionSupport {

    /**
     * Método de execução da ação. Realiza dois forwards: um em caso de sucesso
     * e outro em caso de falha
     * 
     * @param mapping
     * @param form
     *            ActionForm, caso necessário
     * @param request
     * @param response
     *  
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errorMsgs = new ActionMessages();        

        try {
            ScriptCtrl ctrlScript = new ScriptCtrl(getDaoFactory());

            String id = request.getParameter("id");
                       
            // Se foi passado um id de script, trata-se de
            // uma alteração/exclusão
            if (id != null) {
                Script script = ctrlScript.get(new Long(id));

                // Se não encontrou o script, reporta o erro
                if (script == null) {
                    errorMsgs.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("error.script.notFound"));
                    saveMessages(request, errorMsgs);
                }

                // Passa através da requisição a solução escolhida
                // e a ação chamadora, caso exista
                request.setAttribute("Script", script);
                                
            }

            // Verifica se usuario pode editar Script
            String lsEditar = request.getParameter("txtEditar");            
            if( lsEditar != null && lsEditar.equals("N") ){
            	request.setAttribute("podeEditar","readonly");
            } else {
            	request.setAttribute("podeEditar","");
            }
            
            // Recupera a quantidade de utilização do script
            List qdtUtilizacao = ctrlScript.listaQtdUtilizacaoScript(id);                
            request.setAttribute("qdtUtilizacao", qdtUtilizacao.get(0));
            
            // Recupera órgão do funcionário
            Orgao org = getOrgao(request);

            // Recupera lista de assuntos do órgão para preenchimento da "combo"
            Collection lstAssuntos = org.getListaTodosAssuntos("Ativo");

            if (lstAssuntos.isEmpty()) {
                errorMsgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "error.orgao.semassunto"));

                saveMessages(request, errorMsgs);
            }

            // Passa a lista de assuntos do funcionário através da requisição
            // e a ação chamadora, caso exista
            request.setAttribute("lstAssuntos", lstAssuntos);

        } catch (DaoException e) {
            errorMsgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.script.excecao"));
            saveMessages(request, errorMsgs);
        }

        if (errorMsgs.isEmpty()) {
            return (mapping.findForward("success"));
        }
        return (mapping.findForward("failure"));

    }

}