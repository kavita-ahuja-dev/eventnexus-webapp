import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

const API_BASE = "http://localhost:8080";
const MODES = ["ONLINE", "OFFLINE"];

export default function AdminEditEvent() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [original, setOriginal] = useState(null);
  const [types, setTypes] = useState([]);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState({
    title: "",
    description: "",
    date: "",
    price: "",
    year: "",
    mode: "",
    zoomUrl: "",
    address: "",
    latitude: "",
    longitude: "",
    mapUrl: "",
    typeId: "",
    exhibitorId: "",
    isActive: true,      
    imageUrl: "",
    keepImage: true,
    removeImage: false,
  });

  useEffect(() => {
    let ignore = false;
    (async () => {
      try {
        const [evRes, typeRes] = await Promise.all([
          axios.get(`${API_BASE}/api/events/${id}`),
          axios.get(`${API_BASE}/api/event-types`),
        ]);

        if (ignore) return;

        const ev = evRes.data || {};
        const dateStr = ev.date && ev.date.slice ? ev.date.slice(0, 10) : ev.date;

        setOriginal(ev);
        setTypes(typeRes.data || []);

        setForm((f) => ({
          ...f,
          title: ev.title ?? "",
          description: ev.description ?? "",
          date: dateStr ?? "",
          price: ev.price ?? "",
          year: ev.year ?? "",
          mode: ev.mode ?? "",
          zoomUrl: ev.zoomUrl ?? "",
          address: ev.address ?? "",
          latitude: ev.latitude ?? "",
          longitude: ev.longitude ?? "",
          mapUrl: ev.mapUrl ?? "",
          typeId: ev.typeId ?? ev.type?.id ?? "",
          exhibitorId: ev.exhibitorId ?? "",
          isActive: (ev.active ?? ev.isActive ?? true) ? true : false,
          imageUrl: ev.imageUrl ?? "",
          keepImage: true,
          removeImage: false,
        }));
      } catch (e) {
        setError(e.response?.data?.message || "Failed to load event.");
      } finally {
        setLoading(false);
      }
    })();
    return () => (ignore = true);
  }, [id]);

  const onChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((f) => ({ ...f, [name]: type === "checkbox" ? checked : value }));
  };

  const setRemoveImage = () =>
    setForm((f) => ({ ...f, removeImage: true, keepImage: false, imageUrl: "" }));
  const setKeepImage = () =>
    setForm((f) => ({ ...f, keepImage: true, removeImage: false }));

  // Build payload: only changed fields; map isActive -> active
  const payload = useMemo(() => {
    if (!original) return null;
    const p = { id: Number(id) };

    const setIfChanged = (k, castNum = false) => {
      const cur = form[k];
      // original might have slightly different keys; compare against original[k]
      const old = original[k];
      if (cur === "" || cur === undefined || cur === null) return;
      if (castNum) {
        const num = Number(cur);
        if (!Number.isNaN(num) && String(num) !== String(old ?? "")) p[k] = num;
      } else {
        if (String(cur) !== String(old ?? "")) p[k] = cur;
      }
    };

    [
      "title", "description", "date", "price", "year", "mode", "zoomUrl",
      "address", "latitude", "longitude", "mapUrl"
    ].forEach((k) => setIfChanged(k));

    setIfChanged("typeId", true);
    setIfChanged("exhibitorId", true);

    // active flag (from isActive)
    const oldActive = (original.active ?? original.isActive ?? false) ? true : false;
    const newActive = !!form.isActive;
    if (newActive !== oldActive) p.active = newActive;

    // image rules
    if (form.removeImage) {
      p.imageUrl = ""; // remove image
    } else if (!form.keepImage) {
      if (form.imageUrl && form.imageUrl !== original.imageUrl) {
        p.imageUrl = form.imageUrl.trim();
      }
    }

    return p;
  }, [form, original, id]);

  const submit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    try {
      await axios.put(`${API_BASE}/api/events/${id}`, payload ?? { id: Number(id) });
      navigate("/admin/manage-events");
    } catch (e) {
      setError(e.response?.data?.message || "Update failed.");
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="container py-4">Loading…</div>;
  if (error) return <div className="container py-4 alert alert-danger">{error}</div>;

  return (
    <div className="container py-4">
      <h2 className="h5 fw-bold mb-3">Edit Event #{id}</h2>

      <form className="row g-3" onSubmit={submit}>
        <div className="col-md-6">
          <label className="form-label">Title</label>
          <input className="form-control" name="title" value={form.title} onChange={onChange} required />
        </div>

        <div className="col-md-3">
          <label className="form-label">Date</label>
          <input type="date" className="form-control" name="date" value={form.date ?? ""} onChange={onChange} />
        </div>

        <div className="col-md-3 form-check d-flex align-items-end ps-4">
          <input type="checkbox" className="form-check-input me-2" id="isActive" name="isActive" checked={!!form.isActive} onChange={onChange} />
          <label className="form-check-label" htmlFor="isActive">Active</label>
        </div>

        <div className="col-12">
          <label className="form-label">Description</label>
          <textarea className="form-control" rows={3} name="description" value={form.description} onChange={onChange} />
        </div>

        <div className="col-md-4">
          <label className="form-label">Event Type</label>
          <select
            className="form-select"
            name="typeId"
            value={form.typeId ?? ""}
            onChange={onChange}
            required
          >
            <option value="">Select type…</option>
            {types.map((t) => (
              <option key={t.id} value={t.id}>
                {t.name || t.typeName || `Type ${t.id}`}
              </option>
            ))}
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label">Mode</label>
          <select className="form-select" name="mode" value={form.mode ?? ""} onChange={onChange}>
            <option value="">Select…</option>
            {MODES.map((m) => <option key={m} value={m}>{m}</option>)}
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label">Price</label>
          <input className="form-control" name="price" value={form.price ?? ""} onChange={onChange} />
        </div>

        <div className="col-md-4">
          <label className="form-label">Year</label>
          <input className="form-control" name="year" value={form.year ?? ""} onChange={onChange} />
        </div>

        <div className="col-md-4">
          <label className="form-label">Exhibitor ID</label>
          <input className="form-control" name="exhibitorId" value={form.exhibitorId ?? ""} onChange={onChange} />
            
       
        </div>

        {form.mode === "ONLINE" ? (
          <div className="col-12">
            <label className="form-label">Zoom URL</label>
            <input className="form-control" name="zoomUrl" value={form.zoomUrl ?? ""} onChange={onChange} />
          </div>
        ) : (
          <>
            <div className="col-12">
              <label className="form-label">Address</label>
              <input className="form-control" name="address" value={form.address ?? ""} onChange={onChange} />
            </div>
            <div className="col-md-4">
              <label className="form-label">Latitude</label>
              <input className="form-control" name="latitude" value={form.latitude ?? ""} onChange={onChange} />
            </div>
            <div className="col-md-4">
              <label className="form-label">Longitude</label>
              <input className="form-control" name="longitude" value={form.longitude ?? ""} onChange={onChange} />
            </div>
            <div className="col-md-4">
              <label className="form-label">Map URL</label>
              <input className="form-control" name="mapUrl" value={form.mapUrl ?? ""} onChange={onChange} />
            </div>
          </>
        )}

        <div className="col-md-8">
          <label className="form-label">Image URL</label>
          <input
            className="form-control"
            name="imageUrl"
            value={form.imageUrl}
            onChange={(e) => setForm((f) => ({ ...f, imageUrl: e.target.value, keepImage: false, removeImage: false }))}
            placeholder="Set a new URL to replace image"
            disabled={form.keepImage || form.removeImage}
          />
        </div>

        <div className="col-12">
          <button className="btn btn-primary" disabled={saving}>
            {saving ? "Saving…" : "Save"}
          </button>
          <button type="button" className="btn btn-outline-secondary ms-2" onClick={() => navigate(-1)}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
