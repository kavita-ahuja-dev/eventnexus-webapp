import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const getUserFromToken = () => {
  try {
    const token = localStorage.getItem('token');
    if (!token) return null;

    const base64Url = token.split('.')[1];
    const payload = JSON.parse(atob(base64Url));
    return payload;
  } catch (error) {
    return null;
  }
};

const ProtectedRoute = ({ allowedRoles }) => {
  const user = getUserFromToken();

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (!allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
