import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
// import api from "../../api/axios";
import api from '../../api/api';

const API_BASE = ''; // unused; api base is '/api'

const ManageEvents = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let ignore = false;
    (async () => {
      setLoading(true);
      setError("");
      try {
             const { data } = await api.get('/events')

        if (!ignore) setEvents(data ?? []);
      } catch (e) {
        if (!ignore) setError(e.response?.data?.message || "Failed to load events.");
      } finally {
        if (!ignore) setLoading(false);
      }
    })();
    return () => { ignore = true; };
  }, []);

  const deleteEvent = async (id) => {
    if (!window.confirm("Delete this event?")) return;
    try {
      await api.delete(`/events/${id}`)

      setEvents((list) => list.filter((e) => e.id !== id));
    } catch (e) {
      alert(e.response?.data?.message || "Delete failed.");
    }
  };

  return (
    <div className="card p-4 mb-4">
      <h2 className="h5 fw-bold mb-3">Manage Events</h2>

      {loading ? (
        <p>Loading…</p>
      ) : error ? (
        <div className="alert alert-danger">{error}</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped align-middle">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Date</th>
                <th>Mode</th>
                <th>Address</th>
                <th>Exhibitor Name</th>
                <th style={{ width: 180 }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {events.length === 0 ? (
                <tr>
                  <td colSpan="8" className="text-center text-muted py-4">
                    No events found.
                  </td>
                </tr>
              ) : (
                events.map((ev) => (
                  <tr key={ev.id}>
                    <td>{ev.id}</td>
                    <td className="text-truncate" style={{ maxWidth: 260 }}>
                      <Link to={`/events/${ev.id}`} className="text-decoration-none">
                        {ev.title}
                      </Link>
                      {ev.imageUrl && <div className="small text-muted">📷 image</div>}
                    </td>
                    <td>{ev.date ?? "-"}</td>
                    <td>{ev.mode ?? "-"}</td>
                    <td className="text-truncate" style={{ maxWidth: 180 }}>
                      {ev.address ?? "-"}
                    </td>
                    
                    <td>
                      {ev.exhibitorName ??
                        ev.exhibitor?.name ??
                        ev.exhibitor?.username ??
                        "-"}
                    </td>             
                    <td>
                      <div className="btn-group btn-group-sm">
                        <Link className="btn btn-outline-primary" to={`/admin/events/${ev.id}/edit`}>
                          Edit
                        </Link>
                        <button className="btn btn-outline-danger" onClick={() => deleteEvent(ev.id)}>
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

        <Link to="/admin/dashboard" className=" bg-green-100 rounded hover:bg-green-200">
                Go to Dashboard
              </Link>
    </div>
  );
};

export default ManageEvents;
