import React from 'react';
import AdminSummary from '../../components/summaries/AdminSummary';

import { Link } from 'react-router-dom';

const AdminDashboard = () => {
  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Admin Dashboard</h2>
      <AdminSummary />
      <div align="center" className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
        <br/>
        <Link   onClick={(e) => e.preventDefault()} to="/admin/manage-users" className="block p-4 bg-blue-100 rounded shadow hover:bg-blue-200">
          Manage Users
        </Link>
        <Link to="/admin/manage-events" className="block p-4 bg-green-100 rounded shadow hover:bg-green-200">
          Manage Events
        </Link>
        <Link to="/admin/manage-event-types" className="block p-4 bg-yellow-100 rounded shadow hover:bg-yellow-200">
          Manage Event Types
        </Link>
      </div>
    </div>
  );
};

export default AdminDashboard;
