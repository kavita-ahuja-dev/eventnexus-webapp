import React, { useEffect, useState } from 'react';
import { Table, Spinner, Alert } from 'react-bootstrap';
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

const MyRegisteredEvents = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  const customerId = localStorage.getItem('customerId');

  useEffect(() => {
    if (!customerId) {
      setErr('No customer logged in.');
      setLoading(false);
      return;
    }

    axios
      // .get(`${API_BASE}/api/event-registrations/customer/${customerId}`)
        .get(`${API_BASE}/api/payments/my/${customerId}`)

      .then((res) => {
        setEvents(res.data || []);
      })
      .catch((e) => {
        console.error('Failed to load registered events:', e);
        setErr('Could not load registered events.');
      })
      .finally(() => setLoading(false));
  }, [customerId]);

  if (loading) {
    return (
      <div className="d-flex align-items-center gap-2">
        <Spinner animation="border" size="sm" />
        <span>Loading registered events…</span>
      </div>
    );
  }

  if (err) {
    return <Alert variant="danger">{err}</Alert>;
  }

  if (events.length === 0) {
    return <Alert variant="info">You have not registered for any events yet.</Alert>;
  }

  return (
    <Table striped bordered hover responsive>
      <thead>
        <tr>
          <th>Event Title</th>
          <th>Payment Mode</th>
          <th>Registered On</th>
        </tr>
      </thead>
      <tbody>
          {events.map((ev) => (
            <tr key={ev.eventTitle}>
              <td>{ev.eventTitle}</td>
              <td>{ev.paymentMode}</td>
              <td>{ev.paymentDate?.replace('T', ' ').slice(0, 19) ?? '-'}</td>
            </tr>
          ))}
      </tbody>
    </Table>
  );
};

export default MyRegisteredEvents;
