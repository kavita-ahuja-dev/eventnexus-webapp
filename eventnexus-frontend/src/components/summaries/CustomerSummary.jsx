import { useEffect, useState } from "react";
import axios from "axios";

const API_BASE = 'http://localhost:8080';

export default function CustomerSummary() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const res = await axios.get(`${API_BASE}/api/customer/dashboard`, {
          // headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        });
        setData(res.data);
      } catch (err) {
        const status = err?.response?.status;
        const msg = err?.response?.data?.message || err?.message;
        setError(`Error ${status ?? ""}: ${msg}`);
        console.error("Customer summary error:", err?.response || err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <div>Loading dashboard…</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (!data) return null;

  return (
    <div className="container" style={{ padding: 16 }}>
      <h2>Customer Summary</h2>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))", gap: 16 }}>
        <Card title="Registered Events" value={data.registeredEvents} />
        <Card title="Upcoming" value={data.upcomingRegisteredEvents} />
        <Card title="Past" value={data.pastRegisteredEvents} />
      </div>
    </div>
  );
}

function Card({ title, value }) {
  return (
    <div style={{ border: "1px solid #eee", borderRadius: 12, padding: 16, boxShadow: "0 2px 8px rgba(0,0,0,0.06)" }}>
      <div style={{ fontSize: 14, opacity: 0.7 }}>{title}</div>
      <div style={{ fontSize: 28, fontWeight: 700, marginTop: 6 }}>{value}</div>
    </div>
  );
}
