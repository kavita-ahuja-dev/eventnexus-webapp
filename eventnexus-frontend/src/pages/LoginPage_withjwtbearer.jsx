import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const normalizeRole = (raw) => {
    if (!raw) return '';
    const r = String(raw).toUpperCase();
    return r.startsWith('ROLE_') ? r.substring(5) : r; // ROLE_ADMIN -> ADMIN
  };

   const clearAuth = () => {
    ['token', 'role', 'userId', 'customerId', 'username'].forEach(k => localStorage.removeItem(k));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    clearAuth(); // clear any stale auth before attempting login

    try {
      console.log('Attempting login via', `${API_BASE}/auth/login`);
      const res = await axios.post(`${API_BASE}/auth/login`, {
        email,
        username: email,
        password
      });

      const { token, role, id, username } = res.data || {};

      if (!token || !role) {
        setError('Login failed: missing token/role in response.');
        return;
      }

      // Persist auth
    //   localStorage.setItem('token', token);
    //   localStorage.setItem('role', role);
    //   if (id) {
    //     localStorage.setItem('userId', id);
    //     localStorage.setItem('customerId', id); // if your app expects this
    //   }
    //   if (username) {
    //     localStorage.setItem('username', username);
    //   }


 const user = res.data;

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role); // or username
      localStorage.setItem("userId", res.data.id); 

      // store customerId for later use in RegisterEvent
      localStorage.setItem("customerId", res.data.id);
      console.log(res.data);

      //for username

      localStorage.setItem("username", res.data.username);  

    
      console.log("Login response:", res.data);
      localStorage.getItem("role");


       //const role = res.data.role?.toUpperCase();

      
      // Route by role
      const roleNorm = normalizeRole(role); // ADMIN | EXHIBITOR | CUSTOMER
      if (roleNorm === 'CUSTOMER') {
        navigate('/customer/dashboard');
      } else if (roleNorm === 'EXHIBITOR') {
        navigate('/exhibitor/dashboard');
      } else if (roleNorm === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        setError('Unrecognized user role.');
      }
    } catch (err) {
      const status = err.response?.status;
      const msg = (err.response?.data?.message || '').toLowerCase();

      if (status === 400 || status === 401 || status === 403) {
        if (msg.includes('inactive')) {
          setError('Your account is inactive. Please contact support.');
        } else if (msg.includes('bad credentials')) {
          setError('Invalid credentials.');
        } else {
          setError(err.response?.data?.message || 'Invalid credentials.');
        }
      } else {
        setError('Login failed. Please try again later.');
      }
      console.log('Login error:', status, err.response?.data || err.message);
    }
  };

  const handleReset = () => {
    setEmail('');
    setPassword('');
    setError('');
  };

  return (
    <div style={{
      border: '2px solid blue',
      width: '300px',
      padding: '20px',
      margin: '100px auto',
      borderRadius: '20px'
    }}>
      <h2>Login to EventNexus</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <form onSubmit={handleLogin}>
        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="email">Email :</label><br />
          <input
            type="email"
            id="email"
            name="email"
            value={email}
            required
            minLength={5}
            maxLength={50}
            onChange={(e) => setEmail(e.target.value)}
            autoFocus
          />
        </div>

        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="passwd">Password :</label><br />
          <input
            type="password"
            id="passwd"
            name="password"
            value={password}
            required
            minLength={5}
            maxLength={20}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div>
          <button type="submit">Login</button>&nbsp;&nbsp;
          <button type="button" onClick={handleReset}>Cancel</button>
        </div>

        <div style={{ marginTop: '10px' }}>
          <Link to="/register">Register me?</Link>&nbsp;&nbsp;
          <Link to="/forgot-password">Forgot password</Link>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;