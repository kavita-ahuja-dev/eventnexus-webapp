import React from 'react';
import CustomerSummary from '../../components/summaries/CustomerSummary';
import MyRegisteredEvents from './MyRegisteredEvents';


import { Link } from 'react-router-dom';

const CustomerDashboard = () => {

  const username = localStorage.getItem('username');  
  
  return (
    <div className="container py-4">
      <h2 className="h4 fw-bold mb-4">Customer Dashboard</h2>
       <h2 className="h4 fw-bold mb-4">
        Welcome{username ? `, ${username}` : ''}!
      </h2>
      <marquee><h4 className="fw-bold mb-3">To continue with booking events customer should be approved by admin with additional details</h4></marquee>
       <h4 className="mt-4 mb-3">My Registered Events</h4>
      <MyRegisteredEvents />
      <div className="row mt-4">
        <div className="col-md-4 mb-3">
          <Link to="/customer/explore-events" className="btn btn-primary w-100">
            Explore Events
          </Link>
        </div>
        <div className="col-md-4 mb-3">
          <Link to="/customer/register-event" className="btn btn-success w-100">
            Register for Event
          </Link>
        </div>
        <div className="col-md-4 mb-3">
          <Link to="/customer/submit-feedback" className="btn btn-warning w-100">
            Submit Feedback
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;