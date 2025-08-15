// import React, { useState } from 'react';
// import { Link, useNavigate } from 'react-router-dom';
// import api, { setAuthToken } from '../api/api';

// const LoginPage = () => {
//   const [email, setEmail] = useState('');
//   const [password, setPassword] = useState('');
//   const [error, setError] = useState('');
//   const navigate = useNavigate();

//   const handleLogin = async (e) => {
//     e.preventDefault();
//     setError('');
//     try {
//       // Prefer JWT endpoint
//       let res;
//       try {
//         res = await api.post('/auth/login', { email, password });
//       } catch (err) {
//         // Fallback to legacy endpoint if present
//         if (err?.response?.status === 404) {
//           res = await api.post('/users/login', { email, password });
//         } else {
//           throw err;
//         }
//       }
//       const data = res?.data || {};
//       if (!data.token) {
//         throw new Error('No token returned from server');
//       }

//       localStorage.setItem('token', data.token);
//       setAuthToken(data.token);

//       if (data.role) localStorage.setItem('role', String(data.role).toUpperCase());
//       if (data.id) localStorage.setItem('userId', String(data.id));
//       if (data.username) localStorage.setItem('username', String(data.username));
//       if (data.customerId) localStorage.setItem('customerId', String(data.customerId));

//       // Basic role-based redirect (customize as needed)
//       const role = String(data.role || '').toUpperCase();
//       if (role === 'ADMIN') navigate('/admin/dashboard');
//       else if (role === 'EXHIBITOR') navigate('/exhibitor/dashboard');
//       else navigate('/customer/dashboard');
//     } catch (err) {
//       if (err?.response?.status === 400 || err?.response?.status === 403) {
//         if (err?.response?.data?.message?.toLowerCase?.().includes('inactive')) {
//           setError('Your account is inactive. Please contact support.');
//         } else {
//           setError('Invalid credentials.');
//         }
//       } else {
//         setError(err?.response?.data?.message || err?.message || 'Login failed. Please try again.');
//       }
//     }
//   };

//   const handleReset = () => {
//     setEmail('');
//     setPassword('');
//     setError('');
//   };

//   return (
//     <div style={{ border: '2px solid blue', width: 300, padding: 20, margin: '100px auto', borderRadius: 20 }}>
//       <h2>Login to EventNexus</h2>
//       {error && <p style={{ color: 'red' }}>{error}</p>}
//       <form onSubmit={handleLogin}>
//         <div style={{ marginBottom: 10 }}>
//           <label htmlFor="email">Email :</label><br />
//           <input
//             type="email"
//             id="email"
//             name="email"
//             value={email}
//             onChange={(e) => setEmail(e.target.value)}
//             required
//             minLength={5}
//             maxLength={100}
//             style={{ width: '100%' }}
//           />
//         </div>
//         <div style={{ marginBottom: 10 }}>
//           <label htmlFor="password">Password :</label><br />
//           <input
//             type="password"
//             id="password"
//             name="password"
//             value={password}
//             onChange={(e) => setPassword(e.target.value)}
//             required
//             minLength={3}
//             maxLength={50}
//             style={{ width: '100%' }}
//           />
//         </div>
//         <div style={{ marginTop: 10 }}>
//           <button type="submit">Login</button>&nbsp;&nbsp;
//           <button type="button" onClick={handleReset}>Cancel</button>
//         </div>
//         <div style={{ marginTop: 10 }}>
//           <Link to="/register">Register me?</Link>&nbsp;&nbsp;
//           <Link to="/forgot-password">Forgot password</Link>
//         </div>
//       </form>
//     </div>
//   );
// };

// export default LoginPage;
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api, { setAuthToken } from '../api/api';
import axios from 'axios';


const LoginPage = () => {


useEffect(() => {
  // Debug: prove it ran and what the token was
  console.log('[LoginPage] before clear token =', localStorage.getItem('token'));

  try {
    localStorage.clear();             // nuke everything for safety
  } catch {}

  // Nuke axios headers on BOTH instances
  setAuthToken(null);
  delete axios.defaults.headers?.common?.Authorization;
  delete api.defaults.headers?.common?.Authorization;

  // Debug: confirm it’s gone
  console.log('[LoginPage] after clear token =', localStorage.getItem('token'));

  // Optional: catch any sneaky re-set happening after mount
  const t = setTimeout(() => {
    console.log('[LoginPage] 200ms later token =', localStorage.getItem('token'));
  }, 200);
  return () => clearTimeout(t);
}, []);

// If user reaches the Login page, drop any old session
 useEffect(() => {
    try {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
    } catch {}
    setAuthToken(null);
  }, []);


  const [email, setEmail] = useState('');       // treat as usernameOrEmail field
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const normalizeRole = (raw) =>
    raw ? String(raw).toUpperCase().replace(/^ROLE_/, '') : '';

  const extractToken = (res) => {
    // Body first (handle common shapes)
    let t = res?.data?.token || res?.data?.jwt || res?.data?.access_token;
    if (!t) {
      // Fallback to Authorization header
      const h = res?.headers?.authorization || res?.headers?.Authorization;
      if (h && h.startsWith('Bearer ')) t = h.slice(7);
    }
    return t;
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    // Clear any stale auth before new login
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('customerId'); 
    setAuthToken(null);

    try {
      // IMPORTANT: ensure this path matches your backend
    //  const res = await api.post('/auth/login', {
      // const res = await api.post('/auth/login', {

      //   email: email, // <— align with your backend DTO
      //   password,
      // });


      const res = await api.post('/auth/login', { email, password });

      console.log('[LoginPage] url =', (res.config?.baseURL || '') + (res.config?.url || ''));
      console.log('[LoginPage] data =', res.data);
      console.log('[LoginPage] auth header =', res.headers?.authorization || res.headers?.Authorization);

      console.log('[LoginPage] status', res.status, res.data);

      const token = extractToken(res);
      if (!token || token.split('.').length !== 3) {
       // throw new Error('Login succeeded but no valid JWT returned from server.');
      
       setAuthToken(null);
        // wipe everything just in case
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        localStorage.removeItem('customerId'); 
        setError(res?.data?.error || res?.data?.message || 'Invalid credentials.');
        return; // <-- do not navigate
      }

      // Role extraction from several possible shapes
      const rawRole =
        res?.data?.role ||
        res?.data?.roles?.[0] ||
        res?.data?.authorities?.[0]?.authority ||
        '';

      const role = normalizeRole(rawRole);

      const customerId =
          res?.data?.customerId ??        // preferred: plain field
          res?.data?.customer?.id ??      // fallback: nested object
          null;

      // Optional extras if backend returns them
      const userId = res?.data?.id ?? res?.data?.userId ?? null;
      const username = res?.data?.username ?? null;

      // Persist + set default header
      localStorage.setItem('token', token);
      if (role) localStorage.setItem('role', role);
      if (userId) localStorage.setItem('userId', String(userId));
      if (username) localStorage.setItem('username', String(username));

        if (customerId != null) {
          localStorage.setItem('customerId', String(customerId));
        } else {
          localStorage.removeItem('customerId'); // clean if not available yet
        }

      setAuthToken(token); // sets axios Authorization: Bearer <token>

      // Redirect based on role (fallback to customer)
      if (role === 'ADMIN') navigate('/admin/dashboard');
      else if (role === 'EXHIBITOR') navigate('/exhibitor/dashboard');
      else navigate('/customer/dashboard');
    } catch (err) {
      // On any failure, ensure app is NOT “logged in”
      setAuthToken(null);
      localStorage.removeItem('token');
      localStorage.removeItem('role');

      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      localStorage.removeItem('customerId'); 

      const status = err?.response?.status;
      const respMsg = err?.response?.data?.message || err?.response?.data?.error || '';
      const lowerMsg = respMsg?.toLowerCase?.() || '';

      //      const msg =
      //   err?.response?.data?.message ||
      //   err?.response?.data?.error ||     // <-- backend uses "error"
      //   err?.message ||
      //   'Login failed. Please try again.';
      // setError(msg);

       // Earlier behavior: special-case 400/403 and "inactive"
        if (status === 400 || status === 403) {
          if (lowerMsg.includes('inactive')) {
            setError('Your account is inactive. Please contact support.');
          } else {
            setError('Invalid credentials.');
          }
        } else {
          // Generic fallback
          setError(respMsg || err?.message || 'Login failed. Please try again.');
        }
      
    }
  };

  const handleReset = () => {
    setEmail('');
    setPassword('');
    setError('');
  };

  return (
    <div style={{ border: '2px solid blue', width: 300, padding: 20, margin: '100px auto', borderRadius: 20 }}>
      <h2>Login to EventNexus</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleLogin}>
        <div style={{ marginBottom: 10 }}>
          <label htmlFor="email">Email or Username :</label><br />
          <input
            type="text"
            id="email"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            minLength={3}
            maxLength={100}
            style={{ width: '100%' }}
          />
        </div>
        <div style={{ marginBottom: 10 }}>
          <label htmlFor="password">Password :</label><br />
          <input
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={3}
            maxLength={50}
            style={{ width: '100%' }}
          />
        </div>
        <div style={{ marginTop: 10 }}>
          <button type="submit">Login</button>&nbsp;&nbsp;
          <button type="button" onClick={handleReset}>Cancel</button>
        </div>
        <div style={{ marginTop: 10 }}>
          <Link to="/register">Register me?</Link>&nbsp;&nbsp;
          <Link to="/forgot-password">Forgot password</Link>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;
