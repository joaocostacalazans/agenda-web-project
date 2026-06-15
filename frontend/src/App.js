import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProfissionalList from './components/ProfissionalList';
import ProfissionalForm from './components/ProfissionalForm';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1>📅 Agenda de Saúde</h1>
          <div className="nav-links">
            <Link to="/profissionais">Profissionais de Saúde</Link>
          </div>
        </nav>

        <main className="container">
          <Routes>
            <Route path="/" element={<ProfissionalList />} />
            <Route path="/profissionais" element={<ProfissionalList />} />
            <Route path="/profissionais/novo" element={<ProfissionalForm />} />
            <Route path="/profissionais/editar/:id" element={<ProfissionalForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;

