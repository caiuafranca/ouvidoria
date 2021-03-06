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
package br.gov.serpro.ouvidoria.controller.andamento;

import br.gov.serpro.ouvidoria.dao.Dao;
import br.gov.serpro.ouvidoria.dao.DaoException;
import br.gov.serpro.ouvidoria.dao.DaoFactory;
import br.gov.serpro.ouvidoria.model.Acionamento;
import br.gov.serpro.ouvidoria.model.TipoAvaliacaoResposta;

/**
 * Objetivo: Controlar as operações sobre os objetos relacionados à parte de
 * salvar a avaliação da resposta da funcionalidade Avaliar Solução.
 * 
 * @author SERPRO
 * @version $Revision: 1.1.2.4 $, $Date: 2011/10/18 17:55:13 $
 * @version 0.1, Date: 2004/12/02
 */
public class AvaliarRespostaCtrl {

    /** atributo para datasource do acionamento */
    /* Requerido */
    private Dao acionamentoDao;

    /** atributo para datasource do tipo de avaliação de resposta */
    /* Requerido */
    private Dao tipoAvaliacaoRespostaDao;

    /**
     * Construtor recebendo objeto Dao
     * 
     * @param daoFactory
     */
    public AvaliarRespostaCtrl(final DaoFactory daoFactory) {
        acionamentoDao = daoFactory.create(Acionamento.class);
        tipoAvaliacaoRespostaDao = daoFactory
                .create(TipoAvaliacaoResposta.class);
    }

    /**
     * Método para recuperar o acionamento a partir de um número de protocolo
     * 
     * @param numeroProtocolo
     *            número do protocolo do acionamento
     * @param idTipoAvaliacao
     *            identificação do tipo de avaliação da resposta
     */
    public void save(Acionamento acionamento, Long idTipoAvaliacao)
            throws DaoException {

        if (idTipoAvaliacao == null) {
            throw new DaoException("Tipo de avaliação não pode ser nulo.");
        }

        TipoAvaliacaoResposta tipoAvaliacaoResposta = (TipoAvaliacaoResposta) tipoAvaliacaoRespostaDao
                .get(idTipoAvaliacao);
        if (!tipoAvaliacaoResposta.equals(acionamento.getRespostaAcionamento()
                .getTipoAvaliacaoResposta())) {
            acionamento.getRespostaAcionamento().setTipoAvaliacaoResposta(
                    tipoAvaliacaoResposta);
            acionamentoDao.save(acionamento);
        }
    }

}