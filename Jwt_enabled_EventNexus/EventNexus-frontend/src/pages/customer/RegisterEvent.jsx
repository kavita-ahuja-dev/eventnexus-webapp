import React, { useEffect, useState, useMemo } from "react";
import { Container, Form, Button, Alert, Spinner } from "react-bootstrap";
import { useNavigate, Link } from "react-router-dom";
import api from "../../api/api";

const normalizeRole = (raw) => {
  if (!raw) return "";
  const r = String(raw).toUpperCase();
  return r.startsWith("ROLE_") ? r.slice(5) : r; // ROLE_CUSTOMER -> CUSTOMER
};

function formatLabel(code) {
  return String(code)
    .split("_")
    .map((w) => w.charAt(0) + w.slice(1).toLowerCase())
    .join(" ");
}

const RegisterEvent = () => {

  const token = localStorage.getItem("token") || "";
  const role = normalizeRole(localStorage.getItem("role"));
  const storedCustomerId = localStorage.getItem("customerId"); // used previously
  const allowed = !!token && role === "CUSTOMER";

  // ---- State (hooks must be unconditional)
  const [events, setEvents] = useState([]);
  const [eventsLoading, setEventsLoading] = useState(true);
  const [eventsError, setEventsError] = useState("");

  const [paymentModes, setPaymentModes] = useState([]);
  const [pmLoading, setPmLoading] = useState(true);
  const [pmError, setPmError] = useState("");

  const [eventId, setEventId] = useState("");
  const [customerId, setCustomerId] = useState(""); // kept (unused)
  const [paymentMode, setPaymentMode] = useState("");
  const [transactionId, setTransactionId] = useState("");

  const [submitting, setSubmitting] = useState(false);
  const [msg, setMsg] = useState("");
  const [err, setErr] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    let cancelled = false;
    setEventsLoading(true);
    setEventsError("");

    if (!allowed) {
      setEventsLoading(false);
      return () => { cancelled = true; };
    }

    api
      .get("/events")
      .then((res) => {
        if (cancelled) return;
        setEvents(Array.isArray(res.data) ? res.data : []);
      })
      .catch((e) => {
        if (cancelled) return;
        console.error("Failed to load events:", e);
        setEventsError("Could not load events. Please try again.");
      })
      .finally(() => !cancelled && setEventsLoading(false));

    return () => { cancelled = true; };
  }, [allowed]);

  useEffect(() => {
    let cancelled = false;
    setPmLoading(true);
    setPmError("");

    if (!allowed) {
      setPmLoading(false);
      return () => { cancelled = true; };
    }

    api
      .get("/payments/payment-modes")
      .then((res) => {
        if (cancelled) return;
        const modes = Array.isArray(res.data) ? res.data : [];
        setPaymentModes(modes);
      })
      .catch((e) => {
        if (cancelled) return;
        console.error("Failed to load payment modes:", e);
        setPmError("Could not load payment modes.");
      })
      .finally(() => !cancelled && setPmLoading(false));

    return () => { cancelled = true; };
  }, [allowed]);

  // ---- Derived
  const selectedEvent = useMemo(
    () => events.find((ev) => String(ev.id) === String(eventId)) || null,
    [events, eventId]
  );

  const free = !!selectedEvent && Number(selectedEvent?.price ?? 0) === 0;

  // ---- One authoritative effect to set/lock payment mode (NO flicker)
  useEffect(() => {
    if (!selectedEvent) {
      if (paymentMode !== "") setPaymentMode("");
      return;
    }

    if (free) {
      if (paymentMode !== "FREE") setPaymentMode("FREE");
      return;
    }

    // Paid event: choose first non-FREE mode if current is invalid/empty/FREE
    const firstPaid = paymentModes.find((m) => m !== "FREE") || "";
    const invalid =
      paymentMode === "" || paymentMode === "FREE" || !paymentModes.includes(paymentMode);

    if (invalid && paymentMode !== firstPaid) {
      setPaymentMode(firstPaid);
    }
    // note: do NOT include paymentMode in deps to avoid loops
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedEvent, free, paymentModes]);

  // ---- Clear txn id when not needed
  useEffect(() => {
    const needsTxnId =
      !free && paymentMode && paymentMode !== "FREE" && paymentMode !== "CASH";
    if (!needsTxnId && transactionId !== "") setTransactionId("");
  }, [free, paymentMode, transactionId]);

  // ---- Submit (minimal change: read customerId fresh)
  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setErr("");

    if (!allowed) {
      setErr("Please log in as a Customer to register for events.");
      return;
    }

    // 🔑 Minimal change: read latest customerId right now
    const freshCustomerId = localStorage.getItem("customerId") || "";

    if (!freshCustomerId) {
      setErr("Missing customer ID. Please log in again.");
      return;
    }
    if (!eventId) {
      setErr("Please select an event.");
      return;
    }
    if (!paymentMode) {
      setErr("Please select a payment mode.");
      return;
    }
    if (free && paymentMode !== "FREE") {
      setErr("Paid methods are not allowed for a free event.");
      return;
    }
    if (!free && paymentMode === "FREE") {
      setErr("Free option is not allowed for a paid event.");
      return;
    }

    const needsTxnId =
      !free && paymentMode !== "FREE" && paymentMode !== "CASH";
    if (needsTxnId && (!transactionId || transactionId.trim().length < 6)) {
      setErr("Please enter a valid Transaction ID (min 6 characters).");
      return;
    }

    setSubmitting(true);
    try {
      // keep your endpoint name:
      await api.post(`/event-registrations/test`, {
        eventId: Number(eventId),
        customerId: Number(freshCustomerId), // ✅ use the fresh value
        paymentMode,
        transactionId: needsTxnId ? transactionId.trim() : null,
      });

      setMsg("Registered successfully!");
      setTimeout(() => navigate("/customer/dashboard"), 1500);

      // reset fields (keep loaded lists)
      setEventId("");
      setCustomerId("");
      setPaymentMode("");
      setTransactionId("");
    } catch (e2) {
      console.error("Registration failed:", e2);
      setErr(
        e2?.response?.data?.message ||
          "Registration failed. Check the Event and Customer IDs and try again."
      );
    } finally {
      setSubmitting(false);
    }
  };

  // ---- Render
  if (!allowed) {
    return (
      <Container className="mt-4">
        <Alert variant="warning" className="mb-3">
          Please log in as a <strong>Customer</strong> to register for events.
        </Alert>
        <Link to="/login">Go to Login</Link>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Register for Event</h2>

      {msg && <Alert variant="success">{msg}</Alert>}
      {(err || eventsError || pmError) && (
        <Alert variant="danger">{err || eventsError || pmError}</Alert>
      )}

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
            required
            disabled={pmLoading || paymentModes.length === 0 || free} // locked on FREE events
          >
            {paymentModes.length === 0 && <option value="">-- No modes --</option>}
            {paymentModes.map((mode) => (
              <option key={mode} value={mode}>
                {formatLabel(mode)}
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        {!free && paymentMode && paymentMode !== "FREE" && paymentMode !== "CASH" && (
          <Form.Group className="mb-3">
            <Form.Label>Transaction ID</Form.Label>
            <Form.Control
              type="text"
              value={transactionId}
              onChange={(e) => setTransactionId(e.target.value)}
              placeholder={
                paymentMode.includes("UPI") || paymentMode.includes("GPAY")
                  ? "Enter UTR / GPay transaction ID"
                  : "Enter Card Transaction ID / Auth Code"
              }
              required
            />
          </Form.Group>
        )}

        <Button
          variant="success"
          type="submit"
          disabled={submitting || eventsLoading || pmLoading}
        >
          {submitting ? "Registering…" : "Register"}
        </Button>
      </Form>

      <div className="mt-3" style={{ textAlign: "right" }}>
        <Link to="/customer/dashboard" className="bg-green-100 rounded px-2 py-1">
          Go to Customer Dashboard
        </Link>
      </div>
    </Container>
  );
};

export default RegisterEvent;
