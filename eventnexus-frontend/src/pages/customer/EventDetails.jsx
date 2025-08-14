import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { Container, Spinner, Alert, Button } from "react-bootstrap";
import axios from "axios";

const API_BASE = "http://localhost:8080";

export default function EventDetails() {
  const { id } = useParams();
  const [ev, setEv] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const { data } = await axios.get(`${API_BASE}/api/events/${id}`);
        setEv(data);
      } catch (e) {
        setError(e?.response?.data?.message || "Failed to load event.");
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  if (loading) {
    return (
      <Container className="mt-4">
        <Spinner animation="border" />
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  if (!ev) {
    return null; 
  }

  const dateStr =
    typeof ev.date === "string" ? ev.date.slice(0, 10) : ev.date ?? "";


const normalizeNumber = (v) => {
  if (v === null || v === undefined) return null;

  const s = String(v).trim();
  if (s.toLowerCase() === "null" || s === "") return null;

  // Replace comma with dot for decimal coords
  const n = Number.parseFloat(s.replace(",", "."));
  return Number.isNaN(n) ? null : n;
};

const mapHref = (() => {
  // 1) explicit URL from DB
  const raw = ev.mapUrl || ev.mapURL || ev.googleMapUrl || ev.googleMapsUrl || "";
  if (raw && String(raw).trim() && String(raw).trim().toLowerCase() !== "null") {
    const u = String(raw).trim();
    return /^https?:\/\//i.test(u) ? u : `https://${u}`;
  }

  const lat = normalizeNumber(ev.latitude);
  const lng = normalizeNumber(ev.longitude);
if (
    lat != null &&
    lng != null &&
    lat !== 0 &&
    lng !== 0
  ) {
    return `https://www.google.com/maps/search/?api=1&query=${lat},${lng}`;
  }

  const addr = (ev.address || "").toString().trim();
  if (addr) {
    return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(addr)}`;
  }

  return null;
})();

  return (
    <Container className="mt-4">
      <h2 className="mb-2">{ev.title}</h2>
      <div className="text-muted mb-3">
        {dateStr || "TBA"} • {ev.mode || "TBA"} • {ev.address || "TBA"}
      </div>

      {ev.imageUrl && (
        <img src={ev.imageUrl} alt={ev.title} style={{maxWidth: "100%", borderRadius: 8}} className="mb-3" />
      )}

      <p>{ev.description || "No description provided."}</p>

      <div className="mb-3">
        <strong>Type:</strong> {ev.typeName ?? ev.type?.name ?? "General"}<br/>
        <strong>Price:</strong> {ev.price ?? 0}
      </div>

             {mapHref && (
                <div className="mb-3">
                    <a href={mapHref} target="_blank" rel="noopener noreferrer">View map</a>
                </div>
                )}


      <div className="d-flex gap-2">
        <Button as={Link} to={`/customer/register-event?eventId=${id}`} variant="primary">
          Register
        </Button>
        <Button as={Link} to="/customer/explore-events" variant="secondary">
          Back
        </Button>
      </div>
    </Container>
  );
}
