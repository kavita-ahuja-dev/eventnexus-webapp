import { useEffect, useState } from "react";
import axios from "axios";

const API_BASE = 'http://localhost:8080';

export default function ExhibitorSummary() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Get exhibitor ID from localStorage (or context/auth)
  const exhibitorId = localStorage.getItem("userId");

  useEffect(() => {
    if (!exhibitorId) {
      setError("Exhibitor ID not found. Please log in.");
      setLoading(false);
      return;
    }

    const fetchSummary = async () => {
      try {
        const res = await axios.get(
          `${API_BASE}/api/exhibitors/dashboard/${exhibitorId}`,
          {
            // headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
          }
        );
        setData(res.data);
      } catch (err) {
        setError(err?.response?.data?.message || "Failed to load summary");
      } finally {
        setLoading(false);
      }
    };
    fetchSummary();
  }, [exhibitorId]);

  if (loading) return <div>Loading dashboard…</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (!data) return null;

  return (
    <div className="container" style={{ padding: 16 }}>
      <h2>Exhibitor Summary</h2>
      <div style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
        gap: 16
      }}>
        <Card title="My Events" value={data.myEvents} />
        <Card title="My Customers" value={data.myCustomers} />
        <Card title="Total Registrations" value={data.myRegistrations} />
      </div>
    </div>
  );
}

function Card({ title, value }) {
  return (
    <div style={{
      border: "1px solid #eee",
      borderRadius: 12,
      padding: 16,
      boxShadow: "0 2px 8px rgba(0,0,0,0.06)"
    }}>
      <div style={{ fontSize: 14, opacity: 0.7 }}>{title}</div>
      <div style={{ fontSize: 28, fontWeight: 700, marginTop: 6 }}>{value}</div>
    </div>
  );
}
