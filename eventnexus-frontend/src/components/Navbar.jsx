// src/components/Navbar.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();
   const token = localStorage.getItem('token');
   const role = localStorage.getItem('role');

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    navigate('/login');
  };

  return (
    // <nav className="p-4 shadow-md flex justify-between items-center">
      <nav style={{
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "10px 20px",
      backgroundColor: "#007bff",
      color: "white"
    }}>
      <h1 className="text-xl font-bold">EventNexus</h1>
       <div>
        {token ? (
          <button onClick={handleLogout} style={{
            backgroundColor: "white",
            color: "#007bff",
            border: "none",
            padding: "6px 12px",
            borderRadius: "4px",
            cursor: "pointer"
          }}>
            Logout
          </button>
        ) : null}
      </div>
      {/* <button
        onClick={handleLogout}
        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
      >
        Logout
      </button> */}
    </nav>
  );
};

export default Navbar;
