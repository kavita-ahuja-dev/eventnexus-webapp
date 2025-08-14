import React, { useEffect, useState, useMemo } from 'react';
import { Container, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

const RegisterEvent = () => {
  const [events, setEvents] = useState([]);
  const [eventsLoading, setEventsLoading] = useState(true);
  const [eventsError, setEventsError] = useState('');

  
  const [paymentModes, setPaymentModes] = useState([]);      
  const [pmLoading, setPmLoading] = useState(true);           
  const [pmError, setPmError] = useState('');

  const navigate = useNavigate(); 


  const [eventId, setEventId] = useState('');
  const [customerId, setCustomerId] = useState('');
  //const [paymentMode, setPaymentMode] = useState('FREE');
  const [paymentMode, setPaymentMode] = useState('');

  //Added for transactionId
  const [transactionId, setTransactionId] = useState('');        

  const [submitting, setSubmitting] = useState(false);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');

   // Take logged-in customer ID from localStorage
  const storedCustomerId = localStorage.getItem('customerId');


  // Load events for dropdown
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

   //Added 08-08-2025 for loading payment modes option

  // Load payment modes from API
  useEffect(() => {
    let cancelled = false;
    setPmLoading(true);
    setPmError('');

    axios.get(`${API_BASE}/api/payments/payment-modes`)
      .then(res => {
        if (cancelled) return;

        const modes = Array.isArray(res.data) ? res.data : [];
        setPaymentModes(modes);
        // set default if not chosen yet
        if (!paymentMode && modes.length > 0) {
          setPaymentMode(modes[0]);
        }
      })
      .catch(e => {
        console.error('Failed to load payment modes:', e);
        if (!cancelled) setPmError('Could not load payment modes.');
      })
      .finally(() => !cancelled && setPmLoading(false));

    return () => { cancelled = true; };
  }, []);
  //[paymentMode]);


const selectedEvent = useMemo(
    () => events.find(ev => String(ev.id) === String(eventId)) || null,
    [events, eventId]
  );
  const isFreeEvent = (ev) => {
    const p = ev?.price;
    const n = p == null ? 0 : Number(p);
    return !Number.isNaN(n) && n === 0;
  };
  const free = isFreeEvent(selectedEvent);

  useEffect(() => {
    if (!selectedEvent) {
      setPaymentMode('');
      return;
    }
    if (free) {
      setPaymentMode('FREE'); // lock to FREE
    } else if (paymentMode === 'FREE') {

      const firstPaid = paymentModes.find(m => m !== 'FREE') || '';
      setPaymentMode(firstPaid);
    }
  }, [eventId, selectedEvent, free, paymentModes]); 


  // useEffect to clear transactionId when not needed
useEffect(() => {
  const needsTxnId = !free && ['GOOGLEPAY', 'CREDITCARD', 'DEBITCARD'].includes(paymentMode);
  if (!needsTxnId) setTransactionId('');
}, [free, paymentMode]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');
    setErr('');

    if (!eventId) {
      setErr('Please select an event and enter your Customer ID.');
      return;
    }

     if (!storedCustomerId) {
      setErr('No customer is logged in. Please log in again.');
      return;
    }

    if (!paymentMode) {
      setErr('Please select a payment mode.');
      return;
    }

     if (free && paymentMode !== 'FREE') {
      setErr('Paid methods are not allowed for a free event.');
      return;
    }
    if (!free && paymentMode === 'FREE') {
      setErr('Free option is not allowed for a paid event.');
      return;
    }

    //require transactionId for paid modes only but excluding cash
    const needsTxnId = !free && (paymentMode !== 'FREE' && paymentMode !== 'CASH');
    if (needsTxnId && (!transactionId || transactionId.trim().length < 6)) {
      setErr('Please enter a valid Transaction ID (min 6 characters).');
      return;
    }

    setSubmitting(true);
    try {
      // await axios.post(`${API_BASE}/api/event-registrations`, {
      await axios.post(`${API_BASE}/api/event-registrations/test`, {

        eventId: Number(eventId),
        customerId: Number(storedCustomerId),
        //testing code
       // amount: 0,   
        paymentMode,
        //Added for transactionId on 10-08-2025
        transactionId: needsTxnId ? transactionId.trim() : null   // NEW

      });

      setMsg('Registered successfully!');
      setTimeout(() => {
        navigate('/customer/dashboard');  
      }, 1500);
      // Reset form (keep events list)
      setEventId('');
      setCustomerId('');
      setPaymentMode(paymentModes[0] || '');
      setTransactionId('');                                      

    } catch (e) {
      console.error('Registration failed:', e);

      setErr(
        e?.response?.data?.message
          || 'Registration failed. Check the Event and Customer IDs and try again.'
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Register for Event</h2>

      {msg && <Alert variant="success">{msg}</Alert>}
      {(err || eventsError) && <Alert variant="danger">{err || eventsError}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Select Event</Form.Label>
          {eventsLoading ? (
            <div className="d-flex align-items-center gap-2">
              <Spinner animation="border" size="sm" />
              <span>Loading events…</span>
            </div>
          ) : (
            <Form.Select
              value={eventId}
              onChange={(e) => setEventId(e.target.value)}
              required
              disabled={events.length === 0}
            >
              <option value="">-- Select an event --</option>
              {events.map((ev) => (
                <option key={ev.id} value={ev.id}>
                  {ev.title}
                </option>
              ))}
            </Form.Select>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Payment Mode</Form.Label>
          <Form.Select
            value={paymentMode}
            onChange={(e) => setPaymentMode(e.target.value)}
            //Added
             required
             disabled={paymentModes.length === 0}
          >
            {/* added for payment mode */}
            {paymentModes.length === 0 && <option value="">-- No modes --</option>}
             {paymentModes.map((mode) => (
                <option key={mode} value={mode}>
                  {formatLabel(mode)}
                </option>
              ))}

            {/* <option value="FREE">FREE</option>
            <option value="CREDIT_CARD">Credit Card</option>
            <option value="UPI">UPI</option> */}
          </Form.Select>
        </Form.Group>

         {/* NEW: Transaction ID field only for paid modes */}
        {/* {!free && paymentMode && paymentMode !== 'FREE' && ( */}

          {!free && paymentMode && paymentMode !== 'FREE' && paymentMode !== 'CASH' && (
          <Form.Group className="mb-3">
            <Form.Label>Transaction ID</Form.Label>
            <Form.Control
              type="text"
              value={transactionId}
              onChange={(e) => setTransactionId(e.target.value)}
              placeholder={
                paymentMode.includes('UPI') || paymentMode.includes('GPAY')
                  ? 'Enter UTR / GPay transaction ID'
                  : 'Enter Card Transaction ID / Auth Code'
              }
              required
            />
          </Form.Group>
        )}

        <Button variant="success" type="submit" disabled={submitting || eventsLoading}>
          {submitting ? 'Registering…' : 'Register'}
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


function formatLabel(code) {
  return code
    .split('_')
    .map(w => w.charAt(0) + w.slice(1).toLowerCase())
    .join(' ');
}

export default RegisterEvent;
