import React, { useEffect, useState } from "react";
import { Table, Spinner, Alert } from "react-bootstrap";
import api from "../../api/api";

const fmtDateTime = (iso) => {
  if (!iso) return "-";
  const s = String(iso);
  return s.includes("T") ? s.replace("T", " ").slice(0, 19) : s.slice(0, 19);
};

const MyRegisteredEvents = () => {
  // Read from localStorage first
  const token = localStorage.getItem("token") || "";
  const role = (localStorage.getItem("role") || "").replace(/^ROLE_/, "").toUpperCase();
  const customerId = localStorage.getItem("customerId");
  const allowed = !!token && role === "CUSTOMER" && !!customerId;

  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  useEffect(() => {
    let cancelled = false;

    if (!allowed) {
      setLoading(false);
      return () => { cancelled = true; };
    }

    api
      .get(`/payments/my/${customerId}`)
      .then((res) => {
        if (!cancelled) setEvents(res.data || []);
      })
      .catch((e) => {
        console.error("Failed to load registered events:", e);
        if (!cancelled) setErr("Could not load registered events.");
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => { cancelled = true; };
  }, [allowed, customerId]);

  // If not allowed, don't render anything
  if (!allowed) return null;

  if (loading) {
    return (
      <div className="d-flex align-items-center gap-2">
        <Spinner animation="border" size="sm" />
        <span>Loading registered events…</span>
      </div>
    );
  }

  if (err) return <Alert variant="danger">{err}</Alert>;
  if (!events.length) return <Alert variant="info">You haven’t registered for any events yet.</Alert>;

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
        {events.map((ev, idx) => (
          <tr key={ev.registrationId ?? ev.paymentId ?? `${ev.eventTitle}-${idx}`}>
            <td>{ev.eventTitle ?? "-"}</td>
            <td>{ev.paymentMode ?? "-"}</td>
            <td>{fmtDateTime(ev.paymentDate)}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default MyRegisteredEvents;
