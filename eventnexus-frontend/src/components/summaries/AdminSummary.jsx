import { useEffect, useState } from "react";
import axios from "axios";

const API_BASE = 'http://localhost:8080';

export default function AdminSummary() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const res = await axios.get(
          `${API_BASE}/api/admin/dashboard`,  
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
  }, []);

  if (loading) return <div>Loading dashboard…</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (!data) return null;

  return (
    <div className="container" style={{ padding: 16 }}>
      <h2>Admin Summary</h2>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))", gap: 16 }}>
        <Card title="Total Users" value={data.totalUsers} />
        <Card title="Active Users" value={data.activeUsers} />
        <Card title="Exhibitors" value={data.totalExhibitors} />
        <Card title="Customers" value={data.totalCustomers} />
        <Card title="Total Events" value={data.totalEvents} />
        <Card title="Active Events" value={data.activeEvents} />
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


