import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button, Table, Spinner, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const API_BASE = 'http://localhost:8080';

const MyEvents = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const exhibitorId = localStorage.getItem('userId'); 
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvents = async () => {
      if (!exhibitorId) {
        setError('User ID not found. Please login.');
        setLoading(false);
        return;
      }

      try {

        const response = await axios.get(`${API_BASE}/api/events?exhibitorId=${exhibitorId}`);

      //  const response = await axios.get(`http://localhost:8080/api/events?exhibitorId=${exhibitorId}`);
        setEvents(response.data);
      } catch (err) {
        console.error(err);
        setError('Failed to load events.');
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
  }, [exhibitorId]);

// handle delete
  const handleDelete = async (eventId) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this event?');
    if (!confirmDelete) return;

    try {
      await axios.delete(`${API_BASE}/api/events/${eventId}`);
      setEvents(events.filter(ev => ev.id !== eventId)); // update UI instantly
    } catch (err) {
      console.error(err);
      alert(err?.response?.data?.message || 'Failed to delete event.');
    }
  };


  return (
    <div className="container mt-4">
      <h2 className="mb-4">My Events</h2>

      {loading && <Spinner animation="border" />}
      {error && <Alert variant="danger">{error}</Alert>}

      {!loading && !error && (
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Title</th>
              <th>Date</th>
              <th>Type</th>
              <th>Mode</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {events.length > 0 ? (
              events.map(event => (
                <tr key={event.id}>
                  <td>{event.title}</td>
                  <td>{event.date}</td>
                  <td>{event.eventTypeName}</td>
                  <td>{event.mode}</td>
                  <td>
                            <Button
                              variant="warning"
                              size="sm"
                              className="me-2"
                              onClick={() => navigate(`/exhibitor/edit-event/${event.id}`)}
                              
                            >
                              Edit
                            </Button>


                    <Button variant="danger" size="sm" onClick={() => handleDelete(event.id)}>Delete</Button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" className="text-center">No events found</td>
              </tr>
            )}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default MyEvents;
