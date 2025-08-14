import React from 'react';
//import ExhibitorSummary from './exhibitor/ExhibitorSummary';
import ExhibitorSummary from '../../components/summaries/ExhibitorSummary';

import { Link } from 'react-router-dom';

const ExhibitorDashboard = () => {
  return (
    <div className="container py-4">
      <h2 className="h4 fw-bold mb-4">Exhibitor Dashboard</h2>
      <ExhibitorSummary />
      <div className="row mt-4">
        <div className="col-md-4 mb-3">
          <Link to="/exhibitor/my-events" className="btn btn-primary w-100">
            My Events
          </Link>
        </div>
        <div className="col-md-4 mb-3">
          <Link to="/exhibitor/create-event" className="btn btn-success w-100">
            Create Event
          </Link>
        </div>
        <div className="col-md-4 mb-3">
          <Link to="/exhibitor/offers" className="btn btn-warning w-100">
            Offers for Events
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ExhibitorDashboard;
