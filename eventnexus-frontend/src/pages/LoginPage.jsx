
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
   const navigate = useNavigate(); 

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // const res = await axios.post("http://localhost:8080/api/auth/login", {
      //   email,
      //   password
      // });

      const res = await axios.post("http://localhost:8080/api/users/login", {
        email,
        password
      });

// Handle Optional<UserResponseDto> response
        const user = res.data;
        console.log("User response:", user);
        console.log("User response:", user.id);
  

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


       const role = res.data.role?.toUpperCase();

      if (role === "CUSTOMER") {
        navigate("/customer/dashboard");
      } else if (role === "EXHIBITOR") {
        navigate("/exhibitor/dashboard");
      } else if (role === "ADMIN") {
        navigate("/admin/dashboard");
      } else {
        setError("Unrecognized user role");
      }

    } catch (err) {
     // setError("Invalid credentials");
            if (err.response?.status === 400 || err.response?.status === 403) {
            // Backend threw IllegalStateException for inactive accounts
            if (err.response?.data?.message?.includes("inactive")) {
              setError("Your account is inactive. Please contact support.");
            } else {
              setError("Invalid credentials.");
            }
          } else {
            setError("Login failed. Please try again later.");
          }
    }
  };

  const handleReset = () => {
    setEmail('');
    setPassword('');
    setError('');
  };

  return (
    <div style={{
      border: "2px solid blue",
      width: "300px",
      padding: "20px",
      margin: "100px auto",
      borderRadius: "20px"
    }}>
      <h2>Login to EventNexus</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleLogin}>
        <div style={{ marginBottom: "10px" }}>
          <label htmlFor="email">Email :</label><br />
          <input
            type="email"
            id="email"
            name="email"
            value={email}
            required
            minLength="5"
            maxLength="50"
            onChange={(e) => setEmail(e.target.value)}
            autoFocus
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label htmlFor="passwd">Password :</label><br />
          <input
            type="password"
            id="passwd"
            name="password"
            value={password}
            required
            minLength="5"
            maxLength="20"
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div>
          <button type="submit">Login</button>&nbsp;&nbsp;
          <button type="button" onClick={handleReset}>Cancel</button>
        </div>

        <div style={{ marginTop: "10px" }}>
          <Link to="/register">Register me?</Link>&nbsp;&nbsp;
          <Link to="/forgot-password">Forgot password</Link>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;
