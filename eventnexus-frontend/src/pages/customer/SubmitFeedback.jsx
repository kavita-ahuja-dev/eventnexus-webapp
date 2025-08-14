import React, { useEffect, useState } from 'react';
import { Container, Form, Button, Alert, Spinner } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

const API_BASE = 'http://localhost:8080';

const SubmitFeedback = () => {
  const [events, setEvents] = useState([]);
  const [eventsLoading, setEventsLoading] = useState(true);
  const [eventsError, setEventsError] = useState('');

  const navigate = useNavigate();

  const [eventId, setEventId] = useState('');
  const [rating, setRating] = useState('');
  const [comment, setComment] = useState('');

  const [submitting, setSubmitting] = useState(false);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');

  const storedCustomerId = localStorage.getItem('customerId'); 

  // Load events for dropdown (mirrors your RegisterEvent logic)
  useEffect(() => {
    let cancelled = false;
    setEventsLoading(true);
    setEventsError('');

    axios
      .get(`${API_BASE}/api/events`)
      .then((res) => {
        if (cancelled) return;
        setEvents(res.data || []);
      })
      .catch((e) => {
        console.error('Failed to load events:', e);
        if (cancelled) return;
        setEventsError('Could not load events. Please try again.');
      })
      .finally(() => !cancelled && setEventsLoading(false));

    return () => { cancelled = true; };
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');
    setErr('');

    if (!eventId) {
      setErr('Please select an event.');
      return;
    }
    if (!rating) {
      setErr('Please select a rating.');
      return;
    }
    if (comment.trim().length < 10) {
      setErr('Feedback must be at least 10 characters.');
      return;
    }

    setSubmitting(true);

    //submit feedback code

    try {
     // const token = localStorage.getItem('token'); 
      await axios.post(
        `${API_BASE}/api/feedbacks`,
        {
          userId: Number(storedCustomerId),  
          eventId: Number(eventId),
          rating: Number(rating),
          comment: comment.trim(),
        },
        {
          headers: {
            'Content-Type': 'application/json',
            //...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
        }
      );

      setMsg('Thanks! Your feedback has been submitted.');
      setEventId('');
      setRating('');
      setComment('');

      //navigation after success:
       setTimeout(() => {
        navigate('/customer/dashboard');  
      }, 1500); 

    } catch (e) {

        const status = e?.response?.status;
        const apiMsg = e?.response?.data?.message;
          if (status === 409) {
            setErr(apiMsg || 'You already submitted feedback for this event.');
               setTimeout(() => {
                navigate('/customer/dashboard');  
              }, 2000);
          } else {
            setErr(apiMsg || 'Failed to submit feedback. Please try again.');
          }
       
    } finally {
      setSubmitting(false);
    }
  
  };

  const isDisabled = submitting || eventsLoading;

  return (
    <Container className="mt-4">
      <h2 className="mb-3">Submit Feedback</h2>

      {msg && <Alert variant="success" onClose={() => setMsg('')} dismissible>{msg}</Alert>}
      {(err || eventsError) && (
        <Alert variant="danger" onClose={() => setErr('')} dismissible>
          {err || eventsError}
        </Alert>
      )}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Event</Form.Label>

          {eventsLoading ? (
            <div className="d-flex justify-content-center align-items-center" style={{ height: 40 }}>
              <Spinner animation="border" size="sm" role="status" />
              <span className="ms-2">Loading events…</span>
            </div>
          ) : (
            <Form.Select
              value={eventId}
              onChange={(e) => setEventId(e.target.value)}
              disabled={isDisabled || events.length === 0}
              required
            >
              <option value="">Select an event</option>
              {events.map((ev) => (
                <option key={ev.id} value={ev.id}>
                  {ev.title ?? ev.name ?? `Event #${ev.id}`}
                </option>
              ))}
            </Form.Select>
          )}

          {!eventsLoading && events.length === 0 && (
            <Form.Text className="text-muted">
              No events available.
            </Form.Text>
          )}
        </Form.Group>

        <Form.Group className="mb-3" controlId="rating">
          <Form.Label>Rating</Form.Label>
          <Form.Select
            value={rating}
            onChange={(e) => setRating(e.target.value)}
            disabled={isDisabled}
            required
          >
            <option value="">Select rating</option>
            <option value="5">★★★★★ (5)</option>
            <option value="4">★★★★☆ (4)</option>
            <option value="3">★★★☆☆ (3)</option>
            <option value="2">★★☆☆☆ (2)</option>
            <option value="1">★☆☆☆☆ (1)</option>
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3" controlId="comment">
          <Form.Label>Feedback</Form.Label>
          <Form.Control
            as="textarea"
            rows={4}
            placeholder="What did you like? What could be improved?"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            disabled={isDisabled}
            required
          />
          <Form.Text className="text-muted">Minimum 10 characters.</Form.Text>
        </Form.Group>

        <Button type="submit" variant="primary" disabled={isDisabled}>
          {submitting ? (
            <>
              <Spinner animation="border" size="sm" className="me-2" />
              Submitting…
            </>
          ) : (
            'Submit'
          )}
        </Button>
      </Form>
            <div align="right">
                <Link to="/customer/dashboard" className=" bg-green-100 rounded hover:bg-green-200" align="right">
                      Go to Customer Dashboard
                    </Link>
            </div>
    </Container>
  );
};

export default SubmitFeedback;
