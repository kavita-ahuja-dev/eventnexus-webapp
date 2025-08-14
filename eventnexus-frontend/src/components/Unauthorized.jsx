import React from 'react';
import { Link } from 'react-router-dom';

const Unauthorized = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-red-50">
      <div className="bg-white p-8 rounded shadow text-center">
        <h1 className="text-2xl font-bold text-red-600 mb-4">403 - Unauthorized</h1>
        <p className="mb-4">You do not have permission to access this page.</p>
        <Link to="/login" className="text-blue-500 hover:underline">Return to Login</Link>
      </div>
    </div>
  );
};

export default Unauthorized;
