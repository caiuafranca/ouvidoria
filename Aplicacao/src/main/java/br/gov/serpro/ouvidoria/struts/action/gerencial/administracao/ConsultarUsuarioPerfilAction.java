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
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import br.gov.serpro.ouvidoria.controller.PerfilCtrl;
import br.gov.serpro.ouvidoria.model.Perfil;
import br.gov.serpro.ouvidoria.struts.ActionSupport;

/**
 * Objetivo: Chamar a tela de entrada de parâmetros para a consulta a usuários
 * por perfil
 * 
 * @author SERPRO
 * @version $Revision: 1.1.2.5 $, $Date: 2011/10/21 12:51:36 $
 * @version 0.1, 2004/12/27
 */
public class ConsultarUsuarioPerfilAction extends ActionSupport {

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

        // Recupera lista de Perfis
        PerfilCtrl perfilCtrl = new PerfilCtrl(getDaoFactory());

        Collection lstPerfil = perfilCtrl.list();

        // Retira perfil de administrador
        Perfil perfil = null;
        for (Iterator it = lstPerfil.iterator(); it.hasNext();) {
            perfil = (Perfil) it.next();

            if (perfil.getId().longValue() == Perfil.ADMINISTRADOR.getId()
                    .longValue()) {
                break;
            }
        }

        lstPerfil.remove(perfil);

        if (lstPerfil.isEmpty()) {
            errorMsgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.perfil.listavazia"));
            saveMessages(request, errorMsgs);
        }

        // Passa a lista de perfis
        request.setAttribute("lstPerfil", lstPerfil);

        if (errorMsgs.isEmpty()) {
            return (mapping.findForward("success"));
        }
        return (mapping.findForward("failure"));
    }

}