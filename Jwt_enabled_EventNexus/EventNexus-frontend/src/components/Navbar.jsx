import React from 'react';
import { useNavigate } from 'react-router-dom';
import { setAuthToken } from '../api/api';

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const handleLogout = () => {
    try {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
    } catch {}
    setAuthToken(null);
    navigate('/login', { replace: true });
  };

  return (
    <nav style={{ display: "flex", justifyContent: "space-between", alignItems: "center",
                  padding: "10px 20px", backgroundColor: "#007bff", color: "white" }}>
      <h1>EventNexus</h1>
      <div>
        {token && (
          <button onClick={handleLogout}
                  style={{ backgroundColor: "white", color: "#007bff", border: "none",
                           padding: "6px 12px", borderRadius: "4px", cursor: "pointer" }}>
            Logout
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;

