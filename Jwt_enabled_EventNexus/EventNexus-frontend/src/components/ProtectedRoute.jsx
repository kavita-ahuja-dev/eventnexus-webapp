import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { isTokenExpired } from '../authBootstrap';


 const normalizeRole = (raw) => {
  if (!raw) return "";
  const r = String(raw).toUpperCase();
  return r.startsWith("ROLE_") ? r.slice(5) : r; // ROLE_ADMIN -> ADMIN
};

const ProtectedRoute = ({ allowedRoles }) => {
  const location = useLocation();

  // 1) Must have a valid, unexpired JWT
  const token = localStorage.getItem('token');
  if (!token || isTokenExpired(token)) {
    try {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
    } catch {}
    return <Navigate to="/login" replace state={{ from: location }} />;
    
  }

//  2) Role check if provided
 
  const role = normalizeRole(localStorage.getItem("role"));
 
  if (Array.isArray(allowedRoles) && allowedRoles.length > 0) {
    if (!allowedRoles.map((r) => r.toUpperCase()).includes(role)) {
       return <Navigate to="/unauthorized" replace />;
    }

  }
  // 3) Authorized → render the nested route
  return <Outlet />;
};

export default ProtectedRoute;
