import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalForm() {
  const [profissional, setProfissional] = useState({
    nome: '',
    telefone: '',
    endereco: '',
    categoria: 'Médico'
  });
  const [erro, setErro] = useState('');
  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id)
        .then(response => setProfissional(response.data))
        .catch(err => setErro('Erro ao carregar o profissional.'));
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfissional(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!profissional.nome.trim()) {
      setErro('O nome é obrigatório.');
      return;
    }
    const saveAction = id
      ? profissionalService.atualizar(id, profissional)
      : profissionalService.criar(profissional);

    saveAction
      .then(() => navigate('/profissionais'))
      .catch(err => {
        const msg = err.response?.data?.message || 'Erro ao salvar o profissional.';
        setErro(msg);
      });
  };

  return (
    <div className="form-container">
      <h2>{id ? 'Editar Profissional' : 'Novo Profissional'}</h2>
      {erro && <div className="error-alert">{erro}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="nome">Nome Completo *</label>
          <input
            type="text"
            id="nome"
            name="nome"
            value={profissional.nome}
            onChange={handleChange}
            required
            maxLength="100"
          />
        </div>
        <div className="form-group">
          <label htmlFor="telefone">Telefone</label>
          <input
            type="text"
            id="telefone"
            name="telefone"
            value={profissional.telefone}
            onChange={handleChange}
            maxLength="20"
          />
        </div>
        <div className="form-group">
          <label htmlFor="endereco">Endereço</label>
          <input
            type="text"
            id="endereco"
            name="endereco"
            value={profissional.endereco}
            onChange={handleChange}
            maxLength="200"
          />
        </div>
        <div className="form-group">
          <label htmlFor="categoria">Categoria *</label>
          <select
            id="categoria"
            name="categoria"
            value={profissional.categoria}
            onChange={handleChange}
            required
          >
            <option value="Médico">Médico</option>
            <option value="Fisioterapeuta">Fisioterapeuta</option>
            <option value="Psicólogo">Psicólogo</option>
          </select>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <Link to="/profissionais" className="btn btn-secondary">Cancelar</Link>
        </div>
      </form>
    </div>
  );
}

export default ProfissionalForm;
