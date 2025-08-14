import React from 'react';

const EditProfile = () => {
  return (
    <div className="container mt-5">
      <h2>Edit Profile</h2>
      <form>
        <div className="mb-3">
          <label className="form-label">Name</label>
          <input type="text" className="form-control" defaultValue="John Doe" />
        </div>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input type="email" className="form-control" defaultValue="john@example.com" />
        </div>
        <button type="submit" className="btn btn-primary">Update</button>
      </form>
    </div>
  );
};
export default EditProfile;
