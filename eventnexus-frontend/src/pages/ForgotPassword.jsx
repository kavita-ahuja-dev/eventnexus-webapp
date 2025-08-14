import React from 'react';

const ForgotPassword = () => {
  return (
    <div className="container mt-5">
      <h2>Forgot Password</h2>
      <form>
        <div className="mb-3">
          <label className="form-label">Enter your email</label>
          <input type="email" className="form-control" />
        </div>
        <button type="submit" className="btn btn-warning">Send Reset Link</button>
      </form>
    </div>
  );
};
export default ForgotPassword;
