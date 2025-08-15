import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import LoginPage from './pages/LoginPage';
import Register from './pages/Auth/Register';
import ForgotPassword from './pages/ForgotPassword';

import AdminDashboard from './pages/admin/AdminDashboard';
import ManageUsers from './pages/admin/ManageUsers';
import ManageEvents from './pages/admin/ManageEvents';
import ManageEventTypes from './pages/admin/ManageEventTypes';
import AdminEditEvent from './pages/admin/AdminEditEvent';

import ExhibitorDashboard from './pages/exhibitor/ExhibitorDashboard';
import MyEvents from './pages/exhibitor/MyEvents';
import CreateEvent from './pages/exhibitor/CreateEvent';
import OffersForEvent from './pages/exhibitor/OffersForEvent';
import EditEvent from './pages/exhibitor/EditEvent';

import CustomerDashboard from './pages/customer/CustomerDashboard';
import ExploreEvents from './pages/customer/ExploreEvents';
import RegisterEvent from './pages/customer/RegisterEvent';
import SubmitFeedback from './pages/customer/SubmitFeedback';
import EventDetails from "./pages/customer/EventDetails";

import ViewProfile from './pages/profile/ViewProfile';
import EditProfile from './pages/profile/EditProfile';

import Unauthorized from './components/Unauthorized';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';

const App = () => {
  return (
    <>
      <Navbar />
      <Routes>
        {/* Public */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/unauthorized" element={<Unauthorized />} />

        {/* Profile — any authenticated user */}
        <Route element={<ProtectedRoute />}>
          <Route path="/view-profile" element={<ViewProfile />} />
          <Route path="/edit-profile" element={<EditProfile />} />
        </Route>

        {/* Admin-only */}
        <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
          <Route path="/admin/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/manage-users" element={<ManageUsers />} />
          <Route path="/admin/manage-events" element={<ManageEvents />} />
          <Route path="/admin/manage-event-types" element={<ManageEventTypes />} />
          <Route path="/admin/events/:id/edit" element={<AdminEditEvent />} />
        </Route>

        {/* Exhibitor-only */}
        <Route element={<ProtectedRoute allowedRoles={['EXHIBITOR']} />}>
          <Route path="/exhibitor/dashboard" element={<ExhibitorDashboard />} />
          <Route path="/exhibitor/my-events" element={<MyEvents />} />
          <Route path="/exhibitor/create-event" element={<CreateEvent />} />
          <Route path="/exhibitor/offers" element={<OffersForEvent />} />
          <Route path="/exhibitor/edit-event/:id" element={<EditEvent />} />
        </Route>

        {/* Customer-only */}
        <Route element={<ProtectedRoute allowedRoles={['CUSTOMER']} />}>
          <Route path="/customer/dashboard" element={<CustomerDashboard />} />
          <Route path="/customer/explore-events" element={<ExploreEvents />} />
          <Route path="/customer/register-event" element={<RegisterEvent />} />
          <Route path="/customer/submit-feedback" element={<SubmitFeedback />} />
          <Route path="/events/:id" element={<EventDetails />} />
        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </>
  );
};

export default App;

